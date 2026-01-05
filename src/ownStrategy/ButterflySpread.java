package ownStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ButterflySpread extends OptionStrategy {
    private String name;
    private OptionType type;
    private final int spreadNumber = 1;

    public ButterflySpread(String name, Belfort LS) {
        super(name, LS);
    }

    public int getSpreadNumber(){ return this.spreadNumber; }

    @Override
    public boolean getCP() { return true; }

    @Override
    public OptionType getType() { return super.getType(); }

    @Override
    public void setName() {
        if(this.LongOrShort().equals(Belfort.BUY)){
            this.name = "Long Butterfly Spread";
        }
        else{
            this.name = "Short Butterfly Spread";
        }
    }

    @Override
    public List<Double> setThePrices(double price, List<Double> spreads, OptionStrategy o) {
        List <Double> butterfly = new ArrayList<>();
        OneSymmetricalSpreadStrategy alfa = new OneSymmetricalSpreadStrategy();
        butterfly = alfa.AllPrices(price, spreads, o);
        butterfly.add(price);
        butterfly.add(price);
        Collections.swap(butterfly, 1, 3);
        return butterfly;
    }

    @Override
    public List <OptionLeg> setOptionLegs(List<Double> prices){
        OptionType type = this.getType();
        List <OptionLeg> legs = new ArrayList<>();
        if(this.LongOrShort().equals(Belfort.BUY)){
            legs.add(new OptionLeg(prices.get(0), type, Belfort.BUY));
            legs.add(new OptionLeg(super.getPrice(), type, Belfort.SELL));
            legs.add(new OptionLeg(super.getPrice(), type, Belfort.SELL));
            legs.add(new OptionLeg(prices.get(3), type, Belfort.BUY));
        }
        else{
            legs.add(new OptionLeg(prices.get(0), type, Belfort.SELL));
            legs.add(new OptionLeg(super.getPrice(), type, Belfort.BUY));
            legs.add(new OptionLeg(super.getPrice(), type, Belfort.BUY));
            legs.add(new OptionLeg(prices.get(3), type, Belfort.SELL));
        }
        return legs;
    }
}

