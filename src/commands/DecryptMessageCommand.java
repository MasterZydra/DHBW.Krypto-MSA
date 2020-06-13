package commands;

import configuration.*;
import cryptography.*;
import logger.LoggerMSA;

import java.io.File;
import java.lang.reflect.Method;

/*
 * Author: 6439456
 */

public class DecryptMessageCommand extends CqrCommand {
    private LoggerMSA logger;

    @Override
    public void execute() {
        CryptoLoader loader = new CryptoLoader();

        logger = new LoggerMSA("decrypt", getParam("algorithm").toLowerCase());
        logger.log("Executing DecryptMessageCommand");
        try {
            // Load component
            logger.log("Load cryptography method for algorithm " + getParam("algorithm").toLowerCase());
            loader.createCryptographyMethod(CryptoAlgorithm.valueOfCaseIgnore(getParam("algorithm")), CryptoMethod.DECRYPT);
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
                logger.log("Canceled DecryptMessageCommand");
                printMessage(msg);
                return;
            }

            // Decrypt message
            logger.log("Executing decryption logic");
            String decrypted = (String) method.invoke(loader.getPort(), getParam("message"), file);
            logger.log("Message '" + getParam("message") + "' decrypted. Result of decryption: " + decrypted);
            printMessage(decrypted);
            logger.log("Executed DecryptMessageCommand");
        }
        catch (AlgorithmNotFoundException e) {
            String msg = "Error: Algorithm '" + getParam("algorithm").toUpperCase() + "' not found.";
            logger.log(msg);
            logger.log("Canceled DecryptMessageCommand");
            printMessage(msg);
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.log("Error occurred: " + e.getMessage());
            printMessage("Could not process DecryptMessage command");
            logger.log("Executed DecryptMessageCommand");
            return;
        }
    }
}
