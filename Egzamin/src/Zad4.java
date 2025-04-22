
public class Zad4 {
	public static void main(String[] args) {
		System.out.println(time(31536060));
	}
	
	public static String time(int seconds) {
		final int rok = 365*24*60*60;
		final int dzien = 24*60*60;
		final int godzina = 60*60;
		final int minuta = 60;
		StringBuilder wynik = new StringBuilder();
		if (seconds > rok) {
			int lata = seconds/rok;
			wynik.append(Integer.toString(lata) + "r ");
			seconds -= (lata * rok);
		}
		
		if (seconds >= dzien) {
			int dni = seconds/dzien;
			wynik.append(Integer.toString(dni) + "d ");
			seconds -= (dni * dzien);
		}
		
		if (seconds >= godzina) {
			int godziny = seconds/godzina;
			wynik.append(Integer.toString(godziny) + "g ");
			seconds -= (godziny * godzina);
		}
		
		if (seconds >= minuta) {
			int minuty = seconds/minuta;
			wynik.append(Integer.toString(minuty) + "m ");
			seconds -= (minuty * minuta);
		}
		
		if (seconds != 0) {
			wynik.append(seconds + "s");
		}
		
		return wynik.toString().strip();
	}
}
