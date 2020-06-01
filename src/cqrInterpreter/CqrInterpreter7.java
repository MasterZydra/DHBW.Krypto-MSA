package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;

public class CqrInterpreter7 extends CqrInterpreter{

    public CqrInterpreter7(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);
        if (canHandleCQR(cqrTrans, "show channel")) {
            String[] commandParts = cqr.trim().split(" ");
            if(commandParts.length != 2) return null;
            if(commandParts[0].equalsIgnoreCase("show") && commandParts[1].equalsIgnoreCase("channel") ) {
                return CommandFactory.getShowChannelCommand();}
            else {
                return null;
            }
        }
        else
            return super.interpret(cqr);
    }
}
