import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Shift {
    private int key;

    private static Shift instance = new Shift();

    public Port port;

    private Shift() {
        port = new Port();
    }

    public static Shift getInstance() {
        return instance;
    }

    public class Port implements IShiftAlgorithm {
        public String encrypt(String plainMessage, File keyfile) {
            return encryptMessage(plainMessage, keyfile);
        }

        public String decrypt(String encryptedMessage, File keyfile) {
            return decryptMessage(encryptedMessage, keyfile);
        }
    }

    private String encryptMessage(String plainMessage, File keyfile) {
        this.getKey(keyfile);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < plainMessage.length(); i++) {
            char character = (char) (plainMessage.codePointAt(i) + key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private String decryptMessage(String encryptedMessage, File keyfile) {
        this.getKey(keyfile);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char character = (char) (encryptedMessage.codePointAt(i) - key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private void getKey(File keyfile) {
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
                        key = Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
