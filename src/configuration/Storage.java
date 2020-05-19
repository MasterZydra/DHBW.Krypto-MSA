package configuration;

import persistence.IMsaDB;
import persistence.MSA_HSQLDB;

public enum Storage {
    instance;

    public gui.GuiController guiController;
    public IMsaDB db = MSA_HSQLDB.instance;

}
