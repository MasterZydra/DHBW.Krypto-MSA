package cqrInterpreter;

import commands.ICommand;
import commands.SendMessageP2PCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CqrInterpreter10Test {
    CqrInterpreter10 cqr10;
            ICommand cmd;

    @BeforeEach
    void setUp() {
        cqr10 = new CqrInterpreter10(null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void interpretCorrect() {
        cmd = cqr10.interpret("send message \"[message]\" from a to b using RSA and keyfile keyfile.json");
        assertTrue(cmd instanceof SendMessageP2PCommand);
        assertTrue(cmd.getParam("message").equals("[message]"));
        assertTrue(cmd.getParam("participantFrom").equals("a"));
        assertTrue(cmd.getParam("participantTo").equals("b"));
        assertTrue(cmd.getParam("algorithm").equals("RSA"));
        assertTrue(cmd.getParam("keyfile").equals("keyfile.json"));
    }

    @Test
    void interpretFaultyShort() {
        cmd = cqr10.interpret("send");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyLong() {
        cmd = cqr10.interpret("send message \"[message]\" from a to b using RSA and keyfile keyfile json");
        assertNull(cmd);
    }

    @Test
    void interpretFaultyMissingMessage() {
        cmd = cqr10.interpret("send message from a to b using RSA and keyfile keyfile.json");
        assertNull(cmd);
    }


}