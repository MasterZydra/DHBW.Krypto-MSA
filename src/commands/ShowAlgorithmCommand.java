package commands;

import configuration.Configuration;

import java.util.List;

public class ShowAlgorithmCommand implements ICommand {
    public void setParam(String name, String value) {

    }

    public String getParam(String name) {
        return null;
    }

    public void execute() {
        List<String> list = Configuration.instance.getAlgorithmFileNames();
        String output= "";
        for (String algo: list
             ) {
            if(!output.isBlank()) output+=" | ";
            output += algo;

        }
        Configuration.runtimeStorage.guiController.displayText(output);
    }
}
