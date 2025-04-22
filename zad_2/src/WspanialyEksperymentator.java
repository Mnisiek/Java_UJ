import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WspanialyEksperymentator implements Eksperymentator {
	private KostkaDoGry kostka;
	private long czasEksperymentu;
	

	@Override
	public void użyjKostki(KostkaDoGry kostka) {
		this.kostka = kostka;
	}

	@Override
	public void czasJednegoEksperymentu(long czasEksperymentu) {
		this.czasEksperymentu = czasEksperymentu;
	}

	@Override
	public Map<Integer, Double> szansaNaWyrzucenieOczek(int liczbaKostek) {
		Map<Integer, Double> wyniki = new HashMap<Integer, Double>();
		List<Integer> sumy_oczek = new ArrayList<Integer>();
		for (int i = 0; i <= (6 * liczbaKostek); ++i) {
			sumy_oczek.add(i, 0);
		}
		
		long start = System.currentTimeMillis();
		long stop = start + this.czasEksperymentu;
		long liczba_rzutow = 0;
		int suma;
		
		while (System.currentTimeMillis() < stop) {
			++liczba_rzutow;
			suma = 0;
			for (int j = 0; j < liczbaKostek; ++j) {
				suma += this.kostka.rzut();
			}
			// zwiekszamy o jeden licznik odpowiadajacy wyrzuconej sumie oczek
			sumy_oczek.set(suma, sumy_oczek.get(suma) + 1);
		}
		
		for (int i = 1; i <= (6 * liczbaKostek); ++i) {
			wyniki.put(i, (double)sumy_oczek.get(i) / liczba_rzutow);
		}
		
		return wyniki;
	}

	@Override
	public double szansaNaWyrzucenieKolejno(List<Integer> sekwencja) {
		double wynik;
		int dlugosc_sekwencji = sekwencja.size();
		
		long start = System.currentTimeMillis();
		long stop = start + this.czasEksperymentu;
		int udane_sekwencje = 0;
		long liczba_rzutow = 0;
		
		while (System.currentTimeMillis() < stop) {
			List<Integer> tymczasowa_sekwencja = new ArrayList<Integer>();
			++liczba_rzutow;
			for (int i = 0; i < dlugosc_sekwencji; ++i) {
				tymczasowa_sekwencja.add(this.kostka.rzut());
			}
			if (tymczasowa_sekwencja.equals(sekwencja)) {
				++udane_sekwencje;
			}
		}
		wynik = (double)udane_sekwencje / liczba_rzutow;

		return wynik;
	}

	@Override
	public double szansaNaWyrzucenieWDowolnejKolejności(Set<Integer> oczka) {
		double wynik;
		int rozmiar_zbioru = oczka.size();
		
		long start = System.currentTimeMillis();
		long stop = start + this.czasEksperymentu;
		int pasujace_zbiory = 0;
		long liczba_rzutow = 0;
		
		while (System.currentTimeMillis() < stop) {
			Set<Integer> tymczasowy_zbior = new HashSet<Integer>();
			++liczba_rzutow;
			for (int i = 0; i < rozmiar_zbioru; ++i) {
				tymczasowy_zbior.add(this.kostka.rzut());
			}
			if (tymczasowy_zbior.equals(oczka)) {
				++pasujace_zbiory;
			}
		}
		
		wynik = (double)pasujace_zbiory / liczba_rzutow;

		return wynik;
	}

}
