package app;
import dao.TradeDAO;
import model.Trade;
import util.DBConnection;
import java.time.LocalDate;
import java.sql.Connection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
       new Main();
    }
    public Main()
    {
//        testConnection();
         addTrades();
        getAllTrades();
//        deleteTrades(2);

        Trade trade = new Trade(1,
                "XAUUSD",
                1950.5,"BUY",
                1942.0,
                1965.0,
                "WIN",
                "Liquidity b",
                LocalDate.now()
        );
        updateTrade(trade);
        getAllTrades();
    }
    public void testConnection()
    {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to database successfully!");
            }
        } catch (Exception e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
    public void addTrades()
    {
        Trade trade = new Trade(
                "XAUUSD",
                1950.5,"BUY",
                1942.0,
                1965.0,
                "WIN",
                "Liquidity Grab",
                LocalDate.now()
        );
        Trade trade2 = new Trade(
                "XAUUSD",
                1950.5,"SELL",
                1942.0,
                1965.0,
                "WIN",
                "Liquidity Grab",
                LocalDate.now()
        );

        TradeDAO dao = new TradeDAO();
        dao.addTrade(trade);
        dao.addTrade(trade2);
    }
    public void getAllTrades() {
        TradeDAO dao = new TradeDAO();

        ArrayList<Trade> trades = dao.getAllTrades();

        for (Trade trade : trades) {
            trade.toString();
            System.out.println(trade.toString());
        }
    }
    public void deleteTrades(int id)
    {
        TradeDAO dao = new TradeDAO();

        dao.deleteTrade(id);
    }
    public void updateTrade(Trade trade)
    {
        TradeDAO dao=new TradeDAO();
        dao.updateTrade(trade);
    }
}