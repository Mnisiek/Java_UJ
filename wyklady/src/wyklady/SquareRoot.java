package wyklady;

public class SquareRoot {
	
	// final == const w C++
	// zapis liczby w notacji naukowej jak w C++
	public static final double precision = 1.0e-5;
	
	// metoda statyczna, związana z całą klasą SquareRoot
	public static double calculateSquareRoot(double x) {
		double guess = 1.0;
		
		do {
			System.out.println(guess);
			guess = (guess + x/guess)/2.0;
		} while ((guess*guess/x < 1.0 - precision) || (guess*guess/x > 1.0 + precision));
		return guess;
	}

	public static void main(String[] args ) {
		if (args.length < 1)
			System.out.println("Brak argumentu");
		else
			System.out.println(calculateSquareRoot(Double.parseDouble(args[0])));
	}
	// klasa publiczna musi być zdefiniowana w pliku, który nazywa się tak samo jak ta klasa .java
	
}

