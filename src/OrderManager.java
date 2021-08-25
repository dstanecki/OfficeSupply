
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;

public class OrderManager {

	public LinkedList<Order> l = new LinkedList<Order>(); 

	public int create(String cid, String pid, Date date, int amt) {
		Order o = new Order(cid, pid, date, amt);
		l.add(o);
		UserInterface.log("Created " + o);
		return (int) (o.getID() % 2000000);
	}
	public int create(String cid, String pid, Date date, int amt, int per, Date end) {
		RepeatedOrder o = new RepeatedOrder(cid, pid, date, amt, end, per);
		l.add(o);
		UserInterface.log("Created " + o);
		return (int) (o.getID() % 2000000);
	}

	// true on success
	public boolean delete(long oid) {
		for (Order o : l) {
			if ( o.getID() == oid ) {
				l.remove(o);
				return true;
			}
		}
		return false;
	}

	public Order[] listAll() { return (Order[])l.toArray(); }

	public Order[] getAllByCID(String cid) {
		ArrayList<Order> t = new ArrayList<Order>();
		for (Order o : l) if (o.getCID().equals(cid)) t.add(o);
		Order[] oa = new Order[t.size()];
		for (int i = 0; i < oa.length; i++) oa[i] = t.get(i);
		return oa;
	}

	@SuppressWarnings("deprecation")
	public Hashtable<String, Integer> productNeeded(int month, int year) {
		Hashtable<String, Integer> d = new Hashtable<String, Integer>();
		Calendar c = new GregorianCalendar();
		for ( Order o : l) {
			c.setTime(o.getDate());
			if ( c.get(Calendar.MONTH) == month - 1 && 
					c.get(Calendar.YEAR) == year &&
					!(o instanceof RepeatedOrder)) {
				if ( d.get(o.getPID()) == null )
					d.put(o.getPID(), o.getAmnt());
				else
					d.put(o.getPID(), d.get(o.getPID()) + o.getAmnt());
			}
		}
		return d;
	}

	// return true on success
	public boolean importFile(File f) {
		BufferedReader reader;
		try {
			int i = 0;
			reader = new BufferedReader(new FileReader(f.getAbsolutePath()));
			String line = reader.readLine();
			SimpleDateFormat fmt = new SimpleDateFormat("M/dd/yyyy");
			while ((line = reader.readLine()) != null) {
				i++;
				UserInterface.currentName.setText(f.getName() + ": Read " + i);
				UserInterface.log("Read " + line);
				String[] s = line.split(",");
				Order o;
				if ( s[0].equals("O") ) {
					o = new Order(
							s[1],
							s[2],
							fmt.parse(s[3]),
							Integer.parseInt(s[4])
					);
				}
				else {
					o = new RepeatedOrder(
							s[1],
							s[2],
							fmt.parse(s[3]),
							Integer.parseInt(s[4]),
							fmt.parse(s[5]),
							Integer.parseInt(s[6])
					);
				}
				l.add(o);
				UserInterface.log("Created " + o);
			}
			reader.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
}
