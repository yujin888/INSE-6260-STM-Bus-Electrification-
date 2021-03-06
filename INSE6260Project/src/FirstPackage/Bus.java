package FirstPackage;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import devices.*;

enum Place{west,east,onTheWay}
enum Status{charging,stop,movingToEast,movingToWest,beingReady,ready}



public class Bus extends Item {
	public static int IDB=0;
	public static Time travelingTime=new Time(01,00,00);
	public static double consuming=40;
	public static Time timeBeingReady=new Time(00,06,00);
	public Time startOfCharging,endOfCharging;
	public Time departureTime,nextDepartureTime,arrivalTime;
	public Time busready;
	public Time normalWaitingTime,EndingTime;
	public Route[] schedule;
	public Time start;
	public Battery battery;
	public Status status;
	public Schedule[]  trip_charging_schedule;
	public String id;
	public Place place;


	public double consumingperkm=consuming/Route.distance;
	public int waitingTime;



	public double limit=50;
	//Default constructor
	public Bus() {
		// IDB++;
		this.place=Place.west;
		this.id=IDB+""+this.place+this.start;
		this.battery=new Battery();
		this.departureTime=new Time(05,00,00);

		this.schedule=new Route[15];
		this.trip_charging_schedule=new Schedule[15];
		this.status=Status.stop;

	}


	//parameraize Constructor
	public Bus(Model m, double d,Place p,Time t) {
		IDB++;
		this.place=p;

		this.id=IDB+""+this.place+t;
		this.battery=new Battery(m);
		this.departureTime=new Time(05,00,00);
		this.trip_charging_schedule=new Schedule[15];
		this.schedule=new Route[15];
		this.status=Status.stop;
		this.price=d;
	}





	//parametrize constructor
	public Bus(Place s) {
		this.departureTime=new Time(04,55,00);
		this.place=s;
		this.status=Status.stop;   
	}
	public Bus(Bus b) {
		this.arrivalTime=b.arrivalTime;
		this.battery=b.battery;
		this.busready=b.busready;
		this.departureTime=b.departureTime;
		this.EndingTime=b.EndingTime;
		this.endOfCharging=b.endOfCharging;
		this.id=b.id;

	}

	public Time getDerpartureTime() {
		return this.departureTime;


	}

	//Method That make a bus ready
	public void beingReady() {

		this.busready=Driver.addTime(this.arrivalTime,timeBeingReady);
		this.status=Status.ready;


	}
	//method for traveling
	public void traveling(Route r) {


		this.battery.chargingStateBeforeTrip=this.battery.stateOfCharching;


		r.stateOfChargingBeforeTrip=this.battery.stateOfCharching;

		this.arrivalTime=Driver.addTime(this.departureTime,travelingTime);//traveling
		if(this.place==Place.east) {this.place=Place.west;

		}
		else if(this.place==Place.west) {this.place=Place.east;

		}

		this.battery.stateOfCharching=this.battery.stateOfCharching-consuming;
		r.stateOfChargingAfterTrip=this.battery.stateOfCharching;
		this.battery.chargingStateBeforeTrip=this.battery.stateOfCharching;
		r.bas=new Bus(this);

	}


	public void travelingEmpty() {


		Place temp = null;
		if(this.place.equals(Place.east))temp=Place.west;
		if(this.place.equals(Place.west))temp=Place.east;
		this.place=temp;

		this.battery.stateOfCharching-=consuming;
	}
	// method for charging
	public void quickCharging(Charger ch,Time ac,Route nextTrip) {
		double c;
		ArrayList<Time> terminalSchedule=new ArrayList<Time>();
		if(this.place.equals(Place.west))       terminalSchedule=Charger.scheduleWest;
		else if(this.place.equals(Place.east))	 terminalSchedule=Charger.scheduleEast;


		ch.startOfCharging=this.startOfCharging;

		ch.schedule.add(startOfCharging);


		terminalSchedule.add(startOfCharging);
		this.battery.chargingStateBeforeCharging=this.battery.stateOfCharching;


		this.endOfCharging=Driver.addTime(this.startOfCharging,ac);
		ch.endOfCharging=this.endOfCharging;

		ch.chargingSchedule.startSchedule.add(startOfCharging);
		ch.chargingSchedule.finishedSchedule.add(endOfCharging);
		ch.chargingSchedule.busCharged.add(this);
		ch.chargingSchedule.Next_Trip.add(nextTrip);
		/*Adding values to 2nd row*/





		c=(double)priodInMinute(this.endOfCharging,this.startOfCharging)/60;



		terminalSchedule.add(endOfCharging);


		this.battery.stateOfCharching=this.battery.stateOfCharching+(c*(ch.power));

		this.battery.chargingStateAftercharging=this.battery.stateOfCharching;

	}
	//method for overnight charging
	public void overNightCharging(Charger ch,Time ac) {
		double c;
		ArrayList<Time> terminalSchedule=new ArrayList<Time>();
		if(this.place.equals(Place.west))       terminalSchedule=Charger.scheduleWest;
		else if(this.place.equals(Place.east))	 terminalSchedule=Charger.scheduleEast;

		ch.startOfCharging=this.startOfCharging;
		ch.schedule.add(startOfCharging);
		terminalSchedule.add(startOfCharging);
		this.battery.chargingStateBeforeCharging=this.battery.stateOfCharching;
		Time realStart=ch.startOfCharging;
		ch.chargingSchedule.startSchedule.add(startOfCharging);
		while(true) {
			this.endOfCharging=Driver.addTime(this.startOfCharging,ac);
			ch.endOfCharging=this.endOfCharging;





			c=(double)priodInMinute(this.endOfCharging,this.startOfCharging)/60;



			terminalSchedule.add(endOfCharging);


			this.battery.stateOfCharching=this.battery.stateOfCharching+(c*(ch.power));

			if(this.battery.stateOfCharching>=this.battery.capacity) {
				this.battery.stateOfCharching=this.battery.capacity;
				break;


			}
			this.startOfCharging=this.endOfCharging;



		}
		this.startOfCharging=realStart;
		this.battery.chargingStateAftercharging=this.battery.stateOfCharching;

		ch.chargingSchedule.finishedSchedule.add(endOfCharging);
		ch.chargingSchedule.busCharged.add(this);



	}



	public static int priodInMinute(Time t1, Time t2) {
		Time t;
		int c=0;
		t=Driver.subtractTime(t1,t2);
		c=t.getHours()*60+t.getMinutes();
		return c;	 


	}

	public String toString() {
		String str="This bus is  "+this.status+ " in the "+this.place+" at time "+this.arrivalTime+" and the state of charging is "+this.battery.stateOfCharching;

		return str;


	}
	public String Tostring1() {

		String str="This bus is  "+this.status+ " in the "+this.place+" at time "+this.arrivalTime+" and the state of charging is "+this.battery.stateOfCharching;

		return str;


	}
}