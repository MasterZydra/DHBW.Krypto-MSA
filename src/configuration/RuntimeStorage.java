package configuration;

import cqrInterpreter.CqrInterpreter;
import cqrInterpreter.CqrInterpreter1;
import cqrInterpreter.CqrInterpreter10;
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
        return new CqrInterpreter1(cqr10);
    }
}
