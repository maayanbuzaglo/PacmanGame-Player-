package Pacman_game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import Geom.Point3D;

/*
 * This class represents a ghost.
 * @author maayan
 * @author nahama
 */
public class Ghost {

	private Point3D location;
	private long id;
	private double speed;
	
	/*
	 * An empty constructor.
	 */
	public Ghost() {
		
		location = new Point3D(0, 0, 0);
		id = 0;
		this.speed = 1;
	}

	/*
	 * Constructor.
	 */
	public Ghost(Ghost g) {
		
		this.location = new Point3D(g.getLocation());
		this.id = g.getID();
		this.speed = g.getSpeed();
	}
	
	/*
	 * Constructor.
	 */
	public Ghost(Point3D point, long id, double speed) {
		
		this.location = new Point3D(point);
		this.id = id;
		this.speed = speed;
	}
	
	/*
	 * This function gets a csv file (String),
	 * split the elements by ",".
	 * create for every line a ghost,
	 * and returns an array list of ghosts.
	 */
	public ArrayList<Ghost> ReadCsvFile(String file) {
		
		ArrayList<Ghost> Csv = new ArrayList<Ghost>();
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
			if(arr[0].equals("G")) {
			int id = Integer.parseInt(arr[1]); //changes the id from String to int.
			double lon = Double.parseDouble(arr[2]); //changes the longitude from String to double.
			double lat = Double.parseDouble(arr[3]); //changes the latitude from String to double.
			double alt = Double.parseDouble(arr[4]); //changes the altitude from String to double.
			Point3D point = new Point3D(lat, lon, alt);
			double speed = Double.parseDouble(arr[5]); //changes the speed from String to double.
			Ghost row = new Ghost(point, id, speed);
			Csv.add(row); //adds the line.
			}
		}
		sc.close(); //closes the scanner.
		return Csv;
	}

	@Override
	public String toString() {
		return "Ghost [location = " + location +
				", ID = " + id +
				", Weight = " + speed + "]\n";
	}
	
	public Point3D getLocation() {
		return location;
	}
	
	public void setLocation(Point3D point) {
		this.location = point;
	}

	public long getID() {
		return id;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(int price) {
		this.speed = price;
	}
	

}