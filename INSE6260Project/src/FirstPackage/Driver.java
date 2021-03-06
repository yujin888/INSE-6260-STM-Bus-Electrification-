package FirstPackage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Driver {
	static Time ac=new Time(00,01,00);
	static Time bc=new Time(00,30,00);
	static Time start=new Time(04,55,00);
	/*
	 * this is for adding time
	 * 	 * */
	public static Time addTime(Time t1,Time t2) {
		@SuppressWarnings("deprecation")
		Time t=new Time(00,00,00);
		@SuppressWarnings("deprecation")
		int min=t1.getMinutes()+t2.getMinutes();
		int h=0;
		if(min>=60) {
			min-=60;
			h++;
		}
		t.setMinutes(min);
		t.setHours(t1.getHours()+t2.getHours()+h);
		return t;
	}

	public static int lookingInSchedule(Route[] r,Bus b) {
		int i=r.length-1;
		int st=-1;
		while(i>=0 && b.busready.before(r[r.length-1].time))
			//for(int i=r.length-1;i>=r.length;i--)
		{
						if((r[i].time.before(b.busready) && r[i].place.equals(b.place))||(r[i].time.equals(b.busready) && r[i].place.equals(b.place))) {

				st=i;break;

			}else  {
		
			}

			i--;
		}


		return st;
	}



	/*
	 * this is for defining next departure
	 * */





	public static int nextDeparture(Route[] r,Bus b) {
		//		int positionInSchedule=lookingInSchedule(r,b);

		int s,p=-1;

		Time t=b.busready;



		s=lookingInSchedule(r,b);

		if(s!=-1 && s<r.length)
			for(int i=s;i<r.length ;i++) {
			

				if((r[i].time.after(t)|| r[i].time.equals(t)) && r[i].status.equals(RouteStatus.empty)) {

					t=r[i].time;
					r[i].status.equals(RouteStatus.busy);
					p=i;
					r[i].makeItBusy();
					r[i].bas=b;
					//				
					break;
				}
				//				
			}
		//		
		return p;
	}

	public static int defineFirstEmpty(Route[] r) {
		int k=-1;


		for(int i=0;i<r.length;i++) {
		
			if(r[i].status.equals(RouteStatus.empty)) {
				k=i;
				break;
			}
		}
		
		return k;
	}



	public static Scanner sc=new Scanner(System.in);
	public static int countingStation(String s) throws IOException {
		Scanner scF=new Scanner(new FileInputStream(s));
		String temp="";
		int count=0;
		while(scF.hasNext()) {
			temp=scF.nextLine();
			count++;
		}


		//		System.out.println(count);
		return count;
	}








	public static Time subtractTime(Time t1,Time t2) {
		@SuppressWarnings("deprecation")
		Time t=new Time(00,00,00);
		@SuppressWarnings("deprecation")
		int min=t1.getMinutes()-t2.getMinutes();
		int h=0;
		if(min<0) {
			min+=60;
			h--;
		}

		t.setMinutes(min);
		t.setHours(t1.getHours()-t2.getHours()+h);
		return t;
	}



	public static void showSchedule(Route[] r) {
		for(int i=0;i<r.length;i++)
			System.out.println(r[i]);
	}
	@SuppressWarnings("resource")
	public static void writeSchedule(Route[] r,PrintWriter pw) throws FileNotFoundException {
		pw=new PrintWriter(new FileOutputStream("BusSchedule.txt"));
		for(int i=0;i<r.length;i++)
			pw.println(r[i]);
	}

	public static int tour(Route[] W,Route[] E,Bus b,ArrayList<Charger> cWL ,ArrayList<Charger> cEL,ArrayList<Charger>  sCWL,ArrayList<Charger> sCEL ) {
		Route[] terminal=new Route[1];;
		ArrayList<Charger> chargerTerminalList = new ArrayList<Charger>();

		int st=-1;
		for(int i=0;i<b.schedule.length;i++) {
			b.trip_charging_schedule[i]=new Schedule();
			b.beingReady();//time the bus become ready


			if(b.place==Place.west)terminal=W;
			else if(b.place==Place.east) terminal=E;
			if(b.place==Place.west)chargerTerminalList=cWL;
			else if(b.place==Place.east) chargerTerminalList=cEL;
		
			st=nextDeparture(terminal,b);
		
			if(st==-1) {
				String temp="";
			
				b.travelingEmpty();
				
				if(b.place==Place.west)terminal=W;
				if(b.place==Place.east)terminal=E;
				
				b.beingReady();
				b.arrivalTime=Driver.addTime(b.arrivalTime,Bus.travelingTime);//traveling

				b.beingReady();
				st=lookingInSchedule(terminal,b);		

				st=nextDeparture(terminal,b);
				//				System.out.println(st);
				if(st==-1)		break;



			}

			
			b.nextDepartureTime=terminal[st].time;

			b.waitingTime=Bus.priodInMinute( b.nextDepartureTime,b.arrivalTime);

			if(b.waitingTime>20)
			{
				b.startOfCharging=b.arrivalTime;
				b.normalWaitingTime=subtractTime(b.nextDepartureTime,b.arrivalTime);
			
				b.trip_charging_schedule[i].AT_SOC=(int) (b.battery.stateOfCharching/b.consumingperkm);

				Route nextTrip=terminal[st];
				b.quickCharging(chargerTerminalList.get(chargerTerminalList.size()-1),ac,nextTrip);
				b.trip_charging_schedule[i].Start_Time_quickCharg=b.startOfCharging;
				b.trip_charging_schedule[i].End_Time_quickCharge=b.endOfCharging;
				b.trip_charging_schedule[i].Charger_ID=	chargerTerminalList.get(0).getId();
			
				b.arrivalTime=b.endOfCharging;






			}

			//overnightCharging
			if(b.battery.stateOfCharching<90 ) {
				terminal[st].status=RouteStatus.empty;
				b.startOfCharging=b.arrivalTime;
				b.normalWaitingTime=new Time(01,00,00);
				ArrayList<Charger> slowTerminal=new ArrayList<Charger>();
				if(b.place==Place.west) slowTerminal=sCWL;


				else if(b.place==Place.east)slowTerminal=sCWL;
				//for the overKnight charging
				Charger c=new Charger(Charger.overnight,b.place);


				c.place=b.place;
				if(c.place.equals(Place.west)) Charger.westNumberOfOverNightCharging++;
				else Charger.eastNumberOfOverNightCharging++;

				slowTerminal.add(c);

		



				b.trip_charging_schedule[i].AT_SOC2=(int) (b.battery.stateOfCharching/b.consumingperkm);





				b.overNightCharging(c,bc);
				b.EndingTime=b.endOfCharging;

				b.trip_charging_schedule[i].Start_Time_overnight=b.startOfCharging;
				b.trip_charging_schedule[i].End_Time_overnight=b.endOfCharging;
				b.trip_charging_schedule[i].Charger_ID2=c.getId();


				//				System.out.println("the end OF charging is"+b.endOfCharging);
				b.arrivalTime=b.endOfCharging;
				//				System.out.println(b.arrivalTime+""+Driver.start);
				//				System.out.println(b.arrivalTime.before(start));
				if(b.arrivalTime.before(start))break;


				b.beingReady();
				st=nextDeparture(terminal,b);

				//				System.out.println(st);
				if(st==-1)		break;

				terminal[st].bas.battery.chargingStateBeforeCharging=b.battery.chargingStateBeforeTrip;
				b.nextDepartureTime=terminal[st].time;
				Route nextTrip2=terminal[st];
				c.chargingSchedule.Next_Trip.add(nextTrip2);
				//				break;
			}
			
			b.schedule[i]=terminal[st];
			//			b.trip_charging_schedule[i]=new Schedule();
			b.departureTime=terminal[st].time;
			b.trip_charging_schedule[i].Start_Time_Trip=terminal[st].time;
			b.trip_charging_schedule[i].BT_SOC_trip=(int)(b.battery.stateOfCharching/b.consumingperkm);
			b.trip_charging_schedule[i].Trip_ID=terminal[st].Id;
			if (terminal[st].place.equals(Place.west)) b.trip_charging_schedule[i].reach=Place.east;
			if (terminal[st].place.equals(Place.east)) b.trip_charging_schedule[i].reach=Place.west;





			terminal[st].bas.battery.chargingStateBeforeTrip=b.battery.stateOfCharching;
			b.traveling(terminal[st]);

			b.trip_charging_schedule[i].End_Time_Trip=b.arrivalTime;







			terminal[st].bas.battery.chargingStateAfterTrip=b.battery.stateOfCharching;
	
		}




		return st;


	}


	static int numberOfBuyingBus=0;

	@SuppressWarnings("deprecation")
	//	public static void main(String[] args) throws IOException {
	public static void main(int MB,Model m, double price,Manufacture mf) throws IOException{
		numberOfBuyingBus=0;
	


		/*
		 * we are reading the schedule
		 * 
		 * */
		//		System.out.print("numberOfRoutsWest: ");
		int numberOfRoutsWest=countingStation("ScheduleWest.txt");//schedule of west
		//		System.out.print("numberOfRoutsEast: ");
		int numberOfRoutsEast=countingStation("ScheduleEast.txt");//scheduele of east

		Route[] scheduleWest=new Route[numberOfRoutsWest];
		Route[] scheduleEast=new Route[numberOfRoutsEast];

		String s="ScheduleWest.txt";
		Scanner scF=new Scanner(new FileInputStream(s));
		String temp;
		int count=0;

		int ii=0;


		while(scF.hasNext()) {

			StringTokenizer st1 = new StringTokenizer(scF.nextLine(), ",");
			Time tStart=new Time(04,00,00);
			tStart.setHours(Integer.parseInt(st1.nextToken()));
			tStart.setMinutes(Integer.parseInt(st1.nextToken()));
			tStart.setSeconds(Integer.parseInt(st1.nextToken()));

			scheduleWest[ii]=new Route(tStart,Direction.east);
			//			scheduleWest[ii].time.setHours(Integer.parseInt(st1.nextToken()));
			//			scheduleWest[ii].time.setMinutes(Integer.parseInt(st1.nextToken()));
			//			scheduleWest[ii].time.setSeconds(Integer.parseInt(st1.nextToken()));
			//			System.out.println(scheduleWest[ii].time);
			ii++;
		}

		scF.close();

		s="ScheduleEast.txt";
		scF=new Scanner(new FileInputStream(s));



		ii=0;


		while(scF.hasNext()) {
			StringTokenizer st1 = new StringTokenizer(scF.nextLine(), ",");

			Time tStart=new Time(04,00,00);
			tStart.setHours(Integer.parseInt(st1.nextToken()));
			tStart.setMinutes(Integer.parseInt(st1.nextToken()));
			tStart.setSeconds(Integer.parseInt(st1.nextToken()));
			scheduleEast[ii]=new Route(tStart,Direction.west);

			//System.out.println(scheduleEast[ii].time);
			ii++;
		}
		 numberOfBuyingBus=0;




		scF.close();



		//end of reading schedule













		//		showSchedule(scheduleWest);
		//		showSchedule(scheduleEast);	

		Route[] terminal;
		terminal=scheduleWest;




		ArrayList<Charger> chargerWestList = new ArrayList<Charger>();
		ArrayList<Charger> chargerEastList = new ArrayList<Charger>();
		ArrayList<Charger> slowChargerWestList = new ArrayList<Charger>();
		ArrayList<Charger> slowChargerEastList = new ArrayList<Charger>();















		int thereIsEmptyw=0,thereIsEmptyE=0;
		//		System.out.println("Please input the maximum number of the buses that you want to buy");
		//		int MB=sc.nextInt();
		Bus[] bus=new Bus[MB+1];	
		Bus exBus=new Bus();



		int k=-1;
		/*
		 * 
		 * Here we start to bus bus
		 * */
		scF.close();
		Place pl=Place.west;
		Charger c1=new Charger(Charger.fast,pl);

		c1.place=Place.west;
		Charger.westNumberOfFastCharging++;
		chargerWestList.add(c1);
		Place pl2=Place.east;
		Charger c2=new Charger(Charger.fast,pl2);
		Charger.eastNumberOfFastCharging++;
		c2.place=Place.east;
		chargerEastList.add(c2);

		//here we start to go
		while(defineFirstEmpty(scheduleWest)!=-1 && k<(MB-1))//we start from west
		{
			thereIsEmptyw=defineFirstEmpty(scheduleWest);

			if(defineFirstEmpty(scheduleWest)!=-1) {

				
				k++;//the number of index added as 1
						
				Time timeOfStart=scheduleWest[thereIsEmptyw].time;
//				if(numberOfBuyingBus>=MB)break;
				bus[k]=new Bus(m,price,Place.west,timeOfStart);
				numberOfBuyingBus++;//we add the number of bus 
					
				bus[k].place=scheduleWest[thereIsEmptyw].place;//we choose a terminal schedule
				bus[k].busready=scheduleWest[thereIsEmptyw].time;//we estimate the time that bus can be ready
				//				System.out.println(bus[k].busready);
				bus[k].arrivalTime=	subtractTime(bus[k].busready,bus[k].timeBeingReady);//we defind the time the bus should arrive

						
				int st;
				bus[k].beingReady();//the bus becomes ready
				if(bus[k].place==Place.west)terminal=scheduleWest;
				else if(bus[k].place==Place.east)terminal=scheduleEast;
				st=lookingInSchedule(terminal,bus[k]);//we look at the schedule to find a route to go
				//this is traveling of one bus until the end of schedule


				//			System.out.println(bus[k].battery.Id);
				st=tour(scheduleWest,scheduleEast,bus[k],chargerWestList,chargerEastList,slowChargerWestList,slowChargerEastList);



				//				showSchedule(bus[k].schedule);

			}
			if(defineFirstEmpty(scheduleEast)!=-1) {//we start from east
				thereIsEmptyE=defineFirstEmpty(scheduleEast);
				//				System.out.println("Please allow us to buy a bus from east: ");
				//				String d=sc.nextLine();
				k++;
				Time timeOfStart=scheduleEast[thereIsEmptyE].time;
//				if(numberOfBuyingBus>=MB)break;
				bus[k]=new Bus(m,price,Place.east,timeOfStart);

				numberOfBuyingBus++;
				//				System.out.println("Until now we have bought "+numberOfBuyingBus+" bus(es)");
				bus[k].place=scheduleEast[thereIsEmptyE].place;
				bus[k].busready=scheduleEast[thereIsEmptyE].time;
				//				System.out.println(bus[k].busready);

				//				System.out.println(bus[k].busready+" "+bus[k].timeBeingReady);
				bus[k].arrivalTime=	subtractTime(bus[k].busready,bus[k].timeBeingReady);

				if(bus[k].place==Place.west)terminal=scheduleWest;

				if(bus[k].place==Place.east)terminal=scheduleEast;
				int st;
				//=lookingInSchedule(terminal,bus[k]);

				st=tour(scheduleWest,scheduleEast,bus[k],chargerWestList,chargerEastList,slowChargerWestList,slowChargerEastList);

				//				System.out.println("the bus is in"+bus[k].place+"in termianl"+terminal[0].place );
				//				if(bus[k].place.equals(Place.east))



				//				showSchedule(bus[k].schedule);

			}


		}















		ArrayList<Charger> totalCharger=new ArrayList<Charger>();
		totalCharger.add(c1);
		totalCharger.add(c2);
		//		for(int i=0;i<slowChargerWestList.size();i++) {
		//		totalCharger.add(slowChargerWestList.get(i));
		//		}
		//		for(int i=0;i<slowChargerEastList.size();i++) {
		//			totalCharger.add(slowChargerWestList.get(i));
		//		}				

		//		for(int i=0;i<slowChargerWestList.size();i++) {
		//		totalCharger.get(i).ShowMainChargerSchedule();
		//		}				


		//		for(int i=0;i<totalCharger.size();i++)
		//			for(int j=0;j<totalCharger.get(i).busCharged.size();j++ ) {
		//				System.out.println(totalCharger.get(i).chargingSchedule.busCharged.get(j));
		//				
		//			}
		//		
		//
		//		

		//	System.out.println("This is the schedule for the"+scheduleWest[1].place);
		//		
		//		for(int s1=0;s1<scheduleWest.length;s1++) {
		//			System.out.println(scheduleWest[s1]);
		//			
		//		}
		//		



		//		PrintWriter pw=new PrintWriter(new FileOutputStream("C:\\Users\\danie\\Documents\\INSE6260Project\\STM\\BusSchedule.txt"));
		//		for(int j=0;j<k+1;j++) {
		//		pw.println("this is the schedule of bus "+bus[j].id);
		//		
		//		for(int i=0;i<bus[j].schedule.length;i++) {
		//			
		//			pw.println(bus[j].schedule[i]);
		//		}
		//			
		//		}
		//		pw.close();





		//exporting information for total buses schedule
		String busSchedulefile="C:\\Users\\danie\\Documents\\INSE6260Project\\STM\\("+m+mf+")BusSchedule.txt";
		PrintWriter pw=new PrintWriter(new FileOutputStream(busSchedulefile));
		int jj=0;
		while(bus[jj]!=null&&jj<MB) {
			
			pw.println("");
			pw.println("bus ID: "+bus[jj].id);

			pw.printf("%-8s %-15s %-23s %-23s %-15s %-15s %-20s %-20s %-15s %-15s %-15s %-15s %-5s         \n","AT_SOC"+"km","Charger_ID","Start_Time_quickCharg","End_Time_quickCharge",
					"AT_SOC(ON)", "Charger_ID2","Start_Time_overnight","End_Time_overnight","BT_SOC_trip","Trip_ID","Start_Time_Trip","End_Time_Trip","Reach" );

			int i=0;
			while(bus[jj].trip_charging_schedule[i]!=null) {
				Schedule sd=bus[jj].trip_charging_schedule[i];
				pw.printf("%-8s %-15s %-23s %-23s %-15s %-15s %-20s %-20s %-15s %-15s %-15s %-15s %-5s        \n",sd.AT_SOC+"km",sd.Charger_ID,sd.Start_Time_quickCharg,sd.End_Time_quickCharge,sd.AT_SOC2+"Km", sd.Charger_ID2,sd.Start_Time_overnight,sd.End_Time_overnight,sd.BT_SOC_trip,sd.Trip_ID,sd.Start_Time_Trip,sd.End_Time_Trip,sd.reach);


				i+=1;

			}
			jj+=1;
		}


		pw.close();
























		String schedulename="C:\\Users\\danie\\Documents\\INSE6260Project\\STM\\Schedule("+m+mf+").txt";

		PrintWriter pw1=new PrintWriter(new FileOutputStream(schedulename));
		pw1.println("This is the schedule for the"+scheduleWest[1].place);
		for(int s1=0;s1<scheduleWest.length;s1++) {
			pw1.println(scheduleWest[s1]);
		}
		pw1.println("This is the schedule for the"+scheduleWest[1].place);
		for(int s1=0;s1<scheduleEast.length;s1++) {

			pw1.println(scheduleEast[s1]);
		}
		pw1.close();






		String chagerName="C:\\Users\\danie\\Documents\\INSE6260Project\\STM\\Charger("+m+mf+").txt";
		PrintWriter pw2=new PrintWriter(new FileOutputStream(chagerName));

		pw2.println("  Charger "+ c1.Id);
		pw2.println("bus  \t\t"+"Start\t"+"\tfinish"+"\t\tNext Trip");

		for(int i=0;i<c1.chargingSchedule.busCharged.size();i++) { 

			pw2.println(c1.chargingSchedule.busCharged.get(i).id+" "
					+ "\t"+c1.chargingSchedule.startSchedule.get(i)+"\t "+c1.chargingSchedule.finishedSchedule.get(i)+"\t"+c1.chargingSchedule.Next_Trip.get(i).Id);}

		pw2.println("  Charger "+ c2.Id);
		pw2.println("bus  \t\t"+"Start\t"+"\tfinish"+"\t\tNext Trip");
		for(int i=0;i<c2.chargingSchedule.busCharged.size();i++) { 
			pw2.println(c2.chargingSchedule.busCharged.get(i).id+" "
					+ "\t"+c2.chargingSchedule.startSchedule.get(i)+"\t "+c2.chargingSchedule.finishedSchedule.get(i)+"\t"+c2.chargingSchedule.Next_Trip.get(i).Id);}

		for(int j=0;j<slowChargerWestList.size();j++) {
			pw2.println("  Charger "+ slowChargerWestList.get(j).Id);
			pw2.println("bus  \t\t"+"Start\t"+"\tfinish");
			for(int i=0;i<slowChargerWestList.get(j).chargingSchedule.busCharged.size();i++) { 
				pw2.println(slowChargerWestList.get(j).chargingSchedule.busCharged.get(i).id+" "
						+ "\t"+slowChargerWestList.get(j).chargingSchedule.startSchedule.get(i)+"\t "+slowChargerWestList.get(j).chargingSchedule.finishedSchedule.get(i));}

		}
		for(int j=0;j<slowChargerEastList.size();j++) {
			pw2.println("  Charger "+ slowChargerEastList.get(j).Id);
			pw2.println("bus  \t\t"+"Start\t"+"\tfinish");
			for(int i=0;i<slowChargerEastList.get(j).chargingSchedule.busCharged.size();i++) { 
				pw2.println(slowChargerEastList.get(j).chargingSchedule.busCharged.get(i).id+" "
						+ "\t"+slowChargerEastList.get(j).chargingSchedule.startSchedule.get(i)+"\t "+slowChargerEastList.get(j).chargingSchedule.finishedSchedule.get(i));}

		}



		pw2.close();


















		//		for(int i=0;i<k+1;i++) {
		//			System.out.println("this is the schedule of bus"+bus[i].id);
		//			showSchedule(bus[i].schedule);
		//        
		//
		//
		//		};
		//		showSchedule(scheduleWest);
		//		showSchedule(scheduleEast);
//		System.out.println("You have to buy "+ numberOfBuyingBus+" bus(es)");
//		//		System.out.println("You have to buy "+ (k+1)+" bus(es)");
//		System.out.println("You have to buy "+ Charger.westNumberOfFastCharging+" Fast Charger in West");
//		System.out.println("You have to buy "+ Charger.eastNumberOfFastCharging+" Fast Charger in East");
//		System.out.println("You have to buy "+ Charger.westNumberOfOverNightCharging+" overnight Chargers in West");
//		System.out.println("You have to buy "+ Charger.eastNumberOfOverNightCharging+" overnight Chargers in East");

		//
		//		for(int i=0;i<chargerEastList.size();i++) {
		//			System.out.println(  chargerEastList.get(i));
		//
		//		}
		//		for(int i=0;i<chargerEastList.size();i++) {
		//			System.out.println("These are for charger"+ chargerEastList.get(i));
		//			for(int j=0;j<chargerEastList.get(i).schedule.size();j++) {
		//				System.out.println("we are looking for the schedule");
		//				System.out.println(chargerEastList.get(i).schedule.get(j));
		//			}}
		//		for(int i=0;i<chargerEastList.size();i++) {
		//			System.out.println("These are for charger"+ chargerEastList.get(i));
		//			for(int j=0;j<chargerWestList.get(i).schedule.size();j++) {
		//				System.out.println("we are looking for the schedule");
		//				System.out.println(chargerWestList.get(i).schedule.get(j));
		//			}}

		//		System.out.println(chargerWestList.size());
		//		System.out.println(chargerEastList.size());


		//		for(int i=0;i<Charger.scheduleEast.size();i++) {
		//			System.out.println( Charger.scheduleEast.get(i));
		//
		//		}
		//		for(int i=0;i<Charger.scheduleEast.size();i++) {
		//			System.out.println( Charger.scheduleEast.get(i));
		//
		//		}
		//		for(int i=0;i<slowChargerWestList.size();i++) {
		//			System.out.println(slowChargerWestList.get(i));
		//		}

		
		 totalPrice=numberOfBuyingBus * bus[0].price+ (Charger.eastNumberOfFastCharging+Charger.westNumberOfFastCharging)*Charger.priceOfFastCharger+
				(Charger.eastNumberOfOverNightCharging+Charger.westNumberOfOverNightCharging)*Charger.priceOfOvernightCharger;

		;
		//System.out.println("The total price is: "+totalPriceOfBus);







		/*
c1.ShowMainChargerSchedule();
c2.ShowMainChargerSchedule();
		 */
		//for(int i=0;i<slowChargerWestList.size();i++) {
		//	
		//slowChargerWestList.get(i).ShowMainChargerSchedule();
		//}
		//for(int i=0;i<slowChargerEastList.size();i++) {
		//slowChargerWestList.get(i).ShowMainChargerSchedule();
		//}



		int j=0;
		/*
while(bus[j]!=null) {
	System.out.println("");
	System.out.println("bus ID: "+bus[j].id);
	System.out.println("");
	System.out.printf("%-8s %-15s %-23s %-23s %-15s %-15s %-20s %-20s %-15s %-15s %-15s %-15s         \n","AT_SOC"+"km","Charger_ID","Start_Time_quickCharg","End_Time_quickCharge",
			"AT_SOC(ON)", "Charger_ID2","Start_Time_overnight","End_Time_overnight","BT_SOC_trip","Trip_ID","Start_Time_Trip","End_Time_Trip" );

	int i=0;
while(bus[j].trip_charging_schedule[i]!=null) {

	bus[j].trip_charging_schedule[i].show();
	i+=1;

}
j+=1;
}

		 */













	}
	static double totalPrice;
	}