package network;

import com.google.common.eventbus.Subscribe;
import configuration.RuntimeStorage;
import cryptography.CryptoAlgorithm;
import cryptography.CryptoLoader;
import gui.GuiController;
import persistence.IMsaDB;

import java.lang.reflect.Method;

public class ParticipantIntruder extends Participant {
    public ParticipantIntruder(String name) {
        super(name);
    }

    @Override
    @Subscribe
    public void receiveMessage(MessageEvent event){
        //TODO: get cracked success status from cracking method
        boolean crackedSuccess = false;
        GuiController gui = RuntimeStorage.instance.guiController;
        IMsaDB db = RuntimeStorage.instance.db;
        db.insertPostboxMessage(event.getReceiver(), event.getSender(), "unknown" );

        CryptoLoader loader = new CryptoLoader();
        String encryptedMessage = event.getMessage();
        String decryptedMessage = "";
        try {
            loader.createCrackerMethod(CryptoAlgorithm.valueOfCaseIgnore(event.getAlgorithm()));
            Method method = loader.getCryptoMethod();
            //TODO: add cracking attempt limit of 30 seconds & set crackedSuccess
            decryptedMessage = (String) method.invoke(loader.getPort(), event.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        db.insertPostboxMessage(event.getReceiver(), event.getSender(), "unknown");
        if (crackedSuccess) {
            gui.displayText("intruder " + event.getReceiver()
                    + " cracked message from participant " + event.getSender() + " | " + decryptedMessage);
        } else {
            gui.displayText("intruder " + event.getReceiver()
                    + "| cracking message from participant " + event.getSender() + " failed");
        }
        gui.displayText(event.getReceiver() + " received new message");
    }
}
