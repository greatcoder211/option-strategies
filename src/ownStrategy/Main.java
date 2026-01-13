package ownStrategy;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        double price = 0;
        double spread = 0;
        List<Double> spreadValues = new ArrayList<>();
        String ticker = null;
        ticker = TickerSearch.Ticker(sc);
        price = AlphaVantageStock.getPrice(ticker);
        while (price == -1) {
            System.out.println("Enter the right value!");
            ticker = TickerSearch.Ticker(sc);
            price = AlphaVantageStock.getPrice(ticker);
        }
        System.out.println("Price: " + price + " USD");
        if (os.getSpreadNumber() == 1) {
            System.out.println("Enter the spread: ");
        } else if (os.getSpreadNumber() == 2) {
            System.out.println("Enter the lower spread: ");
        }
        spread = sc.nextDouble();
        while (spread > price || spread < 0) {
            System.out.println("Enter the right value!");
            spread = sc.nextDouble();
        }
        spreadValues.add(spread);
        os.setSpread2(spread);
        if (os.getSpreadNumber() == 2) {
            System.out.println("Enter the higher spread: ");
            spread = sc.nextDouble();
            while (spread > price || spread < 0) {
                System.out.println("Enter the right value!");
                spread = sc.nextDouble();
            }
            spreadValues.add(spread);
        }
        if (os.getCP()) {
            while (cpu < 1 || cpu > 2) {
                System.out.println("You want to play on:\n1. CALL\n2. PUT");
                cpu = sc.nextInt();
                if (cpu == 1)
                    os.setType(OptionType.CALL);
                else if (cpu == 2)
                    os.setType(OptionType.PUT);
            }
        }
        System.out.println("Enter the expiration date: ");
        sc.nextLine();
        double days = 0;
        while (inv) {
            try {
                String date = sc.nextLine();
                String[] data = date.split(" ");
                int day = Integer.parseInt(data[0]);
                int month = Integer.parseInt(data[1]);
                int year = Integer.parseInt(data[2]);
                if (LocalDate.of(year, month, day).isBefore(LocalDate.now()) || LocalDate.of(year, month, day).isAfter(LocalDate.now().plusYears(3)) || LocalDate.of(year, month, day).isEqual(LocalDate.now())) {
                    System.out.println("Please enter a valid date!");
                    continue;
                }
                LocalDate expiry = LocalDate.of(year, month, day);
                days = ChronoUnit.DAYS.between(LocalDate.now(), expiry);
                inv = false;
            } catch (Exception e) {
                System.out.println("Please enter a valid date!");
            }
        }
        double timeToExpiry = days / 365.0;
        final double riskFreeRate = 0.05;
        final double volatility = 0.30;
        os.setTimeToExpiry(timeToExpiry);
        os.setName();
        int z = 0;
        List<OptionLeg> optionlegs = new ArrayList<>();
        optionlegs = os.setOptionLegs(os.setThePrices(price, spreadValues, os));
        for (OptionLeg ol : optionlegs) {
            strikePrices.add(ol.getStrikePrice());
            types.add(ol.getType());
            System.out.println(ol);
            z++;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("Generating chartpoints...");
            List<ChartPoint> points = Chart.draw(os, optionlegs, price);
            Map<String, Object> dataPacket = new HashMap<>();
            dataPacket.put("strategyName", os.getName()); // Wkładamy nazwę (dla nagłówka HTML)
            dataPacket.put("chartPoints", points);        // Wkładamy punkty (dla wykresu)
            // 2. Kucharz pakuje do pudełka (zamienia na tekst JSON)
            String json = mapper.writeValueAsString(dataPacket);

            // 3. Otwieramy okienko (Start serwera)
            SimpleHttpServer.startServer(json);

            // 4. WAŻNE: Zatrzymujemy program, żeby nie poszedł do domu
            // Program będzie wisiał na tej linii, dopóki nie wciśniesz ENTER w konsoli
            System.out.println("Press ENTER to exit...");
            sc.nextLine();
            System.out.println("Shutting down server...");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static OptionStrategy createOptionStrategy (StrategyType type, Belfort belf){
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