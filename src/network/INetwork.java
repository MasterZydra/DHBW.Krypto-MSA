package network;

/*
 * Author: 6439456
 */

public interface INetwork {
    void createChannel(String channelName, Participant p1, Participant p2);
    void removeChannel(String channelName);
    void addIntruder(String channelName, Participant intruder);
    void sendMessage(String channelName, MessageEvent message);
    Participant getParticipant(String participantName);
}
