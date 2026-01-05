package ownStrategy;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class Main {
    public static void main(String[] args) {
        boolean inv = true;
        Scanner sc = new Scanner(System.in);
        boolean llong = false;
        OptionStrategy os = null;
        System.out.println("Choose the number of your strategy:  \n1. Butterfly Spread \n2. Vertical Spread \n3. Ratio Spread \n4. Iron Condor \n5. Iron Butterfly \n6. Strangle");
        int n = 0;
        int lsh = 0;
        int cpu = 0;
        List<Double> strikePrices = new ArrayList<>();
        List<OptionType> types = new ArrayList<>();
        while (n < 1 || n > StrategyType.values().length) {
            n = sc.nextInt();
        }
        System.out.println("You want to play Long or short?\n1. Long\n2. Short");
        while (lsh < 1 || lsh > 2) {
            lsh = sc.nextInt();
        }
        sc.nextLine();
        StrategyType strategy = StrategyType.values()[n - 1];
        Belfort belfort = Belfort.values()[lsh - 1];
        os = createOptionStrategy(strategy, belfort);
        os.setName();
        if (lsh == 1)
            System.out.println("Your strategy: " + os.getName());
        else if (lsh == 2)
            System.out.println("Your strategy: " + os.getName());
        double price = 0;
        double spread = 0;
        List<Double> spreadValues = new ArrayList<>();
        String ticker = null;
        System.out.println("Enter the the name of the company you want to invest in:(ticker)");
        ticker =  sc.nextLine();
        price = AlphaVantageStock.getPrice(ticker);
        while(price == -1){
            System.out.println("Enter the right value!");
            ticker = sc.nextLine();
            price = AlphaVantageStock.getPrice(ticker);
        }
        System.out.println("Price: " + price + " USD");
        System.out.println("Enter the spread no. 1");
        spread = sc.nextDouble();
        while(spread > price || spread < 0){
            System.out.println("Enter the right value!");
            spread = sc.nextDouble();
        }
        spreadValues.add(spread);
        if(os.getSpreadNumber() == 2){
            System.out.println("Enter the spread no. 2");
            spread = sc.nextDouble();
            while(spread > price || spread < 0){
                System.out.println("Enter the right value!");
                spread = sc.nextDouble();
            }
            spreadValues.add(spread);
        }
        if (os.getCP()) {
            while (cpu < 1 || cpu > 2) {
                System.out.println("You want to play\n1. CALL\n2. PUT");
                cpu = sc.nextInt();
                if (cpu == 1)
                    os.setType(OptionType.CALL);
                else if (cpu == 2)
                    os.setType(OptionType.PUT);
            }
        }
        System.out.println("Final question: When do you want your options to expire?");
//to trzeba zintegrować z kalendarzem- nie chce mi się na razie z tym pierdolić
        int daisy = 0;
        while (inv) {
            try {
                System.out.println("Year: ");
                int year = sc.nextInt();
                System.out.println("Month: ");
                int month = sc.nextInt();
                System.out.println("Day: ");
                int day = sc.nextInt();
                if (LocalDate.of(year, month, day).isBefore(LocalDate.now()) || LocalDate.of(year, month, day).isAfter(LocalDate.now().plusYears(3)) || LocalDate.of(year, month, day).isEqual(LocalDate.now())) {
                    System.out.println("Please enter a valid date!");
                    continue;
                }
                LocalDate expiry = LocalDate.of(year, month, day);
                long days = ChronoUnit.DAYS.between(LocalDate.now(), expiry);
                daisy = (int) days;
                inv = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double timeToExpiry = daisy / 365.0;
        final double riskFreeRate = 0.05;
        final double volatility = 0.30;
        os.setTimeToExpiry(timeToExpiry);
        int z = 0;
        List<OptionLeg> optionlegs = new ArrayList<>();
        optionlegs = os.setOptionLegs(os.setThePrices(price, spreadValues, os));
        for (OptionLeg ol : optionlegs) {
            strikePrices.add(ol.getStrikePrice());
            types.add(ol.getType());
            System.out.println(ol);
            z++;
        }
        for (int i = 0; i <= z - 1; i++) {
            if (types.get(i).equals(OptionType.CALL)) {
                System.out.println("Option " + (i + 1) + " is worth " + String.format("%.2f", BlackScholes.calculateCallPrice(price, strikePrices.get(i), timeToExpiry, riskFreeRate, volatility)));
            } else if (types.get(i).equals(OptionType.PUT)) {
                System.out.println("Option " + (i + 1) + " is worth " + String.format("%.2f", BlackScholes.calculatePutPrice(price, strikePrices.get(i), timeToExpiry, riskFreeRate, volatility)));
            }
        }
        System.out.println("Now let's check profit or loss at the specific price position at expiry.\nWhat do you think will be the price at the end?");
        int i = 0;
        while (i < 5) {
            double newPrice = sc.nextDouble();
            System.out.println(os.netPremium(optionlegs, price) + os.calculateProfits(optionlegs, newPrice));
            i++;
        }
    }

    public static OptionStrategy createOptionStrategy(StrategyType type, Belfort belf) {
        try {
            Class<? extends OptionStrategy> clazz = type.getStrategyClass();
            String prefix = (belf == Belfort.BUY) ? "Long" : "Short";
            String niceName = prefix + " " + type.name().replace("_", " ");
            Constructor<? extends OptionStrategy> constructor = clazz.getConstructor(String.class, Belfort.class);
            return constructor.newInstance(niceName, belf);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("We didn't manage to create a strategy named: " + type);
        }
    }
}
/*        try {
            while (inv) {
                System.out.println("Instances of American companies: AAPL, TSLA, MSFT, GOOGL, AMZN, NVDA"); // USA
                System.out.println("Instances of Polish companies: CDR.WA, PKO.WA, KGH.WA");                    // Polska (wymagany sufiks .WA)
                System.out.println("Instances of cryptocurrencies: BTC-USD, ETH-USD");                             // Krypto
                System.out.println("In which company do you want to invest: ");
                String company = sc.nextLine();
                if (YahooFinance.get(company) != null) {
                    Stock stock = YahooFinance.get(company);
                    System.out.println("You invested in: " + stock.getName());
                    BigDecimal price = stock.getQuote().getPrice();
                    System.out.println("The price of " + stock.getName() + " is: " + price);
                    inv = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
                try {
            System.out.println("Again");
            System.out.println("Instances of American companies: AAPL, TSLA, MSFT, GOOGL, AMZN, NVDA"); // USA
            System.out.println("Instances of Polish companies: CDR.WA, PKO.WA, KGH.WA");                    // Polska (wymagany sufiks .WA)
            System.out.println("Instances of cryptocurrencies: BTC-USD, ETH-USD");                             // Krypto
            System.out.println("In which company do you want to invest: ");
            while (inv) {
                String company = sc.nextLine();
                if (!OptionStrategy.getCompanyTypes().contains(company)) {
                    System.out.println("Choose the right company!");
                } else {
                    System.out.println("Enter the current price: ");
                    price = sc.nextDouble();
                    os.setPrice(price);
                    for (int i = 1; i <= os.getSpreadNumber(); i++) {
                        System.out.println("Enter the spread no. " + i);
                        double spreadValue = sc.nextDouble();
                        if (price > spreadValue) {
                            if (i == 1) {
                                os.setOgspread(spreadValue);
                            }
                            spreadValues.add(spreadValue);
                        } else {
                            System.out.println("Enter the right value!");
                            i--;
                        }
                        sc.nextLine();
                    }
                    inv = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inv = true;

*/


