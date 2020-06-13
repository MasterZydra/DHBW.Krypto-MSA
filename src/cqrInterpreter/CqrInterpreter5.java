package cqrInterpreter;

import commands.CommandFactory;
import commands.ICommand;

/*
 * Author: 6439456
 */

public class CqrInterpreter5 extends CqrInterpreter{
    private String participant;
    private String type;

    public CqrInterpreter5(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    private boolean getParams(String cqr) {
        String[] cqrParts = cqr.trim().split(" ");
        boolean validCqr = cqrParts.length == 6;
        if (validCqr) {
            validCqr &= cqrParts[3].equalsIgnoreCase("with");
            validCqr &= cqrParts[4].equalsIgnoreCase("type");

            if (validCqr) {
                participant = cqrParts[2];
                type = cqrParts[5].toLowerCase();

                validCqr &= !participant.isEmpty();
                validCqr &= (type.equals("normal") |
                        type.equals("intruder"));
            }
        }
        return validCqr;
    }

    @Override
    public ICommand interpret(String cqr) {
        String cqrTrans = this.transformCqrString(cqr);

        if (canHandleCQR(cqrTrans, "register participant")) {
            if(this.getParams(cqr)) {
                    ICommand command = CommandFactory.getRegisterParticipantCommand();
                    command.setParam("participant", participant);
                    command.setParam("type", type);
                    return command;
            }

            printMessage("Syntax error: 'register participant [name] with type [normal | intruder]");
            return null;
        }
        else
            return super.interpret(cqr);
    }
}
