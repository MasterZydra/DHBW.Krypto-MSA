package commands;
//3894913
import configuration.Configuration;
import logger.LoggerMSA;

import java.util.List;

public class ShowAlgorithmCommand extends CqrCommand {

    @Override
    public void execute() {
        LoggerMSA logger = new LoggerMSA("noCryptoAction", "noAlgorithm");
        logger.log("executing ShowAlgorithmCommand");
        Configuration cfg = Configuration.instance;
        List<String> list = cfg.getAlgorithmFileNames();
        String output= "";
        for (String algo: list) {
            // Exclude crackers
            if (algo.endsWith("cracker")) continue;
            if(!output.isBlank()) output += " | ";
            output += algo;
            logger.log("found algo: "+algo);
        }
        printMessage(output);
        logger.log("displayed output:"+output);
        logger.log("executed ShowAlgorithmCommand");

    }
}
