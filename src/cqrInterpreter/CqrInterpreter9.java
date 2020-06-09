package cqrInterpreter;
//3894913
import commands.CommandFactory;
import commands.ICommand;

public class CqrInterpreter9 extends CqrInterpreter{
    private String intruderName;
    private String channelName;

    public CqrInterpreter9(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean getParams(String cqrUsing) {
        String[] commandParts = cqrUsing.trim().split(" ");
        boolean validCqr = commandParts.length == 5;
        if (!validCqr) return false;
        validCqr = commandParts[0].equalsIgnoreCase("intrude");
        validCqr &= commandParts[1].equalsIgnoreCase("channel");
        validCqr &= commandParts[3].equalsIgnoreCase("by");
        if (!validCqr) return false;

        channelName = commandParts[2];
        intruderName = commandParts[4];

        validCqr &= !intruderName.isEmpty() && !channelName.isEmpty();
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);
        if (canHandleCQR(cqrTrans, "intrude channel")) {
            if(this.getParams(cqr)) {
                ICommand command = CommandFactory.getIntrudeChannelCommand();
                command.setParam("intruderName", intruderName);
                command.setParam("channelName", channelName);
                return command;
            }
            printMessage("Syntax error: 'intrude channel [channel] by [participant]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
