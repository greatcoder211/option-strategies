package ownStrategy;

import java.util.List;

interface SymmetricalSpreadStrategy {
    List<Double> AllPrices(double price, List<Double> spreadValues, OptionStrategy strategy);
}
