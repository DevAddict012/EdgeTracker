package app;
import dao.TradeDAO;
import model.Trade;
import util.DBConnection;
import java.time.LocalDate;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
       new Main();
    }
    public Main()
    {
//        testConnection();
        Trade trade = new Trade(
                "XAUUSD",
                1950.5,
                1942.0,
                1965.0,
                "WIN",
                "Liquidity Grab",
                LocalDate.now()
        );

        TradeDAO dao = new TradeDAO();
        dao.addTrade(trade);
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
}