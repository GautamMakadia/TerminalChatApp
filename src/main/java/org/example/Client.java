package org.example;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author botmg3002
 */
public class Client {
    private final int port;
    private final String host;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private Boolean isConnectionClosed = true;



    public Client(final String host ,final int port) {
        this.port = port;
        this.host = host;
    }


    public Boolean connect() {
        try {
            System.out.println("Connection to "+ host + ":" + port + "");
            socket = new Socket(host, port);
            isConnectionClosed = true;

            System.out.println("Client Connected to Server Successfully: ");
            System.out.println("Host: " + host);
            System.out.println("Port: "+ port);
            System.out.println("To close the connection type 'exit()'.");
            System.out.println("==========================================");

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            isConnectionClosed = false;
            return false;
        }

        return true;
    }

    public void startCommunication() {
        try {
            String message = "Hello Message";
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);

            while (!"exit()".equals(message)) {

                if (!"".equals(message)) {
                    dataOutputStream.writeUTF(message);
                    System.out.print("\tmessage sent :)");
                } else {
                    System.out.print("\tCan't send empty message.");
                }

                System.out.print("\nEnter message to send :=> ");
                message = sc.nextLine();
            }

            dataOutputStream.writeUTF("exit()");
            System.out.println("Closing connection to server "+host+":"+port+ "...");
            isConnectionClosed = closeConnection();


        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }



    private Boolean closeConnection() throws IOException {
        dataOutputStream.flush();
        dataOutputStream.close();

        return true;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public Boolean getIsConnectionClosed() {
        return isConnectionClosed;
    }


    public static void main(String[] args) {
        Client client = new Client("localhost", 3000);

        if(!client.connect()) {
            System.out.println("Can't connect to server...");
            return;
        }


        client.startCommunication();
    }
}