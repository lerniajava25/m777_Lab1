# Laboration 1 – Elprisanalysator

I den här laborationen har jag byggt ett konsolprogram i Java som 
hämtar aktuella elpriser från elprisetjustnu.se.

Programmet låter användaren välja mellan elområdena SE1, SE2, SE3 och SE4 
och sedan analysera priserna för dagens datum. Det går bland annat att se 
lägsta, högsta och genomsnittliga pris, sortera priserna från billigast 
till dyrast och hitta den billigaste sammanhängande perioden på fyra timmar.

## Hur programmet är uppbyggt

Programmet använder en meny i konsolen där användaren väljer vad som ska göras. 
Menyn körs i en loop tills användaren väljer att avsluta programmet. 
Elpriserna hämtas från API:t med HttpClient. JSON-svaret läses sedan in med 
Jackson och omvandlas till ett Java-objekt i en array. Programmet räknar sedan ut min, 
max och medelpris, gör om kvartstimmarna till timpriser och kan sortera timmarna 
från billigast till dyrast. För att hitta den billigaste sammanhängande fyratimmarsperioden 
jämför programmet alla möjliga fyratimmarsblock och sparar det billigaste. Resultatet 
från bästa laddningstid sparas också i filen analysis.txt.

## Reflektion

Java är en ny upplevelse för mig, så en stor del av laborationen har handlat om att 
lära mig syntaxen samtidigt som jag byggt ihop programmet. Jämfört med JavaScript och 
TypeScript känns Java mer strikt, framför allt eftersom typer, klasser och metoder 
behöver vara tydligare definierade. Samtidigt tycker jag att den tydliga strukturen 
gör det lättare att förstå vad olika delar av programmet ansvarar för.

## AI-stöd

Under arbetet med laborationen har jag använt ChatGPT från OpenAI som stöd för att 
förstå mer Java-syntax, felsöka felmeddelanden och dela upp uppgiften i mindre steg. 
Jag har också fått kodexempel som jag sedan har lagt in, testat, ändrat och gått 
igenom steg för steg för att förstå hur de fungerar.





