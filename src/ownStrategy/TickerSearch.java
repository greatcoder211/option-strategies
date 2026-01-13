package ownStrategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

public class TickerSearch {

    private static final String API_KEY = "R875E3J67YS7G93S";
    private static final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static final ObjectMapper mapper = new ObjectMapper();
    // Metoda zwraca wybrany Symbol (String) albo null, jak się nie uda
    public static String Ticker(Scanner scanner) {
        System.out.println("Enter the name of the company: ");
        while(true){
            String keyword = scanner.nextLine();
            String encodedKeyword = keyword.replace(" ", "%20");
            String url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + encodedKeyword + "&apikey=" + API_KEY;

            try {
                HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode root = mapper.readTree(response.body());
                JsonNode matches = root.path("bestMatches");

                if (matches.isEmpty() || !matches.isArray()) {
                    System.out.println("No companies found for: " + keyword + " Try again!");
                    continue;
                }

                int totalMatches = matches.size();
                boolean showAll = false; // Flaga: czy pokazać wszystko?

                // Pętla wyboru - pozwala odświeżyć listę po wybraniu "Pokaż więcej"
                while (true) {
                    // Dynamiczny limit: albo 5, albo wszystko
                    int limit = showAll ? totalMatches : Math.min(totalMatches, 5);

                    System.out.println("\n--- Results (" + limit + " out of " + totalMatches + ") ---");

                    for (int i = 0; i < limit; i++) {
                        JsonNode company = matches.get(i);
                        String symbol = company.path("1. symbol").asText();
                        String name = company.path("2. name").asText();
                        String region = company.path("4. region").asText();
                        String currency = company.path("8. currency").asText();
                        System.out.println("[" + (i + 1) + "] " + symbol + " - " + name + " (" + region + ", " + currency + ")");
                    }

                    // Opcje sterowania
                    System.out.println("--------------------------------");
                    if (!showAll && totalMatches > 5) {
                        System.out.println("[0] Show more results...");
                    }
                    System.out.println("[X] Cancel and search again");
                    System.out.print("Choose your option: ");

                    String input = scanner.nextLine().trim();

                    // Obsługa "Pokaż więcej"
                    if (input.equals("0") && !showAll && totalMatches > 5) {
                        showAll = true; // Zmieniamy flagę
                        continue;       // I kręcimy pętlę od nowa z pełną listą
                    }

                    // Obsługa wyjścia
                    if (input.equalsIgnoreCase("X")) {
                        System.out.println("So, enter the company again: ");
                        break;
                    }

                    // Obsługa wyboru numerka
                    try {
                        int choice = Integer.parseInt(input);
                        if (choice >= 1 && choice <= limit) {
                            return matches.get(choice - 1).path("1. symbol").asText();
                        } else {
                            System.out.println("Wrong number. Choose from the list!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error. Enter a number, '0' lub 'X'.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}