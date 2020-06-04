package logger;
//3894913
import configuration.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

public class LoggerMSA {

    static private Configuration cfg = Configuration.instance;
    static private String logDirectory = cfg.logDirectory;
    static private boolean loggingEnabled = false;
    private File logFile = null;

    public LoggerMSA(String cryptographyActionType, String algorithm) {
        String fileName = cryptographyActionType.toLowerCase() + "_"
                + algorithm + "_"
                + Instant.now().getEpochSecond() + ".txt";
        String outputFileString = logDirectory + cfg.fs + fileName;
        logFile = new File(outputFileString);
        new File(logDirectory).mkdir();
    }

    public void log(String message) {
        if (!loggingEnabled) return;
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
        String path = Configuration.instance.logDirectory;
        Path newestLogFile=null;
        Comparator<Path> byTimeStamp = (path1, path2) -> {
            String path1str = path1.getFileName().toString();
            long timeStamp1 = Long.parseLong( path1str.substring(path1str.lastIndexOf("_")+1, path1str.length() - 4) );
            String path2str = path2.getFileName().toString();
            long timeStamp2 = Long.parseLong( path2str.substring(path2str.lastIndexOf("_")+1, path2str.length() - 4) );
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
            String fileName = newestLogFile.getFileName().toString();
            sb.append("Log:\n").append(fileName).append("\n");
            Instant time = Instant.ofEpochSecond(Long.parseLong( fileName.substring(fileName.lastIndexOf("_")+1, fileName.length() - 4) ));
            LocalDateTime ldt = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
            String date = String.format("%d %s %d at %d:%d%n", ldt.getDayOfMonth(), ldt.getMonth(),
                    ldt.getYear(), ldt.getHour(), ldt.getMinute());
            sb.append(date + "\n");
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
             return sb.toString();
     }

    public static void main(String[] args) {
        LoggerMSA lt = new LoggerMSA("ENCODE", "none");
        lt.log("logger custom message!");
        System.out.println("latest: "+LoggerMSA.getLatestLog());
    }

    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public static void setLoggingEnabled(boolean loggingEnabled) {
        LoggerMSA.loggingEnabled = loggingEnabled;
    }
}
