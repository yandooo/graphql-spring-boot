package graphql.kickstart.spring.web.boot.resolvers;

import graphql.kickstart.spring.web.boot.publishers.StockTickerRxPublisher;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class Subscription implements GraphQLSubscriptionResolver {

    private StockTickerRxPublisher stockTickerPublisher;

    Subscription(StockTickerRxPublisher stockTickerPublisher) {
        this.stockTickerPublisher = stockTickerPublisher;
    }

    Publisher<StockPriceUpdate> stockQuotes(List<String> stockCodes) {
        return stockTickerPublisher.getPublisher(stockCodes);
    }

}
