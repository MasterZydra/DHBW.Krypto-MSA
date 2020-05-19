package configuration;

import cryptography.CryptoAlgorithm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public enum Configuration {
    instance;

    public static Storage storage = Storage.instance;

    //default values
    public CryptoAlgorithm cryptoAlgorithm = CryptoAlgorithm.RSA;
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

    private void loadProperties(){
        props = new Properties();
        try {
            props.load(new FileInputStream(propertiesFileFullPath));
        } catch (IOException e) {
            System.out.println("Could not load config.properties");
            e.printStackTrace();
        }
        algorithm = props.getProperty("algorithm","shift");
    }

    private void loadAlgorithms(){

    }

    public List<String> getAlgorithmFileNames() {
        List<String> filenames = new ArrayList<>();
        String path = Configuration.instance.componentDirectory;
        try {
            Files.walk(Paths.get(Configuration.instance.componentDirectory))
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

//        try {
//            System.out.println(Configuration.instance.ud + Configuration.instance.fs);
//            List<String> a = Configuration.instance.getResourceFiles(Configuration.instance.ud + Configuration.instance.fs);
//            System.out.println(a);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}