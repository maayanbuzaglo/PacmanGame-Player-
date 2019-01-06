package Pacman_game;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import com.mysql.fabric.xmlrpc.base.Array;

import Coords.Coords;
import Coords.GeoBox;
import Geom.Point3D;
import Robot.Game;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;

public class AlgoBoxes {

	public Map map;
	public ArrayList<GeoBox> bList; //list of boxes.
	public ArrayList<Point3D> outerPoint; //list of coordinates of the relevant corners.
	public ArrayList<Point3D> outerPixel; //list of Pixels of the relevant corners.
	public ArrayList<Line2D> cornersLine; //lists of all the lines in the path to the target.
	public Robot.Game game; //a game.

	/*
	 * An empty constructor.
	 */
	public AlgoBoxes() {

		bList = new ArrayList<GeoBox>(); //list of boxes.
		outerPoint = new ArrayList<Point3D>(); //list of coordinates of the relevant corners.
		cornersLine = new ArrayList<Line2D>(); //lists of all the lines in the path to the target.
		this.game = new Game(); //a game.
	}

	/*
	 * The main algorithm of this class.
	 */
	public void mainAlgo(Game game) {

		for (int i = 0; i < game.sizeB(); i++) {
			bList.add(game.getBox(i));
		}
		for (GeoBox it: bList) {

			Line2D temp = new Line2D.Double();
			temp.setLine(it.getMin().y(), it.getMin().x(), it.getMax().y(), it.getMin().x()); //the line - down left-down right.
			cornersLine.add(temp);
			temp.setLine(it.getMin().y(), it.getMin().x(), it.getMin().y(), it.getMax().x()); //the line - down left-up left.
			cornersLine.add(temp);
			temp.setLine(it.getMin().y(), it.getMax().x(), it.getMax().y(), it.getMax().x()); //the line - up left-up right.
			cornersLine.add(temp);
			temp.setLine(it.getMax().y(), it.getMin().x(), it.getMax().y(), it.getMax().x()); //the line - down right-up right.
			cornersLine.add(temp);
		}

		//filters all the relevant corners.
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
			outerPixel.add(new Point3D(temp.getX(), temp.getY()));
		}


		Robot.Fruit closestFruit = closetFruit(game);
		double fruitDis = game.getPlayer().getLocation().distance2D(closestFruit.getLocation());

		if(closestFruit != null) {
			while(!(game.getPlayer().getLocation().equalsXY(closestFruit.getLocation()))) {


			}
		}

	}


	public void test(Point3D a, Point3D b) {

		int size = outerPixel.size();
		//		double[] xx = {38,88,88,356,356,60,60,310,310,120,164,277,422,277,422,238};
		//		double[] yy = {131,50,102,50,102,218,254,254,218,275,275,125,125,192,192,191};
		//		Point3D[] pp = new Point3D[size]; // like outerPixel
		//		for(int i=0;i<size;i++) {
		//			pp[i] = new Point3D(xx[i], yy[i]);
		//		}

		Graph G = new Graph(); 
		String source = "a"; //the player.
		String target = "b"; //Closest fruit.
		G.add(new Node(source)); // Node "a" (0)
		for(int i = 1 ; i < size+1 ; i++) {
			Node d = new Node("" + i);
			G.add(d);
		}
		G.add(new Node(target)); // Node "b" (15)

		ArrayList<Point3D> see = new ArrayList<Point3D>();
		
		see = this.SeePoints(a);
		for (int i = 0; i < see.size(); i++) {
			String s = "" +(i+1);
			if (see.get(i) != null)
				G.addEdge("a", s, a.distance2D(outerPixel.get(i)));
		}

		for (int i = 0; i < outerPixel.size(); i++) {
			see = this.SeePoints(outerPixel.get(i));
			for (int j = 0; j < see.size(); j++) {
				String s = "" +(j+1);
				String s2 = "" +(i+1);
				if ((see.get(j) != null) && (j != i))
					G.addEdge(s2, s, outerPixel.get(i).distance2D(outerPixel.get(j)));
			}
		}
		
		see = this.SeePoints(b);
		for (int i = 0; i < see.size(); i++) {
			String s = "" +(i+1);
			if (see.get(i) != null)
				G.addEdge("b", s, b.distance2D(outerPixel.get(i)));
		}
//
//		G.addEdge("a","1",pp[0].distance2D(pp[1]));
//		G.addEdge("a","2",pp[0].distance2D(pp[2]));
//		G.addEdge("a","5",pp[0].distance2D(pp[5]));
//		G.addEdge("a","6",pp[0].distance2D(pp[6]));
//
//		G.addEdge("1","2",pp[1].distance2D(pp[2]));
//		G.addEdge("1","3",pp[1].distance2D(pp[3]));
//		G.addEdge("3","4",pp[3].distance2D(pp[4]));
//
//		G.addEdge("5","6",pp[5].distance2D(pp[6]));
//
//		G.addEdge("9","10",pp[9].distance2D(pp[10]));
//		G.addEdge("6","9",pp[6].distance2D(pp[9]));
//		G.addEdge("7","10",pp[7].distance2D(pp[10]));
//
//		G.addEdge("7","8",pp[7].distance2D(pp[8]));
//		G.addEdge("8","13",pp[8].distance2D(pp[13]));
//		G.addEdge("8","14",pp[8].distance2D(pp[14]));
//
//		G.addEdge("11","12",pp[11].distance2D(pp[12]));
//		G.addEdge("11","13",pp[11].distance2D(pp[13]));
//		G.addEdge("12","14",pp[12].distance2D(pp[14]));
//		G.addEdge("13","14",pp[13].distance2D(pp[14]));
//
//		G.addEdge("4","11",pp[4].distance2D(pp[11]));
//		G.addEdge("4","12",pp[4].distance2D(pp[12]));
//		G.addEdge("3","12",pp[3].distance2D(pp[12]));
//
//		G.addEdge("8","b",pp[8].distance2D(pp[15]));
//		G.addEdge("13","b",pp[13].distance2D(pp[15]));
//		G.addEdge("11","b",pp[11].distance2D(pp[15]));

		// This is the main call for computing all the shortest path from node 0 ("a")
		Graph_Algo.dijkstra(G, source);

		Node b1 = G.getNodeByName(target);
		System.out.println("***** Graph Demo for OOP_Ex4 *****");
		System.out.println(b);
		System.out.println("Dist: "+b1.getDist());
		ArrayList<String> shortestPath = b1.getPath();
		for(int i=0; i < shortestPath.size(); i++) {
			System.out.print(","+shortestPath.get(i));
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
	public boolean PointIn(Point3D p, GeoBox b, ArrayList<GeoBox> boxes) {

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

	/*
	 * 
	 */
	public ArrayList<Point3D> SeePoints(Point3D a) {

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
				else
					ans.add(null);
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
