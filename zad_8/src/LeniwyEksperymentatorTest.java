import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class LeniwyEksperymentatorTest {
	
    public static void main(String[] args) {
        oneDie();
        idsAreUnique();
        queueingIsFast();
        tenConcurrent();
        tenNonconcurrent();
        concurrentUse();
        
     // Add a delay to ensure background threads complete before the program exits
        try {
            Thread.sleep(10000); // Adjust the duration based on your test runtime
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private static void oneDie() {
        var e = new LeniwyEksperymentator();
        e.fabrykaWatkow(Executors.newFixedThreadPool(4));
        var id = e.kostkaDoZbadania(new TestDie(), 5);
        while (!e.badanieKostkiZakonczono(id)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }
        assert e.histogram(id).equals(Map.of(1, 2, 2, 1, 3, 1, 4, 1));
    }

    private static void idsAreUnique() {
        var e = new LeniwyEksperymentator();
        e.fabrykaWatkow(Executors.newFixedThreadPool(50));
        var lastId = -123;
        for (int i = 0; i < 100; i++) {
            var id = e.kostkaDoZbadania(new TestDie(), 3);
            assert id != lastId;
            lastId = id;
        }

        while (!e.badanieKostkiZakonczono(lastId)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }

        for (int i = 0; i < 100; i++) {
            assert lastId != e.kostkaDoZbadania(new TestDie(), 3);
        }
    }

    private static void queueingIsFast() {
        var e = new LeniwyEksperymentator();
        e.fabrykaWatkow(Executors.newFixedThreadPool(50));

        var start = Instant.now();

        for (int i = 0; i < 100; i++) {
            e.kostkaDoZbadania(new TestDie(), 500);
        }

        var end = Instant.now();

        assert Duration.between(start, end).toMillis() < 5000;
    }

    private static void tenConcurrent() {
        var e = new LeniwyEksperymentator();
        e.fabrykaWatkow(Executors.newFixedThreadPool(10));

        var start = Instant.now();

        var ids = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            ids.add(e.kostkaDoZbadania(new TestDie(), 10));
        }
        for (int i = 0; i < 10; i++) {
            while (!e.badanieKostkiZakonczono(ids.get(i))) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        }

        var end = Instant.now();
        assert Duration.between(start, end).toMillis() < 2000;

        for (int i = 0; i < 10; i++) {
            assert e.histogram(ids.get(i)).equals(Map.of(1, 3, 2, 3, 3, 2, 4, 2));
        }
    }

    private static void tenNonconcurrent() {
        var e = new LeniwyEksperymentator();
        e.fabrykaWatkow(Executors.newFixedThreadPool(5));
        var start = Instant.now();

        var ids = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            ids.add(e.kostkaDoZbadania(new TestDie(), 10));
        }
        for (int i = 0; i < 10; i++) {
            while (!e.badanieKostkiZakonczono(ids.get(i))) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        }

        var end = Instant.now();
        assert Duration.between(start, end).toMillis() > 1000;
        assert Duration.between(start, end).toMillis() < 3000;

        for (int i = 0; i < 10; i++) {
            assert e.histogram(ids.get(i)).equals(Map.of(1, 3, 2, 3, 3, 2, 4, 2));
        }
    }

    private static void concurrentUse() {
        var e = new LeniwyEksperymentator();
        e.fabrykaWatkow(Executors.newFixedThreadPool(5));

        var done = new LinkedBlockingQueue<Integer>();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                var ids = new ArrayList<Integer>();
                for (int j = 0; j < 10; j++) {
                    ids.add(e.kostkaDoZbadania(new TestDie(), 10));
                }

                while (!ids.isEmpty()) {
                    for (int j = 0; j < ids.size(); j++) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ignored) {
                        }

                        if (e.badanieKostkiZakonczono(ids.get(j))) {
                            done.add(ids.get(j));
                            ids.remove(j);
                            break;
                        }
                    }
                }
            }).start();
        }

        for (int i = 0; i < 50; i++) {
            try {
                var id = done.take();
                assert e.histogram(id).equals(Map.of(1, 3, 2, 3, 3, 2, 4, 2));
            } catch (InterruptedException ignored) {
            }
        }
    }
}
