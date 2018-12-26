package Pacman_game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import Coords.Coords;
import Geom.Point3D;

/*
 * This class represents a pacman.
 * @author maayan
 * @author nahama
 */
public class Player {

	Point3D location;
	long id;
	double speed;
	double radius;

	/*
	 * An empty constructor.
	 */
	public Player() {

		this.location = new Point3D(0, 0, 0);
		this.id = 0;
		this.speed = 2;
		this.radius = 1;
	}

	/*
	 * Constructor.
	 */
	public Player(Player p) {

		this.location = new Point3D(p.getLocation().x(), p.getLocation().y(), p.getLocation().z());;
		this.id = p.getID();
		this.speed = p.getSpeed();
		this.radius = p.getRadius();
	}

	/*
	 * Constructor.
	 */
	public Player(Point3D point, long id, double speed, double radius) {

		this.location = new Point3D(point.x(), point.y(), point.z());;
		this.id = id;
		this.speed = speed;
		this.radius = radius;
	}

	/*
	 * This function gets a csv file (String)
	 * split the elements by ",".
	 * create for every line a pacman,
	 * and returns an array list of pacmans.
	 */
	public ArrayList<Player> ReadCsvFile(String file) {

		ArrayList<Player> Csv = new ArrayList<Player>();
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
			if(arr[0].equals("M")) {
				int id = Integer.parseInt(arr[1]); //changes the id from String to int.
				double lat = Double.parseDouble(arr[3]); //changes the longitude from String to double.
				double lon = Double.parseDouble(arr[2]); //changes the latitude from String to double.
				double alt = Double.parseDouble(arr[4]); //changes the altitude from String to double.
				Point3D point = new Point3D(lat, lon, alt);
				double speed = Double.parseDouble(arr[5]); //changes the speed from String to double.
				double radius = Double.parseDouble(arr[6]); //changes the radius from String to double.
				Player row = new Player(point, id, speed, radius);
				Csv.add(row); //adds the line.
			}
		}
		sc.close(); //closes the scanner.
		return Csv;
	}

	public Point3D getLocation() {
		return this.location;
	}

	public void setLocation(Point3D point) {
		this.location = new Point3D(point.x(), point.y(), point.z());
	}

	public long getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;	
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getRadius() {
		return this.radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {

		return "Player [location = " + location + 
				", ID = " + id + 
				", Speed = " + speed +
				", Radius = " + radius + "]\n";
	}

}
