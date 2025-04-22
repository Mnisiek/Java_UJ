
import java.net.URL;

class Start {
	public static void main( String[] argv ) throws Exception {
	   URL url = new URL( "http://zti.if.uj.edu.pl/Piotr.Oramus/dydaktyka" );

	   System.out.println( "getAuthority()   = " + url.getAuthority() );
	   System.out.println( "getContent()     = " + url.getContent() );
	   System.out.println( "getDefaultPort() = " + url.getDefaultPort() );
	   System.out.println( "getFile()        = " + url.getFile() );
	   System.out.println( "getProtocol()    = " + url.getProtocol() );
	   System.out.println( "getUserInfo()    = " + url.getUserInfo() );
        
	}
}

