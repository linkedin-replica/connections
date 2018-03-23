package messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linkedin.replica.connections.config.Configuration;
import com.linkedin.replica.connections.database.DatabaseConnection;
import com.linkedin.replica.connections.database.DatabaseSeed;
import com.linkedin.replica.connections.messaging.MessageReceiver;
import com.rabbitmq.client.*;
import java.sql.*;

import com.rabbitmq.client.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class ClientMessagesTest {
    private static Configuration config;
    private static String QUEUE_NAME;
    private static MessageReceiver messagesReceiver;

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;
    private static java.sql.Connection mySqlConnection;

    @BeforeClass
    public static void init() throws IOException, TimeoutException, SQLException, ClassNotFoundException {
        String rootFolder = "src/main/resources/";
        Configuration.init(rootFolder + "app.config",
                rootFolder + "database.config",
                rootFolder + "commands.config",
                rootFolder + "controller.config");
        DatabaseConnection dbInstance = DatabaseConnection.getInstance();
        mySqlConnection = dbInstance.getMysqlConn();
        config = Configuration.getInstance();

        // init message receiver
        QUEUE_NAME = config.getAppConfigProp("rabbitmq.queue.name");
        messagesReceiver = new MessageReceiver();

        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        DatabaseSeed dbSeed = new DatabaseSeed();
        dbSeed.insertUsers();
    }

    @Test
    public void testUnsuccessfulMessage() throws IOException, InterruptedException {
        JsonObject object = new JsonObject();
        object.addProperty("commandName", "connections.addFriend");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        String resMessage = response.take();
        JsonObject resObject = new JsonParser().parse(resMessage).getAsJsonObject();

        assertEquals("Expecting BAD_REQUEST status", 400, resObject.get("statusCode").getAsInt());
    }

    @Test
    public void testSendMessage() throws IOException, TimeoutException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {

        JsonObject object = new JsonObject();
        object.addProperty("commandName", "connections.addFriend");
        object.addProperty("userID1", "e4def870-f331-4fb5-a44c-967592cf5b42");
        object.addProperty("userID2", "ff810a3f-07fc-4d35-bc84-98aed333b043");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        String resMessage = response.take();
        JsonObject resObject = new JsonParser().parse(resMessage).getAsJsonObject();

        assertEquals("Response status code should be 201 for adding a friend", 201, resObject.get("statusCode").getAsInt());
    }

    @AfterClass
    public static void clean() throws IOException, TimeoutException, SQLException, ClassNotFoundException {
        // close message queue connection
        messagesReceiver.closeConnection();
        channel.close();
        connection.close();

        String query = "delete from user_friends_with_user";
        Statement statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        query = "delete from users";
        statement = mySqlConnection.createStatement();
        statement.executeUpdate(query);

        DatabaseConnection.getInstance().closeConnections();
    }
}