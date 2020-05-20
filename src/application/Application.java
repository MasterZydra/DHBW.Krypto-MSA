package application;


import configuration.RuntimeStorage;
import cryptography.CryptoAlgorithm;
import gui.GUI;
import persistence.IMsaDB;
import persistence.MSA_HSQLDB;
import persistence.dataModels.Channel;

import java.util.List;

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
            db.insertType(channel.getParticipantA().getType());
            db.insertType(channel.getParticipantB().getType());
            db.insertParticipant(channel.getParticipantA());
            db.insertParticipant(channel.getParticipantB());
            db.insertChannel(channel);
        }
        db.shutdown();
    }
}