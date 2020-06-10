package gui;

import commands.ICommand;
import configuration.Configuration;
import configuration.RuntimeStorage;
import logger.LoggerMSA;

public class GuiController {
    private GUI gui;
    private Configuration cfg = Configuration.instance;

    public GuiController(GUI gui) {
        this.gui = gui;
    }

    public void displayText(String text){
        StringBuilder sb = new StringBuilder();
        if (!gui.getDisplayedText().isBlank()) {
            sb.append(gui.getDisplayedText()).append("\n");
        }
        sb.append(text);
        gui.displayText(sb.toString());
    }

    public void close() {
        System.exit(0);
    }


    public void executeCommand(String commandText) {
        ICommand command = RuntimeStorage.instance.cqrInterpreterCoR.interpret(commandText);
        if(command!=null) {
            command.execute();
            displayText("");
        }
    }

    public void disableLogging(){
        displayText("logging off");
        cfg.disableLogging();
    }

    public void enableLogging(){
        displayText("logging on");
        cfg.enableLogging();
    }

    public boolean isLoggingEnabled() {
        return LoggerMSA.isLoggingEnabled();
    }

    public void displayLog() {
        String latestLog = LoggerMSA.getLatestLog();
        if (latestLog != null && !latestLog.isEmpty()){
            gui.clearText();
            displayText(latestLog);
        }
        else{
            displayText("Loading latest log: Could not load Logfile!");
        }
    }

    public void showHelp() {
        displayText("Help");
        displayText("- Available Commands:");
        displayText("  * show algorithm");
        displayText("  * encrypt message");
        displayText("  * decrypt message");
        displayText("  * crack encrypted message");
        displayText("  * register participant");
        displayText("  * create channel");
        displayText("  * show channel");
        displayText("  * drop channel");
        displayText("  * intrude channel");
        displayText("  * send message");

        displayText("");
        displayText("- Key Assignment");
        displayText("  F1  Show help");
        displayText("  F3  De/Activate debug mode");
        displayText("  F5  Execute command");
        displayText("  F8  Show latest log file");

        displayText("");
    }
}
