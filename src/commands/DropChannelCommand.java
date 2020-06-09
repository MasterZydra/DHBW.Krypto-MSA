package commands;
//3894913
import configuration.Configuration;
import configuration.RuntimeStorage;
import gui.GuiController;
import network.INetwork;
import persistence.IMsaDB;

public class DropChannelCommand extends CqrCommand {

    @Override
    public void execute() {
        Configuration cfg = Configuration.instance;
        GuiController gui = RuntimeStorage.instance.guiController;
        INetwork net = RuntimeStorage.instance.network;
        IMsaDB db = RuntimeStorage.instance.db;
        String channelName = getParam("channelName");
        if(db.channelExists(channelName)){
            db.dropChannel(channelName);
            net.removeChannel(channelName);
            gui.displayText("channel " + channelName + " deleted");
        } else {
            gui.displayText("unknown channel " + channelName);
        }
    }
}
