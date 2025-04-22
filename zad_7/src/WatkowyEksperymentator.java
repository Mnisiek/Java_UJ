import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WatkowyEksperymentator implements BadaczKostekDoGry {
    private static final AtomicInteger idWatku = new AtomicInteger(1000);
    private int limitWatkow;
    private ThreadFactory fabrykaWatkow;
    private Map<Integer, Thread> dzialajaceWatki;
    private Map<Integer, Map<Integer, Integer>> wyniki;

    
    public WatkowyEksperymentator() {
        this.dzialajaceWatki = new HashMap<Integer, Thread>();
        this.wyniki = new HashMap<Integer, Map<Integer,Integer>>();
    }

 
    @Override
    public void dozwolonaLiczbaDzialajacychWatkow(int limitWatkow) {
        this.limitWatkow = limitWatkow;
    }


    @Override
    public void fabrykaWatkow(ThreadFactory fabryka) {
        this.fabrykaWatkow = fabryka;
    }


    @Override
    public int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
        int identyfikator = idWatku.incrementAndGet();
        Runnable runnable = new zadanie(kostka, liczbaRzutow, identyfikator, this);

        Thread watek = fabrykaWatkow.getThread(runnable);
        watek.start();
        return identyfikator;
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
	
	
    private class zadanie implements Runnable {
        private KostkaDoGry mojaKostka;
        private int mojaLiczbaRzutow;
        private int mojeIdWatku;
        private Map<Integer, Integer> mojeWyniki;
        private WatkowyEksperymentator eksperymentator;

        zadanie(KostkaDoGry kostka, int liczbaRzutow, int identyfikator, WatkowyEksperymentator eksperymentator) {
            this.mojaKostka = kostka;
            this.mojaLiczbaRzutow = liczbaRzutow;
            this.mojeIdWatku = identyfikator;
            this.mojeWyniki = new HashMap<Integer, Integer>();
            this.eksperymentator = eksperymentator;
        }

        @Override
        public void run() {
            try {
                synchronized (eksperymentator) {
                    while (eksperymentator.limitWatkow <= eksperymentator.dzialajaceWatki.size()) {
                        try {
                            eksperymentator.wait();
                        } catch (InterruptedException e) {
                        	
                        }
                    }
                    eksperymentator.dzialajaceWatki.put(mojeIdWatku, Thread.currentThread());
                }
                
                    int liczbaOczek;
                    while (mojaLiczbaRzutow > 0) {
                        liczbaOczek = mojaKostka.rzut();
                        mojeWyniki.put(liczbaOczek, mojeWyniki.getOrDefault(liczbaOczek, 0) + 1);
                        --mojaLiczbaRzutow;
                    }

                    synchronized (wyniki) {
                        wyniki.put(mojeIdWatku, mojeWyniki);
                    }

            } finally {
                synchronized (eksperymentator) {
                    eksperymentator.dzialajaceWatki.remove(mojeIdWatku);
                    eksperymentator.notifyAll();
                }
            }
        }
    }
    

}

