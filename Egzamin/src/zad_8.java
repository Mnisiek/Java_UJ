

class SharedCounter {
    private int counter = 0;

    public void increment() { // Synchronizacja na poziomie metody
        counter++;
    }

    public int getValue() { // Synchronizacja odczytu wartości
        return counter;
    }
}

public class zad_8 {
    public static void main(String[] args) {
        SharedCounter counter = new SharedCounter();

        // Tworzymy 100 wątków, które zwiększają licznik
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        // Czekamy na zakończenie wszystkich wątków
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Oczekiwany wynik: 100 * 1000 = 100000
        System.out.println("Final counter value: " + counter.getValue());
	    }
	}
