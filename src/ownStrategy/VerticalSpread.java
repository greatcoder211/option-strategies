package ownStrategy;

import java.util.ArrayList;
import java.util.List;

class VerticalSpread extends OptionStrategy {
    private String name;
    private final int spreadNumber = 1;
    private OptionType type;

    public VerticalSpread(String name, Belfort LS) {
        super(name, LS);
    }

    @Override
    public boolean getCP() { return true; }

    @Override
    public int getSpreadNumber(){ return this.spreadNumber; }

    @Override
    public OptionType getType() { return super.getType(); }

    @Override
    public void setName() {
        if(this.LongOrShort().equals(Belfort.BUY)){
            this.name = "Long Vertical Spread";
        }
        else{
            this.name = "Short Vertical Spread";
        }
    }

    @Override
    public List<Double> setThePrices(double price, List<Double> spreads, OptionStrategy o) {
        List <Double> vertical = new ArrayList<>();
        OneSymmetricalSpreadStrategy alfa = new OneSymmetricalSpreadStrategy();
        vertical = alfa.AllPrices(price, spreads, o);
        return vertical;
    }

    @Override
    public List <OptionLeg> setOptionLegs(List<Double> prices){
        OptionType type = this.getType();
        List <OptionLeg> legs = new ArrayList<>();
        if(this.LongOrShort().equals(Belfort.BUY)){
            legs.add(new OptionLeg(prices.get(0), type, Belfort.BUY));
            legs.add(new OptionLeg(prices.get(1), type, Belfort.SELL));
        }
        else{
            legs.add(new OptionLeg(prices.get(0), type, Belfort.SELL));
            legs.add(new OptionLeg(prices.get(1), type, Belfort.BUY));
        }
        return legs;
    }
}
