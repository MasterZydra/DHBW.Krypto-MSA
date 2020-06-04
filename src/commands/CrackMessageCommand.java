package commands;

import configuration.*;
import cryptography.*;
import logger.LoggerMSA;

import java.io.File;
import java.util.concurrent.*;

/*
 * Author: 6439456
 */

public class CrackMessageCommand extends CqrCommand {
    private LoggerMSA logger;

    @Override
    public void execute() {
        logger = new LoggerMSA("crack", getParam("algorithm").toLowerCase());
        logger.log("Executing CrackMessageCommand");

        // Build file
        logger.log("Get file object");
        String fileName = getParam("keyfile");
        File file = null;
        if (fileName != null) {
            if (!fileName.toLowerCase().endsWith(".json"))
                fileName += ".json";
            file = new File(Configuration.instance.getKeyFilePath + fileName);

            // Check if file exists
            if (!file.exists()) {
                String msg = "Error: File '" + getParam("keyfile") + "' not found.";
                logger.log(msg);
                logger.log("Canceled EncryptMessageCommand");
                printMessage(msg);
                return;
            }
        }


        // Crack message
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CrackTask task = new CrackTask();
        task.setKeyfile(file);
        task.setAlgorithm(getParam("algorithm"));
        task.setMessage(getParam("message"));
        Future<String> future = executor.submit(task);
        try {
            logger.log("Start cracking task");
            String crackedMsg = future.get(Configuration.instance.crackingMaxSeconds, TimeUnit.SECONDS);
            printMessage(crackedMsg);
            logger.log("Message cracked:");
            logger.log("- Encrypted message: " + getParam("message"));
            logger.log("- Cracked message: " + crackedMsg);
        }
        catch (TimeoutException | InterruptedException | ExecutionException e) {
            printMessage("cracking encrypted message \"" + getParam("message") + "\" failed");
            logger.log("Failed to crack message");
        }
        future.cancel(true);
        logger.log("Kill cracking task");
        logger.log("Executed CrackMessageCommand");
    }

    private void printMessage(String failMessage) {
        RuntimeStorage.instance.guiController.displayText(failMessage);
    }
}

