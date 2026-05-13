# EdgeTracker

EdgeTracker is a personal desktop trading journal built to help record, review, and organize trade history. The application allows users to capture trade details such as pair, direction, entry price, stop loss, take profit, result, setup, notes, and trade date. It also includes a dark JavaFX interface with a trade history table, summary metrics, calendar-based review, and date-range filtering.

## Features

- Add new trade journal entries
- View recorded trades in a table
- Update and delete selected trades
- Calendar view with highlighted trading days
- Filter trades by selected calendar date
- Filter trades by custom date range
- Summary cards for total trades, win rate, selected date, and daily review
- Dark interface with blue accents, hover effects, and lightweight animations
- SQL Server database persistence through a DAO layer

## Tech Stack

- Java
- JavaFX
- JDBC
- Microsoft SQL Server
- Object-Oriented Programming
- DAO design pattern
- CSS for JavaFX styling
- IntelliJ IDEA project structure

## Project Structure

```text
src/
  app/
    Main.java              # Application launcher
  dao/
    TradeDAO.java          # Database CRUD operations
  model/
    Trade.java             # Trade data model
  ui/
    MainApp.java           # JavaFX user interface
    edge-tracker.css       # Dark theme styling
  util/
    DBConnection.java      # SQL Server connection helper
```

## Database

EdgeTracker currently uses a local SQL Server database named `TradingDB`. The application expects a `Trades` table with fields matching the trade model.

Expected columns:

```text
id
pair
entryPrice
direction
stopLoss
takeProfit
result
setup
tradeDate
```

The current connection string uses Windows integrated authentication and points to a local SQL Server instance:

```text
jdbc:sqlserver://localhost:1433;databaseName=TradingDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true
```

## Running the Project

This project is currently configured as a plain IntelliJ Java project.

To run it locally:

1. Open the project in IntelliJ IDEA.
2. Make sure JavaFX is configured in the project libraries.
3. Add the Microsoft SQL Server JDBC driver to the project libraries.
4. Create a local SQL Server database named `TradingDB`.
5. Create the required `Trades` table.
6. Run `src/app/Main.java`.

## Current Limitations

- No hosted demo is available because this is a desktop application that depends on a local database.
- The database connection is currently hardcoded for a local SQL Server setup.
- The project does not yet use Maven or Gradle for dependency management.
- Journal notes are currently stored together with the setup field.
- Packaging for deployment has not been added yet.

## Future Improvements

- Add a proper database schema file
- Move database settings into a config file
- Add separate journal fields for emotions, mistakes, risk, and screenshots
- Add profit/loss and risk-to-reward calculations
- Add charts for trading performance
- Package the app with `jpackage`
- Consider SQLite for easier personal desktop deployment

## CV Summary

Developed EdgeTracker, a JavaFX desktop trading journal that allows users to record, update, delete, filter, and review trade history through a dark themed interface with calendar-based navigation and summary metrics. The project uses Java, JavaFX, JDBC, Microsoft SQL Server, object-oriented design, and the DAO pattern to separate the data model, database operations, and user interface logic.
