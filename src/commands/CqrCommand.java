package commands;

import java.util.HashMap;
import java.util.Map;

public abstract class CqrCommand implements ICommand {
    private Map<String, String> params = new HashMap();

    public void setParam(String name, String value) {
        this.params.put(name, value);
    }

    public String getParam(String name) {
        return this.params.get(name);
    }

    public abstract void execute();
}
