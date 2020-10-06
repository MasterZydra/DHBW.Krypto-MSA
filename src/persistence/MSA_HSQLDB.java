package persistence;
//3894913
import configuration.Configuration;
import persistence.dataModels.Channel;
import persistence.dataModels.Message;
import persistence.dataModels.Participant;
import persistence.dataModels.PostboxMessage;


import java.sql.*;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public enum MSA_HSQLDB implements IMsaDB {
    instance;

    private Connection connection;

    public static void main(String[] args) {
        MSA_HSQLDB db = MSA_HSQLDB.instance;
        db.setupConnection();
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
        System.out.println(db.getChannels());
        db.shutdown();
    }

    @Override
    public void setupConnection() {
        System.out.println("--- setupConnection");
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = Configuration.instance.driverName + Configuration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, Configuration.instance.username, Configuration.instance.password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        System.out.println("--- shutdown");

        try (Statement statement = connection.createStatement()){
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    @Override
    public void dropAllTables() {
        dropTablePostboxAll();
        dropTableMessages();
        dropTableChannel();
        dropTableParticipants();
        dropTableAlgorithms();
        dropTableTypes();
    }

    @Override
    public void createAllTables() {
        createTableAlgorithms();
        createTableTypes();
        createTableParticipants();
        createTableChannel();
        createTableMessages();
    }

    private synchronized void update(String sqlStatement) {
        try (Statement statement = connection.createStatement()){
            int result = statement.executeUpdate(sqlStatement);

            if (result == -1) {
                System.out.println("error executing " + sqlStatement);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }


    @Override
    public void insertType(String name) {
        name = name.toLowerCase();
        StringBuilder sqlStringBuilder = new StringBuilder();
        if(getTypeID(name)>0) return;
        sqlStringBuilder.append("INSERT INTO types (name)");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertAlgorithm(String algorithmName) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        if (getAlgorithmID(algorithmName)>0) return;
        sqlStringBuilder.append("INSERT INTO algorithms (").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(algorithmName).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertParticipant(String participantName, String typeName) {
        participantName = participantName.toLowerCase();
        int typeID = getTypeID(typeName);
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("name").append(",").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(participantName).append("',");
        sqlStringBuilder.append(typeID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());

        createTablePostbox( participantName );
    }

    @Override
    public void insertParticipant(Participant participant) {
        insertParticipant(participant.getName(), participant.getType());
    }

    @Override
    public void insertChannel(String channelName, String participantA, String participantB) {
        channelName = channelName.toLowerCase();
        int participantA_ID = getParticipantID(participantA);
        int participantB_ID = getParticipantID(participantB);
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO channel (").append("name").append(",").append("participant_01").append(",").append("participant_02").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(channelName).append("',");
        sqlStringBuilder.append(participantA_ID).append(",");
        sqlStringBuilder.append(participantB_ID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertChannel(Channel channel) {
        insertChannel(channel.getName(), channel.getParticipantA().getName(),
                channel.getParticipantB().getName());
    }

    @Override
    public void insertMessage(String participantFrom, String participantTo, String plainMessage,
                              String algorithm, String encodedMessage, String keyFile) {
        int participantFromID = getParticipantID(participantFrom);
        int participantToID = getParticipantID(participantTo);
        int algorithmID = getAlgorithmID(algorithm);
        long timeStamp = Instant.now().getEpochSecond();
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO messages (PARTICIPANT_FROM_ID, PARTICIPANT_TO_ID, PLAIN_MESSAGE, ALGORITHM_ID, ENCRYPTED_MESSAGE, KEYFILE, TIMESTAMP)");
        sqlStringBuilder.append(" VALUES (");
        sqlStringBuilder.append(MessageFormat.format("{0}, {1}, ''{2}'', {3}, ''{4}'', ''{5}'', {6} ",
                participantFromID, participantToID, plainMessage, algorithmID, encodedMessage, keyFile, Long.toString(timeStamp)));
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertMessage(Message message) {
        insertMessage(message.getParticipantFrom().getName(), message.getParticipantTo().getName(),
                message.getPlain_message(), message.getAlgorithm(), message.getEncoded_message(),
                message.getKeyfile());
    }

    @Override
    public void insertPostboxMessage(String participantTo, String participantFrom, String message) {
        if (!participantExists(participantFrom) || !participantExists(participantTo)) {
            System.out.println("Could not save postbox message, participant not found.");
        }
        int participantFromID = getParticipantID(participantFrom);
        long timeStamp = Instant.now().getEpochSecond();
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO postbox_"+participantTo+" (participant_from_id, message, timestamp)");
        sqlStringBuilder.append(" VALUES (");
        sqlStringBuilder.append(MessageFormat.format("{0}, ''{1}'', {2} ",
                participantFromID, message, Long.toString(timeStamp)));
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertPostboxMessage(PostboxMessage message) {
        insertPostboxMessage(message.getParticipantTo().getName(),
                message.getParticipantFrom().getName(), message.getMessage());
    }

    @Override
    public List<String> getTypes() {
        List<String> types = new ArrayList<>();
        try  (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT * from TYPES";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    types.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return types;
    }

    @Override
    public List<String> getAlgorithms() {
        List<String> algos = new ArrayList<>();
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT * from ALGORITHMS";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    algos.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return algos;
    }

    @Override
    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT * from PARTICIPANTS";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String type = getTypeName(resultSet.getInt("type_id"));
                    Participant p = new Participant(name, type);
                    participants.add(p);
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return participants;
    }


    @Override
    public List<PostboxMessage> getPostboxMessages(String partToName) {
        List<PostboxMessage> msgList = new ArrayList<>();
        if(!participantExists(partToName)){
            System.out.println("Could not get postbox message, participant not found.");
        }
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT * from POSTBOX_"+partToName;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    int partFromID = resultSet.getInt("participant_from_id");
                    String partFromName = getParticipantName(partFromID);
                    Participant partFrom = new Participant(partFromName, getParticipantType(partFromName));
                    Participant partTo = new Participant(partToName, getParticipantType(partToName));
                    String timestamp = Integer.toString(resultSet.getInt("timestamp"));
                    String message = resultSet.getString("message");
                    PostboxMessage p = new PostboxMessage(partFrom, partTo, message, timestamp);
                    msgList.add(p);
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return msgList;
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> channelList = new ArrayList<>();

        try  (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT * from channel";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    int part1ID = resultSet.getInt("participant_01");
                    int part2ID = resultSet.getInt("participant_02");
                    Participant partA = getParticipant(part1ID);
                    Participant partB = getParticipant(part2ID);
                    channelList.add(getChannel(partA.getName(), partB.getName()));
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return channelList;
    }

    @Override
    public boolean channelExists(String channelName) {
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT name from channel where LOWER(name)='" + channelName.toLowerCase() + "'";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(channelName + " channel not found");
                }
            }
            return true;
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return false;
    }

    @Override
    public boolean participantExists(String name) {
        int participantAID = getParticipantID(name.toLowerCase());
        return participantAID != -1;
    }

    @Override
    public Channel getChannel(String participantA, String participantB) {
        try (Statement statement = connection.createStatement()){
            int participantAID = getParticipantID(participantA);
            int participantBID = getParticipantID(participantB);
            String sqlStatement = MessageFormat.format(
                    "SELECT name from channel where (participant_01=''{0}'' AND participant_02=''{1}'') or (participant_01=''{1}'' AND participant_02=''{0}'')",
                    participantAID, participantBID);
            String channelName;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(" channel not found with participants " + participantA + " " + participantB);
                }
                channelName = resultSet.getString("name");
            }
            return new Channel(channelName, getParticipant(participantA), getParticipant(participantB));
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return null;
    }

    @Override
    public void dropChannel(String channelName) {
        try (Statement statement = connection.createStatement()){
            String sqlStatement = MessageFormat.format(
                    "DELETE FROM channel WHERE name=''{0}''",
                    channelName);
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(channelName + " could not be deleted");
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    @Override
    public String getParticipantType(String participant) {
        if (participant == null) return null;
        participant = participant.toLowerCase();
        int typeID = -1;
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT TYPE_ID from PARTICIPANTS where name='" + participant + "'";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(participant + " participant not found");
                }
                typeID = resultSet.getInt("TYPE_ID");
            }
            String typeName = getTypeName(typeID);
            return typeName;

        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return null;
    }

    private int getTypeID(String name) {
        try (Statement statement = connection.createStatement()){
            name = name.toLowerCase();
            String sqlStatement = "SELECT ID from TYPES where lower(name)=lower('" + name + "')";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(name + " type not found");
                }
                return resultSet.getInt("ID");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return -1;
    }

    private int getParticipantID(String name) {
        try (Statement statement = connection.createStatement()){
            name = name.toLowerCase();
            String sqlStatement = "SELECT ID from PARTICIPANTS where name='" + name + "'";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(name + " participant not found");
                }
                return resultSet.getInt("ID");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return -1;
    }

    private String getParticipantName(int participantID) {
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT name from participants where ID=" + participantID;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(" participant not found with ID " + participantID);
                }
                return resultSet.getString("name");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return null;
    }

    private String getTypeName(int typeID) {
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT name from TYPES where ID=" + typeID;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(" type not found with ID " + typeID);
                }
                return resultSet.getString("name");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return null;
    }

    private int getAlgorithmID(String algorithm) {
        try (Statement statement = connection.createStatement()){
            String sqlStatement = "SELECT ID from ALGORITHMS where LOWER(name)=LOWER('" + algorithm + "')";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(algorithm + " algorithm not found");
                }
                return resultSet.getInt("ID");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return -1;
    }


    public Participant getParticipant(String name) {
        name=name.toLowerCase();
        if (participantExists(name)) {
            return new Participant(name, getParticipantType(name));
        }
        return null;
    }

    private Participant getParticipant(int partID) {
        String name = getParticipantName(partID);
        return new Participant(name, getParticipantType(name));
    }

    private void createTableTypes() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE types ( ");
        sqlStringBuilder01.append("id tinyint GENERATED BY DEFAULT AS IDENTITY(START WITH 1)").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxTypes ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    private void createTableAlgorithms() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE ALGORITHMS\n" +
                "(\n" +
                "id tinyint GENERATED BY DEFAULT AS IDENTITY(START WITH 1)" +
                "CONSTRAINT ALGORITHMS_PK\n" +
                "PRIMARY KEY,\n" +
                "NAME VARCHAR(10)\n" +
                ")");

        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("create unique index algorithms_name_uindex on algorithms (name);");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    private void createTableChannel() {
        System.out.println("--- createTableChannel");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("create table channel\n" +
                "(\n" +
                "\tname varchar(25) not null\n" +
                "\t\tconstraint channel_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tparticipant_01 tinyint not null\n" +
                "\t\tconstraint channel_PARTICIPANTS_ID_fk\n" +
                "\t\t\treferences PARTICIPANTS,\n" +
                "\tparticipant_02 tinyint not null\n" +
                "\t\tconstraint channel_PARTICIPANTS_ID_fk_2\n" +
                "\t\t\treferences PARTICIPANTS\n" +
                ");");

        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());
    }

    public void createTableParticipants() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("create table PARTICIPANTS(\n" +
                "    id tinyint GENERATED BY DEFAULT AS IDENTITY(START WITH 1) \n" +
                "        primary key,\n" +
                "    NAME    VARCHAR(50) not null,\n" +
                "    TYPE_ID TINYINT     not null\n" +
                "        constraint FKPARTICIPANTS01\n" +
                "            references TYPES\n" +
                ");");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());
    }

    public void createTableMessages() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("create table messages\n" +
                "(\n" +
                "\tid tinyint GENERATED BY DEFAULT AS IDENTITY(START WITH 1)\n" +
                "\t\tconstraint messages_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tparticipant_from_id tinyint not null\n" +
                "\t\tconstraint messages_PARTICIPANTS_ID_fk_2\n" +
                "\t\t\treferences PARTICIPANTS,\n" +
                "\tparticipant_to_id tinyint not null\n" +
                "\t\tconstraint messages_PARTICIPANTS_ID_fk\n" +
                "\t\t\treferences PARTICIPANTS,\n" +
                "\tplain_message varchar(50) not null,\n" +
                "\talgorithm_id tinyint not null\n" +
                "\t\tconstraint messages_ALGORITHMS_ID_fk\n" +
                "\t\t\treferences ALGORITHMS,\n" +
                "\tencrypted_message varchar(50) not null,\n" +
                "\tkeyfile varchar(20) not null,\n" +
                "\ttimestamp int\n" +
                ");\n" +
                "\n");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

    }

    public void createTablePostbox(String participantName) {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append(
                "create table POSTBOX_"+participantName+"\n" +
                "(\n" +
                "\tID TINYINT identity\n" +
                "\t\tconstraint POSTBOX_"+participantName+"_PK\n" +
                "\t\t\tprimary key,\n" +
                "\tPARTICIPANT_FROM_ID TINYINT not null\n" +
                "\t\tconstraint POSTBOX_"+participantName+"_PARTICIPANTS_ID_FK\n" +
                "\t\t\treferences PARTICIPANTS,\n" +
                "\tMESSAGE VARCHAR(50) not null,\n" +
                "\tTIMESTAMP INTEGER\n" +
                ")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());
    }

    public void dropTableParticipants() {
        System.out.println("--- dropTableParticipants");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE participants");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    private void dropTableTypes() {
        System.out.println("--- dropTableTypes");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE types");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    private void dropTablePostboxAll() {
        System.out.println("--- dropTablePostboxAll");

        try (Statement statement = connection.createStatement()){
            List<String> tables = new ArrayList<>();
            String sqlStatement = "SELECT * FROM   INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    tables.add(resultSet.getString("TABLE_NAME"));
                }
            }
            if (tables.isEmpty()) {
                System.out.println("--- drop all postbox tables: no tables found");
                return;
            }

            System.out.println("--- drop all postbox tables from:" + tables.toString());
            for (String tableName: tables
            ) {
                if (tableName.toLowerCase().startsWith("postbox")) {
                    String updateString = "DROP TABLE "+tableName;
                    System.out.println("sqlStringBuilder : " + updateString);
                    update(updateString);
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    private void dropTablePostbox(String participantName) {
        System.out.println("--- dropTablePostbox");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE postbox_"+participantName);
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    private void dropTableMessages() {
        System.out.println("--- dropTableMessages");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE messages");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    private void dropTableChannel() {
        System.out.println("--- dropTableChannel");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE channel");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    private void dropTableAlgorithms() {
        System.out.println("--- dropTableAlgorithms");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE algorithms");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }


}