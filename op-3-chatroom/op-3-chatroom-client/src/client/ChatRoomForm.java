package client;

import client.handlers.*;
import client.rendering.LoggedInUsersRenderer;
import client.rendering.MessageRenderer;
import colors.SolarizedTheme;
import network.handlers.NetworkMessageProxy;
import network.message.Message;
import network.message.RegularMessage;
import network.networkmessage.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatRoomForm {
    public JPanel mainPanel;

    private JButton submitButton;
    private JButton logOutButton;

    private JTextArea userMessageField;

    private JList<Message> chatMessages;
    private DefaultListModel<Message> chatMessagesModel;

    private JList<String> loggedInUsers;
    private DefaultListModel<String> loggedInUsersModel;

    private final Connection connection;
    private static final String DIRECT_MESSAGE = "/dm \"%s\" ";

    public ChatRoomForm(Connection connection) {
        this.connection = connection;

        Thread readThread = new Thread(getMessageHandler());

        readThread.start();

        userMessageField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                handleEnter(e);
            }
            @Override
            public void keyPressed(KeyEvent e) {
                handleEnter(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Interface segregation principle violation
            }

            private void handleEnter(KeyEvent e) {
                if(e.getKeyCode() != KeyEvent.VK_ENTER)
                    return;

                if(e.isShiftDown()) {
                    addNewLineToMessage();
                }
                else {
                    e.consume();
                    sendMessage(connection);
                }
            }
        });

        submitButton.addActionListener(actionEvent -> sendMessage(connection));

        logOutButton.addActionListener(actionEvent -> {
            sendLogoutRequest(connection);
            connection.close();
            LoginForm.show(frame);
        });

        loggedInUsers.addListSelectionListener(listSelectionEvent -> {
            String username = loggedInUsers.getSelectedValue();
            if (username != null) {
                userMessageField.setText(String.format(DIRECT_MESSAGE, username));
            }
            loggedInUsers.clearSelection();
        });

        chatMessages.addListSelectionListener(listSelectionEvent -> {
            Message message = chatMessages.getSelectedValue();
            if(message instanceof RegularMessage) {
                RegularMessage regularMessage = (RegularMessage) message;
                userMessageField.setText(String.format(DIRECT_MESSAGE, regularMessage.getUsername()));
            }
            chatMessages.clearSelection();
        });
    }

    private Runnable getMessageHandler() {
        return () -> {

            NetworkMessageProxy<Connection> proxy = new NetworkMessageProxy<>();
            proxy.subscribe(LoginSuccessNetworkMessage.class, new LoginSuccessHandler());
            proxy.subscribe(LoginFailureNetworkMessage.class, new LoginFailureHandler());
            proxy.subscribe(ServerChatMessageNetworkMessage.class, new ServerChatMessageHandler(chatMessagesModel));
            proxy.subscribe(UserLoggedInNetworkMessage.class, new UserLoggedInHandler(chatMessagesModel, loggedInUsersModel));
            proxy.subscribe(UserLoggedOutNetworkMessage.class, new UserLoggedOutHandler(chatMessagesModel, loggedInUsersModel));

            connection.write(new LoginRequestNetworkMessage(connection.getUsername()));
            while (true) {

                NetworkMessage message = this.connection.read();
                if(message == null || !proxy.proxy(connection, message)) {
                    LoginForm.show(frame);
                    break;
                }
            }
        };
    }

    private void sendLogoutRequest(Connection connection) {
        LogoutRequestNetworkMessage message = new LogoutRequestNetworkMessage(connection.getToken());
        connection.write(message);
    }

    private void sendMessage(Connection connection) {
        String textMessage = userMessageField.getText();
        userMessageField.setText("");
        ClientChatMessageNetworkMessage message = new ClientChatMessageNetworkMessage(connection.getToken(), textMessage);
        connection.write(message);
    }

    private void addNewLineToMessage() {
        userMessageField.setText(String.format("%s%n", userMessageField.getText()));
    }

    private void createUIComponents() {
        chatMessagesModel = new DefaultListModel<>();
        chatMessages = new JList<>(chatMessagesModel);
        chatMessages.setCellRenderer(new MessageRenderer(this.connection.getUsername(), new SolarizedTheme()));

        loggedInUsersModel = new DefaultListModel<>();
        loggedInUsers = new JList<>(loggedInUsersModel);
        loggedInUsers.setCellRenderer(new LoggedInUsersRenderer(this.connection.getUsername(), new SolarizedTheme()));
    }


    private JFrame frame;
    public static void show(JFrame frame, Connection connection) {
        ChatRoomForm form = new ChatRoomForm(connection);
        form.frame = frame;

        frame.setContentPane(form.mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
