

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UserInterface {

	// package vis
	static JFrame main;
	static JTextArea output;
	static File current;
	static OrderManager om;
	static JTabbedPane tpane;
	static JLabel currentName;

	static JTextField pid, cid;
	static Date date, endDate;
	static volatile JLabel dateLbl;
	static JCheckBox rep;
	static JSpinner amt, per;
	static DateControl endDatePick;
	
	static JTextField del;
	
	static JTextArea listPnl;
	static JTextField cid2;
	
	static JTextArea product;
	static JSpinner pMonth, pYear;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		om = new OrderManager();

		// create main frame
		main = new JFrame();
		main.getContentPane().setLayout(new GridLayout(2, 1));
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		Container cpane = main.getContentPane();

		tpane = new JTabbedPane();
		cpane.add(tpane);
		
		output = new JTextArea();
		output.setEditable(false);
		output.setAutoscrolls(true);
		output.setBackground(Color.black);
		output.setForeground(Color.white);
		
		JScrollPane sp = new JScrollPane(output);
		
		cpane.add(sp);

		JPanel panes[] = new JPanel[5];
		for (int i = 0; i < 5; i++) {
			panes[i] = new JPanel();
			tpane.addTab(
					new String[]{"Import", "Add", "Delete", "View", "Output"}[i],
					panes[i]);
		}

		JPanel t; // temporary variable

		log("Starting...");

		// set up import pane
		t = panes[0];
		t.setLayout(new BorderLayout());

		JButton select = new JButton("Select file");
		select.addActionListener(new FileImportListener());

		currentName = new JLabel();

		t.add(select, BorderLayout.NORTH);
		t.add(currentName, BorderLayout.SOUTH);

		//set up add order pane
		t = panes[1];
		t.setLayout(new GridLayout(9, 2));
		t.add(new JLabel("Add a record"));
		t.add(new JLabel()); //dummy
		t.add(new JLabel("Customer ID"));
		cid = new JTextField();
		t.add(cid);
		t.add(new JLabel("Product ID"));
		pid = new JTextField();
		t.add(pid);
		amt  = new JSpinner();
		t.add(new JLabel("Amount of product"));
		t.add(amt);
		t.add(new JLabel("Date"));
		dateLbl = new JLabel();
		t.add(dateLbl);

		t.add(new JLabel("Repeating?"));
		rep = new JCheckBox();
		t.add(rep);
		rep.addActionListener(new RepListener());
		
		t.add(new JLabel("End Date"));
		/*
		 * endDatePick = new JTextField(); t.add(endDatePick);
		 * endDatePick.setEnabled(false);
		 */
		endDatePick = new DateControl();
		t.add(endDatePick);
		
		
		t.add(new JLabel("Period"));
		per = new JSpinner();
		t.add(per);
		per.setEnabled(false);
		
		JButton add = new JButton("Add");
		add.addActionListener(new AddListener());
		t.add(add);
		
		Timer tmr = new Timer();
		tmr.schedule(new TimerTask() {
			@Override
			public void run() {
				UserInterface.dateLbl.setText(
						(new SimpleDateFormat("dd/MM/yyyy")).format(new Date())
						);
			}
		}, 0L, 1000L);
		
		// delete tab
		t = panes[2];
		t.setLayout(new GridLayout(8,1));
		t.add(new JLabel("Enter ID:"));
		del = new JTextField();
		t.add(del);
		JButton b = new JButton("Delete");
		b.addActionListener(new DelListener());
		t.add(b);
		for (int i = 0; i < 5; i++) t.add(new JPanel());
		
		//view tab
		t = panes[3];
		t.setLayout(new BorderLayout());
		listPnl = new JTextArea();
		JScrollPane jsp = new JScrollPane(listPnl);
		t.add(jsp, BorderLayout.CENTER);
		cid2 = new JTextField();
		JButton lookup = new JButton("Lookup");
		JPanel p = new JPanel();
		listPnl.setEditable(false);
		lookup.addActionListener(new LookupListener());
		p.setLayout(new GridLayout(2,1));
		p.add(cid2);
		p.add(lookup);
		t.add(jsp, BorderLayout.CENTER);
		t.add(p, BorderLayout.NORTH);
		
		// output tab
		t = panes[4];
		t.setLayout(new BorderLayout());
		pMonth = new JSpinner();
		pYear = new JSpinner();
		pMonth.setPreferredSize(new Dimension(60, 25));
		pYear.setPreferredSize(new Dimension(100, 25));
		product = new JTextArea();
		product.setEditable(false);
		JPanel pnl = new JPanel(), pnl2 = new JPanel();
		pnl2.setLayout(new GridLayout(1,2));
		pnl.add(pMonth);
		pnl.add(new JLabel("/"));
		pnl.add(pYear);
		pnl2.add(pnl);
		JButton lbtn = new JButton("Lookup");
		pnl2.add(lbtn);
		lbtn.addActionListener(new PLookupListener());
		
		t.add(pnl2, BorderLayout.NORTH);
		t.add(product, BorderLayout.CENTER);
		

		main.setMinimumSize(new Dimension(600, 600));
		main.pack();
		main.setVisible(true); //show() depreciated
		
		log("Started");
	}

	public static void log(String s) {
		output.setText(output.getText() + "\n" + s);
		output.setAutoscrolls(true);
	}

	public static void tryImport() {
		log("Import begin");
		Importer i = new Importer();
		Thread t = new Thread(i);
		t.setDaemon(true);
		t.start();
	}

}

class PLookupListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Hashtable<String, Integer> di = UserInterface.om.productNeeded(
				(int)UserInterface.pMonth.getValue(),
				(int)UserInterface.pYear.getValue());
		if (di.size() == 0)
			UserInterface.product.setText("No records found");
		else {
			String s = "";
			for (String k : di.keySet()) s += k + ":" + di.get(k) + "\n";
			UserInterface.product.setText(s);
		}

	}
}

class Importer implements Runnable {
	public void run() {
		stat = UserInterface.om.importFile(UserInterface.current);
	}
	public boolean stat;
}

class DelListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			if (UserInterface.om.delete(Long.parseLong(UserInterface.del.getText())))
				UserInterface.log("Delete failed");
			else
				UserInterface.log("Delete of " + UserInterface.del.getText() + " succeeded");
		} catch (NumberFormatException e) {
			UserInterface.log("Delete failed");
		}
	}
}

class AddListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		UserInterface.log("Trying to add...");
		if (!UserInterface.rep.isSelected())
			UserInterface.om.create(
					UserInterface.cid.getText(),
					UserInterface.pid.getText(),
					new Date(),
					(Integer)UserInterface.amt.getValue());
		else
			try {
				UserInterface.om.create(
						UserInterface.cid.getText(),
						UserInterface.pid.getText(),
						new Date(),
						(Integer)UserInterface.amt.getValue(),
						(Integer)UserInterface.per.getValue(),
						/*(new SimpleDateFormat("M/dd/yyyy")).parse(UserInterface.endDatePick.getText())*/
						UserInterface.endDatePick.getDate());
			} catch (/*Parse*/Exception e) {
				UserInterface.log("Date parse failed");
				return;
			}
		UserInterface.log("Add succeeded");
	}
}

class LookupListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Order[] a = UserInterface.om.getAllByCID(UserInterface.cid2.getText());
		if (a.length == 0)
			UserInterface.listPnl.setText("None found");
		else {
			UserInterface.listPnl.setText("");
			Arrays.sort(a, new Comparator<Order>() {

				@Override
				public int compare(Order arg0, Order arg1) {
					return arg0.getDate().compareTo(arg1.getDate());
				}
				
			});
			for (int i = 0; i < a.length; i ++) {
				UserInterface.listPnl.setText(UserInterface.listPnl.getText() + "\n" + a[i]);
			}
		}
	}
}

class RepListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		UserInterface.endDatePick.setEnabled(UserInterface.rep.isSelected());
		UserInterface.per.setEnabled(UserInterface.rep.isSelected());
	}
}

class FileImportListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UserInterface.log("Selecting file...");
		JFileChooser fc = new JFileChooser();

		int ret = fc.showOpenDialog(UserInterface.main);

		if (ret == JFileChooser.APPROVE_OPTION) {
			UserInterface.log("File selected");
			UserInterface.log(fc.getSelectedFile().getAbsolutePath());
			UserInterface.current = fc.getSelectedFile();
			UserInterface.tryImport();
		} else {
			UserInterface.log("Import cancelled");
		}

	}
}