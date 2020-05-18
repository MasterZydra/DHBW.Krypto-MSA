package CqrInterpreter;

import main.commands.RegisterParticipantCommand;
import main.commands.ICommand;
import main.cqrInterpreter.CqrInterpreter;
import main.cqrInterpreter.CqrInterpreter5;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CqrInterpreter5Test {
    @Test
    @Order(1)
    public void CqrInterpreter5Match() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with type normal");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals(command.getParam("participant"), "partOne");
        assertEquals(command.getParam("type"), "normal");
    }

    @Test
    @Order(2)
    public void CqrInterpreter5MatchIntruder() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with type intruder");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals(command.getParam("participant"), "partOne");
        assertEquals(command.getParam("type"), "intruder");
    }

    @Test
    @Order(3)
    public void CqrInterpreter5MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("rEgIsTeR participant partOne wITh TYpE nOrmAl");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals(command.getParam("participant"), "partOne");
        assertEquals(command.getParam("type"), "nOrmAl");
    }

    @Test
    @Order(4)
    public void CqrInterpreter5MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret(" register participant partOne with type normal ");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals(command.getParam("participant"), "partOne");
        assertEquals(command.getParam("type"), "normal");
    }

    @Test
    @Order(5)
    public void CqrInterpreter5MismatchTypo() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with types intruder");
        assertNull(command);
    }

    @Test
    @Order(6)
    public void CqrInterpreter5MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne");
        assertNull(command);
    }

    @Test
    @Order(7)
    public void CqrInterpreter5MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with types intruder and admin");
        assertNull(command);
    }

    @Test
    @Order(8)
    public void CqrInterpreter5MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant  with types intruder");
        assertNull(command);
    }
}
