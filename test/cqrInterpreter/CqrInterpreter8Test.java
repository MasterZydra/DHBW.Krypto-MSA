package cqrInterpreter;

import commands.DropChannelCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CqrInterpreter8Test {

    @Test
    void interpretTest() {
        CqrInterpreter8 cqr8 = new CqrInterpreter8(null);

        assertTrue(cqr8.interpret("drop channel channel1").getClass().equals( (new DropChannelCommand().getClass())));

    }

    @Test
    void interpret2Test() {
        CqrInterpreter8 cqr8 = new CqrInterpreter8(null);

        assertTrue(cqr8.interpret("drop channel channel1").getParam("channelName").equals("channel1"));
        assertFalse(cqr8.interpret("drop channel channel1").getParam("channelName").equals("channel2"));

    }

    @Test
    void interpret3Test() {
        CqrInterpreter8 cqr8 = new CqrInterpreter8(null);

        assertNull(cqr8.interpret("wrong command"));
    }

}