package network;

/*
 * Author: 6439456
 */

public class MessageEvent {
    private String receiver;
    private String message;
    private String sender;
    private String algorithm;
    private String keyfile;

    public String getReceiver() {
        return this.receiver;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSender() {
        return sender;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getKeyfile() {
        return keyfile;
    }

    public MessageEvent(String sender, String receiver, String message, String algorithm, String keyfile) {

        this.receiver = receiver;
        this.message = message;
        this.sender = sender;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }
}
