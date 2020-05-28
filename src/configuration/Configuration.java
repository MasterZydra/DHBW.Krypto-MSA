package configuration;

import cryptography.CryptoAlgorithm;
import logger.LoggerMSA;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public enum Configuration {
    instance;

    public static RuntimeStorage runtimeStorage = RuntimeStorage.instance;

    //default values
    public int crackingMaxSeconds = 30;

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

    // logger
    public String logDirectory = ud + fs + "log";

    // properties
    public String propertiesDirectory = ud + fs + "properties";
    public String propertiesFileFullPath = propertiesDirectory + fs + "config.properties";
    public Properties props;

    // algorithms
    String algorithm;
    List<String> algorithms = new ArrayList<>();

    // keyfiles
    public String getKeyFilePath = ud + fs + "keyfiles" + fs;

    Configuration() {
    }

    public String getCryptoAlgorithmPath(CryptoAlgorithm cryptoAlgorithm) {
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
  
    public String getCrackerPath(CryptoAlgorithm cryptoAlgorithm) {
        String path = componentDirectory;
        switch (cryptoAlgorithm) {
            case RSA:
                path += fs + cryptoAlgorithm.toString().toLowerCase() + "_cracker" + fs + "jar" + fs + "RSACracker.jar";
                break;
            case Shift:
                path += fs + cryptoAlgorithm.toString().toLowerCase() + "_cracker" + fs + "jar" + fs + "ShiftCracker.jar";
                break;
            default:
                path = "ERROR";
        }
        return path;
    }

    private void loadProperties(){
        props = new Properties();
        try {
            props.load(new FileInputStream(propertiesFileFullPath));
        } catch (IOException e) {
            System.out.println("Could not load config.properties");
            e.printStackTrace();
        }
        try {
            crackingMaxSeconds = Integer.parseInt(props.getProperty("crackingMaxSeconds"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAlgorithmFileNames() {
        List<String> filenames = new ArrayList<>();
        String path = Configuration.instance.componentDirectory;
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .forEach((f)->{
                        String file = f.toString();
                        if( file.endsWith(".jar")){
                            String fName = f.getFileName().toString().toLowerCase();
                            filenames.add(fName.substring(0,fName.length()-4));}
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filenames;
    }

    public static void main(String[] args) {
        System.out.println(Configuration.instance.getAlgorithmFileNames());
        System.out.println(Configuration.instance.algorithm);
        System.out.println(Configuration.instance.crackingMaxSeconds);
    }

    public void enableLogging() {
        LoggerMSA.setLoggingEnabled(true);
    }

    public void disableLogging() {
        LoggerMSA.setLoggingEnabled(false);
    }
}