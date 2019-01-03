package Pacman_game;

import java.util.ArrayList;

import Coords.Coords;
import Coords.GeoBox;
import Geom.Point3D;

public class AlgoBoxes {

	/**
	 * This function gets an Array List of the corners of the box + 1 meter. 
	 * @param box represent a box.
	 * @return an Array List of the corners of the box + 1 meter.
	 */
	public ArrayList<Point3D> CornersBox (GeoBox box) {
		
		ArrayList<Point3D> corners = new ArrayList<Point3D>();
		Coords c = new Coords();
		Point3D meter = new Point3D(1, 1, 0);		
		
		Point3D downLeft = new Point3D(box.getMin().y(), box.getMin().x());
		corners.add(c.add(downLeft, meter));
		Point3D downRight = new Point3D(box.getMax().y(), box.getMin().x());
		corners.add(c.add(downRight, meter));
		Point3D upLeft = new Point3D(box.getMin().y(), box.getMax().x());
		corners.add(c.add(upLeft, meter));
		Point3D upRight = new Point3D(box.getMax().y(), box.getMax().x());
		corners.add(c.add(upRight, meter));
	
		return corners;
	}
	
	
	public boolean PointIn (Point3D p, ArrayList<GeoBox> boxes)
	{
		for (GeoBox it: boxes) {
			Point3D downLeft = new Point3D(it.getMin().y(), it.getMin().x());
			Point3D downRight = new Point3D(it.getMax().y(), it.getMin().x());
			Point3D upLeft = new Point3D(it.getMin().y(), it.getMax().x());
			Point3D upRight = new Point3D(it.getMax().y(), it.getMax().x());
			
			if (downLeft.x() <= p.x() && downRight.x() >= p.x())
				if (downRight.y() <= p.y() && upRight.y() >= p.y())
					return true;
		}
		return false;
	}
	
	
}
