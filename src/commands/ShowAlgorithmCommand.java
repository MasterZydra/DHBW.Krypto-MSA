package commands;

import configuration.Configuration;
import configuration.RuntimeStorage;
import gui.GuiController;
import logger.LoggerMSA;

import java.util.List;

public class ShowAlgorithmCommand extends CqrCommand {

    @Override
    public void execute() {
        LoggerMSA logger = new LoggerMSA("noCryptoAction", "noAlgorithm");
        logger.log("executing ShowAlgorithmCommand");
        Configuration cfg = Configuration.instance;
        GuiController gui = RuntimeStorage.instance.guiController;
        List<String> list = cfg.getAlgorithmFileNames();
        String output= "";
        for (String algo: list
             ) {
            if(!output.isBlank()) output+=" | ";
            output += algo;
            logger.log("found algo: "+algo);
        }
        gui.displayText(output);
        logger.log("displayed output:"+output);
        logger.log("executed ShowAlgorithmCommand");
    }
}
