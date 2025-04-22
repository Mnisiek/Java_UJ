package wyklady;

import java.util.Locale;

// wszystko (prawie) w Javie jest klasą albo obiektem
// istnieją jeszcze typy prymitywne (w sumie osiem)
// char ma 16 bitów, znaki w unikodzie
public class Wyklad1 {
	// wnętrze klasy
	// public - metoda publiczna, można jej używać wszędzie
	// static - metoda klasy, nie jest związana z konkretnym obiektem
	// void - typ zwracany, metoda nie zwraca nic
	public static void main(String[] args) {
		// String - podobnie jak w C++
		// wnętrze metody
		System.out.println("Witam Cię Javo.");
		
//		for (int i = 0; i < args.length; i++) {
//			// tablice w Javie posiadają atrybuty, np. atrybut length
//			System.out.printf(Locale.US, "%.2f\n", args[i]);
//		}
		
		String s = "Ala";
		do {
			// napisy można konkatenować z wykorzystaniem operatora "+"
			s = " " + s;
		} while (s.length() < 20);
		System.out.println(s);
		
		String[] names = getNames();
		for (int i = 0; i < names.length; i++) {
			// equals porównuje zawartość
			// operator "==" sprawdza, czy referecje wskazują na ten sam obiekt
			// w Javie Stringi zapisywane są w pamięci tylko raz
			// więc equals() i "==" działają dla Stringów tak samo
			// w Javie mamy tylko referencje
			if (names[i].equals("JAVA")) {
				boolean found = true;
				break;
			}
		}
	}

	private static String[] getNames() {
		// TODO Auto-generated method stub
		return null;
	}

}

// kompilowanie (do kodu pośredniego): javac Wyklad1.java
// uruchamianie (wirtualnej maszyny Javy): java Wyklad1

