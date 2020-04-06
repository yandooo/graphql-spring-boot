package com.graphql.spring.boot.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Helper object to test GraphQL subscriptions.
 */
@RequiredArgsConstructor
@Slf4j
public class GraphQLTestSubscription {

    private static final int SLEEP_INTERVAL_MS = 100;
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);
    private static final UriBuilderFactory URI_BUILDER_FACTORY = new DefaultUriBuilderFactory();

    @Getter
    private Session session;

    @Getter
    private boolean initialized = false;
    @Getter
    private boolean acknowledged = false;
    @Getter
    private boolean started = false;
    @Getter
    private boolean stopped = false;

    private final Environment environment;
    private final ObjectMapper objectMapper;
    private final String subscriptionPath;

    private final Queue<GraphQLResponse> responses = new ConcurrentLinkedQueue<>();
    private int id = ID_COUNTER.getAndIncrement();

    /**
     * Sends the "connection_init" message to the GraphQL server without a payload.
     * @return self reference
     */
    public GraphQLTestSubscription init() {
        init(null);
        return this;
    }

    /**
     * Sends the "connection_init" message to the GraphQL server.
     * @param payload The payload of the connection_init message. May be null, if not needed.
     * @return self reference
     */
    public GraphQLTestSubscription init(@Nullable final Object payload) {
        if (initialized) {
            fail("Subscription already initialized.");
        }
        try {
            initClient();
        } catch (Exception e) {
            fail("Could not initialize test subscription client. No subscription defined?", e);
        }
        final ObjectNode message = objectMapper.createObjectNode();
        message.put("type", "connection_init");
        message.set("payload", getFinalPayload(payload));
        sendMessage(message);
        initialized = true;
        return this;
    }

    /**
     * Sends the "start" message to the GraphQL server.
     * @param graphQLResource the GraphQL resource, which contains the query for the subscription start payload. The
     *                        start message will be sent without variables.
     * @return self reference
     */
    public GraphQLTestSubscription start(@NonNull final String graphQLResource) {
        start(graphQLResource, null);
        return this;
    }

    /**
     * Sends the "start" message to the GraphQL Subscription.
     * @param graphGLResource the GraphQL resource, which contains the query for the subscription start payload.
     * @param variables the variables needed for the query to be evaluated.
     * @return self reference
     */
    public GraphQLTestSubscription start(@NonNull final String graphGLResource, @Nullable final Object variables) {
        if (!initialized) {
            init();
        }
        if (started) {
            fail("Start message already sent. To start a new subscription, please call reset first.");
        }
        started = true;
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("query", loadQuery(graphGLResource));
        payload.set("variables", getFinalPayload(variables));
        ObjectNode message = objectMapper.createObjectNode();
        message.put("type", "start");
        message.put("id", id);
        message.set("payload", payload);
        sendMessage(message);
        return this;
    }

    /**
     * Sends the "stop" message to the server.
     * @return self reference
     */
    public GraphQLTestSubscription stop() {
        if (!started) {
            fail("Subscription not yet started.");
        }
        if (stopped) {
            fail("Subscription already stopped.");
        }
        final ObjectNode message = objectMapper.createObjectNode();
        message.put("type", "stop");
        message.put("id", id);
        sendMessage(message);
        stopped = true;
        try {
            session.close();
            session = null;
        } catch (IOException e) {
            fail("Could not close web socket session", e);
        }
        log.debug("Subscription stopped.");
        return this;
    }

    /**
     * Stops (if needed) and resets this instance. This should be called in the "afterEach" method of the test class, to
     * ensure that the bean is reusable between tests.
     */
    public void reset() {
        if (initialized && !stopped) {
            stop();
        }
        if (stopped) {
            id = ID_COUNTER.getAndIncrement();
        }
        initialized = false;
        started = false;
        stopped = false;
        acknowledged = false;
        responses.clear();
    }

    /**
     * Awaits and returns the next response received from the subscription. The subscription will be stopped after
     * receiving the message (or timeout).
     * @param timeout timeout in milliseconds. Test will fail if no message received from the subscription until
     *                the timeout expires.
     * @return The received response.
     */
    public GraphQLResponse awaitAndGetNextResponse(final int timeout) {
        return awaitAndGetNextResponses(timeout, 1, true).get(0);
    }

    /**
     * Awaits and returns the next response received from the subscription.
     * @param timeout timeout in milliseconds. Test will fail if no message received from the subscription until
     *                the timeout expires.
     * @param stopAfter if true, the subscription will be stopped after the message was received (or timeout).
     * @return The received response.
     */
    public GraphQLResponse awaitAndGetNextResponse(final int timeout, final boolean stopAfter) {
        return awaitAndGetNextResponses(timeout, 1, stopAfter).get(0);
    }

    /**
     * Waits a specified amount time and returns all responses received during that time. This method does not have any
     * expectation regarding the number of messages. The subscription will be stopped after the time elapsed.
     * @param timeToWait the time to wait, in milliseconds
     * @return the list of responses received during that time.
     */
    public List<GraphQLResponse> awaitAndGetAllResponses(final int timeToWait) {
        return awaitAndGetNextResponses(timeToWait, -1, true);
    }

    /**
     * Waits a specified amount time and returns all responses received during that time. This method does not have any
     * expectation regarding the number of messages.
     * @param timeToWait the time to wait, in milliseconds
     * @param stopAfter if true, the subscription will be stopped after the time elapsed.
     * @return the list of responses received during that time.
     */
    public List<GraphQLResponse> awaitAndGetAllResponses(final int timeToWait, final boolean stopAfter) {
        return awaitAndGetNextResponses(timeToWait, -1, stopAfter);
    }

    /**
     * Awaits and returns the specified number of responses. The subscription will be stopped after receiving the
     * messages (or timeout).
     * @param timeout timeout in milliseconds. Test will fail if the expected number of responses is not received.
     * @param numExpectedResponses the number of expected responses. If negative, the method will wait the timeout
     *                             and return all responses received during that time. In this case, no assertion is
     *                             made regarding the number of responses, and the returned list may be empty. If
     *                             zero, it is expected that no responses are sent during the timeout period.
     * @return The list containing the expected number of responses. The list contains the responses in the order they
     * were received. If more responses are received than minimally expected, {@link #getRemainingResponses()}  can
     * be used to retrieved them.
     */
    public List<GraphQLResponse> awaitAndGetNextResponses(
        final int timeout,
        final int numExpectedResponses
    ) {
        return awaitAndGetNextResponses(timeout, numExpectedResponses, true);
    }

    /**
     * Awaits and returns the specified number of responses.
     * @param timeout timeout in milliseconds. Test will fail if the expected number of responses is not received.
     * @param numExpectedResponses the number of expected responses. If negative, the method will wait the timeout
     *                             and return all responses received during that time. In this case, no assertion is
     *                             made regarding the number of responses, and the returned list may be empty. If
     *                             zero, it is expected that no responses are sent during the timeout period.
     * @param stopAfter if true, the subscription will be stopped after the messages were received (or timeout).
     * @return The list containing the expected number of responses. The list contains the responses in the order they
     * were received. If more responses are received than minimally expected, {@link #getRemainingResponses()}  can
     * be used to retrieved them.
     */
    public List<GraphQLResponse> awaitAndGetNextResponses(
        final int timeout,
        final int numExpectedResponses,
        final boolean stopAfter
    ) {
        if (!started) {
            fail("Start message not sent. Please send start message first.");
        }
        if (stopped) {
            fail("Subscription already stopped. Forgot to call reset after test case?");
        }
        int elapsedTime = 0;
        while (
            ((responses.size() < numExpectedResponses) || numExpectedResponses <= 0)
            && elapsedTime < timeout
        ) {
            try {
                Thread.sleep(SLEEP_INTERVAL_MS);
                elapsedTime += SLEEP_INTERVAL_MS;
            } catch (InterruptedException e) {
                fail("Test execution error - Thread.sleep failed.", e);
            }
        }
        synchronized (responses) {
            if (stopAfter) {
                stop();
            }
            int responsesToPoll = responses.size();
            if (numExpectedResponses == 0) {
                assertThat(responses)
                    .as(String.format("Expected no responses in %s MS, but received %s", timeout, responses.size()))
                    .isEmpty();
            }
            if (numExpectedResponses > 0) {
                assertThat(responses)
                    .as("Expected at least %s message(s) in %d MS, but %d received.",
                        numExpectedResponses,
                        timeout,
                        responses.size())
                    .hasSizeGreaterThanOrEqualTo(numExpectedResponses);
                responsesToPoll = numExpectedResponses;
            }
            final List<GraphQLResponse> responseList = new ArrayList<>();
            for (int i = 0; i < responsesToPoll; i++) {
                responseList.add(responses.poll());
            }
            log.debug("Returning {} responses.", responseList.size());
            return responseList;
        }
    }

    /**
     * Waits a specified amount of time and asserts that no responses were received during that time.
     * @param timeToWait time to wait, in milliseconds.
     * @param stopAfter if true, the subscription will be stopped afterwards.
     */
    public GraphQLTestSubscription waitAndExpectNoResponse(final int timeToWait, final boolean stopAfter) {
        awaitAndGetNextResponses(timeToWait, 0, stopAfter);
        return this;
    }

    /**
     * Waits a specified amount of time and asserts that no responses were received during that time. The subscription
     * will be stopped afterwards.
     * @param timeToWait time to wait, in milliseconds.
     */
    public GraphQLTestSubscription waitAndExpectNoResponse(final int timeToWait) {
        awaitAndGetNextResponses(timeToWait, 0, true);
        return this;
    }

    /**
     * Returns the remaining responses that were not returned so far. This method should only be called after
     * the subscription was stopped.
     * @return the remaining responses.
     */
    public List<GraphQLResponse> getRemainingResponses() {
        if (!stopped) {
            fail("getRemainingResponses should only be called after the subscription was stopped.");
        }
        final ArrayList<GraphQLResponse> graphQLResponses = new ArrayList<>(responses);
        responses.clear();
        return graphQLResponses;
    }

    private void initClient() throws Exception {
        final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        final String port = environment.getProperty("local.server.port");
        final URI uri = URI_BUILDER_FACTORY.builder().scheme("ws").host("localhost").port(port).path(subscriptionPath)
            .build();
        log.debug("Connecting to client at {}", uri);
        final ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
            .configurator(new TestWebSocketClientConfigurator())
            .build();
        session = webSocketContainer.connectToServer(TestWebSocketClient.class, clientEndpointConfig, uri);
        session.addMessageHandler(new TestMessageHandler());
    }

    private JsonNode getFinalPayload(final Object variables) {
        return (JsonNode) Optional.ofNullable(variables)
            .map(objectMapper::valueToTree)
            .orElseGet(objectMapper::createObjectNode);
    }

    private String loadQuery(final String graphGLResource) {
        try {
            final File file = ResourceUtils.getFile("classpath:" + graphGLResource);
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(String.format("Test setup failure - could not load GraphQL resource: %s", graphGLResource), e);
            return "";
        }
    }

    private void sendMessage(final Object message) {
        try {
            session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
        } catch (IOException e) {
            fail("Test setup failure - cannot serialize subscription payload.", e);
        }
    }

    class TestMessageHandler implements MessageHandler.Whole<String> {
        @Override
        public void onMessage(final String message) {
            try {
                log.debug("Received message from web socket: {}", message);
                final JsonNode jsonNode = objectMapper.readTree(message);
                final JsonNode type = jsonNode.get("type");
                if (type.asText().equals("connection_ack")) {
                    acknowledged = true;
                    log.debug("WebSocket connection acknowledged by the GraphQL Server.");
                } else if (type.asText().equals("data")) {
                    final JsonNode data = jsonNode.get("payload");
                    assertThat(data).as("Data message must have a payload.").isNotNull();
                    final GraphQLResponse graphQLResponse
                        = new GraphQLResponse(ResponseEntity.ok(objectMapper.writeValueAsString(data)), objectMapper);
                    synchronized (responses) {
                        responses.add(graphQLResponse);
                    }
                }
            } catch (JsonProcessingException e) {
                fail("Exception while parsing server response. Response is not a valid GraphQL response.", e);
            }
        }
    }

    public static class TestWebSocketClient extends Endpoint {
        @Override
        public void onOpen(final Session session, final EndpointConfig config) {
            log.debug("Connection established.");
        }
    }

    static class TestWebSocketClientConfigurator extends ClientEndpointConfig.Configurator {

        @Override
        public void beforeRequest(final Map<String, List<String>> headers) {
            super.beforeRequest(headers);
            headers.put("sec-websocket-protocol", Collections.singletonList("graphql-ws"));
        }
    }
}
