
import java.util.Random;

public class MojaKostkaDoGry implements KostkaDoGry {
	private Random random;
	
	MojaKostkaDoGry() {
		this.random = new Random();
	}
	
	@Override
	public int rzut() {
		return random.nextInt(6) + 1;
	}
}
