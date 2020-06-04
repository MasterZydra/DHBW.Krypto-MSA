package cqrInterpreter;
//3894913
import commands.CreateChannelCommand;
import commands.ICommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CqrInterpreter6Test {

    CqrInterpreter6 cqr6;
    ICommand cmd;

    @BeforeEach
    void setUp() {
        cqr6 = new CqrInterpreter6(null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void interpretCorrect() {
        cmd = cqr6.interpret("create channel [name] from [participant01] to [participant02]");
        assertTrue(cmd instanceof CreateChannelCommand);
        assertTrue(cmd.getParam("channelName").equals("[name]"));
        assertTrue(cmd.getParam("participant01").equals("[participant01]"));
        assertTrue(cmd.getParam("participant02").equals("[participant02]"));
    }

    @Test
    void interpretFaultyShort() {
        cmd = cqr6.interpret("create");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyLong() {
        cmd = cqr6.interpret("create channel [name] from [participant01] to [participant02] fast");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyMissingIntruderName() {
        cmd = cqr6.interpret("create channel [name] [participant01] to [participant02]");
        assertNull(cmd);
    }
}