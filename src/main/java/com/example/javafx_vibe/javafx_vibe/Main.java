package com.example.javafx_vibe.javafx_vibe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

//        Mac Version
        URL url = new File("src/main/resources/com/example/javafx_vibe/GUI.fxml").toURI().toURL();
        loader.setLocation(url);

        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    @Override
    public void stop(){
        MainController.closeComPort();
        System.out.println("App stopped");
    }

}

