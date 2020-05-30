package network;

import java.util.HashMap;
import java.util.Map;

public class Network implements INetwork {
    private Map<String , IChannel> channels = new HashMap<>();

    public void createChannel(String channelName, Participant p1, Participant p2) {
        IChannel channel = new Channel(channelName);
        channel.addParticipant(p1);
        channel.addParticipant(p2);
        this.channels.put(channelName, channel);
    }

    public void removeChannel(String channelName) {
        this.channels.remove(channelName);
    }

    public void addIntruder(String channelName, Participant intruder) {
        this.channels.get(channelName).addParticipant(intruder);
    }

    @Override
    public void sendMessage(String channelName, MessageEvent message) {
        this.channels.get(channelName).sendMessage(message);
    }
}
