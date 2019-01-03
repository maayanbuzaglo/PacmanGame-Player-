package Pacman_game;

import java.util.ArrayList;
import Coords.Coords;
import Coords.GeoBox;
import Geom.Point3D;
import Robot.Game;

public class AlgoBoxes {

	public void mainAlgo(Game game) {

		Robot.Fruit closestFruit = closetFruit(game);
		Robot.Packman closestPacman = closetPacman(game);
		double fruitDis = game.getPlayer().getLocation().distance2D(closestFruit.getLocation());
		double pacmanDis = game.getPlayer().getLocation().distance2D(closestPacman.getLocation());

		if(fruitDis < pacmanDis) {
			while(!(game.getPlayer().getLocation().equalsXY(closestFruit.getLocation()))) {

			}
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
}
