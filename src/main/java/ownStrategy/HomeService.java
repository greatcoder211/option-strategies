package ownStrategy;

import org.springframework.stereotype.Service;
import ownStrategy.DTO.OptionPrices;
import ownStrategy.DTO.StrategyRequest;
import ownStrategy.finance.OptionCalculator;
import ownStrategy.sPattern.OptionStrategy;
import ownStrategy.sPattern.SpreadStrategy;
import ownStrategy.sPattern.OptionType;
import ownStrategy.ui.ChartPoint;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    public OptionPrices createPricesReport(OptionType type, double stock, double option, double strike){
        return new OptionPrices(type, stock, option, strike);
    }
    public List<ChartPoint> calculatePayoffChart(OptionType type, double strike, double optionPrice, double minPrice, double maxPrice){
        List<ChartPoint> points = new ArrayList<>();
        int k = (int) Math.floor(minPrice);
        for(int x = k; x <= maxPrice; x++){
            if(type.equals(OptionType.CALL))
                points.add(new ChartPoint(x, Math.max(0, x - strike) - optionPrice));
            else
                points.add(new ChartPoint(x, Math.max(0, strike - x) - optionPrice));
        }
        return points;
    }
    public double calculateStrategyPayoff(StrategyRequest request, double price2){
        OptionStrategy strategy = new OptionStrategy();
        strategy.setLegs(request.getLegs());
        strategy.setTimeToExpiry(request.getTimeToExpiry());
        strategy.setVolatility(request.getVolatility());
        strategy.setRiskFreeRate(request.getRiskFreeRate());
        return OptionCalculator.function(strategy, request.getSpotPrice(), price2, 1);
    }
}
