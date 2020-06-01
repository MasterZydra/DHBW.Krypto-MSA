package cqrInterpreter;

import commands.ICommand;
import commands.ShowChannelCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CqrInterpreter7Test {

    CqrInterpreter7 cqr7;
    ICommand cmd;

    @BeforeEach
    void setUp() {
        cqr7 = new CqrInterpreter7(null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void interpretCorrect() {
        cmd = cqr7.interpret("show channel");
        assertTrue(cmd instanceof ShowChannelCommand);
    }

    @Test
    void interpretFaultyShort() {
        cmd = cqr7.interpret("show");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyLong() {
        cmd = cqr7.interpret("show channel b");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyTypo() {
        cmd = cqr7.interpret("show channelb");
        assertNull(cmd);
    }
}