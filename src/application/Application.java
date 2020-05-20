package application;


import cryptography.CryptoAlgorithm;
import gui.GUI;
import persistence.IMsaDB;
import persistence.MSA_HSQLDB;

public class Application {
    public static void main(String... args) {
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