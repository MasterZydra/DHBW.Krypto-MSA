package test;

import main.commands.EncryptMessageCommand;
import main.commands.ICommand;
import main.cqrInterpreter.CqrInterpreter;
import main.cqrInterpreter.CqrInterpreter2;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CqrInterpreter2Test {
    @Test
    @Order(1)
    public void CqrInterpreter2Match() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using RSA and keyfile myFile");
        assertTrue(command instanceof EncryptMessageCommand);
        assertEquals(command.getParam("message"), "Hello World");
        assertEquals(command.getParam("algorithm"), "RSA");
        assertEquals(command.getParam("keyfile"), "myFile");
    }

    @Test
    @Order(2)
    public void CqrInterpreter2MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("enCrypT mEssAge \"Hello World\" uSiNg RSA aNd KEYfiLe myFile");
        assertTrue(command instanceof EncryptMessageCommand);
        assertEquals(command.getParam("message"), "Hello World");
        assertEquals(command.getParam("algorithm"), "RSA");
        assertEquals(command.getParam("keyfile"), "myFile");
    }

    @Test
    @Order(3)
    public void CqrInterpreter2MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret(" encrypt message \"Hello World\" using RSA and keyfile myFile ");
        assertTrue(command instanceof EncryptMessageCommand);
        assertEquals(command.getParam("message"), "Hello World");
        assertEquals(command.getParam("algorithm"), "RSA");
        assertEquals(command.getParam("keyfile"), "myFile");
    }

    @Test
    @Order(4)
    public void CqrInterpreter2MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using RSA");
        assertNull(command);
    }

    @Test
    @Order(5)
    public void CqrInterpreter2MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using RSA and keyfile myFile other param");
        assertNull(command);
    }

    @Test
    @Order(6)
    public void CqrInterpreter2MismatchQuote() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World using RSA and keyfile myFile");
        assertNull(command);
    }

    @Test
    @Order(7)
    public void CqrInterpreter2MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter2(null);
        ICommand command = interpreter.interpret("encrypt message \"Hello World\" using  and keyfile myFile");
        assertNull(command);
    }
}
