package network;

import java.util.List;

public interface IChannel {
    void sendMessage(MessageEvent message);
    void addParticipant(Participant participant);
    List<Participant> getParticipants();
}
