package main.persistence.dataModels;

public class PostboxMessage {
    private final Participant participantFrom;
    private final Participant participantTo;
    private final String message;
    private final String timestamp;

    public PostboxMessage(Participant participantFrom, Participant participantTo, String message, String timestamp) {
        this.participantFrom = participantFrom;
        this.participantTo = participantTo;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Participant getParticipantFrom() {
        return participantFrom;
    }

    public Participant getParticipantTo() {
        return participantTo;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
