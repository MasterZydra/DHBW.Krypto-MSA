package configuration;

import persistence.IMsaDB;
import persistence.MSA_HSQLDB;

public enum RuntimeStorage {
    instance;

    public gui.GuiController guiController;
    public IMsaDB db = MSA_HSQLDB.instance;
    public Configuration cfg = Configuration.instance;

}
