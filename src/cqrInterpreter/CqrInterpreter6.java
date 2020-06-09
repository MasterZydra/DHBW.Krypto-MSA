package cqrInterpreter;
//3894913
import commands.CommandFactory;
import commands.ICommand;

public class CqrInterpreter6 extends CqrInterpreter{
    private String participant01;
    private String participant02;
    private String channelName;

    public CqrInterpreter6(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean getParams(String cqrUsing) {
        String[] usings = cqrUsing.trim().split(" ");
        boolean validCqr = usings.length == 7;
        if (!validCqr) return false;
        validCqr = usings[0].equalsIgnoreCase("create");
        validCqr &= usings[1].equalsIgnoreCase("channel");
        validCqr &= usings[3].equalsIgnoreCase("from");
        validCqr &= usings[5].equalsIgnoreCase("to");

        if (!validCqr) return false;
        channelName = usings[2];
        participant01 = usings[4];
        participant02 = usings[6];
        validCqr &= !participant01.isEmpty() && !participant02.isEmpty();
        validCqr &= !channelName.isEmpty();
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);
        if (canHandleCQR(cqrTrans, "create channel")) {
            if(this.getParams(cqr)) {
                ICommand command = CommandFactory.getCreateChannelCommand();
                command.setParam("participant01", participant01);
                command.setParam("participant02", participant02);
                command.setParam("channelName", channelName);
                return command;
            }
            printMessage("Syntax error: 'create channel [name] from [participant01] to [participant02]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
