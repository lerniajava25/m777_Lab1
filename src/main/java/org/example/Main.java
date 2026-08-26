import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ElectricityPrice;
import java.time.LocalDate;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



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

                                        IO.println("Antal prisposter: " + prices.length);

                                        IO.println(prices[0]);

                                } catch (Exception e) {
                                        IO.println("Kunde inte hämta elpriser.");
                                        IO.println(e.getMessage());
                                }

                                IO.println(url);
                        }

                        case "3" -> IO.println("Du valde att sortera priser.");
                        case "4" -> IO.println("Du valde bästa laddningstid.");
                        case "e", "E" -> {
                                IO.println("Programmet avslutas");
                        return;
                        }

                default -> IO.println("Ogiltigt val.");
        }

        IO.println();
}
}