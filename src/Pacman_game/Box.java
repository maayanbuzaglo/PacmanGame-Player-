package Pacman_game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import Geom.Point3D;

/*
 * This class represents a box.
 * @author maayan
 * @author nahama
 */
public class Box {

	private Point3D locationStart;
	private Point3D locationEnd;
	private long id;
	private double radius;
	
	/*
	 * An empty constructor.
	 */
	public Box() {
		
		this.locationStart = new Point3D(0, 0, 0);
		this.locationEnd = new Point3D(0, 0, 0);
		this.id = 0;
		this.radius = 1;
	}

	/*
	 * Constructor.
	 */
	public Box(Box b) {
		
		this.locationStart = new Point3D(b.getLocationStart());
		this.locationEnd = new Point3D(b.getLocationEnd());
		this.id = b.getID();
		this.radius = b.getRadius();
	}
	
	/*
	 * Constructor.
	 */
	public Box(Point3D point1, Point3D point2, long id, double radius) {
		
		this.locationStart = new Point3D(point1);
		this.locationEnd = new Point3D(point2);
		this.id = id;
		this.radius = radius;
	}
	
	/*
	 * This function gets a csv file (String),
	 * split the elements by ",".
	 * create for every line a ghost,
	 * and returns an array list of ghosts.
	 */
	public ArrayList<Box> ReadCsvFile(String file) {
		
		ArrayList<Box> Csv = new ArrayList<Box>();
		Scanner sc = null;
		File fi = new File(file); //gets the file.
		try { //reads the file.
			sc = new Scanner(fi);
		}
		catch (FileNotFoundException exc) { //if file not found - Exception.
			exc.printStackTrace();
		}
		String in = sc.nextLine();
		while(sc.hasNext()) { //continues until there are no more lines in the file.
			in = sc.nextLine(); //moves to the next line.
			String[] arr = in.split(","); //splits the elements by ",";
			if(arr[0].equals("B")) {
			int id = Integer.parseInt(arr[1]); //changes the id from String to int.
			double lon1 = Double.parseDouble(arr[2]); //changes the longitude from String to double.
			double lat1 = Double.parseDouble(arr[3]); //changes the latitude from String to double.
			double alt1 = Double.parseDouble(arr[4]); //changes the altitude from String to double.
			double lon2 = Double.parseDouble(arr[5]); //changes the longitude from String to double.
			double lat2 = Double.parseDouble(arr[6]); //changes the latitude from String to double.
			double alt2 = Double.parseDouble(arr[7]); //changes the altitude from String to double.
			Point3D point1 = new Point3D(lat1, lon1, alt1);
			Point3D point2 = new Point3D(lat2, lon2, alt2);
			double radius = Double.parseDouble(arr[8]); //changes the radius from String to double.
			Box row = new Box(point1, point2, id, radius);
			Csv.add(row); //adds the line.
			}
		}
		sc.close(); //closes the scanner.
		return Csv;
	}

	@Override
	public String toString() {
		return "Ghost [location Start = " + locationStart +
				"location End = " + locationEnd +
				", ID = " + id +
				", Radius = " + radius + "]\n";
	}
	
	public Point3D getLocationStart() {
		return this.locationStart;
	}
	
	public void setLocationStart(Point3D point) {
		this.locationStart = point;
	}

	public Point3D getLocationEnd() {
		return this.locationEnd;
	}
	
	public void setLocationEnd(Point3D point) {
		this.locationEnd = point;
	}
	
	public long getID() {
		return id;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public double getRadius() {
		return radius;
	}

	public void setSpeed(double radius) {
		this.radius = radius;
	}	

}