import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class WABnetwork extends Crypto {
	WABnetwork() {
		super("WABnetwork", "WAB", "/2980.png");
		readPrice();
	}
	
	public void readPrice() { //main method to get the price of the coin
		try {
			String wabAPI = "https://api.coinmarketcap.com/v2/ticker/2980/";

			InputStream APIinput = new URL(wabAPI).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(APIinput, Charset.forName("UTF-8")));
			
			String wabPriceStr = subPrice(readData(reader));
			
			setPrice(Double.parseDouble(wabPriceStr));
		}
		catch(MalformedURLException e) {
			System.out.println("Bad URL");
		}
		catch(IOException e) {
			System.out.println("Other exception");
		}
	}
}
