package commands;

import configuration.*;
import cryptography.*;
import logger.LoggerMSA;

import java.io.File;
import java.lang.reflect.Method;

/*
 * Author: 6439456
 */

public class EncryptMessageCommand extends CqrCommand {
    private LoggerMSA logger;

    @Override
    public void execute() {
        CryptoLoader loader = new CryptoLoader();

        logger = new LoggerMSA("encrypt", getParam("algorithm").toLowerCase());
        logger.log("Executing EncryptMessageCommand");
        try {
            // Load component
            logger.log("Load cryptography method for algorithm " + getParam("algorithm").toLowerCase());
            loader.createCryptographyMethod(CryptoAlgorithm.valueOfCaseIgnore(getParam("algorithm")), CryptoMethod.ENCRYPT);
            Method method = loader.getCryptoMethod();

            // Build file
            logger.log("Get file object");
            String fileName = getParam("keyfile");
            if (!fileName.toLowerCase().endsWith(".json"))
                fileName += ".json";
            File file = new File(Configuration.instance.getKeyFilePath + fileName);
            // Check if file exists
            if (!file.exists()) {
                String msg = "Error: File '" + getParam("keyfile") + "' not found.";
                logger.log(msg);
                logger.log("Canceled EncryptMessageCommand");
                printMessage(msg);
                return;
            }

            // Encrypt message
            logger.log("Executing encryption logic");
            String encrypted = (String) method.invoke(loader.getPort(), getParam("message"), file);
            logger.log("Message '" + getParam("message") + "' encrypted. Result of encryption: " + encrypted);
            printMessage(encrypted);
            logger.log("Executed EncryptMessageCommand");
        }
        catch (NullPointerException e) {
            String msg = "Error: Algorithm '" + getParam("algorithm").toUpperCase() + "' not found.";
            logger.log(msg);
            logger.log("Canceled EncryptMessageCommand");
            printMessage(msg);
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.log("Error occurred: " + e.getMessage());
            printMessage("Could not process EncryptMessage command");
            logger.log("Executed EncryptMessageCommand");
            return;
        }
    }
}
