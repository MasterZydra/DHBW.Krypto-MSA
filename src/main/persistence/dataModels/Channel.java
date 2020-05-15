package main.persistence.dataModels;

public class Channel {
    private final String name;
    private final Participant participantA;
    private final Participant participantB;

    public Channel(String name, Participant participantA, Participant participantB) {
        this.name = name;
        this.participantA = participantA;
        this.participantB = participantB;
    }

    public String getName() {
        return name;
    }

    public Participant getParticipantA() {
        return participantA;
    }

    public Participant getParticipantB() {
        return participantB;
    }
}
