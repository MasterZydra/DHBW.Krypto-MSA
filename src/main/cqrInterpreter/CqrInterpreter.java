package main.cqrInterpreter;

public abstract class CqrInterpreter {
    private CqrInterpreter successor;

    public void interprete(String cqr) {
        if (getSuccessor() != null)
            getSuccessor().interprete(cqr);
        else
            System.out.println("Invalid command");
    }

    protected boolean canHandleCQR(String cqr, String cqrStart) {
        return cqr.startsWith(cqrStart);
    }

    public CqrInterpreter getSuccessor() {
        return this.successor;
    }

    public void setSuccessor(CqrInterpreter successor) {
        this.successor = successor;
    }
}
