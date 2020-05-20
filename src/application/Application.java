package application;

import cryptography.CryptoAlgorithm;
import gui.GUI;
import persistence.IMsaDB;
import persistence.MSA_HSQLDB;

public class Application {
    public static void main(String... args) {
        // hsqldb demo
//        HSQLDB.instance.setupConnection();
//
//        HSQLDB.instance.dropTableParticipants();
//        HSQLDB.instance.dropTableTypes();
//
//        HSQLDB.instance.createTableTypes();
//        HSQLDB.instance.createTableParticipants();
//
//        HSQLDB.instance.insertDataTableTypes("normal");
//        HSQLDB.instance.insertDataTableTypes("intruder");
//
//        HSQLDB.instance.insertDataTableParticipants("branch_hkg", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_cpt", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_sfo", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_syd", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_wuh", 1);
//        HSQLDB.instance.insertDataTableParticipants("msa", 2);
//
//        HSQLDB.instance.shutdown();
        Application a = new Application();
        a.init();
        a.startGui();

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
        //db.createAllTables();
        for (CryptoAlgorithm algo: CryptoAlgorithm.values()
        ) {
            db.insertAlgorithm(algo.toString());
        }
        db.shutdown();
    }
}