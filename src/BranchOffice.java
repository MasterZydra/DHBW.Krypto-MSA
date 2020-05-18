import com.google.common.eventbus.Subscribe;
import network.MessageEvent;
import network.Participant;

public class BranchOffice extends Participant {
    public BranchOffice(String name) {
        super(name);
    }

    @Subscribe
    protected void receive(MessageEvent messageEvent) {
        if (!isReceiver(messageEvent))
            return;

        System.out.println(this.name + " got message from " + messageEvent.getReceiver());
    }
}
