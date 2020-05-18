package CqrInterpreter;

import commands.DecryptMessageCommand;
import commands.ICommand;
import cqrInterpreter.CqrInterpreter;
import cqrInterpreter.CqrInterpreter3;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CqrInterpreter3Test {
    @Test
    @Order(1)
    public void CqrInterpreter3Match() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using RSA and keyfile myFile");
        assertTrue(command instanceof DecryptMessageCommand);
        assertEquals(command.getParam("message"), "Hello World");
        assertEquals(command.getParam("algorithm"), "RSA");
        assertEquals(command.getParam("keyfile"), "myFile");
    }

    @Test
    @Order(2)
    public void CqrInterpreter3MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("deCrypT mEssAge \"Hello World\" uSiNg RSA aNd KEYfiLe myFile");
        assertTrue(command instanceof DecryptMessageCommand);
        assertEquals(command.getParam("message"), "Hello World");
        assertEquals(command.getParam("algorithm"), "RSA");
        assertEquals(command.getParam("keyfile"), "myFile");
    }

    @Test
    @Order(3)
    public void CqrInterpreter3MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret(" decrypt message \"Hello World\" using RSA and keyfile myFile ");
        assertTrue(command instanceof DecryptMessageCommand);
        assertEquals(command.getParam("message"), "Hello World");
        assertEquals(command.getParam("algorithm"), "RSA");
        assertEquals(command.getParam("keyfile"), "myFile");
    }

    @Test
    @Order(4)
    public void CqrInterpreter3MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using RSA");
        assertNull(command);
    }

    @Test
    @Order(5)
    public void CqrInterpreter3MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using RSA and keyfile myFile other param");
        assertNull(command);
    }

    @Test
    @Order(6)
    public void CqrInterpreter3MismatchQuote() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World using RSA and keyfile myFile");
        assertNull(command);
    }

    @Test
    @Order(7)
    public void CqrInterpreter3MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter3(null);
        ICommand command = interpreter.interpret("decrypt message \"Hello World\" using  and keyfile myFile");
        assertNull(command);
    }
}
