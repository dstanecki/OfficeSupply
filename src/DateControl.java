

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public final class DateControl extends JPanel implements ChangeListener {
	
	JSpinner day, month, year;
	
	public DateControl() {
		day = new JSpinner();	day.addChangeListener(this);
		month = new JSpinner(); month.addChangeListener(this);
		year = new JSpinner();	year.addChangeListener(this);
		
		this.setLayout(new FlowLayout());
		
		day.setPreferredSize(new Dimension(60, 25));
		month.setPreferredSize(new Dimension(60, 25));
		year.setPreferredSize(new Dimension(100, 25));
		
		this.add(month);
		this.add(new JLabel("/"));
		this.add(day);
		this.add(new JLabel("/"));
		this.add(year);
		
		day.addChangeListener(this);
		month.addChangeListener(this);
		year.addChangeListener(this);
		
		setCurrent();
		
	}
	
	public void setCurrent() {
		new GregorianCalendar();
		Calendar c = Calendar.getInstance();
		set(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
	}
	
	public Date getDate() { return new GregorianCalendar((Integer)year.getValue(), (Integer)month.getValue() - 1, (Integer)day.getValue()).getTime(); }

	public void stateChanged(ChangeEvent arg0) {
		int y = (int) year.getValue(), d = (int) day.getValue(), m = (int) month.getValue();
		if (validate(m, d, y)) return;
		if (m > 12) m = 12;
		if (m < 1) m = 1;
		if (d < 1) d = 1;
		int[] maxDay = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (((y % 4 == 0) && (y % 100 != 0))|| (y % 400 == 0)) if (d == 29 && m == 2) d = 28;
		if (d > maxDay[m-1]) d = maxDay[m-1];
			
		

		day.setValue(d);
		year.setValue(y);
		month.setValue(m);
	}
	
	private boolean validate(int m, int d, int y) {
		try {
			Calendar c = Calendar.getInstance();
			c.setLenient(false);
			c.set(Calendar.DAY_OF_MONTH, d);
			c.set(Calendar.YEAR, y);
			c.set(Calendar.MONTH, m - 1);
			c.getTime();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public void set(int m, int d, int y) {
		day.setValue(d);
		year.setValue(y);
		month.setValue(m);
		if (validate(m, d, y))  return;
		else stateChanged(null);
	}
}
