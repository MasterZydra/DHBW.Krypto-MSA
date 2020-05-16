package CqrInterpreter;

import main.commands.CrackMessageCommand;
import main.commands.ICommand;
import main.cqrInterpreter.CqrInterpreter;
import main.cqrInterpreter.CqrInterpreter4;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CqrInterpreter4Test {
    @Test
    @Order(1)
    public void CqrInterpreter3Match() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals(command.getParam("message"), "AbCDe");
        assertEquals(command.getParam("algorithm"), "RSA");
    }

    @Test
    @Order(2)
    public void CqrInterpreter3MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("cRaCk eNcRypTed mESsaGe \"AbCDe\" uSInG RSA");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals(command.getParam("message"), "AbCDe");
        assertEquals(command.getParam("algorithm"), "RSA");
    }

    @Test
    @Order(3)
    public void CqrInterpreter3MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret(" crack encrypted message \"AbCDe\" using RSA ");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals(command.getParam("message"), "AbCDe");
        assertEquals(command.getParam("algorithm"), "RSA");
    }

    @Test
    @Order(4)
    public void CqrInterpreter3MismatchTypo() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted messages \"AbCDe\" using RSA");
        assertNull(command);
    }

    @Test
    @Order(5)
    public void CqrInterpreter3MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using");
        assertNull(command);
    }

    @Test
    @Order(6)
    public void CqrInterpreter3MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA and SHIFT");
        assertNull(command);
    }

    @Test
    @Order(7)
    public void CqrInterpreter3MismatchQuote() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe using RSA");
        assertNull(command);
    }

    @Test
    @Order(8)
    public void CqrInterpreter3MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using   ");
        assertNull(command);
    }
}
