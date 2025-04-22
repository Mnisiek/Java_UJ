
import java.util.Random;

public class TestDie implements KostkaDoGry {

    private Random random;

    public TestDie() {
        this.random = new Random();
    }

    @Override
    public int rzut() {
        // Symulacja rzutu kostką o 6 ścianach
        return random.nextInt(6) + 1;
    }
}
