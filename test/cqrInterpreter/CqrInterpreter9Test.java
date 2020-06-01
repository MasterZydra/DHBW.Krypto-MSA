package cqrInterpreter;

import commands.ICommand;
import commands.IntrudeChannelCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CqrInterpreter9Test {

    CqrInterpreter9 cqr9;
    ICommand cmd;

    @BeforeEach
    void setUp() {
        cqr9 = new CqrInterpreter9(null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void interpretCorrect() {
        cmd = cqr9.interpret("intrude channel [channel] by a");
        assertTrue(cmd instanceof IntrudeChannelCommand);
        assertTrue(cmd.getParam("channelName").equals("[channel]"));
        assertTrue(cmd.getParam("intruderName").equals("a"));
    }

    @Test
    void interpretFaultyShort() {
        cmd = cqr9.interpret("send");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyLong() {
        cmd = cqr9.interpret("intrude channel [channel] by a b");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyMissingIntruderName() {
        cmd = cqr9.interpret("intrude channel [channel] by ");
        assertNull(cmd);
    }
}