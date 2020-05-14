package main.persistence;

import org.junit.*;

import static org.junit.Assert.*;

public class MSA_HSQLDBTest {

static MSA_HSQLDB db;

    @BeforeClass
    public static void beforeClass() throws Exception {
        db = MSA_HSQLDB.instance;
        db.setupConnection();
        db.dropAllTables();
        db.createAllTables();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        MSA_HSQLDB.instance.shutdown();
    }

    @Before
    public void setUp() throws Exception {
        db.insertType("normal");
        db.insertType("intruder");
        db.insertAlgorithm("none");
        db.insertAlgorithm("rsa");
        db.insertAlgorithm("shift");
        db.insertParticipant("a","normal");
        db.insertParticipant("b","normal");
        db.insertParticipant("c","intruder");
        db.insertChannel("channel1","a","b");
        db.insertMessage("a","b","plainmessage","none","plainmessage","keyfileName");
    }

    @Test
    public void insertType() {
        db.insertType("type3");
        db.insertParticipant("d","type3");
        String d = db.getParticipantType("d");
        assertEquals("type3", d);
    }

    @Test
    public void insertAlgorithm() {
    }

    @Test
    public void insertParticipant() {
    }

    @Test
    public void insertChannel() {
    }

    @Test
    public void insertMessage() {
    }

    @Test
    public void insertPostboxMessage() {
    }

    @Test
    public void getPostboxMessages() {
    }

    @Test
    public void channelExists() {
    }

    @Test
    public void getChannel() {
    }

    @Test
    public void getParticipantType() {
    }
}