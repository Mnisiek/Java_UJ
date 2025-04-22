import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WatkowyEksperymentator2 implements BadaczKostekDoGry {
	private int limitWatkow;
	private volatile AtomicInteger dzialajaceWatki;
	private ThreadFactory fabryka;
	private static AtomicInteger idWatku;
	private Map<Integer, Map<Integer, Integer>> wyniki;
	
//	protected KostkaDoGry kostka;
//	protected int liczbaRzutow;
	
	
	public WatkowyEksperymentator2() {
		dzialajaceWatki = new AtomicInteger(0);
		idWatku = new AtomicInteger(1000);
		wyniki = new HashMap<Integer, Map<Integer, Integer>>();
	}
	

	@Override
	public void dozwolonaLiczbaDzialajacychWatkow(int limitWatkow) {
		this.limitWatkow = limitWatkow;
	}


	@Override
	public void fabrykaWatkow(ThreadFactory fabryka) {
		this.fabryka = fabryka;
	}


	@Override
	public int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
//		this.kostka = kostka;
//		this.liczbaRzutow = liczbaRzutow;
		
		Runnable zadanie = new Zadanie(kostka, liczbaRzutow);
		Thread watek = fabryka.getThread(zadanie);
		watek.start();
		
		return 0;
	}


	@Override
	public boolean badanieKostkiZakonczono(int identyfikator) {
		synchronized (wyniki) {
			if (wyniki.containsKey(identyfikator)) {
				return true;
			} else {
				return false;
			}

		}
	}


	@Override
	public Map<Integer, Integer> histogram(int identyfikator) {
		synchronized (wyniki) {
			return wyniki.get(identyfikator);
		}
	}
	
	
	public class Zadanie implements Runnable {
		private KostkaDoGry mojaKostka;
		private int mojaLiczbaRzutow;
		private int mojeIdWatku;
		private Map<Integer, Integer> mojeWyniki;
		
		Zadanie(KostkaDoGry kostka, int liczbaRzutow) {
			this.mojaKostka = kostka;
			this.mojaLiczbaRzutow = liczbaRzutow;
			this.mojeIdWatku = idWatku.incrementAndGet();
			this.mojeWyniki = new HashMap<Integer, Integer>();
		}
		
		@Override
		public void run() {
			synchronized (dzialajaceWatki) {
				dzialajaceWatki.incrementAndGet();
				while (limitWatkow < dzialajaceWatki.get()) {
					try {
						dzialajaceWatki.wait();
					} catch (InterruptedException e) {
						
					}
				}
			}
			
			int liczbaOczek;

            while (mojaLiczbaRzutow > 0) {
                liczbaOczek = mojaKostka.rzut();
                mojeWyniki.put(liczbaOczek, mojeWyniki.getOrDefault(liczbaOczek, 0) + 1);
                --mojaLiczbaRzutow;
            }
            synchronized (dzialajaceWatki) {
            	wyniki.put(mojeIdWatku, mojeWyniki);
	            dzialajaceWatki.decrementAndGet();
	            dzialajaceWatki.notifyAll();
			}
        }
	}
	
}
