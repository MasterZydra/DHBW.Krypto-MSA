package application;
//3894913

import configuration.RuntimeStorage;
import cryptography.CryptoAlgorithm;
import gui.GUI;
import network.INetwork;
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
        RuntimeStorage runtimeStorage = RuntimeStorage.instance;
        runtimeStorage.init();
        Application a = new Application();
        a.init();
        a.startGui();
        a.close();
    }

    public void init(){
        MSA_HSQLDB.instance.setupConnection();
        loadNetworksFromDatabase();
    }

    private void startGui() {
        javafx.application.Application.launch(GUI.class);
    }

    private void close() {
        MSA_HSQLDB.instance.shutdown();
    }

    private void loadNetworksFromDatabase() {
        IMsaDB db = MSA_HSQLDB.instance;
        INetwork net = RuntimeStorage.instance.network;
        Map<String, Participant> participants = new HashMap<>();
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

        populateDatabase(db);
    }

    private void populateDatabase(IMsaDB db) {
        //add example data
        db.insertType("normal");
        db.insertType("intruder");
        db.insertAlgorithm("rsa");
        db.insertAlgorithm("shift");
        db.insertParticipant("branch_hkg", "normal");
        db.insertParticipant("branch_cpt", "normal");
        db.insertParticipant("branch_sfo", "normal");
        db.insertParticipant("branch_syd", "normal");
        db.insertParticipant("branch_wuh", "normal");
        db.insertParticipant("msa", "intruder");
        db.insertChannel("hkg_wuh", "branch_hkg", "branch_wuh");
        db.insertChannel("hkg_cpt", "branch_hkg", "branch_cpt");
        db.insertChannel("cpt_syd", "branch_cpt", "branch_syd");
        db.insertChannel("syd_sfo", "branch_syd", "branch_sfo");
    }

}