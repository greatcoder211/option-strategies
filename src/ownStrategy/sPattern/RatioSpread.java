package ownStrategy.sPattern;

import java.util.ArrayList;
import java.util.List;

public class RatioSpread extends OptionStrategy {
    private String name;
    private final int spreadNumber = 1;

    public RatioSpread(String name, Belfort LS) {
        super(name, LS);
    }

    @Override
    public boolean getCP() { return true; }

    @Override
    public OptionType getType(){ return super.getType(); }

    @Override
    public int getSpreadNumber(){ return this.spreadNumber; }

    @Override
    public void setName() {
        if(this.LongOrShort().equals(Belfort.BUY) && this.getType().equals(OptionType.CALL)){
            this.name = "Call Ratio Spread";
        }
        else if(this.LongOrShort().equals(Belfort.BUY) && this.getType().equals(OptionType.PUT)){
            this.name = "Put Ratio Spread";
        }
        else if(this.LongOrShort().equals(Belfort.SELL) && this.getType().equals(OptionType.CALL)){
            this.name = "Call Backspread";
        }
        else if(this.LongOrShort().equals(Belfort.SELL) && this.getType().equals(OptionType.PUT)){
            this.name = "Put Backspread";
        }
    }

    public List<Double> setThePrices(double price, List<Double> spreads, OptionStrategy o){
        List<Double> prices = new ArrayList<>();
        prices.add(price);
        return prices;
    }

    @Override
    public List<OptionLeg> setOptionLegs(List<Double> prices) {
        double spread = super.getSpread2();
        List<OptionLeg> legs = new ArrayList<>();
        if (this.LongOrShort().equals(Belfort.BUY)) {
            if (getType().equals(OptionType.PUT)) {
                legs.add(new OptionLeg(prices.get(0) - spread, OptionType.PUT, Belfort.SELL));
                legs.add(new OptionLeg(prices.get(0) - spread, OptionType.PUT, Belfort.SELL));
                legs.add(new OptionLeg(prices.get(0), OptionType.PUT, Belfort.BUY));

            } else {
                legs.add(new OptionLeg(prices.get(0), OptionType.CALL, Belfort.BUY));
                legs.add(new OptionLeg(prices.get(0) + spread, OptionType.CALL, Belfort.SELL));
                legs.add(new OptionLeg(prices.get(0) + spread, OptionType.CALL, Belfort.SELL));
            }
            return legs;
        } else {
            if (getType().equals(OptionType.PUT)) {
                legs.add(new OptionLeg(prices.get(0) - spread, OptionType.PUT, Belfort.BUY));
                legs.add(new OptionLeg(prices.get(0) - spread, OptionType.PUT, Belfort.BUY));
                legs.add(new OptionLeg(prices.get(0), OptionType.PUT, Belfort.SELL));
            } else {
                legs.add(new OptionLeg(prices.get(0), OptionType.CALL, Belfort.SELL));
                legs.add(new OptionLeg(prices.get(0) + spread, OptionType.CALL, Belfort.BUY));
                legs.add(new OptionLeg(prices.get(0) + spread, OptionType.CALL, Belfort.BUY));
            }
        }
        return legs;
    }
}
