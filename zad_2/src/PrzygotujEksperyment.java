import java.util.List;
import java.util.Set;

public class PrzygotujEksperyment {
	protected int liczba_kostek;
	protected List<Integer> sekwencja;
	protected Set<Integer> oczka;
	
	public PrzygotujEksperyment() {
		this.liczba_kostek = 2;
		this.sekwencja = List.of(1, 2, 3);
		this.oczka = Set.of(1, 2, 3);
	}
}