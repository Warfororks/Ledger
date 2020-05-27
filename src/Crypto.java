import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Crypto {
	private String name;
	private double price;
	private double balance;
	private ImageIcon icon;
	private JTextField text; //balance
	private JLabel labelLeft; //name, icon
	private JLabel labelRight; //@ price and total
	
	Crypto(String n, double b) {
		name = n;
		balance = b;
		icon = new ImageIcon(Ledger.class.getResource("/" + n.toLowerCase() + ".png"));
		text = new JTextField("0"); //default balance
		labelLeft = new JLabel(leftString(), icon, JLabel.CENTER); //name, icon
		labelRight = new JLabel(rightString()); //@ price and total
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
		return leftString() + getBalance() + rightString();
	}
	
	public String leftString() {
		return getName() + " Balance: ";
	}
	
	public String rightString() {
		return " @ " + getPrice() + ": " + getUSD();
	}
}