package com.pekko;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.Props;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.StashBuffer;

import java.util.function.Function;

public class StashingActor {

    public interface Command {}

    public static class ProcessMessage implements Command {
        private final String payload;

        public ProcessMessage(String payload) {
            this.payload = payload;
        }

        public String getPayload() {
            return payload;
        }
    }


    private final StashBuffer<Command> buffer;
    private final String id;
    private boolean isProcessing;

    public StashingActor(StashBuffer<Command> buffer, String id) {
        this.buffer = buffer;
        this.id = id;
        this.isProcessing = false;
    }

    public static Behavior<Command> create(String id) {
        return Behaviors.withStash(
                100,
                stash -> Behaviors.setup(context -> new StashingActor(stash, id).initialBehaviorStart()));
    }

    private Behavior<Command> initialBehaviorStart() {
        return Behaviors.receive(Command.class)
                .onMessage(ProcessMessage.class, this::onProcessMessage)
                .build();
    }

    private Behavior<Command> onProcessMessage(ProcessMessage processMessage) {
        System.out.println("Received message: " + processMessage.getPayload());
        if (isProcessing) {
            System.out.println("Stashing message: " + processMessage.getPayload());
            buffer.stash(processMessage);
        } else {
            System.out.println("Processing message: " + processMessage.getPayload());
            isProcessing = true;
            return save(processMessage);
        }
        return Behaviors.same();
    }

    private Behavior<Command> save(ProcessMessage processMessage) {
        System.out.println("Saving message: " + processMessage.getPayload());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        isProcessing = false;
        return unstashNextMessage();
    }

    private Behavior<Command> unstashNextMessage() {
        if (!buffer.isEmpty()) {
            System.out.println("Unstashing next message...");
            return buffer.unstash(initialBehaviorStart(), 1, Function.identity());
        }
        return initialBehaviorStart();
    }


    public static void main(String[] args) {

        ActorSystem<Void> actorSystem = ActorSystem.create(Behaviors.empty(), "TestActorSystem");

        ActorRef<Command> actor1 = actorSystem.systemActorOf(StashingActor.create("uniqueId1"), "stashActor1", Props.empty());


        actor1.tell(new ProcessMessage("Message 1"));
        actor1.tell(new ProcessMessage("Message 2"));
        actor1.tell(new ProcessMessage("Message 3"));
        actor1.tell(new ProcessMessage("Message 4"));
    }
}
