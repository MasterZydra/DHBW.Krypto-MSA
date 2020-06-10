import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: 6439456
 */

public class RsaTest {
    public final String ud = System.getProperty("user.dir");
    public final String fs = System.getProperty("file.separator");

    private RSA.Port port = RSA.getInstance().port;
    private File file12 = new File(ud + fs + "test" + fs + "RSA12.json");
    private File file24 = new File(ud + fs + "test" + fs + "RSA24.json");
    private File file48 = new File(ud + fs + "test" + fs + "RSA48.json");
    private File file128 = new File(ud + fs + "test" + fs + "RSA128.json");
    private File file256 = new File(ud + fs + "test" + fs + "RSA256.json");

    @Test
    public void encryptLettersOnlyVeryShort() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Du", file12);
        assertEquals("LfF/", encrypted);
    }

    @Test
    public void decryptLettersOnlyVeryShort() throws FileNotFoundException {
        String decrypted = this.port.decrypt("LfF/", file12);
        assertEquals("Du", decrypted);
    }

    @Test
    public void encryptLettersOnlyShort() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo", file24);
        assertEquals("BqGfopSO", encrypted);
    }

    @Test
    public void decryptLettersOnlyShort() throws FileNotFoundException {
        String decrypted = this.port.decrypt("BqGfopSO", file24);
        assertEquals("Hallo", decrypted);
    }

    @Test
    public void encryptLettersOnlyMedium() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo wie", file48);
        assertEquals("XWYxNmdA2rSVRbwF", encrypted);
    }

    @Test
    public void decryptLettersOnlyMedium() throws FileNotFoundException {
        String decrypted = this.port.decrypt("XWYxNmdA2rSVRbwF", file48);
        assertEquals("Hallo wie", decrypted);
    }

    @Test
    public void encryptLettersOnlyLong() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo wie geht es Ihnen", file128);
        assertEquals("elpQZyMQ+/EPBIMkxyLdLWM0NJO9owpxA0GryZJ5scs=", encrypted);
    }

    @Test
    public void decryptLettersOnlyLong() throws FileNotFoundException {
        String decrypted = this.port.decrypt("elpQZyMQ+/EPBIMkxyLdLWM0NJO9owpxA0GryZJ5scs=", file128);
        assertEquals("Hallo wie geht es Ihnen", decrypted);
    }

    @Test
    public void encryptLettersOnlyAlphabet() throws FileNotFoundException {
        String encrypted = this.port.encrypt("abcdefghijklmnopqrstuvwxyz", file128);
        assertEquals("NnJ++X4jZ57H1ODd3pcp0wlGDzk5+RMpH15AMrCyhDc=", encrypted);
    }

    @Test
    public void decryptLettersOnlyAlphabet() throws FileNotFoundException {
        String decrypted = this.port.decrypt("NnJ++X4jZ57H1ODd3pcp0wlGDzk5+RMpH15AMrCyhDc=", file128);
        assertEquals("abcdefghijklmnopqrstuvwxyz", decrypted);
    }

    @Test
    public void encryptLettersWithSpecialCharacter() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo, wie geht es Ihnen. Gut?", file128);
        assertEquals("bI4uZRX498kGmE+rp/PO8bL20f8+NEJH5S2b2vPwo+g=", encrypted);
    }

    @Test
    public void decryptLettersWithSpecialCharacter() throws FileNotFoundException {
        String decrypted = this.port.decrypt("bI4uZRX498kGmE+rp/PO8bL20f8+NEJH5S2b2vPwo+g=", file128);
        assertEquals("Hallo, wie geht es Ihnen. Gut?", decrypted);
    }

    @Test
    public void encryptLettersWithNumbers() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Hallo, heute ist das Jahr 2020", file128);
        assertEquals("aa7nMxQ4wZyKNga5VRC4wHxvWEwNffSZG8erq0LnIeQ=", encrypted);
    }

    @Test
    public void decryptLettersWithNumbers() throws FileNotFoundException {
        String decrypted = this.port.decrypt("aa7nMxQ4wZyKNga5VRC4wHxvWEwNffSZG8erq0LnIeQ=", file128);
        assertEquals("Hallo, heute ist das Jahr 2020", decrypted);
    }

    @Test
    public void encryptPleaseEncrypt() throws FileNotFoundException {
        String encrypted = this.port.encrypt("Diese Nachricht bitte verschluesseln!", file256);
        assertEquals("JtzsIKkM/0o5aTuh7ZXMi5wzLwVbJMEg1sMdzIs4oeTxq3gXnacH5iut5rVylXbkkjj9ysZt/umam/YOwMy/5g==", encrypted);
    }

    @Test
    public void decryptPleaseEncrypt() throws FileNotFoundException {
        String decrypted = this.port.decrypt("JtzsIKkM/0o5aTuh7ZXMi5wzLwVbJMEg1sMdzIs4oeTxq3gXnacH5iut5rVylXbkkjj9ysZt/umam/YOwMy/5g==", file256);
        assertEquals("Diese Nachricht bitte verschluesseln!", decrypted);
    }

    @Test
    public void fileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            String decrypt = this.port.decrypt("some message", new File("non existing file.json"));
        });
    }
}