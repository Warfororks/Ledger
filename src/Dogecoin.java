import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Dogecoin extends Crypto {
	Dogecoin() {
		super("Dogecoin", "DOGE", "/74.png");
		readPrice();
	}
	
	public void readPrice() {
		try {
			String dogeAPI = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?CMC_PRO_API_KEY=6278fa27-87ca-4a85-96ec-9f4d7395ceff&id=74";

			InputStream APIinput = new URL(dogeAPI).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(APIinput, Charset.forName("UTF-8")));
			
			String dogePriceStr = subPrice(readData(reader));
			
			setPrice(Double.parseDouble(dogePriceStr));
		}
		catch(MalformedURLException e) {
			System.out.println("Bad URL");
		}
		catch(IOException e) {
			System.out.println("Other exception");
		}
	}
}