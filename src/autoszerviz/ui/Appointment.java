package autoszerviz.ui;

import java.sql.Date;
import java.sql.Time;

public class Appointment {

	int id;
	Date date;
	Time time;
	String owner;
	String brand;
	
	
	public Appointment(int id, Date date, Time time, String owner, String brand) {
		this.id = id;
		this.date = date;
		this.time = time;
		this.owner = owner;
		this.brand = brand;
	}
	
	
	public String toString() {
		return id+"_"+date+"_"+time+"_"+owner+"_"+brand;
	}
	
}
