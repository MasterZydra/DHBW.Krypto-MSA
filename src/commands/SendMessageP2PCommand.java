package commands;

import configuration.Configuration;
import configuration.RuntimeStorage;
import cryptography.CryptoAlgorithm;
import cryptography.CryptoLoader;
import cryptography.CryptoMethod;
import gui.GuiController;
import logger.LoggerMSA;
import network.INetwork;
import network.MessageEvent;
import persistence.IMsaDB;
import persistence.dataModels.Channel;
import persistence.dataModels.Message;

import java.io.File;
import java.lang.reflect.Method;

//        "message"
//        "participantFrom"
//        "participantTo"
//        "algorithm"
//        "keyfile"

public class SendMessageP2PCommand extends CqrCommand {
    private LoggerMSA logger;
    @Override
    public void execute() {
        logger = new LoggerMSA("noCryptoAction", "noAlgorithm");
        logger.log("executing SendMessageP2PCommand");
        Configuration cfg = Configuration.instance;
        GuiController gui = RuntimeStorage.instance.guiController;
        INetwork net = RuntimeStorage.instance.network;
        IMsaDB db = RuntimeStorage.instance.db;

        Channel channel = db.getChannel(getParam("participantFrom"), getParam("participantTo"));
        if (channel==null){
            printFailMessage("No valid channel between "+getParam("participantFrom")
                    + " and "+getParam("participantTo"));
            return;
        }

        String nameFrom = getParam("participantFrom");
        String nameTo = getParam("participantTo");

        CryptoLoader loader = new CryptoLoader();
        try {
            loader.createCryptographyMethod(CryptoAlgorithm.valueOfCaseIgnore(getParam("algorithm")), CryptoMethod.ENCRYPT);
            Method method = loader.getCryptoMethod();
            File file = new File(Configuration.instance.getKeyFilePath + getParam("keyfile"));
            String encrypted = (String) method.invoke(loader.getPort(),  getParam("message"), file);
            net.sendMessage(channel.getName(), new MessageEvent(nameFrom, nameTo, encrypted, getParam("algorithm"),getParam("keyfile")));
            Message msg = new Message(
                    db.getParticipant(nameFrom),
                    db.getParticipant(nameTo),
                    getParam("message"),
                    encrypted,
                    getParam("algorithm"),
                    getParam("keyfile"),
                    "0");
            db.insertMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            printFailMessage();
            return;
        }
        logger.log("sent message: "+ getParam("message") + " from " + getParam("participantFrom") + " to " + getParam("participantTo"));
    }

    private void printFailMessage() {
        printFailMessage("Couldn't process SendMessageP2P command");
    }

    private void printFailMessage(String failMessage) {
        RuntimeStorage.instance.guiController.displayText(failMessage);
        logger.log("processed SendMessageP2P command");
    }
}