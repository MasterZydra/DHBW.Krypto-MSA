import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: 6439456
 */

public class ShiftCrackerTest {
    private ShiftCracker.Port port = ShiftCracker.getInstance().port;

    @Test
    public void crackHalloWieGehtEsIhnen() {
        String decrypted = this.port.decrypt("Lepps ami kilx iw Mlrir");
        assertEquals("Hallo wie geht es Ihnen", decrypted);
    }

    @Test
    public void crackEsIstWiederMalSoweit() {
        String decrypted = this.port.decrypt("Ao eop seazan iwh oksaep. Sen sqnzaj cadwygp!");
        assertEquals("Es ist wieder mal soweit. Wir wurden gehackt!", decrypted);
    }

    @Test
    public void crackDieMeistenKurzenNachrichten() {
        String decrypted = this.port.decrypt("Nso woscdox uebjox Xkmrbsmrdox gobnox xsmrd uybboud fyw Mbkmuob qoxkmud, nk nso Pboaeoxj fyx k, o, s, y exn e xsmrd yzdswkv scd.");
        assertEquals("Die meisten kurzen Nachrichten werden nicht korrekt vom Cracker genackt, da die Frequenz von a, e, i, o und u nicht optimal ist.", decrypted);
    }

    @Test
    public void crackDieCrackbarkeitEinerNachricht() {
        String decrypted = this.port.decrypt("Xcy Wluwevuleycn ychyl Huwblcwbn bähan pih xyl Uhtubf xyl Ogfuony uv.");
        assertEquals("Die Crackbarkeit einer Nachricht hängt von der Anzahl der Umlaute ab.", decrypted);
    }
}