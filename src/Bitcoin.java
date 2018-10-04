import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

public class Bitcoin extends Crypto {
	Bitcoin() {
		super("Bitcoin", "BTC", "/1.png");
		readPrice();
	}
	
	public void readPrice() { //main method to get the price of the coin
		try {
			String btcAPI = "https://api.coinmarketcap.com/v2/ticker/1/";

			InputStream APIinput = new URL(btcAPI).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(APIinput, Charset.forName("UTF-8")));
			
			String btcPriceStr = subPrice(readData(reader));
			
			setPrice(Double.parseDouble(btcPriceStr));
		}
		catch(MalformedURLException e) {
			System.out.println("Bad URL");
		}
		catch(IOException e) {
			System.out.println("Other exception");
		}
	}
}
