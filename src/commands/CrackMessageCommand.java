package commands;

import configuration.*;
import cryptography.*;
import logger.LoggerMSA;

import java.io.File;
import java.lang.reflect.Method;

public class CrackMessageCommand extends CqrCommand {
    private LoggerMSA logger;

    @Override
    public void execute() {
        CryptoLoader loader = new CryptoLoader();
        try {
            // Load component
            loader.createCrackerMethod(CryptoAlgorithm.valueOfCaseIgnore(getParam("algorithm")));
            Method method = loader.getCryptoMethod();

            // Build file
            String fileName = getParam("keyfile");
            File file = null;
            if (fileName != null) {
                if (!fileName.toLowerCase().endsWith(".json"))
                    fileName += ".json";
                file = new File(Configuration.instance.getKeyFilePath + fileName);
            }

            // Decrypt message
            String cracked = (String) method.invoke(loader.getPort(), getParam("message"), file);
            printMessage(cracked);
        } catch (Exception e) {
            e.printStackTrace();
            printFailMessage();
            return;
        }
    }

    private void printFailMessage() {
        printMessage("Could not process DecryptMessage command");
    }

    private void printMessage(String failMessage) {
        RuntimeStorage.instance.guiController.displayText(failMessage);
    }
}

