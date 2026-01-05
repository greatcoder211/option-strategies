package ownStrategy;

import java.util.ArrayList;
import java.util.List;

class IronCondor extends OptionStrategy {
    private String name;
    private final int spreadNumber = 2;

    public IronCondor(String name, Belfort LS) {
        super(name, LS);
    }

    @Override
    public boolean getCP() { return false; }

    @Override
    public int getSpreadNumber(){ return this.spreadNumber; }

    @Override
    public String getName() { return this.name; }

    @Override
    public void setName() {
        if(this.LongOrShort().equals(Belfort.BUY)){
            this.name = "Long Iron Condor";
        }
        else{
            this.name = "Iron Condor";
        }
    }


    @Override
    public List<Double> setThePrices(double price, List<Double> spreads, OptionStrategy o) {
        List <Double> Condor = new ArrayList<>();
        TwoSymmetricalSpreadStrategy beta = new TwoSymmetricalSpreadStrategy();
        Condor = beta.AllPrices(price, spreads, o);
        return Condor;
    }

    @Override
    public List <OptionLeg> setOptionLegs(List<Double> prices){
        List <OptionLeg> legs = new ArrayList<>();
        if(this.LongOrShort().equals(Belfort.BUY)){
            legs.add(new OptionLeg(prices.get(0), OptionType.PUT, Belfort.SELL));
            legs.add(new OptionLeg(prices.get(1), OptionType.PUT, Belfort.BUY));
            legs.add(new OptionLeg(prices.get(2), OptionType.CALL, Belfort.BUY));
            legs.add(new OptionLeg(prices.get(3), OptionType.CALL, Belfort.SELL));
            return legs;
        }
        else{
            legs.add(new OptionLeg(prices.get(0), OptionType.PUT, Belfort.BUY));
            legs.add(new OptionLeg(prices.get(1), OptionType.PUT, Belfort.SELL));
            legs.add(new OptionLeg(prices.get(2), OptionType.CALL, Belfort.SELL));
            legs.add(new OptionLeg(prices.get(3), OptionType.CALL, Belfort.BUY));
            return legs;
        }
    }
}

