package network;

public class MessageEvent {
    private String receiver;
    private String message;

    public MessageEvent(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public String getMessage() {
        return this.message;
    }
}
