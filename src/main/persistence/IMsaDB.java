package main.persistence;

import java.util.List;

public interface IMsaDB {

    void setupConnection();

    void dropAllTables();
    void createAllTables();

    void insertType(String type);
    void insertAlgorithm(String algorithmName);
    void insertParticipant(String participantName, String type);
    void insertChannel(String channelName, String participantA, String participantB);
    void insertMessage(String participantFrom, String participantTo, String plainMessage,
                       String algorithm, String encodedMessage, String keyFile);
    void insertPostboxMessage(String participantTo, String participantFrom, String message);

    List<String> getTypes();
    List<String> getAlgorithms();
    List<String> getParticipants();
    List<String> getChannels();
    List<String> getMessages();
    List<String> getPostboxMessages(String participant);
    List<String> getParticipantType(String participant);

//    int getTypeID(String typeName);
//    int getParticipantID(String participantName);
//    int getAlgorithmID(String algorithmName);
    boolean channelExists(String channelName);
    boolean channelExists(String participantA, String participantB);

}
