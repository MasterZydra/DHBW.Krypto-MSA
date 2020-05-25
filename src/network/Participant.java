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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Participant {
    protected String name;

    public Participant(String name) {
        this.name = name;
    }

    protected boolean isReceiver(MessageEvent messageEvent) {
        return this.name.equalsIgnoreCase(messageEvent.getReceiver());
    }

    @Subscribe
    public void receiveMessage(MessageEvent event){
        GuiController gui = RuntimeStorage.instance.guiController;
        IMsaDB db = RuntimeStorage.instance.db;
        if (!event.getReceiver().equals(this.name)) {
            return;
        }
        CryptoLoader loader = new CryptoLoader();
        String decrypted;
        try {
            loader.createCryptographyMethod(CryptoAlgorithm.valueOfCaseIgnore(event.getAlgorithm()), CryptoMethod.DECRYPT);
            Method method = loader.getCryptoMethod();
            File file = new File(Configuration.instance.getKeyFilePath + event.getKeyfile());
            decrypted = (String) method.invoke(loader.getPort(), event.getMessage(), file);
            System.out.println(this.name +" received message from " + event.getSender());
            System.out.println("message:"+event.getMessage() + " decrypted:"+decrypted);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }
        db.insertPostboxMessage(event.getReceiver(), event.getSender(), decrypted );
        gui.displayText(event.getReceiver() + " received new message");
    }


}
