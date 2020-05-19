package gui;

import commands.ICommand;
import configuration.Configuration;
import cqrInterpreter.CqrInterpreter1;
import logger.LoggerMSA;

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
        String latestLog = LoggerMSA.getLatestLog();
        if (latestLog != null && !latestLog.isEmpty()){
            displayText(latestLog);}
        else{
            displayText("Loading latest log: Could not load Logfile!");
        }


    }
}
