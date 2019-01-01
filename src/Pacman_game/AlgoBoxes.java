package Pacman_game;

import java.util.ArrayList;

import Coords.Coords;
import Coords.GeoBox;
import Geom.Point3D;

public class AlgoBoxes {

	public ArrayList<Point3D> CornersBox (GeoBox box) {
		
		ArrayList<Point3D> corners = new ArrayList<Point3D>();
		Coords c = new Coords();
		Point3D meter = new Point3D(1, 1, 0);		
		
		Point3D  b1 = new Point3D(box.getMin().y(), box.getMin().x());
		corners.add(c.add(b1, meter));
		Point3D b2 = new Point3D(box.getMin().y(), box.getMax().x());
		corners.add(c.add(b2, meter));
		Point3D b3 = new Point3D(box.getMax().y(), box.getMax().x());
		corners.add(c.add(b3, meter));
		Point3D b4 = new Point3D(box.getMax().y(), box.getMax().x());
		corners.add(c.add(b4, meter));
	
		return corners;
	}
}
