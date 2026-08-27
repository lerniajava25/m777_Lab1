import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ElectricityPrice;
import java.time.LocalDate;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.time.OffsetDateTime;

ElectricityPrice[] fetchPrices(String selectedArea, LocalDate today) throws Exception {
        String url = "https://www.elprisetjustnu.se/api/v1/prices/"
                + today.getYear() + "/"
                + "%02d".formatted(today.getMonthValue()) + "-"
                + "%02d".formatted(today.getDayOfMonth()) + "_"
                + selectedArea + ".json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString()
        );

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(
                response.body(),
                ElectricityPrice[].class
        );
}

void main() {

        String selectedArea = "SE4";
        LocalDate today = LocalDate.now();

        while (true) {
                IO.println("Elpriser - Analysverktyg");
                IO.println("========================");
                IO.println("1. Välj elområde (SE1, SE2, SE3, SE4)");
                IO.println("2. Min, Max och Medelpris");
                IO.println("3. Sortera priser (lägst till högst)");
                IO.println("4. Bästa laddningstid (4h sammanhängande)");
                IO.println("e. Avsluta");

                String makeChoice = IO.readln("Välj ett alternativ: ");

                switch (makeChoice) {
                        case "1" -> {
                                String area = IO.readln("Välj elområde (SE1, SE2, SE3, SE4): ").trim().toUpperCase();

                                if (area.equals("SE1") || area.equals("SE2") || area.equals("SE3") || area.equals("SE4")) {
                                        selectedArea = area;
                                IO.println("Valt elområde: " + selectedArea);
                        } else {
                                    IO.println("Ogiltigt elområde.");
                                }
                        }

                        case "2" -> {
                                String url = "https://www.elprisetjustnu.se/api/v1/prices/"
                                        + today.getYear() + "/"
                                        + "%02d".formatted(today.getMonthValue()) + "-"
                                        + "%02d".formatted(today.getDayOfMonth()) + "_"
                                        + selectedArea + ".json";

                                HttpClient client = HttpClient.newHttpClient();
                                HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(url))
                                        .build();
                                try {
                                        HttpResponse<String> response = client.send(
                                                request, HttpResponse.BodyHandlers.ofString()
                                        );

                                        IO.println("Statuskod: " + response.statusCode());

                                        ObjectMapper mapper = new ObjectMapper();

                                        ElectricityPrice[] prices = mapper.readValue(
                                                response.body(),
                                                ElectricityPrice[].class
                                        );

                                        if (prices.length == 0) {
                                                IO.println("Ingen prisdata hittades.");
                                                continue;
                                        }

                                        double minPrice = prices[0].SEK_per_kWh();
                                        double maxPrice = prices[0].SEK_per_kWh();
                                        double totalPrice = 0;

                                        for (ElectricityPrice price : prices) {
                                                if (price.SEK_per_kWh() < minPrice) {
                                                        minPrice = price.SEK_per_kWh();
                                                }

                                                if (price.SEK_per_kWh() > maxPrice) {
                                                        maxPrice = price.SEK_per_kWh();
                                                }

                                                totalPrice += price.SEK_per_kWh();
                                        }

                                        double averagePrice = totalPrice / prices.length;

                                        double minPriceOre = minPrice * 100;
                                        double maxPriceOre = maxPrice * 100;
                                        double averagePriceOre = averagePrice * 100;

                                        IO.println("Lägsta pris: %.2f öre/kWh".formatted(minPriceOre));
                                        IO.println("Högsta pris: %.2f öre/kWh".formatted(maxPriceOre));
                                        IO.println("Medelpris: %.2f öre/kWh".formatted(averagePriceOre));
                                        IO.println("Antal prisposter: " + prices.length);

                                        IO.println(prices[0]);

                                } catch (Exception e) {
                                        IO.println("Kunde inte hämta elpriser.");
                                        IO.println(e.getMessage());
                                }

                                IO.println(url);
                        }

                        case "3" -> {
                                try {
                                        ElectricityPrice[] prices = fetchPrices(selectedArea, today);

                                        if (prices.length < 4 || prices.length % 4 != 0) {
                                                IO.println("Prisdata kunde inte delas upp i hela timmar.");
                                                continue;
                                        }

                                        int numberOfHours = prices.length / 4;
                                        double[] hourlyPrices = new double[numberOfHours];

                                        String[] hourLabels = new String[numberOfHours];

                                        for (int hour = 0; hour < numberOfHours; hour++) {
                                                int startIndex = hour * 4;

                                                OffsetDateTime startTime = OffsetDateTime.parse(prices[startIndex].time_start());
                                                OffsetDateTime endTime = OffsetDateTime.parse(prices[startIndex + 3].time_end());

                                                hourLabels[hour] = "%02d-%02d".formatted(
                                                        startTime.getHour(),
                                                        endTime.getHour()
                                                );

                                                hourlyPrices[hour] = (
                                                        prices[startIndex].SEK_per_kWh()
                                                                + prices[startIndex + 1].SEK_per_kWh()
                                                                + prices[startIndex + 2].SEK_per_kWh()
                                                                + prices[startIndex + 3].SEK_per_kWh()
                                                ) / 4;
                                        }

                                        Integer[] hours = new Integer[numberOfHours];

                                        for (int hour = 0; hour < numberOfHours; hour++) {
                                                hours[hour] = hour;
                                        }

                                        Arrays.sort(
                                                hours,
                                                (a, b) -> Double.compare(hourlyPrices[a], hourlyPrices[b])
                                        );
                                        for (int hour : hours) {
                                                IO.println("%s: %.2f öre/kWh".formatted(
                                                        hourLabels[hour],
                                                        hourlyPrices[hour] * 100
                                                ));
                                        }

                                } catch (Exception e) {

                                        IO.println("Kunde inte hämta elpriser.");

                                }
                        }

                        case "4" -> {
                                try {
                                        ElectricityPrice[] prices = fetchPrices(selectedArea, today);

                                        if (prices.length < 4 || prices.length % 4 != 0) {
                                                IO.println("Prisdata kunde inte delas upp i hela timmar.");
                                                continue;
                                        }

                                        int numberOfHours = prices.length / 4;
                                        double[] hourlyPrices = new double[numberOfHours];

                                        String[] hourLabels = new String[numberOfHours];

                                        for (int hour = 0; hour < numberOfHours; hour++) {
                                                int startIndex = hour * 4;

                                                OffsetDateTime startTime = OffsetDateTime.parse(prices[startIndex].time_start());
                                                OffsetDateTime endTime = OffsetDateTime.parse(prices[startIndex + 3].time_end());

                                                hourLabels[hour] = "%02d-%02d".formatted(
                                                        startTime.getHour(),
                                                        endTime.getHour()
                                                );

                                                hourlyPrices[hour] = (
                                                        prices[startIndex].SEK_per_kWh()
                                                                + prices[startIndex + 1].SEK_per_kWh()
                                                                + prices[startIndex + 2].SEK_per_kWh()
                                                                + prices[startIndex + 3].SEK_per_kWh()
                                                ) / 4;
                                        }

                                        double firstFourHours =
                                                hourlyPrices[0]
                                                        + hourlyPrices[1]
                                                        + hourlyPrices[2]
                                                        + hourlyPrices[3];

                                        double bestTotal = firstFourHours;
                                        int bestStartHour = 0;

                                        for (int startHour = 1; startHour <= numberOfHours - 4; startHour++) {
                                                double currentTotal =
                                                        hourlyPrices[startHour]
                                                                + hourlyPrices[startHour + 1]
                                                                + hourlyPrices[startHour + 2]
                                                                + hourlyPrices[startHour + 3];

                                                if (currentTotal < bestTotal) {
                                                        bestTotal = currentTotal;
                                                        bestStartHour = startHour;
                                                }
                                        }

                                        double averageBestPrice = bestTotal / 4;

                                        String startLabel = hourLabels[bestStartHour];
                                        String endLabel = hourLabels[bestStartHour + 3];

                                        IO.println("Bästa laddningstid: "
                                                + startLabel.substring(0, 2)
                                                + "-"
                                                + endLabel.substring(3, 5));

                                        IO.println("Medelpris: %.2f öre/kWh".formatted(
                                                averageBestPrice * 100
                                        ));

                                } catch (Exception e) {
                                        IO.println("Kunde inte hämta elpriser.");
                                }
                        }

                        case "e", "E" -> {
                                IO.println("Programmet avslutas");
                        return;
                        }

                default -> IO.println("Ogiltigt val.");
        }

        IO.println();
}
}