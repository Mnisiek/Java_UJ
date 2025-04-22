import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProgrammableCalculator2 implements ProgrammableCalculatorInterface {
    private BufferedReader reader;
    private LineReader input;
    private LinePrinter output;
    private Map<String, Instrukcja> mapaProgramu = new LinkedHashMap<>();
    private Map<String, Integer> mapaZmiennych = new HashMap<>();
    private String aktualnaLinia;
    private boolean zakoncz = false;

    @Override
    public void programCodeReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void setStdin(LineReader input) {
        this.input = input;
    }

    @Override
    public void setStdout(LinePrinter output) {
        this.output = output;
    }

    private class Instrukcja {
        String polecenie;
        String trescInstrukcji;

        public Instrukcja(String polecenie, String trescInstrukcji) {
            this.polecenie = polecenie;
            this.trescInstrukcji = trescInstrukcji;
        }

        public String getPolecenie() {
            return polecenie;
        }

        public String getTrescInstrukcji() {
            return trescInstrukcji;
        }
    }

    public void odczytLinii(String linia) {
        if (!linia.isEmpty()) {
            String[] liniaProgramu = linia.split(" ", 2);

            String numer = liniaProgramu[0];
            String polecenie = liniaProgramu[1].split(" ")[0].toUpperCase();

            String trescInstrukcji = liniaProgramu[1].substring(polecenie.length()).trim();

            mapaProgramu.put(numer, new Instrukcja(polecenie, trescInstrukcji));
        }
    }

    public void wykonajPolecenie(String numer, String polecenie, String trescInstrukcji) {
        switch(polecenie) {
            case "LET":
                wykonajLET(trescInstrukcji);
                break;
            case "PRINT":
                wykonajPRINT(trescInstrukcji);
                break;
            case "GOTO":
                wykonajGOTO(trescInstrukcji);
                break;
            case "IF":
                wykonajIF(trescInstrukcji);
                break;
            case "INPUT":
                wykonajINPUT(trescInstrukcji);
                break;
            case "END":
                zakoncz = true;
                break;
            default:
                // Obsługa nieznanych poleceń
                break;
        }
    }

    @Override
    public void run(int line) {
        try {
            String linia;
            while ((linia = reader.readLine()) != null) {
                if (!linia.trim().isEmpty()) {
                    odczytLinii(linia);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        aktualnaLinia = Integer.toString(line);

        while (!zakoncz && mapaProgramu.containsKey(aktualnaLinia)) {
            Instrukcja instrukcja = mapaProgramu.get(aktualnaLinia);
            String polecenie = instrukcja.getPolecenie();
            String trescInstrukcji = instrukcja.getTrescInstrukcji();

            wykonajPolecenie(aktualnaLinia, polecenie, trescInstrukcji);
            if (!polecenie.equals("GOTO")) {
                aktualnaLinia = getNastepnaLinia(aktualnaLinia);
            }
        }

        zakoncz = true;
    }

    public String getNastepnaLinia(String aktualnaLinia) {
        try {
            int liniaNumer = Integer.parseInt(aktualnaLinia);
            liniaNumer++; // Przechodzimy do następnej linii
            return Integer.toString(liniaNumer);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null; // W przypadku błędu zwraca null
        }
    }


    public void wykonajLET(String trescInstrukcji) {
        String[] elementy = trescInstrukcji.trim().split(" ");
        String nazwaZmiennej = elementy[1];
        int wartosc;

        if (elementy.length > 3) { // Obsługa operacji arytmetycznych
            int lewaWartosc = parseValue(elementy[3]);
            int prawaWartosc = parseValue(elementy[5]);
            wartosc = wykonajOperacje(elementy[4], lewaWartosc, prawaWartosc);
        } else { // Proste przypisanie
            wartosc = parseValue(elementy[3]);
        }

        mapaZmiennych.put(nazwaZmiennej, wartosc);
    }

    private int wykonajOperacje(String operator, int lewa, int prawa) {
        switch (operator) {
            case "+": return lewa + prawa;
            case "-": return lewa - prawa;
            case "*": return lewa * prawa;
            case "/": return lewa / prawa;
            default: throw new IllegalArgumentException("Nieznany operator: " + operator);
        }
    }

    private int parseValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return mapaZmiennych.getOrDefault(value, 0);
        }
    }


    public void wykonajPRINT(String trescInstrukcji) {
        String argument = trescInstrukcji.trim().split(" ", 2)[1];

        if (argument.startsWith("\"") && argument.endsWith("\"")) {
            // Wypisywanie stałej tekstowej
            output.printLine(argument.substring(1, argument.length() - 1));
        } else {
            // Wypisywanie wartości zmiennej
            int wartosc = mapaZmiennych.getOrDefault(argument, 0);
            output.printLine(String.valueOf(wartosc));
        }
    }


    public void wykonajGOTO(String trescInstrukcji) {
        // Zmienia aktualną linię na określoną w instrukcji
        aktualnaLinia = trescInstrukcji.split(" ")[1];
    }

    public void wykonajIF(String trescInstrukcji) {
        String[] elementy = trescInstrukcji.trim().split(" ");
        int lewaWartosc = parseValue(elementy[1]);
        String operator = elementy[2];
        int prawaWartosc = parseValue(elementy[3]);

        boolean wynik = false;
        switch (operator) {
            case "=": wynik = (lewaWartosc == prawaWartosc); break;
            case "<": wynik = (lewaWartosc < prawaWartosc); break;
            case ">": wynik = (lewaWartosc > prawaWartosc); break;
        }

        if (wynik) {
            aktualnaLinia = elementy[5];
        }
    }


    public void wykonajINPUT(String trescInstrukcji) {
        String nazwaZmiennej = trescInstrukcji.trim().split(" ")[1];
        try {
            int wartosc = Integer.parseInt(input.readLine());
            mapaZmiennych.put(nazwaZmiennej, wartosc);
        } catch (NumberFormatException e) {
            output.printLine("Nieprawidłowa wartość wejściowa");
        }
    }



}

