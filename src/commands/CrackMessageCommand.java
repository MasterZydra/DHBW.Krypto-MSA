package commands;

import configuration.*;
import cryptography.*;
import logger.LoggerMSA;

import java.io.File;
import java.util.concurrent.*;

public class CrackMessageCommand extends CqrCommand {
    private LoggerMSA logger;

    @Override
    public void execute() {
        // Build file
        String fileName = getParam("keyfile");
        File file = null;
        if (fileName != null) {
            if (!fileName.toLowerCase().endsWith(".json"))
                fileName += ".json";
            file = new File(Configuration.instance.getKeyFilePath + fileName);
        }

        // Crack message
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CrackTask task = new CrackTask();
        task.setKeyfile(file);
        task.setAlgorithm(getParam("algorithm"));
        task.setMessage(getParam("message"));
        Future<String> future = executor.submit(task);
        try {
            String crackedMsg = future.get(Configuration.instance.crackingMaxSeconds, TimeUnit.SECONDS);
            printMessage(crackedMsg);
        }
        catch (TimeoutException | InterruptedException | ExecutionException e) {
            printMessage("cracking encrypted message \"" + getParam("message") + "\" failed");
        }

        executor.shutdownNow();
    }

    private void printMessage(String failMessage) {
        RuntimeStorage.instance.guiController.displayText(failMessage);
    }
}

