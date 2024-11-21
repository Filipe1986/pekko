package com.pekko.factory;

import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

import static com.pekko.config.PulsarConfig.SERVICE_URL;


public class PulsarClientFactory {
    private static volatile PulsarClient pulsarClient;

    public static PulsarClient getPulsarClient() throws PulsarClientException {
        if (pulsarClient == null) {
            synchronized (PulsarClientFactory.class) {
                ClientBuilder clientBuilder = PulsarClient.builder()
                        .serviceUrl(SERVICE_URL);

                pulsarClient = clientBuilder.build();
            }
        }
        return pulsarClient;
    }

    public static void reset() {
        pulsarClient = null;
    }


}
