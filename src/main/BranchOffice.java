package main;

import com.google.common.eventbus.Subscribe;
import main.network.MessageEvent;
import main.network.Participant;

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
