package cqrInterpreter;

import commands.RegisterParticipantCommand;
import commands.ICommand;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CqrInterpreter5Test {
    @Test
    public void CqrInterpreter5Match() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with type normal");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals("partOne", command.getParam("participant"));
        assertEquals("normal", command.getParam("type"));
    }

    @Test
    public void CqrInterpreter5MatchIntruder() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with type intruder");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals("partOne", command.getParam("participant"));
        assertEquals("intruder", command.getParam("type"));
    }

    @Test
    public void CqrInterpreter5MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("rEgIsTeR participant partOne wITh TYpE nOrmAl");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals("partOne", command.getParam("participant"));
        assertEquals("normal", command.getParam("type"));
    }

    @Test
    public void CqrInterpreter5MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret(" register participant partOne with type normal ");
        assertTrue(command instanceof RegisterParticipantCommand);
        assertEquals("partOne", command.getParam("participant"));
        assertEquals("normal", command.getParam("type"));
    }

    @Test
    public void CqrInterpreter5MismatchTypo() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with types intruder");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter5MismatchToShort() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter5MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant partOne with types intruder and admin");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter5MismatchEmptyParam() {
        CqrInterpreter interpreter = new CqrInterpreter5(null);
        ICommand command = interpreter.interpret("register participant  with types intruder");
        assertNull(command);
    }
}
