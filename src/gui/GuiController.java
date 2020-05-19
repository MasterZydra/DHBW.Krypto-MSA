package gui;

import commands.ICommand;
import configuration.Configuration;
import cqrInterpreter.CqrInterpreter1;

public class GuiController {
    private GUI gui;
    private boolean isLoggingEnabled = false;
    private Configuration cfg = Configuration.instance;

    public GuiController(GUI gui) {
        this.gui = gui;
    }

    public void displayText(String text){
        gui.displayText(text);
    }

    public void close() {
        System.exit(0);
    }


    public void executeCommand(String commandText) {
        ICommand command = (new CqrInterpreter1(null)).interpret(commandText);
        if(command!=null) command.execute();
    }

    public void disableLogging(){
        displayText("logging off");
        isLoggingEnabled = false;
    }

    public void enableLogging(){
        displayText("logging on");
        isLoggingEnabled = true;
    }

    public boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public void displayLog() {
        displayText("todo: read latest log");
    }
}
