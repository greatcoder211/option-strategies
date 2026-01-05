package ownStrategy;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

public abstract class OptionStrategy {
    private String name;
    private Belfort ls;
    SymmetricalSpreadStrategy spread;
    private double ogspread;
    private static List<String> companyTypes = new ArrayList<>();
    private OptionType type;
    private double price;
    private double timeToExpiry;
    private final double riskFreeRate = 0.05;
    private final double volatility = 0.30;

    public OptionStrategy(String name, Belfort ls) {
        this.name = name;
        this.ls = ls;
    }

    //glowna metoda
    public List<Double> setThePrices(double price, List<Double> spreads, OptionStrategy o) {
        return spread.AllPrices(price, spreads, o);
    }

    public List <OptionLeg> setOptionLegs(List<Double> prices) {
        return null;
    }

    public abstract int getSpreadNumber();

    public String getName() { return name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public Belfort LongOrShort() { return ls; }

    public OptionType getType() { return type; }

    public void setType(OptionType type) { this.type = type; }

    public double getOgspread() { return ogspread; }

    public void setOgspread(double ogspread) { this.ogspread = ogspread; }

    public abstract boolean getCP();

    public abstract void setName();

    public void setLs(Belfort ls) { this.ls = ls; }

    protected void addCompanyType(String type) { companyTypes.add(type); }

    public void setTimeToExpiry(double timeToExpiry) {this.timeToExpiry = timeToExpiry;}

    public double getTimeToExpiry() { return timeToExpiry;}

    public double getRiskFreeRate(){ return  riskFreeRate;}

    public double  getVolatility(){ return  volatility;}

    public double netPremium(List<OptionLeg> legs, double price) {
        double res = 0;
        for(OptionLeg leg : legs){
            if(leg.getBelfort().equals(Belfort.SELL) && leg.getType().equals(OptionType.CALL)){
                res += BlackScholes.calculateCallPrice(price, leg.getStrikePrice(), timeToExpiry, riskFreeRate, volatility);
            }
            else if(leg.getBelfort().equals(Belfort.SELL) && leg.getType().equals(OptionType.PUT)){
                res += BlackScholes.calculatePutPrice(price, leg.getStrikePrice(), timeToExpiry, riskFreeRate, volatility);
            }
            else if(leg.getBelfort().equals(Belfort.BUY) && leg.getType().equals(OptionType.CALL)){
                res -= BlackScholes.calculateCallPrice(price, leg.getStrikePrice(), timeToExpiry, riskFreeRate, volatility);
            }
            else if(leg.getBelfort().equals(Belfort.BUY) && leg.getType().equals(OptionType.PUT)){
                res -= BlackScholes.calculatePutPrice(price, leg.getStrikePrice(), timeToExpiry, riskFreeRate, volatility);
            }
        }
        return res;
    }

    public double calculateProfits(List<OptionLeg> legs, double price2) {
        double res = 0;
        for(OptionLeg leg : legs){
            if(leg.getBelfort().equals(Belfort.BUY)){
                if(leg.getType().equals(OptionType.CALL) && leg.getStrikePrice() < price2){
                    res += price2 - leg.getStrikePrice();
                }
                if(leg.getType().equals(OptionType.PUT) && leg.getStrikePrice() > price2){
                    res += leg.getStrikePrice() - price2;
                }
            }
            else if(leg.getBelfort().equals(Belfort.SELL)){
                if(leg.getType().equals(OptionType.CALL) && leg.getStrikePrice() < price2){
                    res -= price2 - leg.getStrikePrice();
                }
                if(leg.getType().equals(OptionType.PUT) && leg.getStrikePrice() > price2){
                    res -= leg.getStrikePrice() - price2;
                }
            }
        }
        return res;
    }

    public static List<String> getCompanyTypes() { return companyTypes; }

    static{
        companyTypes.add("AAPL");
        companyTypes.add("TSLA");
        companyTypes.add("MSFT");
        companyTypes.add("GOOGL");
        companyTypes.add("AMZN");
        companyTypes.add("NVDA");
        companyTypes.add("CDR.WA");
        companyTypes.add("PKO.WA");
        companyTypes.add("KGH.WA");
        companyTypes.add("BTC-USD");
        companyTypes.add("ETH-USD");
    }
}

