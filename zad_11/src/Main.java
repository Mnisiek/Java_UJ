
public class Main {
	public static void main(String[] args) {
		Klient klient = new Klient();
		klient.password("1000");
		klient.connect("172.30.24.12", 9090);
	}
}
