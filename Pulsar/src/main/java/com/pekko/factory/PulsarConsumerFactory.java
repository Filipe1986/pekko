package com.pekko.factory;

import org.apache.pulsar.client.api.*;

import static com.pekko.config.PulsarConfig.*;

public class PulsarConsumerFactory {

    public static Consumer<String> createConsumer() throws PulsarClientException {

        return PulsarClientFactory.getPulsarClient().newConsumer(Schema.STRING)
                .topic(TOPIC)
                .subscriptionName(SUBSCRIPTION)
                .subscriptionType(SUBSCRIPTION_TYPE)
                .subscriptionInitialPosition(SUBSCRIPTION_INITIAL_POSITION)
                .subscribe();
    }
}
