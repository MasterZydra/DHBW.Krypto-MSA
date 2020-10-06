package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;

/*
 * Author: 6439456
 */

public class CqrInterpreter4 extends CqrInterpreter{
    private String algo;
    private String file;

    public CqrInterpreter4(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean isValid(String beforeMessage) {
        boolean valid;

        String[] messageParts = this.transformCqrString(beforeMessage).split(" ");
        valid = messageParts[2].equalsIgnoreCase("message");

        return valid;
    }

    private boolean getUsingParams(String cqrUsing) {
        String[] usings = cqrUsing.trim().split(" ");

        if (usings.length == 2 || usings.length == 5) {
            boolean validCqr = usings[0].equalsIgnoreCase("using");

            if (usings.length == 5) {
                validCqr = usings[2].equalsIgnoreCase("and");
                validCqr &= usings[3].equalsIgnoreCase("keyfile");
            }

            if (validCqr) {
                algo = usings[1];
                validCqr &= !algo.isEmpty();

                if (usings.length == 5) {
                    file = usings[4];
                    validCqr &= !algo.isEmpty() && !file.isEmpty();
                }
            }
            return validCqr;
        }
        return false;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);

        if (canHandleCQR(cqrTrans, "crack encrypted")) {
            int firstQuote = cqr.indexOf('\"');
            int lastQuote = cqr.lastIndexOf('\"');

            if (firstQuote != -1 &&
                    lastQuote != -1 &&
                    firstQuote != lastQuote
                    && this.isValid(cqr.substring(0, firstQuote - 1)))
            {
                String message = cqr.substring(firstQuote + 1, lastQuote);
                String using = cqr.substring(lastQuote + 1);

                if(this.getUsingParams(using)) {
                    ICommand command = CommandFactory.getCrackMessageCommand();
                    command.setParam("message", message);
                    command.setParam("algorithm", algo);
                    command.setParam("keyfile", file);
                    return command;
                }
            }

            printMessage("Syntax error: 'crack encrypted message \"[message]\" using [algorithm] [and keyfile [filename]]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
