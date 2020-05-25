package cryptography;

import configuration.Configuration;

import java.io.File;
import java.lang.reflect.Method;

/*
 * Author: 6439456
 */

public class CryptoLoaderTest {
    public static void main(String[] args) {
        CryptoLoader loader = new CryptoLoader();
        loader.createCryptographyMethod(CryptoAlgorithm.Shift, CryptoMethod.ENCRYPT);

        try {
            Method method = loader.getCryptoMethod();
            File file = new File(Configuration.instance.getKeyFilePath + "shift.json");
            String encrypted = (String) method.invoke(loader.getPort(),  "Hallo wie geht es Ihnen heute?", file); // "Lepps${mi$kilx$iw$Mlrir$liyxiC", file);//

            loader.createCrackerMethod(CryptoAlgorithm.Shift);
            method = loader.getCryptoMethod();
            String decrypted = (String) method.invoke(loader.getPort(),  encrypted);
            System.out.println(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
