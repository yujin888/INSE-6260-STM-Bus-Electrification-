package FirstPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
























//class ExitListener implements ActionListener
//{
//	public void actionPerformed1(ActionEvent e)
//	{
//	
//		System.out.println("Please input the manufacture of the charger: ABB/HELIOX ");
//		String sfc="ABB";
//		Scanner sc = null;
//		sfc=sc.next();
//		
//	}
//	
//	
//	
//	public void actionPerformed(ActionEvent e)
//	{
//	
//		System.exit(0);
//		
//	}
//	
//	
//	
//}




public class ExtendedDriver extends JApplet  {
	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {
	System.out.println("");
		String sfc;
		String priceString;
		String fastPrice;
		String kindOfBattery;
		String busPrice;
		
		
		
		double number1;
		double number2;

		//obtain first number from user
		sfc=JOptionPane.showInputDialog("Please input the manufacture of the charger: ABB/HELIOX");
		priceString=JOptionPane.showInputDialog("Please input the price of over night charger:");
		fastPrice=JOptionPane.showInputDialog("Please input the price of fast charger:");
		kindOfBattery=JOptionPane.showInputDialog("Please input  the kind of the battery:small(294kw),big(394kw):");
		busPrice=JOptionPane.showInputDialog("Please input the price of Bus:");
		
		Manufacture mf=Manufacture.ABB;
		if(sfc.contentEquals("ABB"))mf=Manufacture.ABB;
		else if(sfc.contentEquals("HELIOX")) mf=Manufacture.HELIOX;
		else  System.out.println("You are wrong");
		if(mf.equals(Manufacture.ABB)) {
			Charger.overnight=ModelCharger.DC50KW;
			Charger.fast=ModelCharger.OC450kw;}
		else if(mf.equals(Manufacture.HELIOX)) {
			Charger.overnight=ModelCharger.HVC100PUS;
			Charger.fast=ModelCharger.HVC300PD;}

		
		Charger.priceOfOvernightCharger=Double.parseDouble(priceString);
	
		Charger.priceOfFastCharger=Double.parseDouble(fastPrice);
		




		Model m=Model.Big;
		if(kindOfBattery.contentEquals("small")) m=Model.small;
		else if(kindOfBattery.contentEquals("big")) m=Model.Big;
		else System.out.println("you are wrong");
//		System.out.println("Please input the price of Bus");
		double price=Double.parseDouble(priceString);
		
		
	
	
	
	
	
	
	
	
	Driver.main( 30,m ,  price, mf);
	
	
	
	
	String str="You have to buy "+ Driver.numberOfBuyingBus+" bus(es)"
			+"\nYou have to buy "+ Charger.westNumberOfFastCharging+" Fast Charger in West"
			+"\nYou have to buy "+ Charger.eastNumberOfFastCharging+" Fast Charger in East"
			+"\nYou have to buy "+ Charger.westNumberOfOverNightCharging+" overnight Chargers in West"
			+"\nYou have to buy "+ Charger.eastNumberOfOverNightCharging+" overnight Chargers in East"
			+"\nYou have to spend"+Driver.totalPrice+"$"
			+"\nNow you can take the schedules of bus and chargers from PC";
	
	
	
	
	
//	Graphics g = null ;
//	g.create();
//	g.drawRect(15, 10, 270, 20);
	
//	
	
	
	
	  JOptionPane.showMessageDialog(null, str, "This is the Final Results",
		        JOptionPane.INFORMATION_MESSAGE);
		  
	
	
	
	
	
	
		System.out.println("The complixity of system is:"+System.currentTimeMillis());

	
	}}

			
			
			
			
			
			
			
			
			
			
	
	
	
	
//	
//	
//	System.out.println("You have to buy "+ Driver.numberOfBuyingBus+" bus(es)");
//	//		System.out.println("You have to buy "+ (k+1)+" bus(es)");
//	System.out.println("You have to buy "+ Charger.westNumberOfFastCharging+" Fast Charger in West");
//	System.out.println("You have to buy "+ Charger.eastNumberOfFastCharging+" Fast Charger in East");
//	System.out.println("You have to buy "+ Charger.westNumberOfOverNightCharging+" overnight Chargers in West");
//	System.out.println("You have to buy "+ Charger.eastNumberOfOverNightCharging+" overnight Chargers in East");
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	}
//	
	
	
//	
//	public static final int BUTTON_WIDTH = 300;
//	public static final int BUTTON_HIGHT = 200;
//	
//	
//	
//	
//	static Scanner sc=new Scanner(System.in);
//	public static void main(String[] args) throws IOException, InterruptedException {
//		
//		JFrame myWindow = new JFrame("STM SOFTWARE");
//		myWindow.setSize(BUTTON_WIDTH, BUTTON_HIGHT);
//	
//
//		// Disable the default close button of the frame (this is the X button usually located 
//				// on the top right corner of the frame) 
//				myWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//				JButton runButton = new JButton("Click Here to Run");
//				
//
//				JButton exitButton = new JButton("Click Here to Terminate the Program.");
//				
//				
//				runButton.setBackground(new Color(200,150,150));
//				exitButton.setBackground(new Color(200,150,150));
//				
//				
//				// Now create the listener, which will actually handle the event
//				ExitListener endLsnr = new ExitListener();
//				
//				// Relate the button to that listener
//			
//				exitButton.addActionListener(endLsnr);
//				
//				// Add the button to the frame
//				myWindow.add(exitButton);
//				
//				
//				// Finally, show the frame after everything is ready
//				myWindow.setVisible(true);
//				
//				
//				
//			
//		
//
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		System.out.println("*******************************************************");
//		System.out.println("     *****************************************");
//		System.out.println("                 *******************");
//		System.out.println("             Welcome To NOBLE STM SOFTWARE                             ");
//		System.out.println("                 *******************");
//		System.out.println("     ******************************************            ");
//		System.out.println("*********************************************************");
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		System.out.println("Please input the manufacture of the charger: ABB/HELIOX ");
//		String sfc;
//		sfc=sc.next();
//		Manufacture mf=Manufacture.ABB;
//		if(sfc.contentEquals("ABB"))mf=Manufacture.ABB;
//		else if(sfc.contentEquals("HELIOX")) mf=Manufacture.HELIOX;
//		else  System.out.println("You are wrong");
//		if(mf.equals(Manufacture.ABB)) {
//			Charger.overnight=ModelCharger.DC50KW;
//			Charger.fast=ModelCharger.OC450kw;}
//		else if(mf.equals(Manufacture.HELIOX)) {
//			Charger.overnight=ModelCharger.HVC100PUS;
//			Charger.fast=ModelCharger.HVC300PD;}
//
//		System.out.println("Please input the price of over night charger:");
//		Charger.priceOfOvernightCharger=sc.nextDouble();
//		System.out.println("Please input the price of fast charger:");
//		Charger.priceOfFastCharger=sc.nextDouble();
//
//
//
//
//		System.out.println("Please input  the kind of the battery:small(294kw),big(394kw)");
//		String sf;
//		sf=sc.next();
//		Model m=Model.Big;
//		if(sf.contentEquals("small")) m=Model.small;
//		else if(sf.contentEquals("big")) m=Model.Big;
//		else System.out.println("you are wrong");
//		System.out.println("Please input the price of Bus");
//		double price=sc.nextDouble();
//
//		
//		Driver.main( 30,m ,  price, mf);
//		
//		
//		
//		System.out.println("Do you have any sugesstion for the number of busses?Yes/No");
//		String answer=sc.next();
//		while(answer.contentEquals("Yes")) {
//		
//			
//			
//			
//			
//		
//	System.out.println("Please input the maximum number of the buses that you want to buy");
//		
//		int MB=sc.nextInt();
//		
//		
//		
//		
//		Driver.main( MB,m ,  price, mf);
//		System.out.println("Do you have any sugesstion for the number of busses?Yes/No");
//		answer=sc.next();
//		}
//		System.out.println("Thanks for using Noble Software");
//		System.out.println(" If you Want to  Order: Please go to the following Website");
//		System.out.println("      www.DR-Chun-WangNobleSoftware6260.com");
//		System.out.println("***********************************************************");
//		System.out.println("***********************************************************");
//		System.out.println("***********************************************************");
//		System.out.println("***********************************************************");
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//	
////	Driver.main(MB);
//	
//	
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
////	Driver s=new Driver();
//		
//	}
//	
//	
////	public final static void clearConsole()
////	{
////	    try
////	    {
////	        final String os = System.getProperty("os.name");
////
////	        if (os.contains("Windows"))
////	        {
////	            Runtime.getRuntime().exec("cls");
////	        }
////	        else
////	        {
////	            Runtime.getRuntime().exec("clear");
////	        }
////	    }
////	    catch (final Exception e)
////	    {
////	        //  Handle any exceptions.
////	    }
////	}
//	
//	
//	
//	
//	
//	
//	
	
	
	


