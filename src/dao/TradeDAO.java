package dao;

import model.Trade;
import util.DBConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TradeDAO {

    public void addTrade(Trade trade) {
        String sql = "INSERT INTO Trades (pair, entryPrice, stopLoss, takeProfit, result, setup, tradeDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trade.getPair());
            stmt.setDouble(2, trade.getEntryPrice());
            stmt.setDouble(3, trade.getStopLoss());
            stmt.setDouble(4, trade.getTakeProfit());
            stmt.setString(5, trade.getResult());
            stmt.setString(6, trade.getSetup());
            stmt.setDate(7, java.sql.Date.valueOf(trade.getTradeDate()));

            stmt.executeUpdate();
            System.out.println("Trade added successfully!");

        } catch (SQLException e) {
            System.out.println("Failed to add trade.");
            e.printStackTrace();
        }
    }
    
}