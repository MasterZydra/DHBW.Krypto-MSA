package network;

public abstract class Participant {
    protected String name;

    public Participant(String name) {
        this.name = name;
    }

    protected boolean isReceiver(MessageEvent messageEvent) {
        return this.name.equalsIgnoreCase(messageEvent.getReceiver());
    }
}
