import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Scanner;

/*
 * Author: 6439456
 */

public class RSA {
    private static RSA instance = new RSA();

    private Key key;

    public Port port;

    private RSA() {
        port = new Port();
    }

    public static RSA getInstance() {
        return instance;
    }

    public class Port implements IRsaAlgorithm {
        public String encrypt(String plainMessage, File publicKeyfile) throws FileNotFoundException {
            return encryptMessage(plainMessage, publicKeyfile);
        }

        public String decrypt(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
            return decryptMessage(encryptedMessage, privateKeyfile);
        }
    }

    private String encryptMessage(String plainMessage, File publicKeyfile) throws FileNotFoundException {
        readPublicKeyFile(publicKeyfile);

        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        byte[] encrypted = crypt(new BigInteger(bytes), key).toByteArray();
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decryptMessage(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
        readPrivateKeyFile(privateKeyfile);

        byte[] cipher = Base64.getDecoder().decode(encryptedMessage);
        byte[] msg = crypt(new BigInteger(cipher), key).toByteArray();
        return new String(msg);
    }

    private BigInteger crypt(BigInteger message, Key key) {
        return message.modPow(key.getE(), key.getN());
    }

    private void readPrivateKeyFile(File keyfile) throws FileNotFoundException {
        readKeyFile(keyfile, "d");
    }

    private void readPublicKeyFile(File keyfile) throws FileNotFoundException {
        readKeyFile(keyfile);
    }

    private void readKeyFile(File keyfile) throws FileNotFoundException {
        readKeyFile(keyfile, "e");
    }

    private void readKeyFile(File keyfile, String eReplacement) throws FileNotFoundException {
        /*
        Example:
        {
          "n": 7448411,
          "e": 7442947,
          "d": 4465771
        }
         */
        BigInteger n = null;
        BigInteger e = null;

        Scanner scanner = new Scanner(keyfile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("\"n\":")) {
                n = getParam(line);
            }
            else if (line.contains("\"" + eReplacement + "\":")) {
                e = getParam(line);
            }
        }
        
        this.key = new Key(n, e);
    }

    private BigInteger getParam(String input) {
        String[] lineParts = input.split(":");
        String line = lineParts[1];
        line = line.replace(",", "").trim();
        return new BigInteger(line);
    }
}