package main.cqrInterpreter;

public class CqrInterpreter1 extends CqrInterpreter{
    public CqrInterpreter1(CqrInterpreter successor) {
        this.setSuccessor(successor);
    }

    @Override
    public void interprete(String cqr) {
        if (canHandleCQR(cqr, "show algorithm")) {

        }
        else
            super.interprete(cqr);
    }
}
