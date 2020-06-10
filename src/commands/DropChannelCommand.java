package commands;
//3894913
import configuration.RuntimeStorage;
import network.INetwork;
import persistence.IMsaDB;

public class DropChannelCommand extends CqrCommand {

    @Override
    public void execute() {
        INetwork net = RuntimeStorage.instance.network;
        IMsaDB db = RuntimeStorage.instance.db;
        String channelName = getParam("channelName");
        if(db.channelExists(channelName)){
            db.dropChannel(channelName);
            net.removeChannel(channelName);
            printMessage("channel " + channelName + " deleted");
        } else {
            printMessage("unknown channel " + channelName);
        }
    }
}
