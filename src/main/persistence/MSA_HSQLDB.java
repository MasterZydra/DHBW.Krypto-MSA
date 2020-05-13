package main.persistence;

import main.configuration.Configuration;

import java.sql.*;
import java.util.List;

public enum MSA_HSQLDB implements IMsaDB{
    instance;

    private Connection connection;

    public static void main(String[] args) {
        MSA_HSQLDB.instance.setupConnection();
        MSA_HSQLDB.instance.dropAllTables();
        MSA_HSQLDB.instance.createAllTables();
        MSA_HSQLDB.instance.insertType("jolly");
        System.out.println(MSA_HSQLDB.instance.getTypeID("jolly"));
        MSA_HSQLDB.instance.shutdown();
    }

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
    public void insertParticipant(String participantName, String type) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("name").append(",").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(participantName).append("',");
        sqlStringBuilder.append("'").append(type).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    @Override
    public void insertChannel(String channelName, String participantA, String participantB) {

    }

    @Override
    public void insertMessage(String participantFrom, String participantTo, String plainMessage, String algorithm, String encodedMessage, String keyFile) {

    }

    @Override
    public void insertPostboxMessage(String participantTo, String participantFrom, String message) {

    }

    @Override
    public List<String> getTypes() {
        return null;
    }

    @Override
    public List<String> getAlgorithms() {
        return null;
    }

    @Override
    public List<String> getParticipants() {
        return null;
    }

    @Override
    public List<String> getChannels() {
        return null;
    }

    @Override
    public List<String> getMessages() {
        return null;
    }

    @Override
    public List<String> getPostboxMessages(String participant) {
        return null;
    }

    @Override
    public List<String> getParticipantType(String participant) {
        return null;
    }

    @Override
    public boolean channelExists(String channelName) {
        return false;
    }

    @Override
    public boolean channelExists(String participantA, String participantB) {
        return false;
    }

    private int getTypeID(String name){
        try {
            String sqlStatement = "SELECT ID from TYPES where name='" + name + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) { throw new SQLException(name + " type not found"); }
            return resultSet.getInt("ID");
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return -1;
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