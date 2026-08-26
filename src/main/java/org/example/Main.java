
void main() {
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
                        case "1" -> IO.println("Du valde elområde.");
                        case "2" -> IO.println("Du valde Min, Max och Medelpris.");
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