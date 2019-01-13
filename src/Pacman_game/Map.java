package Pacman_game;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import Geom.Point3D;

/*
 * This class represents a map that contains a map image file,
 * and all the necessary parameters of its alignment to a global coordinate system.
 * @author maayan
 * @author nahama
 */
public class Map {

	private Image image;
	private static Point3D pStart;
	private static Point3D pEnd;
	static int image_weight;
	static int image_height;

	/*
	 * A default constructor.
	 */
	public Map() throws IOException {

		image = ImageIO.read(new File("Ariel1.png")); //gets the backgrounds image.
		pStart = new Point3D(35.20232, 32.10571);
		pEnd = new Point3D(35.21239, 32.10180);
		image_height = 642;
		image_weight = 1433;
	}
	
	/*
	 * Constructor.
	 */
	public Map(int width, int height) throws IOException {

		image = ImageIO.read(new File("Ariel1.png")); //gets the backgrounds image.
		pStart = new Point3D(35.20232, 32.10571);
		pEnd = new Point3D(35.21239, 32.10180);
		image_height = height;
		image_weight = width;
	}

	/*
	 * Constructor that gets a string of the map image location.
	 */
	public Map(String imgLocation) throws IOException {

		File f = new File(imgLocation);
		image = ImageIO.read(f);
	}

	/*
	 * This function converts from point to pixel.
	 */
	public Pixel Point2Pixel(double longitude, double latitude) {
		
		Pixel pix = Pixel2Point2(image_weight, image_height);
		Point3D gps = new Point3D (longitude, latitude);
		gps.change_Geometric_To_Cart();
		double x = gps.x() - pStart.x();
		double y = gps.y() - pStart.y();
		double dx = x / pix.getX();
		double dy = y / pix.getY();
		Pixel ans = new Pixel (dx, dy);
		gps.change_Cart_To_Geometric();
		pStart.change_Cart_To_Geometric();
		pEnd.change_Cart_To_Geometric();
		return ans;
	}

	/*
	 * This function converts from pixel to point.
	 */
	public Point3D Pixel2Point(Pixel pixel)  {

		Pixel pix = Pixel2Point2(image_weight, image_height);
		double x = pixel.getX() * pix.getX();
		double y = pixel.getY() * pix.getY();
		double dx = x + pStart.x();
		double dy = y + pStart.y();
		Point3D ans = new Point3D(dx, dy);
		ans.change_Cart_To_Geometric();
		pStart.change_Cart_To_Geometric();
		pEnd.change_Cart_To_Geometric();
		return ans;
	}

	/*
	 * This function helps the Pixel2Point function.
	 */
	/**
	 * This function helps the Pixel2Point function.
	 * @param pixWeight represent the weight of the image.
	 * @param pixHeight represent the height of the image.
	 * @return the ratio between the pixels.
	 */
	public Pixel Pixel2Point2(double pixWeight, double pixHeight) {

		pStart.change_Geometric_To_Cart();
		pEnd.change_Geometric_To_Cart();
		double x = pEnd.x() - pStart.x();
		double y = pEnd.y() - pStart.y();
		double dx = x / pixWeight;
		double dy = y / pixHeight;
		Pixel ans = new Pixel (dx,dy);
		return ans;
	}

	/**
	 * This function updates the frame.
	 * @param p represent the new weight and height in pixel of the image.
	 * @param player represent tthe player.
	 * @param pList represent an array list of pacmans.
	 * @param fList represent an array list of fruits.
	 * @param gList represent an array list of ghosts.
	 * @param bList1 represent an array list of points 1 from the boxes.
	 * @param bList2 represent an array list of points 2 from the boxes.
	 * @param bList3 represent an array list of points 3 from the boxes.
	 * @param bList4 represent an array list of points 4 from the boxes.
	 * @return the new pixel for the player.
	 */
	public Pixel changeFrame(Pixel p, Pixel player, ArrayList<Pixel> pList, ArrayList<Pixel> fList, ArrayList<Pixel> gList,
			                ArrayList<Pixel> bList1, ArrayList<Pixel> bList2, ArrayList<Pixel> bList3, 
			                ArrayList<Pixel> bList4) { 

		ArrayList<Point3D> pTemp = new ArrayList<Point3D>();
		ArrayList<Point3D> fTemp = new ArrayList<Point3D>();
		ArrayList<Point3D> gTemp = new ArrayList<Point3D>(); 
		ArrayList<Point3D> bTemp1 = new ArrayList<Point3D>();
		ArrayList<Point3D> bTemp2 = new ArrayList<Point3D>();
		ArrayList<Point3D> bTemp3 = new ArrayList<Point3D>();
		ArrayList<Point3D> bTemp4 = new ArrayList<Point3D>();
		Point3D mTemp = null;
		
		if (player != null) {
		mTemp = new Point3D(this.Pixel2Point(new Pixel(player.getX(), player.getY())));
		}
		
		for (int i = 0; i < pList.size(); i++) {
			Pixel tmp = new Pixel(pList.get(i).getX(), pList.get(i).getY());
			pTemp.add(this.Pixel2Point(tmp));
		}

		for (int i = 0; i < fList.size(); i++) {
			Pixel tmp = new Pixel(fList.get(i).getX(), fList.get(i).getY());
			fTemp.add(this.Pixel2Point(tmp));
		}

		for (int i = 0; i < gList.size(); i++) {
			Pixel tmp = new Pixel(gList.get(i).getX(), gList.get(i).getY());
			gTemp.add(this.Pixel2Point(tmp));
		}
		
		for (int i = 0; i < bList1.size(); i++) {
			Pixel tmp = new Pixel(bList1.get(i).getX(), bList1.get(i).getY());
			bTemp1.add(this.Pixel2Point(tmp));
		}
		
		for (int i = 0; i < bList2.size(); i++) {
			Pixel tmp = new Pixel(bList2.get(i).getX(), bList2.get(i).getY());
			bTemp2.add(this.Pixel2Point(tmp));
		}
		
		for (int i = 0; i < bList3.size(); i++) {
			Pixel tmp = new Pixel(bList3.get(i).getX(), bList3.get(i).getY());
			bTemp3.add(this.Pixel2Point(tmp));
		}
		
		for (int i = 0; i < bList4.size(); i++) {
			Pixel tmp = new Pixel(bList4.get(i).getX(), bList4.get(i).getY());
			bTemp4.add(this.Pixel2Point(tmp));
		}

		this.setImage_weight((int)p.getX());
		this.setImage_height((int)p.getY());

		if (player != null) {
		Pixel mtmp = this.Point2Pixel(mTemp.x(), mTemp.y());
		player.setX(mtmp.getX());
		player.setY(mtmp.getY());
		}

		for (int i = 0; i < pTemp.size(); i++) {
			Pixel tmp = this.Point2Pixel(pTemp.get(i).x(), pTemp.get(i).y());
			pList.get(i).setX(tmp.getX());
			pList.get(i).setY(tmp.getY());
		}

		for (int i = 0; i < fTemp.size(); i++) {
			Pixel tmp = this.Point2Pixel(fTemp.get(i).x(), fTemp.get(i).y());
			fList.get(i).setX(tmp.getX());
			fList.get(i).setY(tmp.getY());
		}

		for (int i = 0; i < gTemp.size(); i++) {
			Pixel tmp = this.Point2Pixel(gTemp.get(i).x(), gTemp.get(i).y());
			gList.get(i).setX(tmp.getX());
			gList.get(i).setY(tmp.getY());
		}
		
		for (int i = 0; i < bTemp1.size(); i++) {
			Pixel tmp = this.Point2Pixel(bTemp1.get(i).x(), bTemp1.get(i).y());
			bList1.get(i).setX(tmp.getX());
			bList1.get(i).setY(tmp.getY());
		}
		
		for (int i = 0; i < bTemp2.size(); i++) {
			Pixel tmp = this.Point2Pixel(bTemp2.get(i).x(), bTemp2.get(i).y());
			bList2.get(i).setX(tmp.getX());
			bList2.get(i).setY(tmp.getY());
		}
		
		for (int i = 0; i < bTemp3.size(); i++) {
			Pixel tmp = this.Point2Pixel(bTemp3.get(i).x(), bTemp3.get(i).y());
			bList3.get(i).setX(tmp.getX());
			bList3.get(i).setY(tmp.getY());
		}
		
		for (int i = 0; i < bTemp4.size(); i++) {
			Pixel tmp = this.Point2Pixel(bTemp4.get(i).x(), bTemp4.get(i).y());
			bList4.get(i).setX(tmp.getX());
			bList4.get(i).setY(tmp.getY());
		}
		
		return player;
		 
	}
	
	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public Point3D getPstart() {
		return this.pStart;
	}

	public void setPstart(Point3D p) {
		this.pStart = new Point3D(p);
	}
	
	public Point3D getPend() {
		return this.pEnd;
	}

	public void setPend(Point3D p) {
		this.pEnd = new Point3D(p);
	}
	
	public int getImage_weight() {
		return this.image_weight;
	}

	public void setImage_weight(int image_weight) {
		this.image_weight = image_weight;
	}

	public int getImage_height() {
		return this.image_height;
	}
	
	public void setImage_height(int image_height) {
		this.image_height = image_height;
	}
	
}