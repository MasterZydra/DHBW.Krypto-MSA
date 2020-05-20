package persistence.dataModels;

public class Message {
    private final Participant participantFrom;
    private final Participant participantTo;
    private final String plain_message;
    private final String encoded_message;
    private final String algorithm;
    private final String keyfile;
    private final String timestamp;

    public Message(Participant participantFrom, Participant participantTo, String plain_message, String encoded_message, String algorithm, String keyfile, String timestamp) {
        this.participantFrom = participantFrom;
        this.participantTo = participantTo;
        this.plain_message = plain_message;
        this.encoded_message = encoded_message;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
        this.timestamp = timestamp;
    }

    public Participant getParticipantFrom() {
        return participantFrom;
    }

    public Participant getParticipantTo() {
        return participantTo;
    }

    public String getPlain_message() {
        return plain_message;
    }

    public String getEncoded_message() {
        return encoded_message;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getKeyfile() {
        return keyfile;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
