import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class WE implements BadaczKostekDoGry {
	private int limitWatkow;
	private ThreadFactory fabryka;
	private static AtomicInteger idWatku = new AtomicInteger(1000);
	private Map<Integer, Thread> dzialajaceWatki;
	private Map<Integer, Map<Integer, Integer>> wyniki;
	
	
	public WE() {
		this.dzialajaceWatki = new HashMap<Integer, Thread>();
		this.wyniki = new HashMap<Integer, Map<Integer,Integer>>();
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
	public synchronized int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
		int identyfikator = idWatku.incrementAndGet();
		Runnable zadanie = new Zadanie(this, kostka, liczbaRzutow, identyfikator);
		Thread watek = fabryka.getThread(zadanie);
		
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
	
	
	public class Zadanie implements Runnable {
		private WE we;
		private KostkaDoGry mojaKostka;
		private int mojaLiczbaRzutow;
		private int mojeIdWatku;
		private Map<Integer, Integer> mojeWyniki;
		
		Zadanie(WE we, KostkaDoGry kostka, int liczbaRzutow, int identyfikator) {
			this.we = we;
			this.mojaKostka = kostka;
			this.mojaLiczbaRzutow = liczbaRzutow;
			this.mojeIdWatku = identyfikator;
			this.mojeWyniki = new HashMap<Integer, Integer>();
		}
		
		@Override
		public void run() {
			try {
                synchronized (we) {
                    while (limitWatkow <= dzialajaceWatki.size()) {
                        try {
							we.wait();
						} catch (InterruptedException e) {

						}
                    }
                    dzialajaceWatki.put(mojeIdWatku, Thread.currentThread());
                }
                
                int liczbaOczek;
                while (mojaLiczbaRzutow > 0) {
                    liczbaOczek = mojaKostka.rzut();
                    mojeWyniki.put(liczbaOczek, mojeWyniki.getOrDefault(liczbaOczek, 0) + 1);
                    --mojaLiczbaRzutow;
                }

                synchronized (we) {
					wyniki.put(mojeIdWatku, mojeWyniki);
				}
                
            } finally {
                synchronized (we) {
                    dzialajaceWatki.remove(mojeIdWatku);
                    notifyAll();
                }
            }
		}
	}
}

////import java.util.HashMap;
////import java.util.Map;
////import java.util.concurrent.atomic.AtomicInteger;
////
////public class WatkowyEksperymentator implements BadaczKostekDoGry {
////private int limitWatkow;
////private volatile AtomicInteger dzialajaceWatki;
////private ThreadFactory fabryka;
////private static AtomicInteger idWatku;
////private Map<Integer, Map<Integer, Integer>> wyniki;
////
////
////public WatkowyEksperymentator() {
////	dzialajaceWatki = new AtomicInteger(0);
////	idWatku = new AtomicInteger(1000);
////	wyniki = new HashMap<Integer, Map<Integer, Integer>>();
////}
////
////
////@Override
////public void dozwolonaLiczbaDzialajacychWatkow(int limitWatkow) {
////	this.limitWatkow = limitWatkow;
////}
////
////
////@Override
////public void fabrykaWatkow(ThreadFactory fabryka) {
////	this.fabryka = fabryka;
////}
////
////
////@Override
////public int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
////	System.out.println(dzialajaceWatki);
////	Runnable zadanie = new Zadanie(kostka, liczbaRzutow);
////	Thread watek = fabryka.getThread(zadanie);
////	watek.start();
////	
////	return idWatku.incrementAndGet();
////}
////
////
////@Override
////public boolean badanieKostkiZakonczono(int identyfikator) {
////	synchronized (wyniki) {
////		if (wyniki.containsKey(identyfikator)) {
////			return true;
////		} else {
////			return false;
////		}
////
////	}
////}
////
////
////@Override
////public Map<Integer, Integer> histogram(int identyfikator) {
////	synchronized (wyniki) {
////		return wyniki.get(identyfikator);
////	}
////}
////
////
////public class Zadanie implements Runnable {
////	private KostkaDoGry mojaKostka;
////	private int mojaLiczbaRzutow;
////	private int mojeIdWatku;
////	private Map<Integer, Integer> mojeWyniki;
////	
////	Zadanie(KostkaDoGry kostka, int liczbaRzutow) {
////		this.mojaKostka = kostka;
////		this.mojaLiczbaRzutow = liczbaRzutow;
////		this.mojeIdWatku = idWatku.incrementAndGet();
////		this.mojeWyniki = new HashMap<Integer, Integer>();
////	}
////	
////	@Override
////	public void run() {
////		synchronized (dzialajaceWatki) {
////            while (dzialajaceWatki.get() >= limitWatkow) {
////                try {
////                    dzialajaceWatki.wait();
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////            dzialajaceWatki.incrementAndGet();
////        }
////		
////		int liczbaOczek;
////
////        while (mojaLiczbaRzutow > 0) {
////            liczbaOczek = mojaKostka.rzut();
////            mojeWyniki.put(liczbaOczek, mojeWyniki.getOrDefault(liczbaOczek, 0) + 1);
////            --mojaLiczbaRzutow;
////        }
////        synchronized (dzialajaceWatki) {
////        	wyniki.put(mojeIdWatku, mojeWyniki);
////            dzialajaceWatki.decrementAndGet();
////            dzialajaceWatki.notifyAll();
////		}
////    }
////}
////
////}
////
//
//
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class WatkowyEksperymentator implements BadaczKostekDoGry {
//
//private static final AtomicInteger counter = new AtomicInteger(0);
//private int limitWatkow;
//private ThreadFactory fabrykaWatkow;
//private Map<Integer, Thread> dzialajaceWatki;
//private Map<Integer, Map<Integer, Integer>> histograms;
//
//public WatkowyEksperymentator() {
//    this.dzialajaceWatki = new HashMap<>();
//    this.histograms = new HashMap<>();
//}
//
//@Override
//public synchronized void dozwolonaLiczbaDzialajacychWatkow(int limitWatkow) {
//    this.limitWatkow = limitWatkow;
//}
//
//@Override
//public synchronized void fabrykaWatkow(ThreadFactory fabryka) {
//    this.fabrykaWatkow = fabryka;
//}
//
//@Override
//public synchronized int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
//    int identyfikator = generateUniqueId();
//    Thread watek = fabrykaWatkow.getThread(() -> {
//        try {
//            synchronized (this) {
//                while (dzialajaceWatki.size() >= limitWatkow) {
//                    try {
//						wait();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} // Czekaj, jeśli osiągnięto limit wątków
//                }
//                dzialajaceWatki.put(identyfikator, Thread.currentThread());
//            }
//
//            int[] wyniki = rzucKostka(kostka, liczbaRzutow);
//            zapiszWyniki(identyfikator, wyniki);
//        } finally {
//            synchronized (this) {
//                dzialajaceWatki.remove(identyfikator);
//                notifyAll(); // Powiadom inne wątki o zwolnieniu miejsca
//            }
//        }
//    });
//
//    watek.start();
//    return identyfikator;
//}
//
//private int[] rzucKostka(KostkaDoGry kostka, int liczbaRzutow) {
//    int[] wyniki = new int[liczbaRzutow];
//    for (int i = 0; i < liczbaRzutow; i++) {
//        wyniki[i] = kostka.rzut();
//    }
//    return wyniki;
//}
//
//private void zapiszWyniki(int identyfikator, int[] wyniki) {
//    Map<Integer, Integer> histogram = new HashMap<>();
//    for (int wynik : wyniki) {
//        histogram.put(wynik, histogram.getOrDefault(wynik, 0) + 1);
//    }
//    synchronized (this) {
//        histograms.put(identyfikator, histogram);
//    }
//}
//
//@Override
//public synchronized boolean badanieKostkiZakonczono(int identyfikator) {
//    return !dzialajaceWatki.containsKey(identyfikator) && histograms.containsKey(identyfikator);
//}
//
//@Override
//public synchronized Map<Integer, Integer> histogram(int identyfikator) {
//    return histograms.getOrDefault(identyfikator, new HashMap<>());
//}
//
//private int generateUniqueId() {
//    return counter.incrementAndGet();
//}
//}


//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//
//public class WatkowyEksperymentator implements BadaczKostekDoGry {
//private int limitWatkow;
//private ThreadFactory fabryka;
//private static AtomicInteger idWatku = new AtomicInteger(1000);
//private Map<Integer, Thread> dzialajaceWatki;
//private Map<Integer, Map<Integer, Integer>> wyniki;
//
//
//public WatkowyEksperymentator() {
//	this.dzialajaceWatki = new HashMap<Integer, Thread>();
//	this.wyniki = new HashMap<Integer, Map<Integer,Integer>>();
//}
//
//
//@Override
//public void dozwolonaLiczbaDzialajacychWatkow(int limitWatkow) {
//	this.limitWatkow = limitWatkow;
//}
//
//
//@Override
//public void fabrykaWatkow(ThreadFactory fabryka) {
//	this.fabryka = fabryka;
//}
//
//
//@Override
//public synchronized int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
//	int identyfikator = idWatku.incrementAndGet();
//	Runnable zadanie = new Zadanie(this, kostka, liczbaRzutow, identyfikator);
//	Thread watek = fabryka.getThread(zadanie);
//	
//	watek.start();
//	return identyfikator;
//}
//
//
//@Override
//public boolean badanieKostkiZakonczono(int identyfikator) {
//	synchronized (this) {
//		if (wyniki.containsKey(identyfikator) && !dzialajaceWatki) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//}
//
//
//@Override
//public Map<Integer, Integer> histogram(int identyfikator) {
//	synchronized (this) {
//		return wyniki.get(identyfikator);
//	}
//}
//
//
//public class Zadanie implements Runnable {
//	private WatkowyEksperymentator watkowyEksp;
//	private KostkaDoGry mojaKostka;
//	private int mojaLiczbaRzutow;
//	private int mojeIdWatku;
//	private Map<Integer, Integer> mojeWyniki;
//	
//	Zadanie(WatkowyEksperymentator watkowyEksp, KostkaDoGry kostka, int liczbaRzutow, int identyfikator) {
//		this.watkowyEksp = watkowyEksp;
//		this.mojaKostka = kostka;
//		this.mojaLiczbaRzutow = liczbaRzutow;
//		this.mojeIdWatku = identyfikator;
//		this.mojeWyniki = new HashMap<Integer, Integer>();
//	}
//	
//	@Override
//	public void run() {
//		try {
//            synchronized (watkowyEksp) {
//                while (limitWatkow <= dzialajaceWatki.size()) {
//                    try {
//						watkowyEksp.wait();
//					} catch (InterruptedException e) {
//
//					}
//                }
//                dzialajaceWatki.put(mojeIdWatku, Thread.currentThread());
//            }
//            
//            int liczbaOczek;
//            while (mojaLiczbaRzutow > 0) {
//                liczbaOczek = mojaKostka.rzut();
//                mojeWyniki.put(liczbaOczek, mojeWyniki.getOrDefault(liczbaOczek, 0) + 1);
//                --mojaLiczbaRzutow;
//            }
//
//            synchronized (watkowyEksp) {
//				wyniki.put(mojeIdWatku, mojeWyniki);
//			}
//            
//        } finally {
//            synchronized (watkowyEksp) {
//                dzialajaceWatki.remove(mojeIdWatku);
//                notifyAll();
//            }
//        }
//	}
//}
//}
