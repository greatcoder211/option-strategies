package ownStrategy.sPattern;

import java.util.ArrayList;
import java.util.List;

public abstract class SpreadStrategy extends OptionStrategy{
    private String name;
    private Belfort ls;
    SymmetricalSpreadStrategy spread;
    private double spread2;
    private OptionType type;
    private double price;
    List<OptionLeg> legs = new ArrayList<>();

    public SpreadStrategy(String name, Belfort ls) {
        this.name = name;
        this.ls = ls;
    }
    public SpreadStrategy(){}

    //glowna metoda- strategy pattern
    public List<Double> setThePrices(double price, List<Double> spreads, SpreadStrategy o) {
        return spread.AllPrices(price, spreads, o);
    }

    public List <OptionLeg> setOptionLegs(List<Double> prices) {
        return null;
    }

    @Override
    public void setLegs(List<OptionLeg>  legs) {super.setLegs(legs);}

    public abstract int getSpreadNumber();

    public String getName() { return name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public Belfort LongOrShort() { return ls; }

    public OptionType getType() { return type; }

    public void setType(OptionType type) { this.type = type; }

    public abstract boolean getCP();

    public double getSpread2() { return spread2; }

    public void setSpread2(double spread2) { this.spread2 = spread2; }

    public abstract void setName();

    public void setLs(Belfort ls) { this.ls = ls; }

    public double getTimeToExpiry() { return super.getTimeToExpiry(); }

    public double getRiskFreeRate(){ return super.getRiskFreeRate();}

    public double  getVolatility(){ return super.getVolatility();}

}

