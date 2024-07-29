package com.example.chat;


import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // To start the server
      //  Server server = new Server();
        //server.start(primaryStage);

        // To start the client
     Client client = new Client();
     client.start(primaryStage);


    }

    public static void main(String[] args) {
        launch();
    }
}
