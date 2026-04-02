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
    public Trade getTradeById(int id) {
        String sql = "SELECT * FROM Trades WHERE id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String pair = rs.getString("pair");
                double entryPrice = rs.getDouble("entryPrice");
                double stopLoss = rs.getDouble("stopLoss");
                double takeProfit = rs.getDouble("takeProfit");
                String result = rs.getString("result");
                String setup = rs.getString("setup");
                java.time.LocalDate tradeDate = rs.getDate("tradeDate").toLocalDate();

                conn.close();

                return new Trade(id, pair, entryPrice, stopLoss, takeProfit, result, setup, tradeDate);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public ArrayList<Trade> getAllTrades()
    {
        ArrayList<Trade> trades = new ArrayList<>();
        String sql="SELECT * FROM Trades";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();//query is stored in rs
            while(rs.next())//while there's still info on the next line , retrieve it.
            {
                    int id = rs.getInt("id");
                    String pair = rs.getString("pair");
                    double entryPrice = rs.getDouble("entryPrice");
                    double stopLoss = rs.getDouble("stopLoss");
                    double takeProfit = rs.getDouble("takeProfit");
                    String result = rs.getString("result");
                    String setup = rs.getString("setup");
                    java.time.LocalDate tradeDate = rs.getDate("tradeDate").toLocalDate();
                    Trade trade = new Trade(id,pair,entryPrice,stopLoss,takeProfit,result,setup,tradeDate);//Turn retrieved data from DB to a Trade Object
                    trades.add(trade);


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trades;
    }
    public void deleteTrade(int id) {
        String sql = "DELETE FROM Trades WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

           int rowsDeleted= stmt.executeUpdate();
           if(rowsDeleted>0) {
               System.out.println("Trade with ID: "+id+" was deleted successfully!");
           }
           else{
               System.out.println("Row with id "+id+ " doesn't exist");
           }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}