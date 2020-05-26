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
        loader.createCryptographyMethod(CryptoAlgorithm.RSA, CryptoMethod.ENCRYPT);

        try {
            Method method = loader.getCryptoMethod();
            File file = new File(Configuration.instance.getKeyFilePath + "rsa128.json");
            String encrypted = (String) method.invoke(loader.getPort(),  "Hallo wie geht es Ihnen heute?", file); // "Lepps${mi$kilx$iw$Mlrir$liyxiC", file);//
            System.out.println("Encr: " + encrypted);
            loader.createCryptographyMethod(CryptoAlgorithm.RSA, CryptoMethod.DECRYPT);
            method = loader.getCryptoMethod();
            encrypted = (String) method.invoke(loader.getPort(),  encrypted, file); // "Lepps${mi$kilx$iw$Mlrir$liyxiC", file);//
            System.out.println("Decr: " + encrypted);

            loader.createCrackerMethod(CryptoAlgorithm.Shift);
            method = loader.getCryptoMethod();
            String decrypted = (String) method.invoke(loader.getPort(),  encrypted);
            System.out.println(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
