package main.network;

public interface IChannel {
    void sendMessage(MessageEvent message);
    void addParticipant(Participant participant);
}
