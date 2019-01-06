package Pacman_game;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import Coords.Coords;
import Coords.GeoBox;
import Geom.Point3D;
import Robot.Game;

public class AlgoBoxes {

	public Map map;
	public ArrayList<GeoBox> bList; //list of boxes.
	public ArrayList<Point3D> outerPoint; //list of coordinate of the relevant corners.
	public ArrayList<Pixel> outerPixel; //list of Pixel of the relevant corners.
	public Robot.Game game; //a game.
	public ArrayList<Line2D> cornersLine;

	public AlgoBoxes() {

		bList = new ArrayList<GeoBox>();
		outerPoint = new ArrayList<Point3D>();
		cornersLine = new ArrayList<Line2D>();
		this.game = new Game();		
	}

	public AlgoBoxes(Game game) {

		outerPoint = new ArrayList<Point3D>();
		this.game = game;
		for (int i = 0; i < game.sizeB(); i++) {
			bList.add(game.getBox(i));
		}
		for (GeoBox it: bList) {
			Point3D downLeft = new Point3D(it.getMin().y(), it.getMin().x());
			Point3D downRight = new Point3D(it.getMax().y(), it.getMin().x());
			Point3D upLeft = new Point3D(it.getMin().y(), it.getMax().x());
			Point3D upRight = new Point3D(it.getMax().y(), it.getMax().x());
			Line2D temp = new Line2D();
			temp.setLine(it.getMin().y(), it.getMin().x(), it.getMax().y(), it.getMin().x()); // The line - down left-down right.
			cornersLine.add(temp);
			temp.setLine(it.getMin().y(), it.getMin().x(), it.getMin().y(), it.getMax().x()); // The line - down left-up left.
			cornersLine.add(temp);
			temp.setLine(it.getMin().y(), it.getMax().x(), it.getMax().y(), it.getMax().x()); // The line - up left-up right.
			cornersLine.add(temp);
			temp.setLine(it.getMax().y(), it.getMin().x(), it.getMax().y(), it.getMax().x()); // The line - down right-up right.
			cornersLine.add(temp);
		}
	}

	public void mainAlgo(Game game) {

		Robot.Fruit closestFruit = closetFruit(game);
		Robot.Packman closestPacman = closetPacman(game);
		double fruitDis = game.getPlayer().getLocation().distance2D(closestFruit.getLocation());
		double pacmanDis = game.getPlayer().getLocation().distance2D(closestPacman.getLocation());

		if(fruitDis < pacmanDis) {
			while(!(game.getPlayer().getLocation().equalsXY(closestFruit.getLocation()))) {

			}
		}
		for (int i = 0; i < game.sizeB(); i++) {
			bList.add(game.getBox(i));
		}
		boolean isIn = false;
		for (GeoBox it: bList) {
			Point3D downLeft = new Point3D(it.getMin().y(), it.getMin().x());
			Point3D downRight = new Point3D(it.getMax().y(), it.getMin().x());
			Point3D upLeft = new Point3D(it.getMin().y(), it.getMax().x());
			Point3D upRight = new Point3D(it.getMax().y(), it.getMax().x());

			//check if the corner down right is in an other box.
			isIn = this.PointIn(downRight, it, bList);
			if (!isIn) {
				outerPoint.add(downRight);
			}

			//check if the corner down left is in an other box.
			isIn = this.PointIn(downLeft, it, bList);
			if (!isIn) {
				outerPoint.add(downLeft);
			}

			//check if the corner up left is in an other box.
			isIn = this.PointIn(upLeft, it, bList);
			if (!isIn) {
				outerPoint.add(upLeft);
			}

			//check if the corner up right is in an other box.
			isIn = this.PointIn(upRight, it, bList);
			if (!isIn) {
				outerPoint.add(upRight);
			}
		}
		for (Point3D it: outerPoint) {
			Pixel temp = map.Point2Pixel(it.y(), it.x());
			outerPixel.add(temp);
		}

	}

	/**
	 * This function gets an Array List of the corners of the box + 1 meter. 
	 * @param box represents a box.
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

	/**
	 * This function says if a point is in other box.
	 * then, we don't have to compute it's coordinates for the algorithm.
	 * @param p represent a point of a corner that we want to check.
	 * @param b represent the box that the corner came from.
	 * @param boxes an Array List of all the boxes in the game.
	 * @return true if the point is in one of the boxes and false otherwise.
	 */
	public boolean PointIn (Point3D p, GeoBox b, ArrayList<GeoBox> boxes) {

		for (GeoBox it: boxes) {
			Point3D downLeft = new Point3D(it.getMin().y(), it.getMin().x());
			Point3D downRight = new Point3D(it.getMax().y(), it.getMin().x());
			Point3D upLeft = new Point3D(it.getMin().y(), it.getMax().x());
			Point3D upRight = new Point3D(it.getMax().y(), it.getMax().x());

			if (!b.equals(it))
				if (downLeft.x() <= p.x() && downRight.x() >= p.x())
					if (downRight.y() <= p.y() && upRight.y() >= p.y())
						return true;
		}
		return false;
	}

	/*
	 * This function checks what fruit is closest to the player.
	 */
	public Robot.Fruit closetFruit(Game game) {

		Robot.Fruit closetFruit = game.getTarget(0);
		double minDistance = game.getPlayer().getLocation().distance2D(game.getTarget(0).getLocation());
		for(Robot.Fruit it: game.getTargets()) {
			double tempDistance = game.getPlayer().getLocation().distance2D(it.getLocation());
			if(minDistance > tempDistance) {
				minDistance = tempDistance;
				closetFruit = it;
			}
		}
		return closetFruit;
	}

	/*
	 * This function checks what pacman is closest to the player.
	 */
	public Robot.Packman closetPacman(Game game) {

		Robot.Packman closetPacman = game.getPackman(0);
		double minDistance = game.getPlayer().getLocation().distance2D(game.getPackman(0).getLocation());
		for(Robot.Packman it: game.getRobots()) {
			double tempDistance = game.getPlayer().getLocation().distance2D(it.getLocation());
			if(minDistance > tempDistance) {
				minDistance = tempDistance;
				closetPacman = it;
			}
		}
		return closetPacman;
	}

	public ArrayList<Point3D> SeePoints (Point3D a) {

		ArrayList<Point3D> ans = new ArrayList<Point3D>();
		boolean inter = false;
		Line2D a2b = new Line2D.Double();
		for (Point3D b: outerPoint) {
			inter = true;
			a2b.setLine(a.x(), a.y(), b.x(), b.y());
			if ((a.x() == b.x()) || (a.y() == b.y()))
				ans.add(b);
			else {
				for (int i = 0; i < cornersLine.size() && !inter; i++) {
					inter = linesCut(a2b, cornersLine.get(i));
				}
				if (!inter)
					ans.add(b);
			}
		}
		return ans;
	}

	public boolean linesCut(Line2D l1, Line2D l2) {
		return l1.intersectsLine(l2);
	}




	//	public boolean linesCut(Line2D l1) {
	//
	//		boolean flage = true;
	//		for (int i = 0; i < cornersLine.size() && flage; i++) {
	//			flage = false;
	//			double denom = (cornersLine.get(i).y2 - cornersLine.get(i).y1) * (l1.x2 - l1.x1) - (cornersLine.get(i).x2 - cornersLine.get(i).x1) * (l1.y2 - l1.y1);
	//			if (denom == 0.0) { // Lines are parallel.
	//				flage = true;
	//			}
	//			double ua = ((cornersLine.get(i).x2 - cornersLine.get(i).x1) * (l1.y1 - cornersLine.get(i).y1) - (cornersLine.get(i).y2 - cornersLine.get(i).y1) * (l1.x1 - cornersLine.get(i).x1)) / denom;
	//			double ub = ((l1.x2 - l1.x1) * (l1.y1 - cornersLine.get(i).y1) - (l1.y2 - l1.y1) * (l1.x1 - cornersLine.get(i).x1)) / denom;
	//			if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
	//				// Get the intersection point.
	//				return false;
	//			}
	//		}
	//		if (flage)
	//			return true;
	//		else
	//			return false;
	//	}
}
