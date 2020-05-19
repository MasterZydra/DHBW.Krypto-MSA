package logger;

import configuration.Configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LoggerMSA {

    String logDirectory = Configuration.instance.logDirectory;
    File  logFile = null;

    public LoggerMSA(String cryptographyActionType, String algorithm) {
        String fileName = cryptographyActionType + "_"
                + algorithm + "_"
                + Instant.now().getEpochSecond() + ".txt";
        String outputFileString = logDirectory + Configuration.instance.fs + fileName;
        logFile = new File(outputFileString);
        new File(logDirectory).mkdir();
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

    static public String getLatestLog() {
        List<String> filedates = new ArrayList<>();
        String path = Configuration.instance.logDirectory;
        Long youngestTimeStamp = 0L;
        Path newestLogFile=null;
        try {
            newestLogFile = Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .max(Comparator.naturalOrder()).orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(newestLogFile==null?null:newestLogFile.getFileName().toString());
        return newestLogFile==null?null:newestLogFile.getFileName().toString();
     }

    public static void main(String[] args) {
        LoggerMSA lt = new LoggerMSA("encode", "none");
        lt.log("message!");
        System.out.println("latest: "+LoggerMSA.getLatestLog());
    }
}
