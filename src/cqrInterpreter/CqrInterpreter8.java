package cqrInterpreter;
//3894913
import commands.CommandFactory;
import commands.ICommand;

public class CqrInterpreter8 extends CqrInterpreter{
    private String channelName;

    public CqrInterpreter8(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean getParams(String cqrUsing) {
        String[] commandParts = cqrUsing.trim().split(" ");
        boolean validCqr = commandParts.length == 3;
        if (!validCqr) return false;
        validCqr = commandParts[0].equalsIgnoreCase("drop");
        validCqr &= commandParts[1].equalsIgnoreCase("channel");
        if (!validCqr) return false;

        channelName = commandParts[2];

        validCqr &= !channelName.isEmpty();
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);
        if (canHandleCQR(cqrTrans, "drop channel")) {
            if(this.getParams(cqr)) {
                ICommand command = CommandFactory.getDropChannelCommand();
                command.setParam("channelName", channelName);
                return command;
            }
            printMessage("Syntax error: 'drop channel [channel]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
