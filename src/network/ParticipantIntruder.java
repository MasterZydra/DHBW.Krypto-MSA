package network;
//3894913
import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;
import configuration.Configuration;
import configuration.RuntimeStorage;
import cryptography.AlgorithmNotFoundException;
import cryptography.CryptoAlgorithm;
import cryptography.CryptoLoader;
import gui.GuiController;
import persistence.IMsaDB;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ParticipantIntruder extends Participant {
    public ParticipantIntruder(String name) {
        super(name);
    }

    @Override
    @Subscribe
    public void receiveMessage(MessageEvent event){
        //TODO: get cracked success status from cracking method
        AtomicBoolean crackedSuccess = new AtomicBoolean(true);
        GuiController gui = RuntimeStorage.instance.guiController;
        IMsaDB db = RuntimeStorage.instance.db;
        Configuration cfg = Configuration.instance;

        db.insertPostboxMessage(this.name, event.getSender(), "unknown" );

        gui.displayText(this.name + " received new message");

        CryptoLoader loader = new CryptoLoader();
        String encryptedMessage = event.getMessage();
        AtomicReference<String> decryptedMessage = new AtomicReference<>("");
        ForkJoinPool forkJoinPool = new ForkJoinPool(1);
        Stopwatch timer = Stopwatch.createUnstarted();

        try {
            forkJoinPool.submit(() ->
            {
                try {
                    loader.createCrackerMethod(CryptoAlgorithm.valueOfCaseIgnore(event.getAlgorithm()));
                } catch (AlgorithmNotFoundException e) {
                    e.printStackTrace();
                }
                Method method = loader.getCryptoMethod();
                try {
                    timer.start();
                    String filePath = cfg.getKeyFilePath + cfg.fs + event.getKeyfile();
                    File keyFile = new File(filePath);
                    decryptedMessage.set((String) method.invoke(loader.getPort(), event.getMessage(), keyFile ));
                    timer.stop();
                    System.out.println("cracking stopped after: " + timer.toString());
                    System.out.println("cracking timeout setting: "+ Configuration.instance.crackingMaxSeconds + "s");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    crackedSuccess.set(false);
                    System.out.println( "decryption call failed");
                }
            }).get((long) Configuration.instance.crackingMaxSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            crackedSuccess.set(false);
            System.out.println( "decryption attempt timed out");
        } catch (InterruptedException | ExecutionException e) {
            crackedSuccess.set(false);
            e.printStackTrace();
            System.out.println( "decryption attempt failed");
        }
        if (crackedSuccess.get()) {
            gui.displayText("intruder " + event.getReceiver()
                    + " cracked message from participant " + event.getSender() + " | " + decryptedMessage.get());
        } else {
            gui.displayText("intruder " + event.getReceiver()
                    + " | cracking message from participant " + event.getSender() + " failed");
        }

    }
}
