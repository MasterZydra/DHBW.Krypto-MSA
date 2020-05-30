package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;
import configuration.RuntimeStorage;

public class CqrInterpreter4 extends CqrInterpreter{
    private String algo;

    public CqrInterpreter4(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean isValid(String beforeMessage) {
        boolean valid = true;

        String[] messageParts = this.transformCqrString(beforeMessage).split(" ");
        valid &= messageParts[2].equalsIgnoreCase("message");

        return valid;
    }

    private boolean getUsingParams(String cqrUsing) {
        String[] usings = cqrUsing.trim().split(" ");
        boolean validCqr = usings.length == 2;
        if (validCqr) {
            validCqr &= usings[0].equalsIgnoreCase("using");

            if (validCqr) {
                algo = usings[1];
                validCqr &= !algo.isEmpty();
            }
        }
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);

        if (canHandleCQR(cqrTrans, "crack encrypted")) {
            int firstQuote = cqr.indexOf("\"");
            int lastQuote = cqr.lastIndexOf("\"");

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
                    return command;
                }
            }

            RuntimeStorage.instance.guiController.displayText("Syntax error: 'crack encrypted message \"[message]\" using [algorithm]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
