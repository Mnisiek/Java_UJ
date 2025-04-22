import java.util.Stack;


public class Zad5 {
    public static void main(String[] args) {
        System.out.println(decode("3a"));
    }

    public static String decode(String napis) {
        Stack<StringBuilder> stosNapisow = new Stack<>();
        Stack<Integer> stosLiczb = new Stack<>();
        stosNapisow.push(new StringBuilder());
        int liczba = 0;

        for (char znak : napis.toCharArray()) {
            if (Character.isDigit(znak)) {
                liczba = znak - '0';
            } else if (znak == '(') {
                stosLiczb.push(liczba);
                stosNapisow.push(new StringBuilder());
                liczba = 0;
            } else if (znak == ')') {
                StringBuilder temp = stosNapisow.pop();
                int powtorzenia = stosLiczb.pop();
                StringBuilder powtorzonyFragment = new StringBuilder();
                for (int i = 0; i < powtorzenia; i++) {
                    powtorzonyFragment.append(temp);
                }
                stosNapisow.peek().append(powtorzonyFragment);
            } else {
                stosNapisow.peek().append(znak);
            }
        }
        
        return stosNapisow.pop().toString();
    }
}

