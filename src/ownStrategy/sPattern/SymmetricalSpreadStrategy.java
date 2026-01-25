package ownStrategy.sPattern;

import java.util.List;

public interface SymmetricalSpreadStrategy {
    List<Double> AllPrices(double price, List<Double> spreadValues, OptionStrategy strategy);
}
