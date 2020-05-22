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
            char character = (char) plainMessage.codePointAt(i);
            character = shiftChar(character, key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private String decryptMessage(String encryptedMessage, File keyfile) {
        this.getKey(keyfile);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char character = (char) encryptedMessage.codePointAt(i);
            character = shiftChar(character, -key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private char shiftChar(char character, int shiftKey) {
        int offSet = 0;
        if (character > 64 && character < 91)
            offSet = 65;
        else if (character > 96 && character < 123)
            offSet = 97;

        if (offSet != 0) {
            if (shiftKey > 0)
                character = (char) (((int) character + shiftKey - offSet) % 26 + offSet);
            else {
                character = (char) ((int) character + shiftKey - offSet);
                int mul = Math.abs(character) / 26;
                character = (char) ((int) character + 26 * mul);
                character = (char) ((int) character % 26 + offSet);
            }
        }
        return character;
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
