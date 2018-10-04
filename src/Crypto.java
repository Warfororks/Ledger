import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Crypto {
	private String name;
	private String ticker;
	private double price;
	private double balance;
	private ImageIcon icon;
	private JTextField text;
	private JLabel labelLeft;
	private JLabel labelRight;
	
	Crypto() {

	}
	
	Crypto(String n, String t, String i) {
		name = n;
		ticker = t;
		icon = new ImageIcon(Ledger.class.getResource(i));
		text = new JTextField("0");
		labelLeft = new JLabel(leftString(), icon, JLabel.CENTER);
		labelRight = new JLabel(rightString());
	}
	
	public String readData(BufferedReader reader) { //method to read all API data
		StringBuilder stringbuild = new StringBuilder();
		int individualChar;
		try {
			while ((individualChar = reader.read()) != -1) { //checks to see if there is a valid ascii character
				stringbuild.append((char)individualChar);
			}
		}
		catch(IOException e) {
			System.out.println("Error in reading API");
		}
		return stringbuild.toString();
	}
	
	public void setBalance(double b) {
		balance = b;
	}
	
	public void setPrice(double p) {
		price = p;
	}
	
	public void setText(String t) {
		text.setText(t);
	}
	
	public void updateLabel() {
		labelRight.setText(rightString());
	}
	
	public String getName() {
		return name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
	
	public JLabel getLabelLeft() {
		return labelLeft;
	}
	
	public JTextField getTextField() {
		return text;
	}
	
	public JLabel getLabelRight() {
		return labelRight;
	}
	
	public String getUSD() {
		DecimalFormat df2 = new DecimalFormat(".##");
		return df2.format(price * balance);
	}
	
	public String toString() {
		return getName() + " (" + getTicker() + ") " + "Balance: " + getBalance() + " @ " + getPrice() + ": " + getUSD();
	}
	
	public String leftString() {
		return getName() + " (" + getTicker() + ") " + "Balance: ";
	}
	
	public String rightString() {
		return " @ " + getPrice() + ": " + getUSD();
	}
	
	public static String subPrice(String str) { //method to parse API data and return price
		return str.substring(str.indexOf("price")+8, str.indexOf(",", str.indexOf("price")));	 
	}
}
