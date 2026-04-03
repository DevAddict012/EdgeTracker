package ui;
//IMPORT THE BACKEND CLASSES

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        Label heading = new Label("TradeJournal");

        Label pairLabel = new Label("Pair:");
        TextField pairField = new TextField();

        Label entryLabel = new Label("Entry Price:");
        TextField entryField = new TextField();

        Label directionLabel = new Label("Direction:");
        ComboBox<String> directionField = new ComboBox<>();
        directionField.getItems().addAll("BUY","SELL");

        Label stopLossLabel = new Label("Stop Loss:");
        TextField stopLossField = new TextField();

        Label takeProfitLabel = new Label("Take Profit:");
        TextField takeProfitField = new TextField();

        Label resultLabel = new Label("Result:");
        ComboBox<String> resultBox = new ComboBox<>();
        resultBox.getItems().addAll("WIN", "LOSS");

        Label setupLabel = new Label("Setup:");
        TextField setup=new TextField();

        Label dateLabel = new Label("Trade Date:");
        DatePicker datePicker = new DatePicker();

        Button addButton = new Button("Add Trade");

        VBox root = new VBox(10);
        root.getChildren().addAll(
                heading,
                pairLabel, pairField,
                entryLabel, entryField,
                directionLabel,directionField,
                stopLossLabel, stopLossField,
                takeProfitLabel, takeProfitField,
                resultLabel, resultBox,
                setupLabel, setup,
                dateLabel, datePicker,
                addButton
        );

        Scene scene = new Scene(root, 400, 700);

        stage.setTitle("Edge Tracker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}