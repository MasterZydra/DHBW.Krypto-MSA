package commands;

import java.util.HashMap;
import java.util.Map;

public abstract class CqrCommand implements ICommand {
    private Map<String, String> params = new HashMap();

    public void setParam(String name, String value) {
        this.params.put(name, value);
    }

    public String getParam(String name) {
        if (this.params.containsKey(name))
            return this.params.get(name);
        else
            return null;
    }

    public abstract void execute();
}
