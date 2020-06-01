package commands;

import configuration.RuntimeStorage;
import persistence.IMsaDB;
import persistence.dataModels.Participant;

/*
 * Author: 6439456
 */

public class RegisterParticipantCommand extends CqrCommand {
    IMsaDB db = RuntimeStorage.instance.db;

    @Override
    public void execute() {
        // Check if participant exists
        if (db.participantExists(getParam("participant"))) {
            String returnMsg = "participant %s already exists, using existing postbox_%s";
            returnMsg = String.format(returnMsg, getParam("participant"), getParam("participant"));
            RuntimeStorage.instance.guiController.displayText(returnMsg);
            return;
        }

        // Add participant to DB
        Participant participant = new Participant(getParam("participant"), getParam("type"));
        db.insertParticipant(participant);

        // Add postbox
        // TODO Add postbox

        String returnMsg = "participant %s with type %s registered and postbox_%s created";
        returnMsg = String.format(returnMsg, getParam("participant"), getParam("type"), getParam("participant"));
        RuntimeStorage.instance.guiController.displayText(returnMsg);
    }
}
