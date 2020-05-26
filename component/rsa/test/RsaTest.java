import java.io.File;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: 6439456
 */

public class RsaTest {
    public final String ud = System.getProperty("user.dir");
    public final String fs = System.getProperty("file.separator");

    private RSA.Port port = RSA.getInstance().port;
    private File file48 = new File(ud + fs + "test" + fs + "RSA48.json");
    private File file128 = new File(ud + fs + "test" + fs + "RSA128.json");

    @Test
    public void encryptLettersOnly1() {
        String encrypted = this.port.encrypt("Hallo wie", file48);
        assertEquals("XWYxNmdA2rSVRbwF", encrypted);
    }

    @Test
    public void decryptLettersOnly1() {
        String decrypted = this.port.decrypt("XWYxNmdA2rSVRbwF", file48);
        assertEquals("Hallo wie", decrypted);
    }

    @Test
    public void encryptLettersOnly() {
        String encrypted = this.port.encrypt("Hallo wie geht es Ihnen", file128);
        assertEquals("elpQZyMQ+/EPBIMkxyLdLWM0NJO9owpxA0GryZJ5scs=", encrypted);
    }

    @Test
    public void decryptLettersOnly() {
        String decrypted = this.port.decrypt("elpQZyMQ+/EPBIMkxyLdLWM0NJO9owpxA0GryZJ5scs=", file128);
        assertEquals("Hallo wie geht es Ihnen", decrypted);
    }

    @Test
    public void encryptLettersOnlyAlphabet() {
        String encrypted = this.port.encrypt("abcdefghijklmnopqrstuvwxyz", file128);
        assertEquals("NnJ++X4jZ57H1ODd3pcp0wlGDzk5+RMpH15AMrCyhDc=", encrypted);
    }

    @Test
    public void decryptLettersOnlyAlphabet() {
        String decrypted = this.port.decrypt("NnJ++X4jZ57H1ODd3pcp0wlGDzk5+RMpH15AMrCyhDc=", file128);
        assertEquals("abcdefghijklmnopqrstuvwxyz", decrypted);
    }

    @Test
    public void encryptLettersWithSpecialCharacter() {
        String encrypted = this.port.encrypt("Hallo, wie geht es Ihnen. Gut?", file128);
        assertEquals("bI4uZRX498kGmE+rp/PO8bL20f8+NEJH5S2b2vPwo+g=", encrypted);
    }

    @Test
    public void decryptLettersWithSpecialCharacter() {
        String decrypted = this.port.decrypt("bI4uZRX498kGmE+rp/PO8bL20f8+NEJH5S2b2vPwo+g=", file128);
        assertEquals("Hallo, wie geht es Ihnen. Gut?", decrypted);
    }

    @Test
    public void encryptLettersWithNumbers() {
        String encrypted = this.port.encrypt("Hallo, heute ist das Jahr 2020", file128);
        assertEquals("aa7nMxQ4wZyKNga5VRC4wHxvWEwNffSZG8erq0LnIeQ=", encrypted);
    }

    @Test
    public void decryptLettersWithNumbers() {
        String decrypted = this.port.decrypt("aa7nMxQ4wZyKNga5VRC4wHxvWEwNffSZG8erq0LnIeQ=", file128);
        assertEquals("Hallo, heute ist das Jahr 2020", decrypted);
    }
}