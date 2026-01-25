package ownStrategy.oPattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ownStrategy.sPattern.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StrategyCalculator implements Observer {
    private double gamePrice;
    private double currentPrice;
    private List<OptionLeg> legs = new ArrayList<OptionLeg>();
    private PriceWatcher watcher;
    ObjectMapper mapper = new ObjectMapper();
    public void setLegs(List<OptionLeg> legs) {
        this.legs = legs;
    }
    public StrategyCalculator(PriceWatcher watcher) {
        this.watcher = watcher;
    }

    public double getGamePrice() {
        return gamePrice;
    }
    public void setGamePrice(double gamePrice) {
        this.gamePrice = gamePrice;
    }
    public double getCurrentPrice() {
        return currentPrice;
    }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    public List<OptionLeg> getLegs() {
        return legs;
    }

    public int jsonSize(){
        try{
            JsonNode root = mapper.readValue(new File("data/game.json"),  JsonNode.class);
            return root.size();
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public StrategyCalculator(){}
    @Override
    public void update() {
        try{
            JsonNode root = mapper.readValue(new File("data/game.json"),  JsonNode.class);
            gamePrice = root.path("price").asDouble();
            currentPrice = watcher.getCurrentPrice();
            String name =  root.path("name").asText();
            String className = name.replace(" ", "");
            String fullClassName = "ownStrategy.sPattern." + className;
            Class<?> clazz = Class.forName(fullClassName);
            OptionStrategy strategy = (OptionStrategy) clazz.getDeclaredConstructor().newInstance();
            double f = OptionStrategy.function(strategy, legs, gamePrice, currentPrice) ;
            System.out.println("The price was " + gamePrice + " and now it is " + currentPrice);
            if(f > 0){
                System.out.println("Profit: " + f);
            }
            else if(f == 0){
                System.out.println("You are even!");
            }
            else{
                System.out.println("Loss: " + f);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
