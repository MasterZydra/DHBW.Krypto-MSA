import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*
 * Author: 6439456
 */

public class RSACracker {
    private static RSACracker instance = new RSACracker();

    private BigInteger e = BigInteger.ZERO;
    private BigInteger n = BigInteger.ZERO;
    
    public Port port;

    private RSACracker() {
        port = new Port();
    }

    public static RSACracker getInstance() {
        return instance;
    }

    public class Port implements IRSACracker {
        public String decrypt(String encryptedMessage, File publicKeyfile) {
            return decryptMessage(encryptedMessage, publicKeyfile);
        }
    }

    private String decryptMessage(String encryptedMessage, File publicKeyfile) {
        readKeyFile(publicKeyfile);
        
        byte[] bytes = Base64.getDecoder().decode(encryptedMessage);
        
        try {
            byte[] plainBytes = execute(new BigInteger(bytes)).toByteArray();
            if (plainBytes == null)
                return "";
            return new String(plainBytes);
        } catch (RSACrackerException rsae) {
            System.out.println(rsae.getMessage());
        }
        return null;
    }

    private void readKeyFile(File keyfile) {
        /*
        Example:
        {
          "n": 7448411,
          "e": 7442947,
          "d": 4465771
        }
         */
        try {
            Scanner scanner = new Scanner(keyfile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("\"n\":")) {
                    n = getParam(line);
                }
                else if (line.contains("\"e\":")) {
                    e = getParam(line);
                }
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private BigInteger getParam(String input) {
        String[] lineParts = input.split(":");
        String line = lineParts[1];
        line = line.replace(",", "").trim();
        return new BigInteger(line);
    }
    
    private BigInteger execute(BigInteger cipher) throws RSACrackerException {
        BigInteger p, q, d;
        List<BigInteger> factorList = factorize(n);

        if (factorList == null)
            return null;

        if (factorList.size() != 2) {
            throw new RSACrackerException("cannot determine factors p and q");
        }

        p = factorList.get(0);
        q = factorList.get(1);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(phi);
        return cipher.modPow(d, n);
    }

    public List<BigInteger> factorize(BigInteger n) {
        int loopCount = 0;

        BigInteger two = BigInteger.valueOf(2);
        List<BigInteger> factorList = new LinkedList<>();

        if (n.compareTo(two) < 0) {
            throw new IllegalArgumentException("must be greater than one");
        }

        while (n.mod(two).equals(BigInteger.ZERO)) {
            factorList.add(two);
            n = n.divide(two);
            try {
                Thread.sleep(0,1);
            } catch (InterruptedException interruptedException) {
                return null;
            }
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            BigInteger factor = BigInteger.valueOf(3);
            while (factor.multiply(factor).compareTo(n) <= 0) {
                if (n.mod(factor).equals(BigInteger.ZERO)) {
                    factorList.add(factor);
                    n = n.divide(factor);
                } else {
                    factor = factor.add(two);
                }
                try {
                    loopCount++;
                    if (loopCount > 1000) {
                        loopCount = 0;
                        Thread.sleep(0,1);
                    }
                } catch (InterruptedException interruptedException) {
                    return null;
                }
            }
            factorList.add(n);
        }

        return factorList;
    }
}