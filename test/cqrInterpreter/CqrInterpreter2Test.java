package cqrInterpreter;

import commands.EncryptMessageCommand;
import commands.ICommand;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CqrInterpreter2Test {
    @Test
    public void CqrInterpreter2Match() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using RSA and keyfile myFile");
        assertTrue(command instanceof EncryptMessageCommand);
        assertEquals("Hello World", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
        assertEquals("myFile", command.getParam("keyfile"));
    }

    @Test
    public void CqrInterpreter2MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("enCrypT mEssAge \"Hello World\" uSiNg RSA aNd KEYfiLe myFile");
        assertTrue(command instanceof EncryptMessageCommand);
        assertEquals("Hello World", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
        assertEquals("myFile", command.getParam("keyfile"));
    }

    @Test
    public void CqrInterpreter2MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret(" encrypt message \"Hello World\" using RSA and keyfile myFile ");
        assertTrue(command instanceof EncryptMessageCommand);
        assertEquals("Hello World", command.getParam("message"));
        assertEquals("RSA", command.getParam("algorithm"));
        assertEquals("myFile", command.getParam("keyfile"));
    }

    @Test
    public void CqrInterpreter2MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using RSA");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter2MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using RSA and keyfile myFile other param");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter2MismatchQuote() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World using RSA and keyfile myFile");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter2MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using  and keyfile myFile");
        assertNull(command);
    }
}
