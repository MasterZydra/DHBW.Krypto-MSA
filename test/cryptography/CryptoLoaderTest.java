package cryptography;

import configuration.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class CryptoLoaderTest {
    public static void main(String[] args) {
        CryptoLoader loader = new CryptoLoader();
        Configuration.instance.cryptoAlgorithm = CryptoAlgorithm.Shift;
        loader.createCryptographyMethod(CryptoMethod.ENCRYPT);

        Method method = loader.getCryptoMethod();
        File file = new File(Configuration.instance.getKeyFilePath + "shift.json");

        int key = getKey(file);

        String res;
        try {
            encryptMessage("abcde", file);
            res = (String) method.invoke(loader.getPort(),  "abz", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encryptMessage(String plainMessage, File keyfile) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < plainMessage.length(); i++) {
            char character = (char) (plainMessage.codePointAt(i) + getKey(keyfile));
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private static int getKey(File keyfile) {
        /*
        Example:
        {
	        "key": 4
        }
         */
        try {
            Scanner scanner = new Scanner(keyfile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("\"key\":")) {
                    String[] lineParts = line.split(":");
                    line = lineParts[1].trim();
                    try {
                        return Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
