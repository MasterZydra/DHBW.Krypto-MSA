package cryptography;

import java.io.File;
import java.util.concurrent.Callable;

/*
 * Author: 6439456
 */

public class CrackTask implements Callable<String> {
    private String message;
    private String algorithm;
    private File keyfile;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setKeyfile(File keyfile) {
        this.keyfile = keyfile;
    }

    @Override
    public String call() throws Exception {
        CryptoLoader loader = new CryptoLoader();
        // Load component
        loader.createCrackerMethod(CryptoAlgorithm.valueOfCaseIgnore(algorithm));

        // Decrypt message
        String cracked = (String) loader.getCryptoMethod().invoke(loader.getPort(), message, keyfile);
        return cracked;
    }
}
