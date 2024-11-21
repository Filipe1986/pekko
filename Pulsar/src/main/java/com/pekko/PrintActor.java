package com.pekko;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;

public class PrintActor {

    private PrintActor() {}


    public static Behavior<String> create() {
        return Behaviors.setup(context -> {
            System.out.println("Creating Print Actor: " + context.getSelf());
            return initialBehavior();
        });
    }

    private static Behavior<String> initialBehavior() {
        return Behaviors.receive(String.class)
                .onMessage(String.class, message -> {
                    System.out.println("Received message: " + message);
                    return Behaviors.same();
                })
                .build();
    }
}
