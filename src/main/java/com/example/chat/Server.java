
package com.example.chat;

import java.io.*;
import java.net.ServerSocket;

public class Server extends ChatBase {
    private ServerSocket serverSocket;

    @Override
    protected void setupConnection() throws IOException {
        serverSocket = new ServerSocket(8000);
        socket = serverSocket.accept();
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    protected String getTitle() {
        return "YOU";
    }

    @Override
    protected void handleSendButtonClick() {
        String message = "YOU: " + inputMessage.getText();
        messages.appendText(message + "\n");
        inputMessage.clear();
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

