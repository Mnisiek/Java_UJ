
import java.util.List;
import java.util.Set;

public class Main {

	
	public static void main(String[] args) {
		
		KostkaDoGry kostka1 = new MojaKostkaDoGry();
		WspanialyEksperymentator eksperymentator1 = new WspanialyEksperymentator();
		eksperymentator1.użyjKostki(kostka1);
		eksperymentator1.czasJednegoEksperymentu(2000);
		
		System.out.println(eksperymentator1.szansaNaWyrzucenieOczek(1)); // dziala
		System.out.println(eksperymentator1.szansaNaWyrzucenieKolejno(List.of(1, 2, 3))); // dziala
		System.out.println(eksperymentator1.szansaNaWyrzucenieWDowolnejKolejności(Set.of(1, 2))); // dziala
		System.out.println(eksperymentator1.szansaNaWyrzucenieKolejno(List.of()));
	}

}
