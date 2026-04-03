package ui;
//IMPORT THE BACKEND CLASSES
import dao.TradeDAO;
import model.Trade;
import java.time.LocalDate;
//Necessary imports
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
        ComboBox<String> directionBox = new ComboBox<>();
        directionBox.getItems().addAll("BUY","SELL");

        Label stopLossLabel = new Label("Stop Loss:");
        TextField stopLossField = new TextField();

        Label takeProfitLabel = new Label("Take Profit:");
        TextField takeProfitField = new TextField();

        Label resultLabel = new Label("Result:");
        ComboBox<String> resultBox = new ComboBox<>();
        resultBox.getItems().addAll("WIN", "LOSS");

        Label setupLabel = new Label("Setup:");
        TextField setupField=new TextField();

        Label dateLabel = new Label("Trade Date:");
        DatePicker datePicker = new DatePicker();

        Button addButton = new Button("Add Trade");
        //CONNECTING THE ADD BUTTON TO ADD A TRADE IN THE DATABASE
        addButton.setOnAction(e->{
            try{
                String pair = pairField.getText();
                double entryPrice = Double.parseDouble(entryField.getText());
                String direction = directionBox.getValue();
                double stopLoss = Double.parseDouble(stopLossField.getText());
                double takeProfit = Double.parseDouble(takeProfitField.getText());
                String result = resultBox.getValue();
                String setup = setupField.getText();
                LocalDate tradeDate = datePicker.getValue();
            }catch(Exception ex){
                ex.printStackTrace();
            }


        });
        VBox root = new VBox(10);
        root.getChildren().addAll(
                heading,
                pairLabel, pairField,
                entryLabel, entryField,
                directionLabel,directionBox,
                stopLossLabel, stopLossField,
                takeProfitLabel, takeProfitField,
                resultLabel, resultBox,
                setupLabel, setupField,
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