package ownStrategy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AlphaVantageStock {

    private static final String API_KEY = "R875E3J67YS7G93S";

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static double getPrice(String symbol) {
        String url = String.format("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", symbol, API_KEY);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            if (json.contains("Error Message") || json.contains("Note") || json.contains("{}")) {
                System.err.println("Błąd API dla symbolu " + symbol + ": " + json);
                return -1.0;
            }
            String searchKey = "\"05. price\":";
            int startIndex = json.indexOf(searchKey);

            if (startIndex == -1) return -1.0;

            startIndex += searchKey.length();
            int valueStart = json.indexOf("\"", startIndex) + 1;
            int valueEnd = json.indexOf("\"", valueStart);

            String priceStr = json.substring(valueStart, valueEnd);
            return Double.parseDouble(priceStr);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return -1.0;
        }
    }
}
