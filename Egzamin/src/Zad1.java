import java.util.Arrays;


public class Zad1 {
    public static void main(String[] args) {
        System.out.println(najmnieszaWieksza(9001));
    }

    public static int najmnieszaWieksza(int liczba) {
        String liczbaNapis = Integer.toString(liczba);
        char[] cyfry = liczbaNapis.toCharArray();
        int n = liczbaNapis.length();

        int i;
        for (i = n-1; i > 0; i--) {
            if (cyfry[i] > cyfry[i-1]) {
                break;
            }
        }
        if (i == 0) {
            return -1;
        }

        int x = cyfry[i-1], najmniejszaWiekszaInd = i;
        for (int j = i+1; j < n; j++) {
            if (cyfry[j] > x && cyfry[j] < cyfry[najmniejszaWiekszaInd]) {
                najmniejszaWiekszaInd = j;
            }
        }

        char temp = cyfry[i-1];
        cyfry[i-1] = cyfry[najmniejszaWiekszaInd];
        cyfry[najmniejszaWiekszaInd] = temp;

        Arrays.sort(cyfry, i, n);

        int wynik = Integer.parseInt(new String(cyfry));
        return wynik;
    }
}
