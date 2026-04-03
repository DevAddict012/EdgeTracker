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

                Trade trade =new Trade(pair,entryPrice,direction,stopLoss,takeProfit,result,setup,tradeDate);
                TradeDAO dao=new TradeDAO();
                dao.addTrade(trade);
            }catch(Exception ex){
                ex.printStackTrace();
            }


        });
        TableView<Trade> table=TradeHistory();
        
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
                addButton,
                table
        );

        Scene scene = new Scene(root, 400, 700);

        stage.setTitle("Edge Tracker");
        stage.setScene(scene);
        stage.show();
    }
    private TableView<Trade> TradeHistory()
    {
        TableView<Trade> table = new TableView<>();
        //INITIALISE TABLE
        TableColumn<Trade, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Trade, String> pairCol = new TableColumn<>("Pair");
        TableColumn<Trade, String> directionCol = new TableColumn<>("Direction");
        TableColumn<Trade, Double> entryCol = new TableColumn<>("Entry");
        TableColumn<Trade, Double> slCol = new TableColumn<>("Stop Loss");
        TableColumn<Trade, Double> tpCol = new TableColumn<>("Take Profit");
        TableColumn<Trade, String> resultCol = new TableColumn<>("Result");
        TableColumn<Trade, String> setupCol = new TableColumn<>("Setup");
        TableColumn<Trade, LocalDate> dateCol = new TableColumn<>("Date");

        //SET COLUMNS
        idCol.setCellValueFactory(data-> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        pairCol.setCellValueFactory(data-> new javafx.beans.property.SimpleStringProperty(data.getValue().getPair()));
        directionCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDirection()));
        entryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getEntryPrice()).asObject());
        slCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getStopLoss()).asObject());
        tpCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTakeProfit()).asObject());
        resultCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getResult()));
        setupCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSetup()));
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTradeDate()));

        table.getColumns().addAll(idCol, pairCol, entryCol, directionCol,slCol,tpCol,resultCol,setupCol,dateCol);
        return table;
    }

    public static void main(String[] args) {
        launch();
    }
}