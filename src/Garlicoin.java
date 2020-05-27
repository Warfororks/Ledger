import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

public class Garlicoin extends Crypto {
	Garlicoin() {
		super("Garlicoin", "GRLC", "/2475.png");
		readPrice();
	}
	
	public void readPrice() { //main method to get the price of the coin
		try {
			String grlcAPI = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?CMC_PRO_API_KEY=6278fa27-87ca-4a85-96ec-9f4d7395ceff&id=2475";

			InputStream APIinput = new URL(grlcAPI).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(APIinput, Charset.forName("UTF-8")));
			
			String grlcPriceStr = subPrice(readData(reader));
			
			setPrice(Double.parseDouble(grlcPriceStr));
		}
		catch(MalformedURLException e) {
			System.out.println("Bad URL");
		}
		catch(IOException e) {
			System.out.println("Other exception");
		}
		
	}
}
