package network;

import com.google.common.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

public class Channel implements IChannel {
    private String name;
    private EventBus eventBus;
    private List<Participant> participants;

    public Channel(String name) {
        this.name = name;
        this.eventBus = new EventBus();
        this.participants = new LinkedList<>();
    }

    public void sendMessage(MessageEvent message) {
        this.eventBus.post(message);
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
        this.eventBus.register(participant);
    }
}
