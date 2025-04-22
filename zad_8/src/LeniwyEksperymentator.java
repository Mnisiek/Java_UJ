import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


public class LeniwyEksperymentator implements LeniwyBadaczKostekDoGry {
	private static final AtomicInteger idWatku = new AtomicInteger(1000);
	private ExecutorService egzekutor;
	private Map<Integer, Map<Integer, Integer>> wyniki;
	
	public LeniwyEksperymentator() {
		this.wyniki = new ConcurrentHashMap<Integer, Map<Integer,Integer>>();
	}
	

	@Override
	public void fabrykaWatkow(ExecutorService executorService) {
		this.egzekutor = executorService;
	}


	@Override
	public int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
		int identyfikator = idWatku.incrementAndGet();
		Callable<Boolean> callable = new Zadanie(kostka, liczbaRzutow, identyfikator);
		egzekutor.submit(callable);
		
		return identyfikator;
	}


	@Override
	public boolean badanieKostkiZakonczono(int identyfikator) {
	    if (wyniki.containsKey(identyfikator)) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public Map<Integer, Integer> histogram(int identyfikator) {
		return wyniki.get(identyfikator);
	}
	
	
	private class Zadanie implements Callable<Boolean> {
		private KostkaDoGry mojaKostka;
        private int mojaLiczbaRzutow;
        private int mojIdentyfikator;
        private Map<Integer, Integer> mojeWyniki;
	
		Zadanie(KostkaDoGry kostka, int liczbaRzutow, int identyfikator) {
			this.mojaKostka = kostka;
			this.mojaLiczbaRzutow = liczbaRzutow;
			this.mojIdentyfikator = identyfikator;
			this.mojeWyniki = new HashMap<Integer, Integer>();
		}
	
		@Override
		public Boolean call() {
			int liczbaOczek;
	        while (mojaLiczbaRzutow > 0) {
	            liczbaOczek = mojaKostka.rzut();
	            mojeWyniki.put(liczbaOczek, mojeWyniki.getOrDefault(liczbaOczek, 0) + 1);
	            --mojaLiczbaRzutow;
	        }
	        
	        try {
	            wyniki.put(mojIdentyfikator, mojeWyniki);
	        } catch (Exception e) {
	        	// nie udalo sie dodac elementu do mapy i zlapano wyjatek
	            return false;
	        }
	        
			return true;
		}
	}

}


