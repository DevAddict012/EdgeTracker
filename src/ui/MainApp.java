package ui;

import dao.TradeDAO;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Trade;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainApp extends Application {
    private static final String APP_BG = "#05070d";
    private static final String PANEL_BG = "#0b1120";
    private static final String PANEL_BG_SOFT = "#111827";
    private static final String FIELD_BG = "#080d18";
    private static final String BORDER = "#1f2a44";
    private static final String TEXT = "#e5f0ff";
    private static final String MUTED_TEXT = "#8fa3bf";
    private static final String BLUE = "#1d9bf0";
    private static final String BLUE_DARK = "#075985";
    private static final String RED = "#ef4444";
    private static final String GREEN = "#22c55e";

    private final TradeDAO tradeDAO = new TradeDAO();
    private final ObservableList<Trade> allTrades = FXCollections.observableArrayList();
    private final ObservableList<Trade> visibleTrades = FXCollections.observableArrayList();

    private TableView<Trade> tradeTable;
    private GridPane calendarGrid;
    private Label monthLabel;
    private Label totalTradesValue;
    private Label winRateValue;
    private Label selectedDateValue;
    private Label dayResultValue;

    private TextField pairField;
    private TextField entryField;
    private ComboBox<String> directionBox;
    private TextField stopLossField;
    private TextField takeProfitField;
    private ComboBox<String> resultBox;
    private TextField setupField;
    private TextArea journalNotesArea;
    private DatePicker datePicker;
    private DatePicker rangeStartPicker;
    private DatePicker rangeEndPicker;

    private YearMonth displayedMonth = YearMonth.now();
    private LocalDate selectedDate = LocalDate.now();

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setLeft(createJournalPanel());
        root.setCenter(createHistoryPanel());
        root.setRight(createCalendarPanel());
        root.setStyle("-fx-background-color: " + APP_BG + ";");

        refreshTrades();
        filterTradesForDate(selectedDate);

        Scene scene = new Scene(root, 1180, 740);
        scene.getStylesheets().add(getClass().getResource("edge-tracker.css").toExternalForm());
        stage.setTitle("EdgeTracker - Trading Journal");
        stage.setScene(scene);
        stage.show();

        playEntrance(root.getTop(), 0, -18);
        playEntrance(root.getLeft(), 90, -22);
        playEntrance(root.getCenter(), 150, 0);
        playEntrance(root.getRight(), 210, 22);
    }

    private VBox createHeader() {
        Label title = new Label("EdgeTracker");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + TEXT + ";");

        Label subtitle = new Label("Plan the trade. Record the trade. Review the pattern.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MUTED_TEXT + ";");

        VBox titleBox = new VBox(2, title, subtitle);

        totalTradesValue = createMetricValue("0");
        winRateValue = createMetricValue("0%");
        selectedDateValue = createMetricValue(selectedDate.toString());
        dayResultValue = createMetricValue("0 trades");

        HBox metrics = new HBox(12,
                createMetricCard("Total Trades", totalTradesValue),
                createMetricCard("Win Rate", winRateValue),
                createMetricCard("Selected Date", selectedDateValue),
                createMetricCard("Day Review", dayResultValue)
        );
        metrics.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(24, titleBox, spacer, metrics);
        header.setPadding(new Insets(22, 26, 18, 26));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #05070d, #0b1120, #082f49);"
                + " -fx-border-color: " + BORDER + "; -fx-border-width: 0 0 1 0;");
        header.setEffect(blueShadow(0.22, 18));

        return new VBox(header);
    }

    private VBox createJournalPanel() {
        Label heading = sectionTitle("Journal Entry");

        pairField = new TextField();
        pairField.setPromptText("XAUUSD, EURUSD...");
        styleTextInput(pairField);

        entryField = new TextField();
        entryField.setPromptText("Entry price");
        styleTextInput(entryField);

        directionBox = new ComboBox<>();
        directionBox.getItems().addAll("BUY", "SELL");
        directionBox.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(directionBox);

        stopLossField = new TextField();
        stopLossField.setPromptText("Stop loss");
        styleTextInput(stopLossField);

        takeProfitField = new TextField();
        takeProfitField.setPromptText("Take profit");
        styleTextInput(takeProfitField);

        resultBox = new ComboBox<>();
        resultBox.getItems().addAll("WIN", "LOSS", "BREAKEVEN");
        resultBox.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(resultBox);

        setupField = new TextField();
        setupField.setPromptText("Liquidity grab, breakout, retest...");
        styleTextInput(setupField);

        journalNotesArea = new TextArea();
        journalNotesArea.setPromptText("What did you see? Did you follow your plan?");
        journalNotesArea.setPrefRowCount(5);
        journalNotesArea.setWrapText(true);
        styleTextInput(journalNotesArea);

        datePicker = new DatePicker(selectedDate);
        datePicker.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(datePicker);

        Button addButton = primaryButton("Add Trade");
        Button updateButton = secondaryButton("Update Selected");
        Button deleteButton = dangerButton("Delete");
        Button clearButton = secondaryButton("Clear");

        addButton.setOnAction(event -> addTrade());
        updateButton.setOnAction(event -> updateSelectedTrade());
        deleteButton.setOnAction(event -> deleteSelectedTrade());
        clearButton.setOnAction(event -> clearForm());

        HBox actionRow = new HBox(8, addButton, updateButton);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        HBox secondActionRow = new HBox(8, deleteButton, clearButton);
        secondActionRow.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(10,
                heading,
                formField("Pair", pairField),
                formField("Entry", entryField),
                formField("Direction", directionBox),
                formField("Stop Loss", stopLossField),
                formField("Take Profit", takeProfitField),
                formField("Result", resultBox),
                formField("Setup", setupField),
                formField("Trade Date", datePicker),
                formField("Journal Notes", journalNotesArea),
                actionRow,
                secondActionRow
        );

        panel.setPadding(new Insets(18));
        panel.setPrefWidth(300);
        panel.setStyle("-fx-background-color: " + PANEL_BG + "; -fx-border-color: " + BORDER + "; -fx-border-width: 0 1 0 0;");
        panel.setEffect(blueShadow(0.12, 16));
        return panel;
    }

    private VBox createHistoryPanel() {
        Label heading = sectionTitle("Trade History");

        rangeStartPicker = new DatePicker(selectedDate.minusDays(7));
        rangeEndPicker = new DatePicker(selectedDate);
        rangeStartPicker.setPrefWidth(128);
        rangeEndPicker.setPrefWidth(128);
        styleComboBox(rangeStartPicker);
        styleComboBox(rangeEndPicker);

        Button applyRangeButton = primaryButton("Apply Range");
        applyRangeButton.setOnAction(event -> filterTradesByDateRange());

        Button showAllButton = secondaryButton("Show All");
        showAllButton.setOnAction(event -> showAllTrades());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(10,
                heading,
                spacer,
                compactField("From", rangeStartPicker),
                compactField("To", rangeEndPicker),
                applyRangeButton,
                showAllButton
        );
        toolbar.setAlignment(Pos.BOTTOM_LEFT);

        tradeTable = createTradeTable();
        tradeTable.setItems(visibleTrades);
        tradeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldTrade, newTrade) -> {
            if (newTrade != null) {
                populateForm(newTrade);
            }
        });

        VBox panel = new VBox(12, toolbar, tradeTable);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: " + APP_BG + ";");
        VBox.setVgrow(tradeTable, Priority.ALWAYS);
        return panel;
    }

    private VBox createCalendarPanel() {
        Label heading = sectionTitle("Calendar");
        monthLabel = new Label();
        monthLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT + ";");

        Button previousButton = secondaryButton("<");
        Button nextButton = secondaryButton(">");
        previousButton.setOnAction(event -> {
            displayedMonth = displayedMonth.minusMonths(1);
            renderCalendar();
        });
        nextButton.setOnAction(event -> {
            displayedMonth = displayedMonth.plusMonths(1);
            renderCalendar();
        });

        HBox monthControls = new HBox(8, previousButton, monthLabel, nextButton);
        monthControls.setAlignment(Pos.CENTER);

        calendarGrid = new GridPane();
        calendarGrid.setHgap(6);
        calendarGrid.setVgap(6);
        calendarGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 7.0);
            calendarGrid.getColumnConstraints().add(column);
        }

        Label hint = new Label("Days with recorded trades are highlighted. Select a day to review only those trades.");
        hint.setWrapText(true);
        hint.setStyle("-fx-text-fill: " + MUTED_TEXT + "; -fx-font-size: 12px;");

        VBox panel = new VBox(14, heading, monthControls, calendarGrid, hint);
        panel.setPadding(new Insets(18));
        panel.setPrefWidth(320);
        panel.setStyle("-fx-background-color: " + PANEL_BG + "; -fx-border-color: " + BORDER + "; -fx-border-width: 0 0 0 1;");
        panel.setEffect(blueShadow(0.12, 16));
        return panel;
    }

    private TableView<Trade> createTradeTable() {
        TableView<Trade> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle("-fx-background-color: " + PANEL_BG + ";"
                + " -fx-control-inner-background: " + PANEL_BG + ";"
                + " -fx-table-cell-border-color: " + BORDER + ";"
                + " -fx-selection-bar: " + BLUE_DARK + ";"
                + " -fx-selection-bar-non-focused: " + BLUE_DARK + ";"
                + " -fx-text-background-color: " + TEXT + ";"
                + " -fx-background-radius: 10;"
                + " -fx-border-color: " + BORDER + ";"
                + " -fx-border-radius: 10;");
        table.setEffect(blueShadow(0.16, 18));
        table.setPlaceholder(new Label("No trades recorded for this view."));
        table.setRowFactory(view -> createStyledTradeRow());

        TableColumn<Trade, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Trade, String> pairCol = new TableColumn<>("Pair");
        TableColumn<Trade, String> directionCol = new TableColumn<>("Side");
        TableColumn<Trade, Double> entryCol = new TableColumn<>("Entry");
        TableColumn<Trade, Double> slCol = new TableColumn<>("SL");
        TableColumn<Trade, Double> tpCol = new TableColumn<>("TP");
        TableColumn<Trade, String> resultCol = new TableColumn<>("Result");
        TableColumn<Trade, String> setupCol = new TableColumn<>("Setup / Notes");
        TableColumn<Trade, LocalDate> dateCol = new TableColumn<>("Date");

        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        pairCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPair()));
        directionCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDirection()));
        entryCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getEntryPrice()).asObject());
        slCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getStopLoss()).asObject());
        tpCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTakeProfit()).asObject());
        resultCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getResult()));
        setupCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSetup()));
        dateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTradeDate()));

        table.getColumns().add(idCol);
        table.getColumns().add(pairCol);
        table.getColumns().add(directionCol);
        table.getColumns().add(entryCol);
        table.getColumns().add(slCol);
        table.getColumns().add(tpCol);
        table.getColumns().add(resultCol);
        table.getColumns().add(setupCol);
        table.getColumns().add(dateCol);
        return table;
    }

    private TableRow<Trade> createStyledTradeRow() {
        TableRow<Trade> row = new TableRow<>() {
            @Override
            protected void updateItem(Trade trade, boolean empty) {
                super.updateItem(trade, empty);

                if (empty || trade == null) {
                    setStyle("-fx-background-color: " + PANEL_BG + ";");
                    return;
                }

                String resultColor = MUTED_TEXT;
                if ("WIN".equalsIgnoreCase(trade.getResult())) {
                    resultColor = GREEN;
                } else if ("LOSS".equalsIgnoreCase(trade.getResult())) {
                    resultColor = RED;
                }

                setStyle("-fx-background-color: " + PANEL_BG + ";"
                        + " -fx-text-background-color: " + TEXT + ";"
                        + " -fx-border-color: transparent transparent " + BORDER + " transparent;"
                        + " -fx-border-width: 0 0 1 0;"
                        + " -fx-accent: " + resultColor + ";");
            }
        };

        row.setOnMouseEntered(event -> {
            if (!row.isEmpty()) {
                row.setStyle("-fx-background-color: #111f35; -fx-text-background-color: " + TEXT + ";");
            }
        });
        row.setOnMouseExited(event -> {
            Trade trade = row.getItem();
            if (trade != null) {
                row.setStyle("-fx-background-color: " + PANEL_BG + ";"
                        + " -fx-text-background-color: " + TEXT + ";"
                        + " -fx-border-color: transparent transparent " + BORDER + " transparent;"
                        + " -fx-border-width: 0 0 1 0;");
            }
        });
        return row;
    }

    private void addTrade() {
        Trade trade = readTradeFromForm(null);
        if (trade == null) {
            return;
        }

        tradeDAO.addTrade(trade);
        selectedDate = trade.getTradeDate();
        displayedMonth = YearMonth.from(selectedDate);
        refreshTrades();
        filterTradesForDate(selectedDate);
        clearForm();
    }

    private void updateSelectedTrade() {
        Trade selectedTrade = tradeTable.getSelectionModel().getSelectedItem();
        if (selectedTrade == null) {
            showWarning("Select a trade first", "Choose a trade from the table before updating it.");
            return;
        }

        Trade editedTrade = readTradeFromForm(selectedTrade.getId());
        if (editedTrade == null) {
            return;
        }

        tradeDAO.updateTrade(editedTrade);
        selectedDate = editedTrade.getTradeDate();
        displayedMonth = YearMonth.from(selectedDate);
        refreshTrades();
        filterTradesForDate(selectedDate);
    }

    private void deleteSelectedTrade() {
        Trade selectedTrade = tradeTable.getSelectionModel().getSelectedItem();
        if (selectedTrade == null) {
            showWarning("Select a trade first", "Choose a trade from the table before deleting it.");
            return;
        }

        tradeDAO.deleteTrade(selectedTrade.getId());
        refreshTrades();
        filterTradesForDate(selectedDate);
        clearForm();
    }

    private Trade readTradeFromForm(Integer tradeId) {
        try {
            String pair = pairField.getText().trim().toUpperCase();
            String direction = directionBox.getValue();
            String result = resultBox.getValue();
            LocalDate tradeDate = datePicker.getValue();

            if (pair.isEmpty() || direction == null || result == null || tradeDate == null) {
                showWarning("Missing trade details", "Pair, direction, result, and trade date are required.");
                return null;
            }

            double entryPrice = Double.parseDouble(entryField.getText().trim());
            double stopLoss = Double.parseDouble(stopLossField.getText().trim());
            double takeProfit = Double.parseDouble(takeProfitField.getText().trim());
            String setup = combineSetupAndNotes();

            if (tradeId == null) {
                return new Trade(pair, entryPrice, direction, stopLoss, takeProfit, result, setup, tradeDate);
            }

            return new Trade(tradeId, pair, entryPrice, direction, stopLoss, takeProfit, result, setup, tradeDate);
        } catch (NumberFormatException ex) {
            showWarning("Check your prices", "Entry, stop loss, and take profit must be valid numbers.");
            return null;
        }
    }

    private String combineSetupAndNotes() {
        String setup = setupField.getText().trim();
        String notes = journalNotesArea.getText().trim();

        if (notes.isEmpty()) {
            return setup;
        }
        if (setup.isEmpty()) {
            return notes;
        }
        return setup + " | " + notes;
    }

    private void refreshTrades() {
        ArrayList<Trade> trades = tradeDAO.getAllTrades();
        allTrades.setAll(trades);
        updateMetrics();
        renderCalendar();
    }

    private void filterTradesForDate(LocalDate date) {
        selectedDate = date;
        visibleTrades.setAll(filterByDate(date));
        selectedDateValue.setText(date.toString());
        updateDayResult();
        renderCalendar();
    }

    private void showAllTrades() {
        visibleTrades.setAll(allTrades);
        selectedDateValue.setText("All trades");
        dayResultValue.setText(allTrades.size() + " trades");
    }

    private void filterTradesByDateRange() {
        LocalDate startDate = rangeStartPicker.getValue();
        LocalDate endDate = rangeEndPicker.getValue();

        if (startDate == null || endDate == null) {
            showWarning("Choose a date range", "Both the start and end dates are required for range filtering.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showWarning("Check the date range", "The start date cannot be after the end date.");
            return;
        }

        List<Trade> matches = new ArrayList<>();
        for (Trade trade : allTrades) {
            LocalDate tradeDate = trade.getTradeDate();
            if (!tradeDate.isBefore(startDate) && !tradeDate.isAfter(endDate)) {
                matches.add(trade);
            }
        }

        visibleTrades.setAll(matches);
        selectedDateValue.setText(startDate + " to " + endDate);
        updateDayResult();
        fadeNode(tradeTable);
    }

    private List<Trade> filterByDate(LocalDate date) {
        List<Trade> matches = new ArrayList<>();
        for (Trade trade : allTrades) {
            if (date.equals(trade.getTradeDate())) {
                matches.add(trade);
            }
        }
        return matches;
    }

    private void updateMetrics() {
        int totalTrades = allTrades.size();
        int wins = 0;

        for (Trade trade : allTrades) {
            if ("WIN".equalsIgnoreCase(trade.getResult())) {
                wins++;
            }
        }

        totalTradesValue.setText(String.valueOf(totalTrades));
        winRateValue.setText(totalTrades == 0 ? "0%" : Math.round((wins * 100.0) / totalTrades) + "%");
    }

    private void updateDayResult() {
        int wins = 0;
        int losses = 0;
        int breakeven = 0;

        for (Trade trade : visibleTrades) {
            if ("WIN".equalsIgnoreCase(trade.getResult())) {
                wins++;
            } else if ("LOSS".equalsIgnoreCase(trade.getResult())) {
                losses++;
            } else {
                breakeven++;
            }
        }

        dayResultValue.setText(visibleTrades.size() + " trades | W " + wins + " L " + losses + " BE " + breakeven);
    }

    private void renderCalendar() {
        if (calendarGrid == null || monthLabel == null) {
            return;
        }

        calendarGrid.getChildren().clear();
        monthLabel.setText(displayedMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + displayedMonth.getYear());

        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < dayNames.length; i++) {
            Label dayName = new Label(dayNames[i]);
            dayName.setAlignment(Pos.CENTER);
            dayName.setMaxWidth(Double.MAX_VALUE);
            dayName.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + MUTED_TEXT + ";");
            calendarGrid.add(dayName, i, 0);
        }

        Map<LocalDate, Integer> tradeCounts = getTradeCountsByDate();
        LocalDate firstDay = displayedMonth.atDay(1);
        int startColumn = firstDay.getDayOfWeek().getValue() - 1;
        int row = 1;
        int column = startColumn;

        for (int day = 1; day <= displayedMonth.lengthOfMonth(); day++) {
            LocalDate date = displayedMonth.atDay(day);
            int count = tradeCounts.getOrDefault(date, 0);

            Button dayButton = new Button(day + (count > 0 ? "\n" + count : ""));
            dayButton.setMaxWidth(Double.MAX_VALUE);
            dayButton.setMinHeight(46);
            dayButton.setStyle(calendarDayStyle(date, count));
            addHoverScale(dayButton, 1.04);
            dayButton.setOnAction(event -> {
                datePicker.setValue(date);
                pulse(dayButton);
                filterTradesForDate(date);
            });

            calendarGrid.add(dayButton, column, row);

            column++;
            if (column == 7) {
                column = 0;
                row++;
            }
        }
    }

    private Map<LocalDate, Integer> getTradeCountsByDate() {
        Map<LocalDate, Integer> tradeCounts = new HashMap<>();
        for (Trade trade : allTrades) {
            LocalDate tradeDate = trade.getTradeDate();
            tradeCounts.put(tradeDate, tradeCounts.getOrDefault(tradeDate, 0) + 1);
        }
        return tradeCounts;
    }

    private String calendarDayStyle(LocalDate date, int tradeCount) {
        String base = "-fx-background-radius: 8; -fx-border-radius: 8; -fx-font-size: 12px; -fx-font-weight: bold;";
        if (date.equals(selectedDate)) {
            return base + " -fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-border-color: #7dd3fc;"
                    + " -fx-effect: dropshadow(gaussian, rgba(29,155,240,0.55), 14, 0.4, 0, 0);";
        }
        if (tradeCount > 0) {
            return base + " -fx-background-color: #0f2f52; -fx-text-fill: #bfdbfe; -fx-border-color: " + BLUE + ";";
        }
        return base + " -fx-background-color: " + FIELD_BG + "; -fx-text-fill: " + MUTED_TEXT + "; -fx-border-color: " + BORDER + ";";
    }

    private void populateForm(Trade trade) {
        pairField.setText(trade.getPair());
        entryField.setText(String.valueOf(trade.getEntryPrice()));
        directionBox.setValue(trade.getDirection());
        stopLossField.setText(String.valueOf(trade.getStopLoss()));
        takeProfitField.setText(String.valueOf(trade.getTakeProfit()));
        resultBox.setValue(trade.getResult());
        setupField.setText(trade.getSetup());
        journalNotesArea.clear();
        datePicker.setValue(trade.getTradeDate());
    }

    private void clearForm() {
        pairField.clear();
        entryField.clear();
        directionBox.setValue(null);
        stopLossField.clear();
        takeProfitField.clear();
        resultBox.setValue(null);
        setupField.clear();
        journalNotesArea.clear();
        datePicker.setValue(selectedDate);
        tradeTable.getSelectionModel().clearSelection();
    }

    private VBox formField(String labelText, javafx.scene.Node input) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + MUTED_TEXT + ";");
        VBox box = new VBox(4, label, input);
        return box;
    }

    private VBox compactField(String labelText, Node input) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + MUTED_TEXT + ";");
        VBox box = new VBox(4, label, input);
        return box;
    }

    private VBox createMetricCard(String labelText, Label valueLabel) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MUTED_TEXT + ";");
        VBox card = new VBox(4, label, valueLabel);
        card.setPadding(new Insets(10, 14, 10, 14));
        card.setMinWidth(118);
        card.setStyle("-fx-background-color: rgba(8,13,24,0.92);"
                + " -fx-background-radius: 8;"
                + " -fx-border-color: " + BORDER + ";"
                + " -fx-border-radius: 8;");
        card.setEffect(blueShadow(0.18, 14));
        addHoverScale(card, 1.03);
        return card;
    }

    private Label createMetricValue(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: " + TEXT + ";");
        return label;
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT + ";");
        return label;
    }

    private Button primaryButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(to right, " + BLUE_DARK + ", " + BLUE + ");"
                + " -fx-text-fill: white; -fx-background-radius: 7; -fx-font-weight: bold;"
                + " -fx-border-color: #7dd3fc; -fx-border-radius: 7;");
        button.setEffect(blueShadow(0.30, 14));
        addHoverScale(button, 1.04);
        return button;
    }

    private Button secondaryButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + PANEL_BG_SOFT + ";"
                + " -fx-text-fill: " + TEXT + ";"
                + " -fx-background-radius: 7;"
                + " -fx-border-color: " + BORDER + ";"
                + " -fx-border-radius: 7;");
        addHoverScale(button, 1.04);
        return button;
    }

    private Button dangerButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #2a1015; -fx-text-fill: #fecaca;"
                + " -fx-background-radius: 7; -fx-border-color: #7f1d1d; -fx-border-radius: 7;");
        addHoverScale(button, 1.04);
        return button;
    }

    private void styleTextInput(Node input) {
        input.setStyle("-fx-background-color: " + FIELD_BG + ";"
                + " -fx-control-inner-background: " + FIELD_BG + ";"
                + " -fx-text-fill: " + TEXT + ";"
                + " -fx-prompt-text-fill: #53657f;"
                + " -fx-highlight-fill: " + BLUE_DARK + ";"
                + " -fx-highlight-text-fill: white;"
                + " -fx-background-radius: 7;"
                + " -fx-border-color: " + BORDER + ";"
                + " -fx-border-radius: 7;");
    }

    private void styleComboBox(Node input) {
        input.setStyle("-fx-background-color: " + FIELD_BG + ";"
                + " -fx-control-inner-background: " + FIELD_BG + ";"
                + " -fx-text-fill: " + TEXT + ";"
                + " -fx-prompt-text-fill: #53657f;"
                + " -fx-background-radius: 7;"
                + " -fx-border-color: " + BORDER + ";"
                + " -fx-border-radius: 7;"
                + " -fx-mark-color: " + BLUE + ";");
    }

    private DropShadow blueShadow(double opacity, double radius) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(29, 155, 240, opacity));
        shadow.setRadius(radius);
        shadow.setSpread(0.12);
        return shadow;
    }

    private void addHoverScale(Node node, double scale) {
        node.setOnMouseEntered(event -> animateScale(node, scale, 115));
        node.setOnMouseExited(event -> animateScale(node, 1.0, 115));
    }

    private void animateScale(Node node, double scale, int millis) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(millis), node);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }

    private void pulse(Node node) {
        ScaleTransition pulse = new ScaleTransition(Duration.millis(130), node);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(0.94);
        pulse.setToY(0.94);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.play();
    }

    private void playEntrance(Node node, int delayMillis, double fromX) {
        node.setOpacity(0);
        node.setTranslateX(fromX);

        FadeTransition fade = new FadeTransition(Duration.millis(420), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delayMillis));

        TranslateTransition slide = new TranslateTransition(Duration.millis(420), node);
        slide.setFromX(fromX);
        slide.setToX(0);
        slide.setDelay(Duration.millis(delayMillis));

        ParallelTransition entrance = new ParallelTransition(fade, slide);
        entrance.play();
    }

    private void fadeNode(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(180), node);
        fade.setFromValue(0.65);
        fade.setToValue(1);
        fade.play();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
