package main.persistence;

import main.configuration.Configuration;

import java.sql.*;
import java.text.MessageFormat;
import java.time.Instant;
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
        db.insertParticipant("a","normal");
        db.insertParticipant("b","normal");
        db.insertParticipant("c","intruder");
        db.insertChannel("channel1","a","b");
        db.insertMessage("a","b","plainmessage","none","plainmessage","keyfileName");
        System.out.println(db.getTypeID("jolly"));
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

        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    @Override
    public void dropAllTables() {
        dropTablePostbox();
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
        createTablePostbox();
    }

    private synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);

            if (result == -1) {
                System.out.println("error executing " + sqlStatement);
            }
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }


    @Override
    public void insertType(String name) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO types (").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertAlgorithm(String algorithmName) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO algorithms (").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(algorithmName).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertParticipant(String participantName, String typeName) {
        int typeID = getTypeID(typeName);
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("name").append(",").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(participantName).append("',");
        sqlStringBuilder.append(typeID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertChannel(String channelName, String participantA, String participantB) {
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
    public void insertMessage(String participantFrom, String participantTo, String plainMessage, String algorithm, String encodedMessage, String keyFile) {
        int participantFromID = getParticipantID(participantFrom);
        int participantToID = getParticipantID(participantTo);
        int algorithmID = getAlgorithmID(algorithm);
        long timeStamp = Instant.now().getEpochSecond();
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO messages (PARTICIPANT_FROM_ID, PARTICIPANT_TO_ID, PLAIN_MESSAGE, ALGORITHM_ID, ENCRYPTED_MESSAGE, KEYFILE, TIMESTAMP)");
        sqlStringBuilder.append(" VALUES (");
        sqlStringBuilder.append(MessageFormat.format("{0}, {1}, ''{2}'', {3}, ''{4}'', ''{5}'', {6} ",
                participantFromID, participantToID, plainMessage, algorithmID, encodedMessage, keyFile, Long.toString(timeStamp) ));
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertPostboxMessage(String participantTo, String participantFrom, String message) {
        int participantFromID = getParticipantID(participantFrom);
        int participantToID = getParticipantID(participantTo);
        long timeStamp = Instant.now().getEpochSecond();
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO postbox (participant_from_id, participant_to_id, message, timestamp)");
        sqlStringBuilder.append(" VALUES (");
        sqlStringBuilder.append(MessageFormat.format("{0}, {1}, ''{2}'', {3} ",
                participantFromID, participantToID, message, Long.toString(timeStamp)));
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }


    @Override
    public List<String> getPostboxMessages(String participant) {
        return null;
    }


    @Override
    public boolean channelExists(String channelName) {
        try {
            String sqlStatement = "SELECT ID from channel where name='" + channelName + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(channelName + " channel not found");
            }
            return true;
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return false;
    }

    @Override
    public String getChannel(String participantA, String participantB) {
        try {
            String sqlStatement = MessageFormat.format(
                    "SELECT name from channel where (participant_01='{0}' AND participant_02='{1}') or (participant_01='{1}' AND participant_02='{0}')",
                    participantA, participantB);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(" channel not found with participants " + participantA + " " + participantB);
            }
            return resultSet.getString("name");
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return null;
    }

    @Override
    public String getParticipantType(String participant) {
        int typeID=-1;
        try {
            String sqlStatement = "SELECT TYPE_ID from PARTICIPANTS where name='" + participant + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(participant + " participant not found");
            }
            typeID = resultSet.getInt("TYPE_ID");

            String sqlStatement2 = "SELECT name from TYPES where ID=" + typeID;
            Statement statement2 = connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(sqlStatement2);
            if (!resultSet2.next()) {
                throw new SQLException(typeID + " typeID not found");
            }
            return resultSet2.getString("name");

        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return null;
    }


    private int getTypeID(String name) {
        try {
            String sqlStatement = "SELECT ID from TYPES where name='" + name + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(name + " type not found");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return -1;
    }

    private int getParticipantID(String name) {
        try {
            String sqlStatement = "SELECT ID from PARTICIPANTS where name='" + name + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(name + " participant not found");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return -1;
    }

    private int getAlgorithmID(String algorithm) {
        try {
            String sqlStatement = "SELECT ID from ALGORITHMS where name='" + algorithm + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(algorithm + " algorithm not found");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return -1;
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

    public void createTablePostbox() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("create table postbox\n" +
                "(\n" +
                "\tid tinyint GENERATED BY DEFAULT AS IDENTITY(START WITH 1)\n" +
                "\t\tconstraint postbox_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tparticipant_to_id tinyint not null\n" +
                "\t\tconstraint postbox_PARTICIPANTS_ID_fk_2\n" +
                "\t\t\treferences PARTICIPANTS,\n" +
                "\tparticipant_from_id tinyint not null\n" +
                "\t\tconstraint postbox_PARTICIPANTS_ID_fk\n" +
                "\t\t\treferences PARTICIPANTS,\n" +
                "\tmessage varchar(50) not null,\n" +
                "\ttimestamp int\n" +
                ");\n" +
                "\n");
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

    private void dropTablePostbox() {
        System.out.println("--- dropTablePostbox");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE postbox");
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