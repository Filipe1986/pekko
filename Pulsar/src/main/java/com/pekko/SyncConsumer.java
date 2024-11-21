package com.pekko;


import com.pekko.factory.PulsarConsumerFactory;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pulsar.client.api.*;

public class SyncConsumer {

    public static Behavior<String> create() {
        return Behaviors.setup(context -> {
            System.out.println("Creating Pulsar Consumer Actor {}" + context.getSelf());

            Consumer<String> consumer = PulsarConsumerFactory.createConsumer();

            context.getSelf().tell("poll");

            return Behaviors.receive(String.class)
                    .onMessage(String.class, notUsed -> {
                        try {
                            Message<String> msg = consumer.receive();

                            System.out.println(msg.getValue());

                            consumer.acknowledge(msg);
                        } catch (Exception e) {
                            context.getLog().error("Failed to receive message from Pulsar", e);
                        }
                        context.getSelf().tell("poll");
                        return Behaviors.same();
                    })
                    .build();
        });
    }
}
