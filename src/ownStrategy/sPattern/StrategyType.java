package ownStrategy.sPattern;

public enum StrategyType {
    Butterfly_Spread(ButterflySpread.class),
    Vertical_Spread(VerticalSpread.class),
    Ratio_Spread(RatioSpread.class),
    Iron_Condor(IronCondor.class),
    Iron_Butterfly(IronButterfly.class),
    Strangle(Strangle.class);

    private final Class<? extends OptionStrategy> strategyClass;

    StrategyType(Class<? extends OptionStrategy> strategyClass) {
        this.strategyClass = strategyClass;
    }

    public Class<? extends OptionStrategy> getStrategyClass() {
        return strategyClass;
    }
}
