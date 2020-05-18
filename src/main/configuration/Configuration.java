package main.configuration;

import main.CryptoAlgorithm;

public enum Configuration {
    instance;

    public CryptoAlgorithm cryptoAlgorithm = CryptoAlgorithm.RSA;

    // common
    public final String ud = System.getProperty("user.dir");
    public final String fs = System.getProperty("file.separator");

    // database
    public final String dataDirectory = ud + fs + "hsqldb" + fs;
    public final String databaseFile = dataDirectory + "datastore.db";
    public final String driverName = "jdbc:hsqldb:";
    public final String username = "sa";
    public final String password = "";

    // component
    public String componentDirectory = ud + fs + "component";

    public String getCryptoAlgorithmPath() {
        String path = componentDirectory;
        switch (cryptoAlgorithm) {
            case RSA:
                path += fs + cryptoAlgorithm.toString().toLowerCase() + fs + "jar" + fs + "RSA.jar";
                break;
            case Shift:
                path += fs + cryptoAlgorithm.toString().toLowerCase() + fs + "jar" + fs + "Shift.jar";
                break;
            default:
                path = "ERROR";
        }
        return path;
    }

    public String getKeyFilePath = ud + fs + "keyfiles" + fs;
}