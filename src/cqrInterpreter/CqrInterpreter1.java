package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;

/*
 * Author: 6439456
 */

public class CqrInterpreter1 extends CqrInterpreter{
    public CqrInterpreter1(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);

        if (canHandleCQR(cqrTrans, "show algorithm")) {
            if (cqrTrans.equalsIgnoreCase("show algorithm")) {
                return CommandFactory.getShowAlgorithmCommand();
            }

            printMessage("Syntax error: 'show algorithm' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
