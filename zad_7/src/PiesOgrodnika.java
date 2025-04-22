
	class Wiadomość {
	    public volatile String wiadomość = null;
	}

	class Czytelnik implements Runnable {

	    private boolean useWait = true;
	    private Wiadomość txt;

	    public Czytelnik(Wiadomość txt) {
	        this.txt = txt;
	    }

	    @Override
	    public void run() {
	        int showLimit = 0;
	        synchronized (txt) {
	            while (txt.wiadomość == null) {
	                if (showLimit % 10000000 == 0) { // wyświetlamy 1 komunikat na 10000000
	                    System.out.println("Oto ja czytelnik> Brak wieści txt = null, poczekam, jestem cierpliwy...");
	                }
	                if (useWait) {
	                    try {
	                        txt.wait();
	                    } catch (InterruptedException e) {
	                    }
	                }
	                showLimit++;
	            }
	            System.out.println("Oto ja czytelnik> a to wiadomość : \"" + txt.wiadomość + "\"");
	        }
	    }
	}

	class Pisarz implements Runnable {

	    private Wiadomość txt;
	    private boolean useNotify = true;

	    public Pisarz(Wiadomość txt) {
	        this.txt = txt;
	    }

	    @Override
	    public void run() {
	        System.out.println("To ja pisarz> Postaram się wysłać wiadomość. Musze wejść do bloku synchronized");
	        synchronized (txt) {
	            txt.wiadomość = "Cześć czytelniku, ja pisarz piszę do Ciebie!";
	            System.out.println("To ja pisarz> Wiadomość wysłana, czytaj czytelniku");
	            if (useNotify)
	                txt.notifyAll();
	        }
	    }
	}

	
public class PiesOgrodnika {
	

	class Start {
	    public static void main(String[] args) {
	        Wiadomość txt = new Wiadomość();
	        Thread czytelnik = new Thread(new Czytelnik(txt));
	        Thread pisarz = new Thread(new Pisarz(txt));
	        czytelnik.start();
	        System.out.println("MAIN> Uruchomiono wątek czytelnika");
	        pisarz.start();
	        System.out.println("MAIN> Uruchomiono wątek pisarza");
	    }
	}
}
