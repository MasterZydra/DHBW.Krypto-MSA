package commands;
//3894913
import configuration.Configuration;
import configuration.RuntimeStorage;
import cryptography.CryptoAlgorithm;
import cryptography.CryptoLoader;
import cryptography.CryptoMethod;
import logger.LoggerMSA;
import network.INetwork;
import network.MessageEvent;
import persistence.IMsaDB;
import persistence.dataModels.Channel;
import persistence.dataModels.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

public class SendMessageP2PCommand extends CqrCommand {
    @Override
    public void execute() {
        INetwork net = RuntimeStorage.instance.network;
        IMsaDB db = RuntimeStorage.instance.db;
        LoggerMSA logger;

        Channel channel = db.getChannel(getParam("participantFrom"), getParam("participantTo"));
        if (channel==null){
            printMessage("No valid channel between "+getParam("participantFrom")
                    + " and "+getParam("participantTo"));
            return;
        }

        CryptoAlgorithm algorithm = CryptoAlgorithm.valueOfCaseIgnore(getParam("algorithm"));
        if (algorithm==null){
            printMessage("Could not find algorithm: "+getParam("algorithm"));
            return;
        }

        logger = new LoggerMSA("decrypt", getParam("algorithm").toLowerCase());
        logger.log("Executing SendMessageP2P Command");

        String nameFrom = getParam("participantFrom");
        String nameTo = getParam("participantTo");

        logger.log("From: "+nameFrom);
        logger.log("To: "+nameTo);
        logger.log("On channel: "+channel.getName());
        logger.log("Algorithm: "+getParam("algorithm"));
        logger.log("Keyfile: "+getParam("keyfile"));
        logger.log("Clear Message: "+getParam("message"));


        CryptoLoader loader = new CryptoLoader();
        try {
            logger.log("getting loader..");
            loader.createCryptographyMethod(algorithm, CryptoMethod.ENCRYPT);
            logger.log("getting method..");
            Method method = loader.getCryptoMethod();
            logger.log("getting keyfile..");
            File file = new File(Configuration.instance.getKeyFilePath + getParam("keyfile"));
            logger.log("encrypting..");
            String encrypted = (String) method.invoke(loader.getPort(),  getParam("message"), file);
            logger.log("encrypted message: "+encrypted);
            logger.log("sending message on net..");
            net.sendMessage(channel.getName(), new MessageEvent(nameFrom, nameTo, encrypted, getParam("algorithm"),getParam("keyfile")));
            Message msg = new Message(
                    db.getParticipant(nameFrom),
                    db.getParticipant(nameTo),
                    getParam("message"),
                    encrypted,
                    getParam("algorithm"),
                    getParam("keyfile"),
                    "0");
            logger.log("writing message to database..");
            db.insertMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() instanceof FileNotFoundException ) {
                String errorMsg = "Could not find keyfile " + getParam("keyfile");
                logger.log(errorMsg);
                printMessage(errorMsg);
            }
            String errorMsg = "Could not process SendMessageP2P command";
            logger.log(errorMsg);
            printMessage(errorMsg);
            return;
        }
    }
}
