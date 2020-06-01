package cqrInterpreter;

import commands.DecryptMessageCommand;
import commands.ICommand;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CqrInterpreter3Test {
    @Test
    public void CqrInterpreter3Match() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using RSA and keyfile myFile");
        assertTrue(command instanceof DecryptMessageCommand);
        assertEquals("Hello World", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
        assertEquals("myFile", command.getParam("keyfile"));
    }

    @Test
    public void CqrInterpreter3MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("deCrypT mEssAge \"Hello World\" uSiNg RSA aNd KEYfiLe myFile");
        assertTrue(command instanceof DecryptMessageCommand);
        assertEquals("Hello World", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
        assertEquals("myFile", command.getParam("keyfile"));
    }

    @Test
    public void CqrInterpreter3MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret(" decrypt message \"Hello World\" using RSA and keyfile myFile ");
        assertTrue(command instanceof DecryptMessageCommand);
        assertEquals("Hello World", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
        assertEquals("myFile", command.getParam("keyfile"));
    }

    @Test
    public void CqrInterpreter3MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using RSA");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter3MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using RSA and keyfile myFile other param");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter3MismatchQuote() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World using RSA and keyfile myFile");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter3MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using  and keyfile myFile");
        assertNull(command);
    }
}
