//Subject object class
//PriceWatcher- jak sama nazwa wskazuje- obserwuje API
package ownStrategy;
import java.util.ArrayList;
import java.util.List;
public class PriceWatcher implements MarketSubject{
    private List<Observer> observers = new ArrayList<>();
    private String ticker;
    private double lastPrice = 0;
    private double currentPrice;
    public PriceWatcher(String ticker) {
        this.ticker = ticker;
        currentPrice = AlphaVantageStock.getPrice(ticker);
    }
    public double getCurrentPrice() {
        return currentPrice;
    }
    public void checkPrice(){
        if(lastPrice != currentPrice){
            lastPrice = currentPrice;
            notifyObservers();
        }
    }
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
