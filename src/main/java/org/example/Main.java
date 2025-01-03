package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello, World!");

        Server server = new Server(3000, (data) -> {
            System.out.println("Messgae recived: "+ data);
        });

        System.out.println("created server object");
        if (!server.connect()) {
            System.out.println("Can't connect to server.");
            return;
        }

        System.out.println("Server is waiting to receive message...");
        boolean isRunning = server.isServerRunning();
        server.waitForExitSignal();
        System.out.println("Communication complited.");
    }
}