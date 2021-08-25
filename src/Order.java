

import java.util.Date;

public class Order {

	public Order(String cid, String pid, Date date, int amt) {
		this.cid = cid; 
		this.pid = pid; 
		this.date = date;
		this.amt = amt;
		this.id = System.currentTimeMillis(); 
	}

	private long id;
	private int amt;
	private Date date;
	private String cid, pid;

	public long getID() { return id; }
	public String getCID() { return cid; }
	public String getPID() { return pid; }
	public Date getDate() { return date; }
	public int getAmnt() { return amt; }

	
	@Override
	public String toString() { 
		return "Order by customer " + cid + " for " + amt + " of product " + pid +
				" on date " + date;
	}
}