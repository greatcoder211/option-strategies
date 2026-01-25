//let's make it big
package ownStrategy.oPattern;
import ownStrategy.sPattern.*;

public interface MarketSubject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers();
}
