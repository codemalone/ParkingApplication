package view;

import java.util.List;

import control.ParkingDb;
import model.Lot;

public class ParkingTestGUI {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		List<Lot> allLots = ParkingDb.getAllLots();
		
		for (Lot e : allLots)
			System.out.println(e.getName());
		
		
		List<Lot> west = ParkingDb.getLots("South Tower");
		
		for (Lot e : west)
			System.out.println(e.getName());
		
		
	}

}
