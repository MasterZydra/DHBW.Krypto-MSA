import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: 6439456
 */

public class ShiftTest {
    public final String ud = System.getProperty("user.dir");
    public final String fs = System.getProperty("file.separator");

    private Shift.Port port = Shift.getInstance().port;
    private File file = new File(ud + fs + "test" + fs + "keyfile.json");

    @Test
    public void encryptLettersOnly() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo wie geht es Ihnen", file);
        assertEquals("Lepps ami kilx iw Mlrir", encrypted);
    }

    @Test
    public void encryptLettersOnlyAlphabet() throws FileNotFoundException {
        String decrypted = this.port.encrypt("abcdefghijklmnopqrstuvwxyz", file);
        assertEquals("efghijklmnopqrstuvwxyzabcd", decrypted);
    }

    @Test
    public void encryptLettersWithSpecialCharacter() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo, wie geht es Ihnen. Gut?", file);
        assertEquals("Lepps, ami kilx iw Mlrir. Kyx?", encrypted);
    }

    @Test
    public void encryptLettersWithNumbers() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo, heute ist das Jahr 2020", file);
        assertEquals("Lepps, liyxi mwx hew Nelv 2020", encrypted);
    }

    @Test
    public void decryptLettersOnly() throws FileNotFoundException {
        String decrypted = this.port.decrypt("Lepps ami kilx iw Mlrir", file);
        assertEquals("Hallo wie geht es Ihnen", decrypted);
    }

    @Test
    public void decryptLettersOnlyAlphabet() throws FileNotFoundException {
        String decrypted = this.port.decrypt("efghijklmnopqrstuvwxyzabcd", file);
        assertEquals("abcdefghijklmnopqrstuvwxyz", decrypted);
    }

    @Test
    public void decryptLettersWithSpecialCharacter() throws FileNotFoundException {
        String decrypted = this.port.decrypt("Lepps, ami kilx iw Mlrir. Kyx?", file);
        assertEquals("Hallo, wie geht es Ihnen. Gut?", decrypted);
    }

    @Test
    public void decryptLettersWithNumbers() throws FileNotFoundException {
        String decrypted = this.port.decrypt("Lepps, liyxi mwx hew Nelv 2020", file);
        assertEquals("Hallo, heute ist das Jahr 2020", decrypted);
    }

    @Test
    public void fileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            String decrypt = this.port.decrypt("some message", new File("non existing file.json"));
        });
    }
}