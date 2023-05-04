package com.example.javafx_vibe.javafx_vibe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:///C:/Users/Owner/Code/javafx_vibe/src/main/resources/com/example/javafx_vibe/GUI.fxml"));
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Time (s)");
//        yAxis.setLabel("Acceleration (m/s^2)");
//        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        lineChart.setTitle("Accelerometer Data");
//
//        // create series for x, y, and z axes
//        xSeries.setName("X-axis");
//        lineChart.getData().add(xSeries);
//
//        ySeries.setName("Y-axis");
//        lineChart.getData().add(ySeries);
//
//        zSeries.setName("Z-axis");
//        lineChart.getData().add(zSeries);
//
//        StackPane root = new StackPane();
//        root.getChildren().add(lineChart);
//
//        Scene scene = new Scene(root, 800, 600);
//        stage.setScene(scene);
//        stage.show();
//
//
//    }

    }
