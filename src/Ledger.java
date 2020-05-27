import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.google.gson.*;

class Ledger {
	ArrayList<Crypto> list;
	JLabel empty;
	JLabel totalTitle;
	JLabel grandTotal;
	double gTotal;
	StringBuilder url;
	StringBuilder coins;
	StringBuilder currencies;
	Gson gson;
	
	Ledger() throws IOException {
		url = new StringBuilder("https://api.coingecko.com/api/v3/simple/price?ids="); //api url, need to add IDs of coins
		currencies = new StringBuilder("&vs_currencies=usd"); //needs to be appended at the end of the url after the ids are added
		
		JFrame frame = new JFrame("Ledger v0.7"); //create frame
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 500);
		ImageIcon logo = new ImageIcon(Ledger.class.getResource("/logo.png")); //use logo
		frame.setIconImage(logo.getImage());

		init(); //read in balance.txt to initialize coins and balances, otherwise create just bitcoin
		initPrice(); //uses the api url to initialize the prices, totals, and grand total
		
		Container contentPane = frame.getContentPane();
		SpringLayout layout = new SpringLayout();
		contentPane.setLayout(layout);
		
		for(int i = 0; i < list.size(); i++) { //for every coin in the list add its 
			contentPane.add(list.get(i).getLabelLeft()); //name
			contentPane.add(list.get(i).getTextField()); //balance
			contentPane.add(list.get(i).getLabelRight()); //price and total
		}
		
		empty = new JLabel(); //empty label to keep formatting
		totalTitle = new JLabel("Grand Total:"); //title of the total
		contentPane.add(empty);
		contentPane.add(totalTitle);
		contentPane.add(grandTotal); //initial grand total is set in initPrice()
		
		SpringUtilities s = new SpringUtilities();
		s.makeGrid(contentPane, list.size()+1, 3, 5, 5, 5, 5); //list.size() for every coin and +1 for the grand total
		
		frame.pack();
		frame.setVisible(true);
		
		for(int i = 0; i < list.size(); i++) { //add listeners to each text field to update frame when text field changes
			list.get(i).getTextField().getDocument().addDocumentListener(new DListen());
		}
		
		frame.addWindowListener(new WListen()); //for closing the program
	}
	
	public void init() { //read in balance.txt to initialize coins and balances, otherwise create just bitcoin
		list = new ArrayList<>(); //create an empty list

		try {
			Scanner inputFile = new Scanner(new File("balance.txt")); //scanner for reading balance.txt

			while(inputFile.hasNextLine()) {
				String line = inputFile.nextLine(); //parse entire line

				String strName = line.substring(0, line.indexOf(':')).trim(); //get name from line - use : to determine
				Crypto c = new Crypto(strName, Double.parseDouble(line.substring(line.indexOf(':')+1))); //create a coin using the name and balance
				
				list.add(c); //add a new coin to the list
				url.append(strName + ','); //append the name and a comma to the url in order to read price data later
				
				c.setText(new Double(c.getBalance()).toString()); //read balance and set the textfield to equal the balance
			}
			inputFile.close();
		}
		catch(FileNotFoundException e) { //when balance.txt isn't found
			System.out.println("Balance.txt is not found, initializing only bitcoin");
			list.add(new Crypto("Bitcoin", 0.0));
			url.append("Bitcoin"); //append bitcoin to read price data later
		}
		finally {
			url.append(currencies); //after all coins are added append the usd conversion string
		}
	}
	
	public void initPrice() { 
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url.toString()).openStream())); //turn the url into a URL and open the reader
			JsonObject data = (JsonObject)JsonParser.parseString(reader.readLine()); //read the single line with all price data
			for (Crypto c : list) { //for each coin in the list
				c.setPrice(data.getAsJsonObject(c.getName().toLowerCase()).getAsJsonPrimitive("usd").getAsDouble()); //get the usd as a double for each coin and set the price
				c.updateLabel(); //update the price and total on the label
				gTotal += Double.parseDouble(c.getUSD()); //sum the grand total
			}
		} catch (IOException e) {
			System.out.println("API url is probably busted");
			e.printStackTrace();
		} finally {
			grandTotal = new JLabel(new DecimalFormat(".##").format(gTotal)); //grand total label should be properly formatted to 2 decimals
		}
	}
	
	public void updateBalance() { //for when the balance text fields are changed
		try {
			gTotal = 0;
			for(int i = 0; i < list.size(); i++) { //to-do: how to make this more efficient
				System.out.println(list.get(i).getTextField().getText());
				list.get(i).setBalance(Double.parseDouble(list.get(i).getTextField().getText()));
				list.get(i).updateLabel();
				gTotal += Double.parseDouble(list.get(i).getUSD());
			}
			grandTotal.setText(new DecimalFormat(".##").format(gTotal));
		}
		catch(NumberFormatException e) {
			System.out.println("Numbers aren't properly formatted but it doesn't really matter");
		}
	}
	
	public void writeToBalance() { //called after the app is closed
		String newLine = "";
		
		try {
			PrintWriter outputFile = new PrintWriter(new File("balance.txt"));
			
			for(int i = 0; i < list.size(); i++) {
				newLine = list.get(i).getName() + ":" + list.get(i).getBalance(); //output line in format name:balance
				outputFile.println(newLine);
			}
			outputFile.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("Balance.txt is not found, cannot rewrite");
		}
	}
	
	private class WListen extends WindowAdapter {
		public void windowClosed(WindowEvent e) { //when clicking the X button
			System.out.println("closed");
			writeToBalance(); //updates balances.txt
			System.exit(0); //quits program
		}
	}
	
	private class DListen implements DocumentListener { //for inserting/updating/removing anything in the balance text field
		public void insertUpdate(DocumentEvent e) {
			updateBalance();
		}
		
		public void removeUpdate(DocumentEvent e) {
			updateBalance();
		}
		
		public void changedUpdate(DocumentEvent e) {
			updateBalance();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Ledger();
	}
	
	//everything below this line is used for formatting the ledger and was code not written by me
	public class SpringUtilities {
	    /**
	     * A debugging utility that prints to stdout the component's
	     * minimum, preferred, and maximum sizes.
	     */
	    public void printSizes(Component c) {
	        System.out.println("minimumSize = " + c.getMinimumSize());
	        System.out.println("preferredSize = " + c.getPreferredSize());
	        System.out.println("maximumSize = " + c.getMaximumSize());
	    }
	 
	    /**
	     * Aligns the first <code>rows</code> * <code>cols</code>
	     * components of <code>parent</code> in
	     * a grid. Each component is as big as the maximum
	     * preferred width and height of the components.
	     * The parent is made just big enough to fit them all.
	     *
	     * @param rows number of rows
	     * @param cols number of columns
	     * @param initialX x location to start the grid at
	     * @param initialY y location to start the grid at
	     * @param xPad x padding between cells
	     * @param yPad y padding between cells
	     */
	    public void makeGrid(Container parent,
	                                int rows, int cols,
	                                int initialX, int initialY,
	                                int xPad, int yPad) {
	        SpringLayout layout;
	        try {
	            layout = (SpringLayout)parent.getLayout();
	        } catch (ClassCastException exc) {
	            System.err.println("The first argument to makeGrid must use SpringLayout.");
	            return;
	        }
	 
	        Spring xPadSpring = Spring.constant(xPad);
	        Spring yPadSpring = Spring.constant(yPad);
	        Spring initialXSpring = Spring.constant(initialX);
	        Spring initialYSpring = Spring.constant(initialY);
	        int max = rows * cols;
	 
	        //Calculate Springs that are the max of the width/height so that all
	        //cells have the same size.
	        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).
	                                    getWidth();
	        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).
	                                    getHeight();
	        for (int i = 1; i < max; i++) {
	            SpringLayout.Constraints cons = layout.getConstraints(
	                                            parent.getComponent(i));
	 
	            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
	            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
	        }
	 
	        //Apply the new width/height Spring. This forces all the
	        //components to have the same size.
	        for (int i = 0; i < max; i++) {
	            SpringLayout.Constraints cons = layout.getConstraints(
	                                            parent.getComponent(i));
	 
	            cons.setWidth(maxWidthSpring);
	            cons.setHeight(maxHeightSpring);
	        }
	 
	        //Then adjust the x/y constraints of all the cells so that they
	        //are aligned in a grid.
	        SpringLayout.Constraints lastCons = null;
	        SpringLayout.Constraints lastRowCons = null;
	        for (int i = 0; i < max; i++) {
	            SpringLayout.Constraints cons = layout.getConstraints(
	                                                 parent.getComponent(i));
	            if (i % cols == 0) { //start of new row
	                lastRowCons = lastCons;
	                cons.setX(initialXSpring);
	            } else { //x position depends on previous component
	                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
	                                     xPadSpring));
	            }
	 
	            if (i / cols == 0) { //first row
	                cons.setY(initialYSpring);
	            } else { //y position depends on previous row
	                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH),
	                                     yPadSpring));
	            }
	            lastCons = cons;
	        }
	 
	        //Set the parent's size.
	        SpringLayout.Constraints pCons = layout.getConstraints(parent);
	        pCons.setConstraint(SpringLayout.SOUTH,
	                            Spring.sum(
	                                Spring.constant(yPad),
	                                lastCons.getConstraint(SpringLayout.SOUTH)));
	        pCons.setConstraint(SpringLayout.EAST,
	                            Spring.sum(
	                                Spring.constant(xPad),
	                                lastCons.getConstraint(SpringLayout.EAST)));
	    }
	}
}