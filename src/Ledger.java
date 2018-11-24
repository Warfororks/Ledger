import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class Ledger {
	
	Crypto grlc;
	Crypto btc;
	Crypto eth;
	Crypto nano;
	Crypto lbry;
	Crypto wab;
	Crypto doge;
	ArrayList<Crypto> list;
	JLabel empty1;
	JLabel empty2;
	JLabel grandTotal;
	double gTotal;
	
	Ledger() throws IOException {
		JFrame frame = new JFrame("Ledger v0.6");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 500);
		ImageIcon logo = new ImageIcon(Ledger.class.getResource("/logo.png"));
		frame.setIconImage(logo.getImage());

		init();
		
		Container contentPane = frame.getContentPane();
		SpringLayout layout = new SpringLayout();
		contentPane.setLayout(layout);
		
		for(int i = 0; i < list.size(); i++) {
			contentPane.add(list.get(i).getLabelLeft());
			contentPane.add(list.get(i).getTextField());
			contentPane.add(list.get(i).getLabelRight());
		}
		
		empty1 = new JLabel();
		empty2 = new JLabel("Grand Total:");
		contentPane.add(empty1);
		contentPane.add(empty2);
		contentPane.add(grandTotal);
		
		SpringUtilities s = new SpringUtilities();
		s.makeGrid(contentPane, 8, 3, 5, 5, 5, 5); //increment first number to increase rows
		
		frame.pack();
		frame.setVisible(true);
		
		for(int i = 0; i < list.size(); i++) {
			list.get(i).getTextField().getDocument().addDocumentListener(new DListen());
		}
		
		frame.addWindowListener(new WListen());
	}
	
	public void init() throws NumberFormatException, IOException {
		grlc = new Garlicoin();
		btc = new Bitcoin();
		eth = new Ethereum();
		doge = new Dogecoin();
		nano = new Nano();
		lbry = new Lbry();
		wab = new WABnetwork();
		
		list = new ArrayList<>();
		list.add(grlc);
		list.add(btc);
		list.add(eth);
		list.add(doge);
		list.add(nano);
		list.add(lbry);
		list.add(wab);
		
		Scanner inputFile = null;

		try {
			inputFile = new Scanner(new File("balance.txt"));
			int i = 0;
			String line;
			//while((line = reader.readLine()) != null) {
			while(inputFile.hasNextLine()) {
				line = inputFile.nextLine(); //parse entire line
				String strTicker = line.substring(0, 4).trim(); //get ticker from line - first three or four characters
				
				if(strTicker.equals(list.get(i).getTicker())) list.get(i).setBalance(Double.parseDouble(line.substring(4))); //set balance according to ticker
				
				list.get(i).setText(new Double(list.get(i).getBalance()).toString()); //to initialize balance in the app
				list.get(i).updateLabel(); //to initialize USDs in the app
				gTotal += Double.parseDouble(list.get(i).getUSD()); //to initialize a grand total in the app
				
				i++;
			}
			inputFile.close();
			//grandTotal = new JLabel(new DecimalFormat(".##").format(gTotal)); //for initial grand total
		}
		catch(FileNotFoundException e) {
			System.out.println("Balance.txt is not found, initializing balances to 0");
			for(int i = 0; i < list.size(); i++) {
				list.get(i).setBalance(0);
			}
		}
		catch(NullPointerException e) {
			System.out.println("Some sort of error");
		}
		finally {
			grandTotal = new JLabel(new DecimalFormat(".##").format(gTotal)); //for initial grand total
		}
	}
	
	public void updateBalance() {
		try {
			gTotal = 0;
			for(int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getTextField().getText());
				list.get(i).setBalance(Double.parseDouble(list.get(i).getTextField().getText()));
				list.get(i).updateLabel();
				gTotal += Double.parseDouble(list.get(i).getUSD());
			}
			grandTotal.setText(new DecimalFormat(".##").format(gTotal));
		}
		catch(NumberFormatException e) {
			
		}
	}
	
	public void writeToBalance() { //called after the app is closed
		String newLine = "";
		PrintWriter outputFile = null;
		
		try {
			outputFile = new PrintWriter(new File("balance.txt"));
			
			for(int i = 0; i < list.size(); i++) {
				newLine = list.get(i).getTicker() + " " + list.get(i).getBalance();
				outputFile.println(newLine);
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("Balance.txt is not found, cannot rewrite");
		}
		outputFile.close();
	}
	
	private class WListen extends WindowAdapter {
		public void windowClosed(WindowEvent e) { //when clicking the X button
			System.out.println("closed");
			writeToBalance(); //updates balances.txt
			System.exit(0); //quits program
		}
	}
	
	private class DListen implements DocumentListener {
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
