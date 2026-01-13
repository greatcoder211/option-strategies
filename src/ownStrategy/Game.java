package ownStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Belfort belfort;
    private OptionStrategy strategy;
    private double price;
    private List<Double> spreads = new ArrayList<>();
    private LocalDate expiration;
    public Game(Belfort belfort, OptionStrategy strategy, double price, List<Double> spreads, LocalDate expiration) {
        this.belfort = belfort;
        this.strategy = strategy;
        this.price = price;
        this.spreads = spreads;
        this.expiration = expiration;
    }
}
//git check