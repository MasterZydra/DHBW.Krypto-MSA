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

    public static final String NORMAL = "normal";
    public static final String BRANCH_HKG = "branch_hkg";
    public static final String BRANCH_CPT = "branch_cpt";
    public static final String BRANCH_SFO = "branch_sfo";
    public static final String BRANCH_SYD = "branch_syd";
    public static final String BRANCH_WUH = "branch_wuh";

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
            Participant partNetA = participants.computeIfAbsent(partA.getName(), ParticipantDefault::new);
            Participant partNetB = participants.computeIfAbsent(partB.getName(), ParticipantDefault::new);
            net.createChannel(channel.getName(), partNetA, partNetB);
        }

        populateDatabase(db);
    }

    private void populateDatabase(IMsaDB db) {
        //add example data
        db.insertType(NORMAL);
        db.insertType("intruder");
        db.insertAlgorithm("rsa");
        db.insertAlgorithm("shift");
        db.insertParticipant(BRANCH_HKG, NORMAL);
        db.insertParticipant(BRANCH_CPT, NORMAL);
        db.insertParticipant(BRANCH_SFO, NORMAL);
        db.insertParticipant(BRANCH_SYD, NORMAL);
        db.insertParticipant(BRANCH_WUH, NORMAL);
        db.insertParticipant("msa", "intruder");
        db.insertChannel("hkg_wuh", BRANCH_HKG, BRANCH_WUH);
        db.insertChannel("hkg_cpt", BRANCH_HKG, BRANCH_CPT);
        db.insertChannel("cpt_syd", BRANCH_CPT, BRANCH_SYD);
        db.insertChannel("syd_sfo", BRANCH_SYD, BRANCH_SFO);
    }

}