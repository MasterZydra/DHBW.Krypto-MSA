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
        CqrInterpreter6 cqr6 = new CqrInterpreter6(cqr7);
        CqrInterpreter5 cqr5 = new CqrInterpreter5(cqr6);
        CqrInterpreter4 cqr4 = new CqrInterpreter4(cqr5);
        CqrInterpreter3 cqr3 = new CqrInterpreter3(cqr4);
        CqrInterpreter2 cqr2 = new CqrInterpreter2(cqr3);
        CqrInterpreter1 cqr1 = new CqrInterpreter1(cqr2);
        return cqr1;
    }
}
