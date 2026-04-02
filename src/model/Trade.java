package model;

import java.time.LocalDate;

public class Trade {
    private int id;
    private String pair;
    private double entryPrice;
    private double stopLoss;
    private double takeProfit;
    private String result;
    private String setup;
    private LocalDate tradeDate;

    public Trade(String pair, double entryPrice, double stopLoss, double takeProfit,
                 String result, String setup, LocalDate tradeDate) {
        this.pair = pair;
        this.entryPrice = entryPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.result = result;
        this.setup = setup;
        this.tradeDate = tradeDate;
    }

    public Trade(int id, String pair, double entryPrice, double stopLoss, double takeProfit,
                 String result, String setup, LocalDate tradeDate) {
        this.id = id;
        this.pair = pair;
        this.entryPrice = entryPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.result = result;
        this.setup = setup;
        this.tradeDate = tradeDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public double getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(double entryPrice) {
        this.entryPrice = entryPrice;
    }

    public double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public double getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(double takeProfit) {
        this.takeProfit = takeProfit;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", pair='" + pair + '\'' +
                ", entryPrice=" + entryPrice +
                ", stopLoss=" + stopLoss +
                ", takeProfit=" + takeProfit +
                ", result='" + result + '\'' +
                ", setup='" + setup + '\'' +
                ", tradeDate=" + tradeDate +
                '}';
    }
}