package cqrInterpreter;

import commands.CrackMessageCommand;
import commands.ICommand;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CqrInterpreter4Test {
    @Test
    @Order(1)
    public void CqrInterpreter4Match() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals(command.getParam("message"), "AbCDe");
        assertEquals(command.getParam("algorithm"), "RSA");
    }

    @Test
    @Order(2)
    public void CqrInterpreter4MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("cRaCk eNcRypTed mESsaGe \"AbCDe\" uSInG RSA");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals(command.getParam("message"), "AbCDe");
        assertEquals(command.getParam("algorithm"), "RSA");
    }

    @Test
    @Order(3)
    public void CqrInterpreter4MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret(" crack encrypted message \"AbCDe\" using RSA ");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals(command.getParam("message"), "AbCDe");
        assertEquals(command.getParam("algorithm"), "RSA");
    }

    @Test
    @Order(4)
    public void CqrInterpreter4MismatchTypo() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted messages \"AbCDe\" using RSA");
        assertNull(command);
    }

    @Test
    @Order(5)
    public void CqrInterpreter4MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using");
        assertNull(command);
    }

    @Test
    @Order(6)
    public void CqrInterpreter4MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA and SHIFT");
        assertNull(command);
    }

    @Test
    @Order(7)
    public void CqrInterpreter4MismatchQuote() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe using RSA");
        assertNull(command);
    }

    @Test
    @Order(8)
    public void CqrInterpreter4MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using   ");
        assertNull(command);
    }
}
