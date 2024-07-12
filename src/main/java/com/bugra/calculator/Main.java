package com.bugra.calculator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setMinWidth(372);
        primaryStage.setMinHeight(589);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/CalculatorMainView.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Yeni genişlik: " + newValue);
        });

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Yeni yükseklik: " + newValue);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
