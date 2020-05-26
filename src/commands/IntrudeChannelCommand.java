package commands;

import configuration.Configuration;
import configuration.RuntimeStorage;
import gui.GuiController;
import logger.LoggerMSA;
import network.INetwork;
import network.ParticipantDefault;
import network.ParticipantIntruder;
import persistence.IMsaDB;

public class IntrudeChannelCommand extends CqrCommand {
    private LoggerMSA logger;
    @Override
    public void execute() {

        Configuration cfg = Configuration.instance;
        GuiController gui = RuntimeStorage.instance.guiController;
        INetwork net = RuntimeStorage.instance.network;
        IMsaDB db = RuntimeStorage.instance.db;

        //"intruderName"
        //"channelName"

        if ( !db.channelExists(getParam("channelName")) ){
            printFailMessage("Could not add intruder " + getParam("intruderName") + "to channel " + getParam("channelName"));
            return;
        }
        if (!db.getTypes().contains("intruder")) {
            db.insertType("intruder");
        }
        net.addIntruder(getParam("channelName"), new ParticipantIntruder(getParam("intruderName")));
    }

    private void printFailMessage() {
        printFailMessage("Could not process IntrudeChannel command");
    }

    private void printFailMessage(String failMessage) {
        RuntimeStorage.instance.guiController.displayText(failMessage);
    }
}
