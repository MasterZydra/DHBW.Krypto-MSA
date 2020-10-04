package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;

/*
 * Author: 6439456
 */

public class CqrInterpreter2 extends CqrInterpreter{
    private String algo;
    private String file;

    public CqrInterpreter2(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean getUsingParams(String cqrUsing) {
        String[] usings = cqrUsing.trim().split(" ");
        boolean validCqr = usings.length == 5;
        if (validCqr) {
            validCqr = usings[0].equalsIgnoreCase("using");
            validCqr &= usings[2].equalsIgnoreCase("and");
            validCqr &= usings[3].equalsIgnoreCase("keyfile");

            if (validCqr) {
                algo = usings[1];
                file = usings[4];
                validCqr = !algo.isEmpty() && !file.isEmpty();
            }
        }
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);

        if (canHandleCQR(cqrTrans, "encrypt message")) {
            int firstQuote = cqr.indexOf('\"');
            int lastQuote = cqr.lastIndexOf('\"');

            if (firstQuote != -1 && lastQuote != -1 && firstQuote != lastQuote) {
                String message = cqr.substring(firstQuote + 1, lastQuote);
                String using = cqr.substring(lastQuote + 1);

                if(this.getUsingParams(using)) {
                    ICommand command = CommandFactory.getEncryptMessageCommand();
                    command.setParam("message", message);
                    command.setParam("algorithm", algo);
                    command.setParam("keyfile", file);
                    return command;
                }
            }

            printMessage("Syntax error: 'encrypt message \"[message]\" using [algorithm] and keyfile [filename]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
