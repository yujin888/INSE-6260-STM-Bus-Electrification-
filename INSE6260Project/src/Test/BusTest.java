package Test;

import java.sql.Time;

import org.junit.Assert;

import FirstPackage.Bus;

public class BusTest {
public void shouldCreateAndReturnName() {
	
	//Given
	Bus bas=new Bus();
	
	
	//When
	@SuppressWarnings("deprecation")
	Time s=new Time(05,00,00);
	
	
	//Then
	Assert.assertTrue((bas.getDerpartureTime().equals(s)));
	
}
}
