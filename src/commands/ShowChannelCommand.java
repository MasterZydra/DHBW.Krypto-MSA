package commands;
//3894913
import configuration.RuntimeStorage;
import gui.GuiController;
import persistence.IMsaDB;
import persistence.dataModels.Channel;

import java.util.List;

public class ShowChannelCommand extends CqrCommand {

    GuiController gui = RuntimeStorage.instance.guiController;
    IMsaDB db = RuntimeStorage.instance.db;

    @Override
    public void execute() {
        List<Channel> channels = db.getChannels();
        for (Channel channel: channels
             ) {
            gui.displayText(channel.getName() + " | " + channel.getParticipantA().getName()
                    + " and " + channel.getParticipantB().getName() + "\n");
        }
    }
}
