package commands;

import configuration.*;
import cryptography.*;
import logger.LoggerMSA;

import java.io.File;
import java.lang.reflect.Method;

public class EncryptMessageCommand extends CqrCommand {
    private LoggerMSA logger;

    @Override
    public void execute() {
        CryptoLoader loader = new CryptoLoader();
        try {
            // Load component
            loader.createCryptographyMethod(CryptoAlgorithm.valueOfCaseIgnore(getParam("algorithm")), CryptoMethod.ENCRYPT);
            Method method = loader.getCryptoMethod();

            // Build file
            String fileName = getParam("keyfile");
            if (!fileName.toLowerCase().endsWith(".json"))
                fileName += ".json";
            File file = new File(Configuration.instance.getKeyFilePath + fileName);

            // Encrypt message
            String encrypted = (String) method.invoke(loader.getPort(), getParam("message"), file);
            printMessage(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            printFailMessage();
            return;
        }
    }

    private void printFailMessage() {
        printMessage("Could not process EncryptMessage command");
    }

    private void printMessage(String failMessage) {
        RuntimeStorage.instance.guiController.displayText(failMessage);
    }
}
