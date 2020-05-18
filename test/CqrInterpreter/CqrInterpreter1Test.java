package CqrInterpreter;

import commands.ICommand;
import commands.ShowAlgorithmCommand;
import cqrInterpreter.CqrInterpreter;
import cqrInterpreter.CqrInterpreter1;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CqrInterpreter1Test {
    @Test
    @Order(1)
    public void CqrInterpreter1Match() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("show algorithm");
        assertTrue(command instanceof ShowAlgorithmCommand);
    }

    @Test
    @Order(2)
    public void CqrInterpreter1MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("sHoW aLgoRitHm");
        assertTrue(command instanceof ShowAlgorithmCommand);
    }

    @Test
    @Order(3)
    public void CqrInterpreter1MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret(" show algorithm ");
        assertTrue(command instanceof ShowAlgorithmCommand);
    }

    @Test
    @Order(4)
    public void CqrInterpreter1MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("show algorithm abc");
        assertNull(command);
    }

    @Test
    @Order(5)
    public void CqrInterpreter1Mismatch2() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("command show algorithm");
        assertFalse(command instanceof ShowAlgorithmCommand);
    }
}
