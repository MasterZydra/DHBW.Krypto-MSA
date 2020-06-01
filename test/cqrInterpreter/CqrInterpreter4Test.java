package cqrInterpreter;

import commands.CrackMessageCommand;
import commands.ICommand;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CqrInterpreter4Test {
    @Test
    public void CqrInterpreter4Match() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals("AbCDe", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
    }

    @Test
    public void CqrInterpreter4MatchWithFile() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA and keyfile file");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals("AbCDe", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
        assertEquals("file", command.getParam("keyfile"));
    }

    @Test
    public void CqrInterpreter4MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("cRaCk eNcRypTed mESsaGe \"AbCDe\" uSInG RSA");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals("AbCDe", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
    }

    @Test
    public void CqrInterpreter4MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret(" crack encrypted message \"AbCDe\" using RSA ");
        assertTrue(command instanceof CrackMessageCommand);
        assertEquals("AbCDe", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
    }

    @Test
    public void CqrInterpreter4MismatchTypo() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted messages \"AbCDe\" using RSA");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter4MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter4MismatchNofile() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA and keyfile");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter4MismatchWithFileToLong() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA and keyfile file and");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter4MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using RSA and SHIFT");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter4MismatchQuote() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe using RSA");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter4MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter4(null);
        ICommand command = interpreter.interpret("crack encrypted message \"AbCDe\" using   ");
        assertNull(command);
    }
}
