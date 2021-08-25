

import java.util.Date;

public class RepeatedOrder extends Order {

	private Date end;
	private int per;

	
	public RepeatedOrder(String cid, String pid, Date date, int amt, Date end, int per) {
		super(cid, pid, date, amt);
		this.end = end;
		this.per = per;
	}

	public Date getEnd() {
		return end;
	}

	public int getPeriod() {
		return per;
	}

	
	@Override
	public String toString() {
		return super.toString() + ", repeating every " + per + " days until " + end;
	}

}
