package cryptography;

import configuration.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: 6439456
 */

public class CryptoLoaderTest {
    CryptoLoader loader = new CryptoLoader();

    File fileShift6 = new File(Configuration.instance.getKeyFilePath + "shift6.json");
    File fileRSA24 = new File(Configuration.instance.getKeyFilePath + "rsa24.json");
    File fileRSA256 = new File(Configuration.instance.getKeyFilePath + "rsa256.json");

    @Test
    public void shiftEncryption() {
        String encryptedMsg = "";
        try {
            loader.createCryptographyMethod(CryptoAlgorithm.Shift, CryptoMethod.ENCRYPT);
            encryptedMsg = (String) loader.getCryptoMethod().invoke(loader.getPort(), "Diese Nachricht bitte verschlüsseln!", fileShift6);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Jokyk Tginxoinz hozzk bkxyinrüyykrt!", encryptedMsg);
    }

    @Test
    public void shiftDecryption() {
        String decryptedMsg = "";
        try {
            loader.createCryptographyMethod(CryptoAlgorithm.Shift, CryptoMethod.DECRYPT);
            decryptedMsg = (String) loader.getCryptoMethod().invoke(loader.getPort(), "Kot Hklknr but Paroay Igkygx oyz kotmkzxullkt.", fileShift6);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Ein Befehl von Julius Caesar ist eingetroffen.", decryptedMsg);
    }

    @Test
    public void shiftEnDecryptionFileNotFound() throws AlgorithmNotFoundException {
        loader.createCryptographyMethod(CryptoAlgorithm.Shift, CryptoMethod.DECRYPT);
        Exception e = assertThrows(InvocationTargetException.class, () -> {
            loader.getCryptoMethod().invoke(loader.getPort(),"some message", new File("non existing file.json"));
        });
        assertTrue(e.getCause() instanceof FileNotFoundException);
    }

    @Test
    public void shiftCracker() {
        String crackedMsg = "";
        try {
            loader.createCrackerMethod(CryptoAlgorithm.Shift);
            crackedMsg = (String) loader.getCryptoMethod().invoke(loader.getPort(), "Tmxxa. Yquz Zmyq uef Vgxuge Omqemd.", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Hallo. Mein Name ist Julius Caesar.", crackedMsg);
    }

    @Test
    public void rsaEncryption() {
        String encryptedMsg = "";
        try {
            loader.createCryptographyMethod(CryptoAlgorithm.RSA, CryptoMethod.ENCRYPT);
            encryptedMsg = (String) loader.getCryptoMethod().invoke(loader.getPort(), "Diese Nachricht bitte verschluesseln!", fileRSA256);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("V3PAduevQaaviJ6/37YTc0IxrxSYZImfyxfpcdaMn5AH1MF5YIT6DG1NaDkZDgkcuUSMmT0MS7ylpizPw60oYQ==", encryptedMsg);
    }

    @Test
    public void rsaDecryption() {
        String decryptedMsg = "";
        try {
            loader.createCryptographyMethod(CryptoAlgorithm.RSA, CryptoMethod.DECRYPT);
            decryptedMsg = (String) loader.getCryptoMethod().invoke(loader.getPort(), "V3PAduevQaaviJ6/37YTc0IxrxSYZImfyxfpcdaMn5AH1MF5YIT6DG1NaDkZDgkcuUSMmT0MS7ylpizPw60oYQ==", fileRSA256);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Diese Nachricht bitte verschluesseln!", decryptedMsg);
    }

    @Test
    public void rsaEnDecryptionFileNotFound() throws AlgorithmNotFoundException {
        loader.createCryptographyMethod(CryptoAlgorithm.RSA, CryptoMethod.DECRYPT);
        Exception e = assertThrows(InvocationTargetException.class, () -> {
            loader.getCryptoMethod().invoke(loader.getPort(),"some message", new File("non existing file.json"));
        });
        assertTrue(e.getCause() instanceof FileNotFoundException);
    }

    @Test
    public void rsaCracker() {
        String crackedMsg = "";
        try {
            loader.createCrackerMethod(CryptoAlgorithm.RSA);
            crackedMsg = (String) loader.getCryptoMethod().invoke(loader.getPort(), "BqGfopSO", fileRSA24);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Hallo", crackedMsg);
    }

    @Test
    public void rsaCrackerFileNotFound() throws AlgorithmNotFoundException {
        loader.createCrackerMethod(CryptoAlgorithm.RSA);
        Exception e = assertThrows(InvocationTargetException.class, () -> {
            loader.getCryptoMethod().invoke(loader.getPort(),"some message", new File("non existing file.json"));
        });
        assertTrue(e.getCause() instanceof FileNotFoundException);
    }
}
