package Pacman_game;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import Coords.Coords;
import Coords.GeoBox;
import Geom.Point3D;
import Robot.Game;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;

public class Algorithm {

	public Map map;
	public ArrayList<GeoBox> bList; //list of boxes.
	public ArrayList<Point3D> outerPoint; //list of coordinates of the relevant corners.
	public ArrayList<Pixel> outerPixel; //list of Pixels of the relevant corners.
	public ArrayList<Line2D> cornersLine; //lists of all the lines in the path to the target.
	public Robot.Game game; //a game.
	public ArrayList<Point3D> shortPath;

	/*
	 * An empty constructor.
	 */
	public Algorithm() throws IOException {

		map = new Map();
		bList = new ArrayList<GeoBox>(); //list of boxes.
		outerPoint = new ArrayList<Point3D>(); //list of coordinates of the relevant corners.
		cornersLine = new ArrayList<Line2D>(); //lists of all the lines in the path to the target.
		this.game = new Game(); //a game.
		shortPath = new ArrayList<Point3D>(); 
		outerPixel = new ArrayList<Pixel>();

	} 

	public Algorithm(Game game) throws IOException {

		map = new Map();
		shortPath = new ArrayList<Point3D>(); 
		bList = new ArrayList<GeoBox>(); //list of boxes.
		outerPoint = new ArrayList<Point3D>(); //list of coordinates of the relevant corners.
		cornersLine = new ArrayList<Line2D>(); //lists of all the lines in the path to the target.
		game = new Game(); //a game.
		outerPixel = new ArrayList<Pixel>();


	}

	/*
	 * The main algorithm of this class.
	 */
	public void mainAlgo() {

		for (int i = 0; i < game.sizeB(); i++) {

			bList.add(game.getBox(i));
		}
		for (GeoBox it: bList) {

			Line2D temp = new Line2D.Double();
			GeoBox box = new GeoBox(it);
			Pixel downLeft = new Pixel(map.Point2Pixel(box.getMin().y(), box.getMin().x()));
			Pixel upLeft = new Pixel(map.Point2Pixel(box.getMin().y(), box.getMax().x()));
			Pixel upRight = new Pixel(map.Point2Pixel(box.getMax().y(), box.getMax().x()));
			Pixel downRight = new Pixel(map.Point2Pixel(box.getMax().y(), box.getMin().x()));


			Point3D DL = map.Pixel2Point(downLeft);
			Point3D DR = map.Pixel2Point(downRight);
			Point3D UL = map.Pixel2Point(upLeft);
			Point3D UR = map.Pixel2Point(upRight);

			temp.setLine(DL.x(), DL.y(), DR.x(), DR.y()); //the line - down left-down right.
			cornersLine.add(temp);


			temp.setLine(DL.x(), DL.y(), UL.x(), UL.y()); //the line - down left-up left.
			cornersLine.add(temp);


			temp.setLine(UL.x(), UL.y(), UR.x(), UR.y()); //the line - up left-up right.
			cornersLine.add(temp);


			temp.setLine(DR.x(), DR.y(), UR.x(), UR.y()); //the line - down right-up right.
			cornersLine.add(temp);

		}

		//filters all the relevant corners.
		boolean isIn = false;

		for (GeoBox it: bList) {
			Point3D downLeft = new Point3D(it.getMin().y(), it.getMin().x());
			Point3D downRight = new Point3D(it.getMax().y(), it.getMin().x());
			Point3D upLeft = new Point3D(it.getMin().y(), it.getMax().x());
			Point3D upRight = new Point3D(it.getMax().y(), it.getMax().x());

			//checks if the corner down right is in another box.
			isIn = this.PointIn(downRight, it, bList);
			if (!isIn) {
				outerPoint.add(downRight);
			}

			//checks if the corner down left is in another box.
			isIn = this.PointIn(downLeft, it, bList);
			if (!isIn) {
				outerPoint.add(downLeft);
			}

			//checks if the corner up left is in another box.
			isIn = this.PointIn(upLeft, it, bList);
			if (!isIn) {
				outerPoint.add(upLeft);
			}

			//checks if the corner up right is in another box.
			isIn = this.PointIn(upRight, it, bList);
			if (!isIn) {
				outerPoint.add(upRight);
			}
		}

		for (Point3D it: outerPoint) {
			Pixel temp = map.Point2Pixel(it.x(), it.y());
			outerPixel.add(temp);
		}
	}

	public void test(Point3D a, Point3D b, Game game) {

		outerPixel.clear();
		outerPoint.clear();
		bList.clear();
		cornersLine.clear();
		shortPath.clear();

		System.out.println("a: "+a);
		System.out.println("b: "+b);


		this.game = new Game(game);
		this.mainAlgo();

		int size = outerPixel.size();

		Graph G = new Graph(); 
		String source = "a"; //the player.
		String target = "b"; //Closest fruit.
		G.add(new Node(source)); // Node "a" (0)
		for(int i = 1 ; i < size + 1 ; i++) {
			Node d = new Node("" + i);
			G.add(d);
		}

		G.add(new Node(target)); // Node "b" (15)

		ArrayList<Point3D> see = new ArrayList<Point3D>();


		see = this.SeePoints(a);
		System.out.println("seeeee");
		System.out.println(see);


		for (int i = 0; i < see.size(); i++) {
			if (see.get(i)!= null) {
				System.out.println("a>>"+i);
				String s = "" +(i+1);
				G.addEdge("a", s, a.distance2D(outerPoint.get(i)));
			}
		}

		for (int i = 0; i < outerPoint.size(); i++) {
			see = this.SeePoints(outerPoint.get(i));
			for (int j = 0; j < see.size(); j++) {
				if (see.get(j)!= null) {
					System.out.println(i+">>"+j);
					String s = "" +(j+1);
					String s2 = "" +(i+1);
					G.addEdge(s2, s, outerPoint.get(i).distance2D(outerPoint.get(j)));
				}
			}
		}

//		Pixel b1 = map.Point2Pixel(b.x(), b.y());

		see = this.SeePoints(b);
		for (int i = 0; i < see.size()-1; i++) {
			if (see.get(i)!= null) {
				System.out.println("b>>"+i);
				String s = "" +(i+1);
				G.addEdge("b", s, b.distance2D(outerPoint.get(i)));
			}
		}

		// This is the main call for computing all the shortest path from node 0 ("a")
		//Graph_Algo.dijkstra(G, source);
		Graph_Algo.dijkstra(G, source);


		Node bb = G.getNodeByName(target);
		System.out.println("** Graph Demo for OOP_Ex4 **");
		System.out.println(b);
		System.out.println("Dist: " + bb.getDist());
		ArrayList<String> shortestPath = bb.getPath();

		for(int i = 1; i < shortestPath.size(); i++) {
			int index = Integer.parseInt(shortestPath.get(i));
			shortPath.add(outerPoint.get(index));
		}
		System.out.println("Path "+shortPath);



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
	public static Robot.Fruit closetFruit(ArrayList<Robot.Fruit> fruits, Robot.Packman player) {

		Robot.Fruit closetFruit = fruits.get(0);
		double minDistance = player.getLocation().distance2D(fruits.get(0).getLocation());
		for(Robot.Fruit it: fruits) {
			double tempDistance = player.getLocation().distance2D(it.getLocation());
			if(minDistance > tempDistance) {
				minDistance = tempDistance;
				closetFruit = it;
			}
		}
		return closetFruit;
	}

	//	/*
	//	 * This function checks what pacman is closest to the player.
	//	 */
	//	public Robot.Packman closetPacman(Game game) {
	//
	//		Robot.Packman closetPacman = game.getPackman(0);
	//		double minDistance = game.getPlayer().getLocation().distance2D(game.getPackman(0).getLocation());
	//		for(Robot.Packman it: game.getRobots()) {
	//			double tempDistance = game.getPlayer().getLocation().distance2D(it.getLocation());
	//			if(minDistance > tempDistance) {
	//				minDistance = tempDistance;
	//				closetPacman = it;
	//			}
	//		}
	//		return closetPacman;
	//	}

	/*
	 * This function finds the points the player "see".
	 */
	public ArrayList<Point3D> SeePoints(Point3D a) {

		ArrayList<Point3D> ans = new ArrayList<Point3D>();
		for (Point3D b: outerPoint) {
			Line2D a2b = new Line2D.Double(a.x(),a.y(),b.x(),b.y());
			boolean flag = true ; 
			for (int i = 0; i < cornersLine.size() && flag; i++) {
				Point3D p1 = new Point3D(cornersLine.get(i).getX1(),cornersLine.get(i).getY1());
				Point3D p2 = new Point3D(cornersLine.get(i).getX2(),cornersLine.get(i).getY2());
				if(!a.equals(p1) && !a.equals(p2) && !b.equals(p1) && !b.equals(p2) && linesCut(a2b,cornersLine.get(i))) {
					flag = false ; 
				}
			}
			if (flag) {
				ans.add(b);
			}
			else {
				ans.add(null);
			}
		}
		return ans;
	}

	private boolean DidISeehim(Pixel p1 , Pixel p2) 
	{
		Line2D Line = new Line2D.Double(p1.getX(),p1.getY(),p2.getX(),p2.getY());
		boolean ans = true ; 
		boolean checkIFonLine = true;
		for (int i = 0; i < cornersLine.size() && ans; i++)
		{
			checkIFonLine = true ; 
			Pixel pixel1Line = new Pixel(cornersLine.get(i).getX1(),cornersLine.get(i).getY1());
			Pixel pixel2Line = new Pixel(cornersLine.get(i).getX2(),cornersLine.get(i).getY2());
			if(p1.equals(pixel1Line) || p1.equals(pixel2Line) || p2.equals(pixel1Line) || p2.equals(pixel2Line)) checkIFonLine = false ;
			if(checkIFonLine && linesCut(Line,cornersLine.get(i))) ans = false ; 
		}
		return ans;
	}

	/*
	 * This function checks if the lines are cut.
	 * true - if they are.
	 * false - if they are not.
	 */
	public boolean linesCut(Line2D l1, Line2D l2) {
		return l1.intersectsLine(l2);
	}

	/*
	 * Example.
	 */
	public static void main(String[] args) throws IOException {
		double[] xx = {88, 88, 356, 356, 60, 60, 310, 310, 120, 164, 277, 422, 277, 422};
		double[] yy = {50, 102, 50, 102, 218, 254, 254, 218, 275, 275, 125, 125, 192, 192};
		int size = xx.length;
		ArrayList<Point3D> pp = new ArrayList<Point3D>(); // like outerPixel
		for(int i = 0; i < size; i++) {
			pp.add(new Point3D(xx[i], yy[i]));
		}
		Map m = new Map();
		Game g = new Game("C:\\Users\\nahama\\eclipse-workspace\\PacmanGame\\data\\Ex4_OOP_example4.csv");
		Algorithm algo = new Algorithm(g);
		Point3D pac = new Point3D(g.getPackman(1).getLocation().y(),g.getPackman(1).getLocation().x());
		Point3D tar = new Point3D(g.getTarget(0).getLocation().y(),g.getTarget(0).getLocation().x());
		Pixel a1 = m.Point2Pixel(pac.x(), pac.y());
		Pixel b1 = m.Point2Pixel(tar.x(), tar.y());

		Point3D a =  new Point3D(new Point3D(a1.getX(), a1.getY()));
		Point3D b =  new Point3D(new Point3D(b1.getX(), b1.getY()));
		algo.test(a, b, g);
	}

}
