package application;


import configuration.RuntimeStorage;
import cqrInterpreter.*;
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
        RuntimeStorage runtimeStorage = RuntimeStorage.instance;
        INetwork net = runtimeStorage.network;
        CqrInterpreter cqrCoR = runtimeStorage.cqrInterpreterCoR;
        Application a = new Application();
        a.init();
        a.startGui();
    }

    private void startGui() {
        javafx.application.Application.launch(GUI.class);
    }

    public void init(){
        loadNetworksFromDatabase();
        RuntimeStorage.instance.cqrInterpreterCoR =  createInterpreterCoR();
    }

    private void loadNetworksFromDatabase() {
        IMsaDB db = MSA_HSQLDB.instance;
        INetwork net = RuntimeStorage.instance.network;
        Map<String, Participant> participants = new HashMap<>();
        db.setupConnection();
        populateDatabase(db);
        List<Channel> channels = db.getChannels();
        db.dropAllTables();
        db.createAllTables();

        for (CryptoAlgorithm algo: CryptoAlgorithm.values()
        ) {
            db.insertAlgorithm(algo.toString());
        }
        for (Channel channel : channels
             ) {
            System.out.println("creating channel "+ channel.getName() + " from DB");
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
        net.sendMessage("channel1",new MessageEvent("a","message on channel1"));

    }

    private void populateDatabase(IMsaDB db) {
        //add example data
        db.insertType("normal");
        db.insertType("intruder");
        db.insertAlgorithm("rsa");
        db.insertAlgorithm("shift");
        db.insertParticipant("a", "normal");
        db.insertParticipant("b", "normal");
        db.insertParticipant("c", "intruder");
        db.insertChannel("channel1", "a", "b");
        db.insertMessage("a", "b", "plainmessage", "none", "plainmessage", "keyfileName");
    }

    CqrInterpreter createInterpreterCoR(){
        CqrInterpreter10 cqr10 = new CqrInterpreter10(null);
        return new CqrInterpreter1(cqr10);
    }
}