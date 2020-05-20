package persistence;


import persistence.dataModels.Channel;
import persistence.dataModels.Message;
import persistence.dataModels.Participant;
import persistence.dataModels.PostboxMessage;


import java.util.List;

public interface IMsaDB {

    void setupConnection();

    void dropAllTables();

    void createAllTables();

    void shutdown();

    void insertType(String type);

    void insertAlgorithm(String algorithmName);

    void insertParticipant(String participantName, String type);


    void insertParticipant(Participant participant);

    void insertChannel(String channelName, String participantA, String participantB);

    void insertChannel(Channel channel);

    void insertMessage(String participantFrom, String participantTo, String plainMessage,
                       String algorithm, String encodedMessage, String keyFile);

    void insertMessage(Message message);

    void insertPostboxMessage(String participantTo, String participantFrom, String message);

    void insertPostboxMessage(PostboxMessage message);

    List<String> getTypes();

    List<String> getAlgorithms();

    List<Participant> getParticipants();

    List<PostboxMessage> getPostboxMessages(String participant);

    List<Channel> getChannels();


    boolean channelExists(String channelName);

    boolean participantExists(String name);


    Channel getChannel(String participantA, String participantB);

    String getParticipantType(String participantName);


    void dropChannel(String channelName);
}
