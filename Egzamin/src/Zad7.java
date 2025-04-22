import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zad7 {
	public static void main(String[] args) {
		System.out.println(roman(1247));
	}
	
	public static String roman(int liczba) {
		if (liczba < 1 || liczba > 3999) {
			return null;
		}
		
		Map<String, Integer> konwersja = new HashMap<>();
		konwersja.put("I", 1);
		konwersja.put("V", 5);
		konwersja.put("X", 10);
		konwersja.put("L", 50);
		konwersja.put("C", 100);
		konwersja.put("D", 500);
		konwersja.put("M", 1000);
		
		List<String> lista = new ArrayList<>();
		lista.add("M");
		lista.add("D");
		lista.add("C");
		lista.add("L");
		lista.add("X");
		lista.add("V");
		lista.add("I");

		StringBuilder wynik = new StringBuilder();
		for (String element : lista) {
			while (liczba >= konwersja.get(element)) {
				wynik.append(element);
				liczba -= konwersja.get(element);
			}
		}
		
		return wynik.toString();
	}
}
