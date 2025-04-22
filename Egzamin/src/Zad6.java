import java.util.Map;
import java.util.TreeMap;

public class Zad6 {
	public static void main(String[] args) {
		System.out.println(decompose(15));
	}
	
	
	public static String decompose(int n) {
        Map<Integer, Integer> primeFactors = new TreeMap<>();

        for (int i = 2; i <= n; i++) {
            int temp = i;
            for (int factor = 2; factor <= temp; factor++) {
                while (temp % factor == 0) {
                    primeFactors.put(factor, primeFactors.getOrDefault(factor, 0) + 1);
                    temp /= factor;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : primeFactors.entrySet()) {
            if (result.length() > 0) {
                result.append(" * ");
            }
            result.append(entry.getKey());
            if (entry.getValue() > 1) {
                result.append("^").append(entry.getValue());
            }
        }

        return result.toString();
    }
}
