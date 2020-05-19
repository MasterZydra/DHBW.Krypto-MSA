package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class LoggerMSA {

    public static final String logFolder = "log";
    public static final String fileSeparator = File.separator;
    public static final String logFolderPath = logFolder + fileSeparator;
    File  logFile = null;

    public LoggerMSA(String cryptographyActionType, String algorithm) {
        String outputFileString = logFolderPath + cryptographyActionType + "_"
                + algorithm + "_"
                + Instant.now().getEpochSecond() + ".txt";
        logFile = new File(outputFileString);
        new File(logFolderPath).mkdir();
    }

    public void log(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, StandardCharsets.UTF_8, true)))
        {
            bw.write(message);
            bw.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoggerMSA lt = new LoggerMSA("encode", "none");
        lt.log("message!");
    }
}
