package org.example;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author botmg3002
 */
public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final Runnable runnable;
    private DataInputStream dataInputStream;
    private final OnMessageListener onMessageListener;
    private Boolean isRunning = false;
    public Thread task;

    public Server(int port, OnMessageListener onMessageListener) {
        this.port = port;
        this.onMessageListener = onMessageListener;
        this.isRunning = true;
        this.runnable = () -> {
            boolean running = true;
            try{
                while(true) {
                    String data = dataInputStream.readUTF();

                    this.onMessageListener.onMessageReceive(data);

                    if ("exit()".equals(data)) {
                        System.out.println("Exiting server");
                        break;
                    }
                }

                close();
                isRunning = running;
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        };
    }

    public boolean connect() {
        try {
            this.serverSocket = new ServerSocket(port);
            Socket socket = this.serverSocket.accept();
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            task = new Thread(runnable);
            task.start();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }

        return true;
    }

    private void close() throws Exception{
        try {
            serverSocket.close();
            isRunning = false;
        } catch (IOException exception) {
            throw new Exception("Exception while closing connection.");
        }
    }

    public void waitForExitSignal() {
        try {
            this.task.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isServerRunning() {
        return isRunning;
    }

    public interface OnMessageListener {
        void onMessageReceive(String data);
    }
}
