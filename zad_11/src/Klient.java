import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

public class Klient implements NetConnection {
	private BigInteger password;
	private BigInteger result;
	private BigInteger serverResponse;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	
	public Klient() {
		result = BigInteger.ZERO;
		serverResponse = BigInteger.ONE;
	}


	@Override
	public void password(String password) {
		this.password = new BigInteger(password);
	}


	@Override
	public void connect(String host, int port) {
		String myHost = host;
		int myPort = port;
		String line;
		
		try {
			socket = new Socket(myHost, myPort);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			
			input.readLine();
			input.readLine();
			input.readLine();
			
			output.println("Program");
			
			input.readLine();
			input.readLine();
			input.readLine();
			
			while ((line = input.readLine()) != null && !(line.equals("EOD"))) {
				result = result.add(new BigInteger(line));
			}
			result = result.add(this.password);
			
			input.readLine();
			line = input.readLine();
			
			try {
				if (line.startsWith("Mnie")) {
					serverResponse = new BigInteger(line.split(" ")[2]);
				}
		    } catch (Exception e) {

		    }

			if (serverResponse.equals(result)) {
				output.println(result.toString());
			} else {
				output.println(NetConnection.ODPOWIEDZ_DLA_OSZUSTA);
			}
			
		} catch (UnknownHostException e) {

		} catch (IOException e) {

		}
	}
}


