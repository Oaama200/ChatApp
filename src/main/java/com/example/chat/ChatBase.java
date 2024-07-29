package com.example.chat;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public abstract class ChatBase {
    protected TextField inputMessage;
    protected TextArea messages;
    protected Button sendButton;
    protected Socket socket;
    protected ObjectOutputStream outputStream;
    protected ObjectInputStream inputStream;
    private Thread messageThread;

    public void start(Stage primaryStage) {
        setupUI(primaryStage);
        setupNetwork();
        startMessageHandling();
    }

    private void setupUI(Stage primaryStage) {
        inputMessage = new TextField();
        messages = new TextArea();
        sendButton = new Button("Send");

        messages.setPrefHeight(600);
        inputMessage.setPrefSize(400, 50);
        sendButton.setPrefSize(90, 50);
        sendButton.setTextFill(Color.GREEN);
        sendButton.setFont(Font.font(22));

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(inputMessage, sendButton);
        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(messages, hbox);

        Scene scene = new Scene(vBox, 500, 690);
        primaryStage.setTitle(getTitle());
        primaryStage.setScene(scene);
        primaryStage.show();

        messages.setEditable(false);

        sendButton.setOnMouseClicked(e -> handleSendButtonClick());
    }

    private void setupNetwork() {
        messageThread = new Thread(() -> {
            try {
                setupConnection();
                handleMessages();
            } catch (IOException | ClassNotFoundException ex) {
                Platform.runLater(() -> showErrorAlert("Network Error", ex.getMessage()));
            }
        });
        messageThread.setDaemon(true);
        messageThread.start();
    }

    private void startMessageHandling() {
        // Ensure that messages are handled in the JavaFX Application Thread
        Platform.runLater(() -> {
            // Additional setup or UI updates if needed
        });
    }

    protected abstract void setupConnection() throws IOException;
    protected abstract String getTitle();
    protected abstract void handleSendButtonClick();

    private void handleMessages() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                String input = (String) inputStream.readObject();
                Platform.runLater(() -> messages.appendText(input + "\n"));
            }
        } catch (SocketException e) {
            Platform.runLater(() -> showErrorAlert("Connection Error", "Connection to the server was lost."));
        }
    }

    protected void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void stop() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            if (messageThread != null && messageThread.isAlive()) messageThread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

