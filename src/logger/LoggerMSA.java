package logger;

import configuration.Configuration;

import java.io.*;
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
        Path newestLogFile=null;
        Comparator<Path> byTimeStamp = (path1, path2) -> {
            String path1str = path1.getFileName().toString();
            long timeStamp1 = Long.parseLong( path1str.substring(path1str.lastIndexOf("_"), path1str.length() - 4) );
            String path2str = path2.getFileName().toString();
            long timeStamp2 = Long.parseLong( path2str.substring(path2str.lastIndexOf("_"), path2str.length() - 4) );
            return Long.compare(timeStamp1,timeStamp2);
        };

        try {
            newestLogFile = Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .max(byTimeStamp).orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(newestLogFile.toFile(), StandardCharsets.UTF_8)))
        {
            String line=null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return newestLogFile==null?null:newestLogFile.getFileName().toString();
             return sb.toString();
     }

    public static void main(String[] args) {
        LoggerMSA lt = new LoggerMSA("encode", "none");
        lt.log("message!");
        System.out.println("latest: "+LoggerMSA.getLatestLog());
    }
}
