package commands;
//3894913
import configuration.RuntimeStorage;
import logger.LoggerMSA;
import network.INetwork;
import network.ParticipantIntruder;
import persistence.IMsaDB;

public class IntrudeChannelCommand extends CqrCommand {
    private LoggerMSA logger;
    @Override
    public void execute() {
        INetwork net = RuntimeStorage.instance.network;
        IMsaDB db = RuntimeStorage.instance.db;

        if ( !db.channelExists(getParam("channelName")) ){
            printMessage("Could not add intruder " + getParam("intruderName") + "to channel " + getParam("channelName"));
            return;
        }
        if (!db.getTypes().contains("intruder")) {
            db.insertType("intruder");
        }

        if (!db.participantExists(getParam("intruderName"))) {
            db.insertParticipant(getParam("intruderName"), "intruder");
        }
        net.addIntruder(getParam("channelName"), new ParticipantIntruder(getParam("intruderName")));
    }
}
