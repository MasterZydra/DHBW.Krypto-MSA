package persistence;

import persistence.dataModels.PostboxMessage;
import org.junit.*;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

public class MSA_HSQLDBTest {

    static MSA_HSQLDB db;

    @BeforeClass
    public static void beforeClass() throws Exception {
        db = MSA_HSQLDB.instance;
        db.setupConnection();

    }

    @AfterClass
    public static void afterClass() throws Exception {
        MSA_HSQLDB.instance.shutdown();
    }

    @Before
    public void setUp() throws Exception {
        db.dropAllTables();
        db.createAllTables();
        db.insertType("normal");
        db.insertType("intruder");
        db.insertAlgorithm("none");
        db.insertAlgorithm("rsa");
        db.insertAlgorithm("shift");
        db.insertParticipant("a", "normal");
        db.insertParticipant("b", "normal");
        db.insertParticipant("c", "intruder");
        db.insertChannel("channel1", "a", "b");
        db.insertMessage("a", "b", "plainmessage", "none", "plainmessage", "keyfileName");
        db.insertPostboxMessage("a","b","unit-test setUp message");
    }

    @Test
    public void insertType() {
        db.insertType("type3");
        db.insertParticipant("d", "type3");
        String d = db.getParticipantType("d");
        assertEquals("type3", d);
    }

    @Test
    public void insertAlgorithm() {
        db.insertAlgorithm("type3");
        List<String> d = db.getAlgorithms();
        assertTrue(d.contains("type3"));
    }

    @Test
    public void insertParticipant() {
        db.insertParticipant("e", "normal");
        assertEquals("normal", db.getParticipantType("e"));

    }

    @Test
    public void insertChannel() {
        db.insertParticipant("f", "normal");
        db.insertParticipant("g", "normal");
        db.insertChannel("channel2","f","g");
        assertTrue(db.channelExists("channel2"));
    }

    @Test
    public void insertMessage() {
        long now = Instant.now().getEpochSecond();
        String b = "";
        while(Instant.now().getEpochSecond()<now+3){ //wait 3 seconds for different timestamps
            b=""+Instant.now().getEpochSecond();
        }
        db.insertMessage("a", "b", "secondmessage", "rsa", "plainmessage", "fileName2");
       // assertTrue(db.getMessages("a")!=null);
    }

    @Test
    public void insertPostboxMessage() {
        long now = Instant.now().getEpochSecond();
        String b = "";
        db.insertPostboxMessage("a","b","message\ncontent\ncontent2");
        db.insertPostboxMessage("a","b","message2\ncontent\ncontent2");
        while(Instant.now().getEpochSecond()<now+3){ //wait 3 seconds for different timestamps
            b=""+Instant.now().getEpochSecond();
        }
        db.insertPostboxMessage("a","b","message3\ncontent\ncontent2");
        for (PostboxMessage msg : db.getPostboxMessages("a")){
            System.out.println(msg.getTimestamp() + "\n " +
                    msg.getParticipantFrom().getName() + "\n " + msg.getParticipantTo().getName()
                    +"\n " + msg.getMessage());
            System.out.println();
        }
        assertTrue(db.getPostboxMessages("a").size()>0);
    }

    @Test
    public void getPostboxMessages() {
        long now = Instant.now().getEpochSecond();
        String b = "";
        db.insertPostboxMessage("a","b","message\ncontent\ncontent2");
        db.insertPostboxMessage("a","b","message2\ncontent\ncontent2");
        while(Instant.now().getEpochSecond()<now+3){ //wait 3 seconds for different timestamps
            b=""+Instant.now().getEpochSecond();
        }
        db.insertPostboxMessage("a","b","message3\ncontent\ncontent2");
        for (PostboxMessage msg : db.getPostboxMessages("a")){
            System.out.println(msg.getTimestamp() + "\n " +
                    msg.getParticipantFrom().getName() + "\n " + msg.getParticipantTo().getName()
                    +"\n " + msg.getMessage());
            System.out.println();
        }
        assertTrue(db.getPostboxMessages("a").size()>0);
    }

    @Test
    public void channelExists() {
        assertTrue(db.channelExists("channel1"));
    }

    @Test
    public void getChannel() {
        String  expected="channel1";
        String  result = db.getChannel("a","b").getName();
        assertEquals(expected,result);
    }

    @Test
    public void getChannels() {
        String expected = "channel1";
        String result = db.getChannels().get(0).getName();
        assertEquals(expected,result);
    }

    @Test
    public void getParticipantType() {
        String expected="normal";
        String result = db.getParticipantType("a");
        assertEquals(expected,result);
    }


    @Test
    public void participantExists() {
        assertTrue(db.participantExists("a"));
        assertFalse(db.participantExists("z"));
    }

    @Test
    public void dropChannel() {
        assertTrue(db.channelExists("channel1"));
        db.dropChannel("channel1");
        assertFalse(db.channelExists("channel1"));
    }
}