package cqrInterpreter;

import commands.ICommand;
import commands.ShowAlgorithmCommand;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CqrInterpreter1Test {
    @Test
    public void CqrInterpreter1Match() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("show algorithm");
        assertTrue(command instanceof ShowAlgorithmCommand);
    }

    @Test
    public void CqrInterpreter1MatchUpperLowerCase() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("sHoW aLgoRitHm");
        assertTrue(command instanceof ShowAlgorithmCommand);
    }

    @Test
    public void CqrInterpreter1MatchSpaces() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret(" show algorithm ");
        assertTrue(command instanceof ShowAlgorithmCommand);
    }

    @Test
    public void CqrInterpreter1MismatchToLong() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("show algorithm abc");
        assertNull(command);
    }

    @Test
    public void CqrInterpreter1Mismatch2() {
        CqrInterpreter interpreter = new CqrInterpreter1(null);
        ICommand command = interpreter.interpret("command show algorithm");
        assertFalse(command instanceof ShowAlgorithmCommand);
    }
}
