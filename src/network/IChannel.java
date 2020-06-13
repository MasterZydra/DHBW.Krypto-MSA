package network;

import java.util.List;

/*
 * Author: 6439456
 */

public interface IChannel {
    void sendMessage(MessageEvent message);
    void addParticipant(Participant participant);
    List<Participant> getParticipants();
}
