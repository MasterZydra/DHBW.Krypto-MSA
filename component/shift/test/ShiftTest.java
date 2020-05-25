import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: 6439456
 */

public class ShiftTest {
    public final String ud = System.getProperty("user.dir");
    public final String fs = System.getProperty("file.separator");

    private Shift.Port port;
    private File file = new File(ud + fs + "test" + fs + "keyfile.json");

    @BeforeEach
    private void init() {
        this.port = Shift.getInstance().port;
    }

    @Test
    public void encryptLettersOnly() {
        String encrypted = this.port.encrypt("Hallo wie geht es Ihnen", file);
        assertEquals("Lepps ami kilx iw Mlrir", encrypted);
    }

    @Test
    public void encryptLettersOnlyAlphabet() {
        String decrypted = this.port.encrypt("abcdefghijklmnopqrstuvwxyz", file);
        assertEquals("efghijklmnopqrstuvwxyzabcd", decrypted);
    }

    @Test
    public void encryptLettersWithSpecialCharacter() {
        String encrypted = this.port.encrypt("Hallo, wie geht es Ihnen. Gut?", file);
        assertEquals("Lepps, ami kilx iw Mlrir. Kyx?", encrypted);
    }

    @Test
    public void encryptLettersWithNumbers() {
        String encrypted = this.port.encrypt("Hallo, heute ist das Jahr 2020", file);
        assertEquals("Lepps, liyxi mwx hew Nelv 2020", encrypted);
    }

    @Test
    public void decryptLettersOnly() {
        String decrypted = this.port.decrypt("Lepps ami kilx iw Mlrir", file);
        assertEquals("Hallo wie geht es Ihnen", decrypted);
    }

    @Test
    public void decryptLettersOnlyAlphabet() {
        String decrypted = this.port.decrypt("efghijklmnopqrstuvwxyzabcd", file);
        assertEquals("abcdefghijklmnopqrstuvwxyz", decrypted);
    }

    @Test
    public void decryptLettersWithSpecialCharacter() {
        String decrypted = this.port.decrypt("Lepps, ami kilx iw Mlrir. Kyx?", file);
        assertEquals("Hallo, wie geht es Ihnen. Gut?", decrypted);
    }

    @Test
    public void decryptLettersWithNumbers() {
        String decrypted = this.port.decrypt("Lepps, liyxi mwx hew Nelv 2020", file);
        assertEquals("Hallo, heute ist das Jahr 2020", decrypted);
    }
}