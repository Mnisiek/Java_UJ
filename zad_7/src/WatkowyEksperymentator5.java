import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WatkowyEksperymentator5 implements BadaczKostekDoGry {
	private int limitWatkow;
	private volatile AtomicInteger dzialajaceWatki;
	private ThreadFactory fabryka;
	private static AtomicInteger idWatku;
	private Map<Integer, Map<Integer, Integer>> wyniki;
	
	protected KostkaDoGry kostka;
	protected int liczbaRzutow;
	
	
	public WatkowyEksperymentator5() {
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
	    this.kostka = kostka;
	    this.liczbaRzutow = liczbaRzutow;

//        if (limitWatkow > dzialajaceWatki.get()) {
//        	// worek z działającymi wątkami
//            return uruchomNowyWatek();
//        } else {
//        	// worek z czekającymi i śpiącymi wątkami
//            
//            
//            // tu jest problem
//        }
////	    while ()
//	    System.out.println(dzialajaceWatki);
	    return uruchomNowyWatek();
	}


	public int uruchomNowyWatek() {
		Runnable zadanie = utworzObiektRunnable();
		Thread watek = fabryka.getThread(zadanie);
		watek.start();
		idWatku.getAndIncrement();
//		    // getId() zwraca long, wiec rzutujemy 
//		    return (int) watek.getId();
		return idWatku.get();
	}


	public Runnable utworzObiektRunnable() {
	    return new Runnable() {
	        private KostkaDoGry mojaKostka = WatkowyEksperymentator5.this.kostka;
	        private int mojaLiczbaRzutow = WatkowyEksperymentator5.this.liczbaRzutow;
	        private Map<Integer, Integer> mojeWyniki = new HashMap<Integer, Integer>();

	        @Override
	        public void run() {
	        	synchronized (dzialajaceWatki) {
	        		dzialajaceWatki.incrementAndGet();
		        	if (limitWatkow < dzialajaceWatki.get()) {
		        		try {
							dzialajaceWatki.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
				}
	        	
	            int liczbaOczek;

	            while (mojaLiczbaRzutow > 0) {
	                liczbaOczek = mojaKostka.rzut();
	                mojeWyniki.put(liczbaOczek, mojeWyniki.getOrDefault(liczbaOczek, 0) + 1);
	                --mojaLiczbaRzutow;
	            }
	            synchronized (wyniki) {
	            	wyniki.put(idWatku.get(), mojeWyniki);
		            dzialajaceWatki.decrementAndGet();
		            wyniki.notifyAll();
				}
	        }
	    };
	}


	public int czekajNaDostepneWatki() {
//	    synchronized (wyniki) {
//	    	do {
//		        try {
//		            wyniki.wait();
//		        } catch (InterruptedException e) {
//		            
//		        }
//		    } while (limitWatkow <= dzialajaceWatki.get());
//	    	
//	    	return kostkaDoZbadania(kostka, liczbaRzutow);
//		}
//		synchronized (wyniki) {
//			try {
//				wyniki.wait();
//			} catch (InterruptedException e) {
//				
//			}
//		}
//		
//		return kostkaDoZbadania(kostka, liczbaRzutow);
		
		Runnable zadanie = utworzObiektRunnable();
		Thread watek = fabryka.getThread(zadanie);
		
		try {
			watek.wait();
		} catch (InterruptedException e) {
			
		}
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

}
