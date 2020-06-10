package commands;

import configuration.RuntimeStorage;
import logger.LoggerMSA;
import persistence.IMsaDB;
import persistence.dataModels.Participant;

/*
 * Author: 6439456
 */

public class RegisterParticipantCommand extends CqrCommand {
    IMsaDB db = RuntimeStorage.instance.db;

    @Override
    public void execute() {
        LoggerMSA logger = new LoggerMSA("noCryptoAction", "noAlgorithm");
        logger.log("Executing CrackMessageCommand");

        // Check if participant exists
        logger.log("Check if participant '" + getParam("participant") + "' exists");
        if (db.participantExists(getParam("participant"))) {
            String returnMsg = "participant %s already exists, using existing postbox_%s";
            returnMsg = String.format(returnMsg, getParam("participant"), getParam("participant"));
            RuntimeStorage.instance.guiController.displayText(returnMsg);
            logger.log(returnMsg);
            logger.log("Executed CrackMessageCommand");
            return;
        }

        // Add participant to DB and create postbox
        logger.log("Insert participant into DB");
        Participant participant = new Participant(getParam("participant"), getParam("type"));
        db.insertParticipant(participant);

        String returnMsg = "participant %s with type %s registered and postbox_%s created";
        returnMsg = String.format(returnMsg, getParam("participant"), getParam("type"), getParam("participant"));
        RuntimeStorage.instance.guiController.displayText(returnMsg);

        logger.log("Executed CrackMessageCommand");
    }
}
