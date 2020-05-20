package network;

import com.google.common.eventbus.Subscribe;

public class ParticipantDefault extends Participant {
    public ParticipantDefault(String name) {
        super(name);
    }

    @Subscribe
    public void receiveMessage(MessageEvent event){
        System.out.println(this.name + " received " + event.getMessage());
    }
}
