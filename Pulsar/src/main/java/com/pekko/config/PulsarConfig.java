package com.pekko.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.api.SubscriptionType;

public class PulsarConfig {
    public static final Config CONFIG = ConfigFactory.load().getConfig("pulsar.inbound");
    public static final String SERVICE_URL = CONFIG.getString("serviceUrl");

    public static final String TOPIC = CONFIG.getString("topic");
    public static final String SUBSCRIPTION = CONFIG.getString("subscription");
    public  static final SubscriptionType SUBSCRIPTION_TYPE = CONFIG.getString("subscription").equals("Key_Shared") ? SubscriptionType.Key_Shared : SubscriptionType.Exclusive;
    public static final SubscriptionInitialPosition SUBSCRIPTION_INITIAL_POSITION = CONFIG.getString("subscriptionInitialPosition").equals("Earliest") ? SubscriptionInitialPosition.Earliest : SubscriptionInitialPosition.Latest;
}
