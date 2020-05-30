package cqrInterpreter;

import commands.ICommand;
import configuration.RuntimeStorage;
import gui.GuiController;

public abstract class CqrInterpreter {
    private CqrInterpreter successor;

    public ICommand interpret(String cqr) {
        if (getSuccessor() != null)
            return getSuccessor().interpret(cqr);
        else {
            System.out.println("Invalid command");
            return null;
        }
    }

    protected String transformCqrString(String cqr) {
        return cqr.trim().toLowerCase();
    }

    protected boolean canHandleCQR(String cqr, String cqrStart) {
        String cqrTrans = this.transformCqrString(cqr);
        String cqrStartTrans = this.transformCqrString(cqrStart);
        return cqrTrans.startsWith(cqrStartTrans);
    }

    public CqrInterpreter getSuccessor() {
        return this.successor;
    }

    public void setSuccessor(CqrInterpreter successor) {
        this.successor = successor;
    }

    protected void printMessage(String message) {
        GuiController guiController = RuntimeStorage.instance.guiController;
        if (guiController != null)
            guiController.displayText(message);
        else
            System.out.println(message);
    }
}
