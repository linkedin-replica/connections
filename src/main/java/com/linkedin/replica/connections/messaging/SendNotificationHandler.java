package com.linkedin.replica.connections.messaging;


import com.google.gson.JsonObject;
import com.linkedin.replica.connections.config.Configuration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SendNotificationHandler {
    private static SendNotificationHandler sendNotificationHandler;
    private final Configuration configuration = Configuration.getInstance();
    private final String QUEUE_NAME = configuration.getAppConfigProp("notifications.queue.name");

    private Connection connection;
    private SendNotificationHandler() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(configuration.getAppConfigProp("rabbitmq.ip"));
        connection = factory.newConnection();
    }

    public void sendNotification(String userId, String text, String link) throws IOException {
        // encode notification as json
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("text", text);
        object.addProperty("link", link);
        String message = object.toString();

        // send message
        Channel channel = connection.createChannel();
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    }

    public static void init() throws IOException, TimeoutException {
        sendNotificationHandler = new SendNotificationHandler();
    }

    public static SendNotificationHandler getInstance() {
        return sendNotificationHandler;
    }

    public void closeConnections() throws IOException {
        connection.close();
    }
}
