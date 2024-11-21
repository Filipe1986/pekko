package com.pekko;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Props;
import org.apache.pekko.actor.typed.javadsl.Behaviors;

public class App {
    public static void main(String[] args) {
        ActorSystem<Void> actorSystem = ActorSystem.create(Behaviors.empty(), "TestActorSystem");

        ActorRef<String> actor1 = actorSystem.systemActorOf(PrintActor.create(), "PrintActor", Props.empty());

        actor1.tell("Message 1");
        actor1.tell("Message 2");
        actor1.tell("Message 3");
        actor1.tell("Message 4");


        actorSystem.systemActorOf(SyncConsumer.create(), "SyncConsumer", Props.empty());
    }
}
