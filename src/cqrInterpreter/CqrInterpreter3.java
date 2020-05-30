package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;

public class CqrInterpreter3 extends CqrInterpreter{
    private String algo;
    private String file;

    public CqrInterpreter3(CqrInterpreter successor) {
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
                validCqr &= !algo.isEmpty() && !file.isEmpty();
            }
        }
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);

        if (canHandleCQR(cqrTrans, "decrypt message")) {
            int firstQuote = cqr.indexOf("\"");
            int lastQuote = cqr.lastIndexOf("\"");

            if (firstQuote != -1 && lastQuote != -1 && firstQuote != lastQuote) {
                String message = cqr.substring(firstQuote + 1, lastQuote);
                String using = cqr.substring(lastQuote + 1);

                if(this.getUsingParams(using)) {
                    ICommand command = CommandFactory.getDecryptMessageCommand();
                    command.setParam("message", message);
                    command.setParam("algorithm", algo);
                    command.setParam("keyfile", file);
                    return command;
                }
            }

            printMessage("Syntax error: 'decrypt message \"[message]\" using [algorithm] and keyfile [filename]' expected");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
