import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class SwMikolaj implements Inwentaryzator {
	private Map<String, Integer> wyniki;
	private Set<String> poszukiwanePola;
	private List<String> mojaListaKlas;
	
	public SwMikolaj() {
		this.wyniki = new HashMap<String, Integer>();
		this.poszukiwanePola = new HashSet<String>(List.of(
				"bombki", "lancuchy", "cukierki",
				"prezenty", "szpice", "lampki"));
	}
	

	@Override
	public Map<String, Integer> inwentaryzacja(List<String> listaKlas) {
		mojaListaKlas = listaKlas;
		
		for (String nazwaKlasy : mojaListaKlas) {
			try {
				Class<?> klasa = Class.forName(nazwaKlasy);
				Object instancjaKlasy = klasa.newInstance();
				Field[] polaKlasy = klasa.getDeclaredFields();
				
				for (Field pole : polaKlasy) {
					sprawdzenieWarunkow(pole, instancjaKlasy);
				}
			} catch (ClassNotFoundException e) {

			} catch (InstantiationException e) {

			} catch (IllegalAccessException e) {

			}
		}
		return wyniki;
	}
	
	
	private boolean sprawdzenieWarunkow(Field pole, Object instancjaKlasy) {
		String nazwaPola = pole.getName();
		int wartoscPola;
		if (pole.getType().equals(int.class)) {
			if (!(Modifier.isStatic(pole.getModifiers())) && Modifier.isPublic(pole.getModifiers())) {
				if (poszukiwanePola.contains(nazwaPola)) {
					try {
						wartoscPola = pole.getInt(instancjaKlasy);
						wyniki.put(nazwaPola, wyniki.getOrDefault(nazwaPola, 0) + wartoscPola);
						
						return true;
					} catch (IllegalArgumentException e) {
						
					} catch (IllegalAccessException e) {
						
					}
				}
			}
		}
		
		return false;
	}

}
