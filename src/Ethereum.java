import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

public class Ethereum extends Crypto {
	Ethereum() {
		super("Ethereum", "ETH", "/1027.png");
		readPrice();
	}
	
	public void readPrice() { //main method to get the price of the coin
		try {
			String ethAPI = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?CMC_PRO_API_KEY=6278fa27-87ca-4a85-96ec-9f4d7395ceff&id=1027";

			InputStream APIinput = new URL(ethAPI).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(APIinput, Charset.forName("UTF-8")));
			
			String ethPriceStr = subPrice(readData(reader));
			
			setPrice(Double.parseDouble(ethPriceStr));
		}
		catch(MalformedURLException e) {
			System.out.println("Bad URL");
		}
		catch(IOException e) {
			System.out.println("Other exception");
		}
	}
}
