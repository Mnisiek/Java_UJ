
public class Zad3 {
	public static void main(String[] args) {
		System.out.println(sum("1234", "12"));
	}
	
	public static String sum(String a, String b) {
        StringBuilder wynik = new StringBuilder();
		char[] aTablica = a.toCharArray();
		char[] bTablica = b.toCharArray();

        int aIndex = a.length() - 1;
        int bIndex = b.length() - 1;
        int przeniesienie = 0;

        while (aIndex >= 0 || bIndex >= 0 || przeniesienie != 0) {
            int sumaCyfr = przeniesienie;
            if (aIndex >= 0) {
                sumaCyfr += aTablica[aIndex] - '0';
                aIndex--;
            }
            if (bIndex >= 0) {
                sumaCyfr += bTablica[bIndex] - '0';
                bIndex--;
            }
            wynik.append(sumaCyfr % 10);
            przeniesienie = sumaCyfr / 10;
        }

        return wynik.reverse().toString();
	}
}
