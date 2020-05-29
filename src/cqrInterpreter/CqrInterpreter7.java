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
            ICommand command = CommandFactory.getShowChannelCommand();
            return command;
        }
        else
            return super.interpret(cqr);
    }
}
