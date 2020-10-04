package commands;

import configuration.RuntimeStorage;
import gui.GuiController;

import java.util.HashMap;
import java.util.Map;

public abstract class CqrCommand implements ICommand {
    private Map<String, String> params = new HashMap<>();

    public void setParam(String name, String value) {
        this.params.put(name, value);
    }

    public String getParam(String name) {
        return this.params.getOrDefault(name, null);
    }

    protected void printMessage(String message) {
        GuiController guiController = RuntimeStorage.instance.guiController;
        if (guiController != null)
            guiController.displayText(message);
        else
            System.out.println(message);
    }
}
