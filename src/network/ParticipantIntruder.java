package network;

import com.google.common.eventbus.Subscribe;
import configuration.Configuration;
import configuration.RuntimeStorage;
import cryptography.CryptoAlgorithm;
import cryptography.CryptoLoader;
import cryptography.CryptoMethod;
import gui.GuiController;
import persistence.IMsaDB;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParticipantIntruder extends Participant {
    public ParticipantIntruder(String name) {
        super(name);
    }

    @Override
    @Subscribe
    public void receiveMessage(MessageEvent event){
        GuiController gui = RuntimeStorage.instance.guiController;
        IMsaDB db = RuntimeStorage.instance.db;
        db.insertPostboxMessage(event.getReceiver(), event.getSender(), "unknown" );

        CryptoLoader loader = new CryptoLoader();
        String decrypted=event.getMessage();
        try {
            loader.createCryptographyMethod(CryptoAlgorithm.valueOfCaseIgnore(event.getAlgorithm()), CryptoMethod.DECRYPT);
            Method method = loader.getCryptoMethod();
            //TODO: add cracking attempt for 30 seconds
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        db.insertPostboxMessage(event.getReceiver(), event.getSender(), decrypted );
        gui.displayText(event.getReceiver() + " received new message");
    }
}
