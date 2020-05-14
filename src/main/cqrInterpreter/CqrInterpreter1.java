package main.cqrInterpreter;

import main.commands.CommandFactory;
import main.commands.ICommand;

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
            
            System.out.println("Syntax error: 'show algorithm' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
