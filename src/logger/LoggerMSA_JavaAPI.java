package main.logger;

import java.io.File;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerMSA_JavaAPI {

    private static final String logFolder = "log";
    private final String fileSeparator = File.separator;

    private final Logger logger = Logger.getAnonymousLogger();
    private FileHandler fileHandler = null;

    public LoggerMSA_JavaAPI(String cryptographyActionType, String algorithm) {
        try {
            fileHandler = new FileHandler(cryptographyActionType + "_"
                    + algorithm + "_"
                    + Instant.now().getEpochSecond() + ".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    public void log(String message) {
        logger.info(message);
    }

    public static void main(String[] args) {
        LoggerMSA_JavaAPI lt  = new LoggerMSA_JavaAPI("encode", "none");
        lt.log("message!");
        lt.close();
    }

    public void close() {
        fileHandler.close();
    }
}
