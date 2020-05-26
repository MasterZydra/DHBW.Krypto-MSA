package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;
import configuration.RuntimeStorage;
import gui.GuiController;

public class CqrInterpreter10 extends CqrInterpreter{
    private String participantFrom;
    private String participantTo;
    private String message;
    private String algorithm;
    private String keyfile;

    public CqrInterpreter10(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean getParams(String cqrUsing) {
        String[] usings = cqrUsing.trim().split(" ");
        boolean validCqr = usings.length == 9;
        if (!validCqr) return false;
        validCqr = usings[0].equalsIgnoreCase("from");
        validCqr &= usings[2].equalsIgnoreCase("to");
        validCqr &= usings[4].equalsIgnoreCase("using");
        validCqr &= usings[6].equalsIgnoreCase("and");
        validCqr &= usings[7].equalsIgnoreCase("keyfile");

        if (!validCqr) return false;
        participantFrom = usings[1];
        participantTo = usings[3];
        algorithm = usings[5];
        keyfile = usings[8];
        validCqr &= !participantFrom.isEmpty() && !participantTo.isEmpty();
        validCqr &= !algorithm.isEmpty() && !keyfile.isEmpty();
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);
        GuiController gui = RuntimeStorage.instance.guiController;
        if (canHandleCQR(cqrTrans, "send message")) {
            int firstQuote = cqr.indexOf("\"");
            int lastQuote = cqr.lastIndexOf("\"");

            if (firstQuote != -1 && lastQuote != -1 && firstQuote != lastQuote) {
                message = cqr.substring(firstQuote + 1, lastQuote);
                String partAfterQuotes = cqr.substring(lastQuote + 1);

                if(this.getParams(partAfterQuotes)) {
                    ICommand command = CommandFactory.sendMessageP2PCommand();
                    command.setParam("message", message);
                    command.setParam("participantFrom", participantFrom);
                    command.setParam("participantTo", participantTo);
                    command.setParam("algorithm", algorithm);
                    command.setParam("keyfile", keyfile);
                    return command;
                }
            }
            gui.displayText("Syntax error: 'send message \"[message]\" from [participant] to [participant] using [algorithm] and keyfile [filename]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
