package configuration;

import cqrInterpreter.*;
import network.INetwork;
import network.Network;
import persistence.IMsaDB;
import persistence.MSA_HSQLDB;

public enum RuntimeStorage {
    instance;

    public gui.GuiController guiController;
    public IMsaDB db;
    public Configuration cfg ;
    public INetwork network ;
    public CqrInterpreter cqrInterpreterCoR;

    RuntimeStorage() {
    }

    public void init(){
        db = MSA_HSQLDB.instance;
        cfg = Configuration.instance;
        network = new Network();
        cqrInterpreterCoR = generateCqrChain();
    }

    private CqrInterpreter generateCqrChain() {
        CqrInterpreter10 cqr10 = new CqrInterpreter10(null);
        CqrInterpreter9 cqr9 = new CqrInterpreter9(cqr10);
        CqrInterpreter8 cqr8 = new CqrInterpreter8(cqr9);
        CqrInterpreter7 cqr7 = new CqrInterpreter7(cqr8);
        CqrInterpreter1 cqr1 = new CqrInterpreter1(cqr7);
        return cqr1;
    }
}
