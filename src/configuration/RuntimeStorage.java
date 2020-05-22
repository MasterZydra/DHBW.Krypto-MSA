package configuration;

import cqrInterpreter.CqrInterpreter;
import network.INetwork;
import network.Network;
import persistence.IMsaDB;
import persistence.MSA_HSQLDB;

public enum RuntimeStorage {
    instance;

    public gui.GuiController guiController;
    public IMsaDB db = MSA_HSQLDB.instance;
    public Configuration cfg = Configuration.instance;
    public INetwork network = new Network();
    public CqrInterpreter cqrInterpreterCoR;

}
