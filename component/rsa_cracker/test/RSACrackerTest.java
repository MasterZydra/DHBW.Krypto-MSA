import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: 6439456
 */

public class RSACrackerTest {
    public final String ud = System.getProperty("user.dir");
    public final String fs = System.getProperty("file.separator");

    private RSACracker.Port port = RSACracker.getInstance().port;
    
    private File file12 = new File(ud + fs + "test" + fs + "RSA12.json");
    private File file24 = new File(ud + fs + "test" + fs + "RSA24.json");
    
    @Test
    public void crackLength12() {
        String decryptedMsg = port.decrypt("LfF/", file12);
        assertEquals("Du", decryptedMsg);
    }

    @Test
    public void crackLength24() {
        String decryptedMsg = port.decrypt("BqGfopSO", file24);
        assertEquals("Hallo", decryptedMsg);
    }
}