package server;

import network.*;

import java.io.*;
import java.net.Socket;

public class Connection {

    public String getUsername() { return username; }
    public String getToken() { return token; }
    public String getAddress() { return address; }
    public boolean isValid() { return valid; }

    public Connection(Socket socket) {
        this.valid = true;
        this.socket = socket;
        this.address = this.socket.getInetAddress().toString();

        System.out.println("Connection request received from " + this.getAddress());

        if(!initIO()) {
            System.out.println("Failed to establish I/O with requester " + this.getAddress());
            valid = false;
        }

        NetworkMessage message = this.read();
        if(message instanceof LoginRequestNetworkMessage)
        {
            LoginRequestNetworkMessage loginNM = (LoginRequestNetworkMessage)message;
            this.username = loginNM.getUsername();

            if(this.username.length() < Limits.MIN_USERNAME_LENGTH || this.username.length() > Limits.MAX_USERNAME_LENGTH) {
                System.out.println("Requester " + this.getAddress() + " username [" + this.username + "] is invalid!");
                this.write(new LoginFailureNetworkMessage("Invalid username!"));
                this.close();
            }
            else {
                this.token = TokenGenerator.generateToken();
                this.write(new LoginSuccessNetworkMessage(this.token));
            }
        }
        else {
            System.out.println("Requester " + this.getAddress() + " sent an invalid message!");
            this.write(new LoginFailureNetworkMessage("Invalid message!"));
            this.close();
        }
    }

    public NetworkMessage read()  {
        return Protocol.read(this.in);
    }

    public int write(NetworkMessage message) {
        return Protocol.send(this.out, message);
    }

    public void close() {
        this.closeIO();
    }

    private boolean initIO() {
        try {
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
            return true;
        }
        catch (IOException exception) {
            return false;
        }
    }

    private void closeIO() {
        try {
            if (this.out != null)
                this.out.close();
        }
        catch (IOException exception) {
            valid = false;
        }

        try {
            if(this.in != null)
                this.in.close();
        }
        catch (IOException exception) {
            valid = false;
        }

        try {
            if(this.socket != null)
                this.socket.close();
        }
        catch (IOException exception) {
            valid = false;
        }

        valid = false;
    }

    private boolean valid;

    private String username;
    private String token;
    private final String address;

    private DataOutputStream out;
    private DataInputStream in;

    private final Socket socket;

}