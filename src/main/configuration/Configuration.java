package main.configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public enum Configuration {
    instance;

    // default values
    String defaultAlgorithm = "shift";

    // common
    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");

    // database
    public final String dataDirectory = userDirectory + fileSeparator + "hsqldb" + fileSeparator;
    public final String databaseFile = dataDirectory + "datastore.db";
    public final String driverName = "jdbc:hsqldb:";
    public final String username = "sa";
    public final String password = "";

    // component
    public String componentDirectory = userDirectory + fileSeparator + "component";

    // logger
    public String logDirectory = userDirectory + fileSeparator + "log";

    // properties
    public String propertiesDirectory = userDirectory + fileSeparator + "properties";
    public String propertiesFileFullPath = propertiesDirectory + fileSeparator + "config.properties";
    public Properties props;

    // algorithms
    String algorithm;
    List<String> algorithms = new ArrayList<>();

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

    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static void main(String[] args) {

        try {
            System.out.println(Configuration.instance.userDirectory + Configuration.instance.fileSeparator);
            List<String> a = Configuration.instance.getResourceFiles(Configuration.instance.userDirectory + Configuration.instance.fileSeparator);
            System.out.println(a);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}