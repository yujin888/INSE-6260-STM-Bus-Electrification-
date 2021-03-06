package FirstPackage;

import java.sql.Time;

import devices.*;

public class Schedule {
	

	public int AT_SOC;//At-SOC(KM)
	public String Charger_ID;
	public int slack;
	public Time Start_Time_quickCharg;
	public Time End_Time_quickCharge;
	public Time Start_Time_overnight;
	public Time End_Time_overnight;
	public int AT_SOC2;
	
	public String Charger_ID2;
	public int BT_SOC_trip;
	public String Trip_ID;
	public Time Start_Time_Trip;
	public Time End_Time_Trip;
	public Place reach;

	
	public Schedule() {
	this.AT_SOC=000;
	this.Charger_ID="";
	this.slack=0;
	Start_Time_quickCharg=null;
	End_Time_quickCharge=null;
	Start_Time_overnight=null;
	this.End_Time_overnight=null;
	AT_SOC2=000;
	
	Charger_ID2="";
	BT_SOC_trip=000;
	 Trip_ID="";
	 Start_Time_Trip=null;
	End_Time_Trip=null;
	this.reach=null;
	
	
	}
	
	
	
	
	
	
	
	
	
	
	


public String toString() {
	return this.AT_SOC+" Km%-10s"+this.Charger_ID+"\t\t"+this.Start_Time_quickCharg+"\t\t"+this.End_Time_quickCharge+"\t\t"+this.AT_SOC2+"Km\t\t"+this.Charger_ID2+"\t\t"+this.Start_Time_overnight+
			"\t\t"+this.End_Time_overnight+"\t\t"+this.BT_SOC_trip+"km\t\t"+this.Trip_ID+"\t\t"+this.Start_Time_Trip+"\t\t"+this.End_Time_Trip;
	
	
	
}
public void show() {
	System.out.printf("%-8s %-15s %-23s %-23s %-15s %-15s %-20s %-20s %-15s %-15s %-15s %-15s         \n",this.AT_SOC+"km",this.Charger_ID,this.Start_Time_quickCharg,this.End_Time_quickCharge,
			this.AT_SOC2+"Km", this.Charger_ID2,this.Start_Time_overnight,this.End_Time_overnight,this.BT_SOC_trip,this.Trip_ID,this.Start_Time_Trip,this.End_Time_Trip );
	
	
	
}

}