import java.util.HashSet;
import java.util.Set;


public class Zad2 {
	public static void main(String[] args) throws ClassNotFoundException {
//		System.out.println(solve("((1)23(45))", 0));
		System.out.println(sumIntervals(new int[][] { {4, 5}, {-5, 10}, {8, 12} }));
		System.out.println(Class.forName("[[Ljava.io.PrintStream;").getSimpleName());
	}
	
	public static int sumIntervals(int[][] intervals) {
		Set<Integer> zbiorLiczb = new HashSet<>();
		
		for (int[] przedzial : intervals) {
			int start = przedzial[0];
			int koniec = przedzial[1];
			for (int i = start; i < koniec; ++i) {
				zbiorLiczb.add(i);
			}
		}
		
		return zbiorLiczb.size();
	}
//	// dla wskazanego ciągu tekstowego i indeksu znaku otwierającego nawias, zwróci indeks odpowiadającego mu znaku zamykającego ten nawias
//	// lub -1, gdy takiego znaku nie ma
//	public static int solve(String s, int i) {
//		
//		int licznik = 0;
//		char[] tablicaZnakow = s.toCharArray();
//		for (int j = i; j < tablicaZnakow.length; ++j) {
//			if (s.charAt(j) == '(') {
//				licznik += 1;
//			} else if (s.charAt(j) == ')') {
//				--licznik;
//			}
//
//			if (licznik == 0) {
//				return j;
//			}
//		}
//		
//		
//		
//		return -1;
//	}
}