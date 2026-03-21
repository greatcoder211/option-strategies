package ownStrategy.ui;

import ownStrategy.finance.OptionCalculator;
import ownStrategy.sPattern.OptionLeg;
import ownStrategy.sPattern.OptionStrategy;
import ownStrategy.sPattern.SpreadStrategy;

import java.util.ArrayList;
import java.util.List;

public class Chart {
    public static List<ChartPoint> draw(OptionStrategy o, List<OptionLeg> legs, double price, int quantity) {
        o.setLegs(legs);
        List <ChartPoint> resList = new ArrayList<>();
        double bottom = 0.8 * legs.get(0).getStrikePrice();
        double top = 1.2 * legs.get(legs.size() - 1).getStrikePrice();
        double totalRange = top - bottom;
        for(int i = 0; i < 100; i++){
            double d = bottom + totalRange * i / 100;
            resList.add(new ChartPoint(d, OptionCalculator.function(o, price, d, quantity)));
        }
        return resList;
    }
}
