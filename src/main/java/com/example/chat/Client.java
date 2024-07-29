package com.example.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends ChatBase {
    @Override
    protected void setupConnection() throws IOException {
        socket = new Socket("localhost", 8000);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    protected String getTitle() {
        return "FRIEND";
    }

    @Override
    protected void handleSendButtonClick() {
        String message = "FRIEND: " + inputMessage.getText();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
