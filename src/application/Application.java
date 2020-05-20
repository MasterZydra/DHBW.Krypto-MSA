package application;


import configuration.RuntimeStorage;
import cryptography.CryptoAlgorithm;
import gui.GUI;
import network.INetwork;
import network.MessageEvent;
import network.Participant;
import network.ParticipantDefault;
import persistence.IMsaDB;
import persistence.MSA_HSQLDB;
import persistence.dataModels.Channel;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    public static void main(String... args) {
        IMsaDB db = MSA_HSQLDB.instance;
        Application a = new Application();
        a.init();
        a.startGui();
        RuntimeStorage.instance.guiController.displayText(db.getAlgorithms().toString());
    }

    private void startGui() {
        javafx.application.Application.launch(GUI.class);
    }

    public void init(){
        loadNetworksFromDatabase();
    }

    private void loadNetworksFromDatabase() {
        IMsaDB db = MSA_HSQLDB.instance;
        INetwork net = RuntimeStorage.instance.network;
        Map<String, Participant> participants = new HashMap<>();
        db.setupConnection();
        List<Channel> channels = db.getChannels();
        db.dropAllTables();
        db.createAllTables();
        for (CryptoAlgorithm algo: CryptoAlgorithm.values()
        ) {
            db.insertAlgorithm(algo.toString());
        }
        for (Channel channel : channels
             ) {
            persistence.dataModels.Participant partA = channel.getParticipantA();
            persistence.dataModels.Participant partB = channel.getParticipantB();
            //add to DB
            db.insertType(partA.getType());
            db.insertType(partB.getType());
            db.insertParticipant(partA);
            db.insertParticipant(partB);
            db.insertChannel(channel);
            //add to network
            Participant partNetA = participants.computeIfAbsent(partA.getName(), (name)->new ParticipantDefault(name) );
            Participant partNetB = participants.computeIfAbsent(partB.getName(), (name)->new ParticipantDefault(name) );
            net.createChannel(channel.getName(), partNetA, partNetB);
        }
        db.shutdown();
        net.createChannel("testchannel", new ParticipantDefault("tom"), new ParticipantDefault("bom") );
        net.sendMessage("testchannel", new MessageEvent("tom","testMessage"));
    }
}