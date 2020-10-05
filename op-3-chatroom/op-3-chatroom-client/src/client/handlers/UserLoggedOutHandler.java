package client.handlers;

import client.Connection;
import network.handlers.NetworkMessageHandler;
import network.message.Message;
import network.message.SystemMessage;
import network.networkmessage.UserLoggedInNetworkMessage;
import network.networkmessage.UserLoggedOutNetworkMessage;

import javax.swing.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserLoggedOutHandler extends NetworkMessageHandler<Connection, UserLoggedOutNetworkMessage> {

    public UserLoggedOutHandler(ConcurrentLinkedQueue<Message> messages, ConcurrentLinkedQueue<String> users) {
        this.messages = messages;
        this.users = users;
    }

    @Override
    protected void handleCore(Connection connection, UserLoggedOutNetworkMessage message) {
        messages.add(new SystemMessage("User Logged Out", message.getUsername()));
        users.add(message.getUsername());
        message.setHandled();
    }

    private final ConcurrentLinkedQueue<Message> messages;
    private final ConcurrentLinkedQueue<String> users;
}
