package commands;

import configuration.RuntimeStorage;
import network.INetwork;
import persistence.IMsaDB;
import persistence.dataModels.Channel;
import persistence.dataModels.Participant;
//3894913
public class CreateChannelCommand extends CqrCommand {
    @Override
    public void execute() {
        INetwork net = RuntimeStorage.instance.network;
        IMsaDB db = RuntimeStorage.instance.db;

        String name01 = getParam("participant01");
        String name02 = getParam("participant02");
        String channelName = getParam("channelName");

        if (db.channelExists(channelName)) {
            printMessage("channel " + channelName + " already exists");
            return;
        }

        Channel channel = db.getChannel(name01, name02);
        if (channel!=null){
            printMessage("communication channel between " + name01
                    + " and " + name02 + " already exists");
            return;
        }

        Participant participant01 = db.getParticipant(name01);
        Participant participant02 = db.getParticipant(name02);
        if (participant01==null) {
            printMessage("participant " + name01 + " not registered");
            return;
        }
        if (participant02==null) {
            printMessage("participant " + name02 + " not registered");
            return;
        }
        channel = new Channel(channelName, participant01, participant02);

        network.Participant netParticipant1 = net.getParticipant(name01);
        network.Participant netParticipant2 = net.getParticipant(name02);
        if (netParticipant1==null) netParticipant1=new network.ParticipantDefault(name01);
        if (netParticipant2==null) netParticipant2=new network.ParticipantDefault(name02);

        db.insertChannel(channel);
        net.createChannel(channelName,netParticipant1, netParticipant2);
    }
}
