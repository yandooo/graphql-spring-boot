package com.oembedler.moon.graphql.boot.resolvers;

import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import com.oembedler.moon.graphql.boot.publishers.StockTickerPublisher;
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
