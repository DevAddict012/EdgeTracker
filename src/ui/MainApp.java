package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Welcome to the Edge Tracker");

        Label pairLabel=new Label("Pair: ");
        TextField pairField=new TextField();

        VBox root = new VBox();
        root.getChildren().add(label);


        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Edge Tracker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}