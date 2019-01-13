package Pacman_game;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.FontUIResource;

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
	public ArrayList<Point3D> outPoints; //list of coordinates of the relevant corners.
	public ArrayList<Pixel> outPixels; //list of Pixels of the relevant corners.
	public ArrayList<Line2D> pathLines; //lists of all the lines in the path to the target.
	public ArrayList<Point3D> shortestPath;
	public Robot.Game game; //a game.	

	/*
	 * An empty constructor.
	 */
	public Algorithm() throws IOException {

		map = new Map();
		bList = new ArrayList<GeoBox>(); //list of boxes.
		outPoints = new ArrayList<Point3D>(); //list of coordinates of the relevant corners.
		outPixels = new ArrayList<Pixel>(); //list of Pixels of the relevant corners.
		pathLines = new ArrayList<Line2D>(); //lists of all the lines in the path to the target.
		shortestPath = new ArrayList<Point3D>(); 
		this.game = new Game(); //a game.
	} 

	public void mainAlgo2() {

		//gets all the boxes from the game.
		for (int i = 0; i < game.sizeB(); i++) {

			bList.add(game.getBox(i));
		}

		//adds all the lines of the boxes to a list of lines.
		for (GeoBox it: bList) {

			GeoBox box = new GeoBox(it);
			Pixel downLeft = new Pixel(map.Point2Pixel(box.getMin().y(), box.getMin().x()));
			Pixel upLeft = new Pixel(map.Point2Pixel(box.getMin().y(), box.getMax().x()));
			Pixel upRight = new Pixel(map.Point2Pixel(box.getMax().y(), box.getMax().x()));
			Pixel downRight = new Pixel(map.Point2Pixel(box.getMax().y(), box.getMin().x()));

			Line2D A = new Line2D.Double();
			A.setLine(downLeft.getX(), downLeft.getY(), downRight.getX(), downRight.getY()); //the line - down left-down right.
			pathLines.add(A);

			Line2D B = new Line2D.Double();
			B.setLine(downLeft.getX(), downLeft.getY(), upLeft.getX(), upLeft.getY()); //the line - down left-up left.
			pathLines.add(B);

			Line2D C = new Line2D.Double();
			C.setLine(upLeft.getX(), upLeft.getY(), upRight.getX(), upRight.getY()); //the line - up left-up right.
			pathLines.add(C);

			Line2D D = new Line2D.Double();
			D.setLine(downRight.getX(),downRight.getY(), upRight.getX(), upRight.getY()); //the line - down right-up right.
			pathLines.add(D);
		}

		//filters all the relevant corners.
		boolean isIn = false;

		for (GeoBox it: bList) {
			
//			 value: 0- down left, 1- down right, 2- up left, 3-up right.
			Point3D downLeft = new Point3D(it.getMin().y(), it.getMin().x(), 0, 0);
			Point3D downRight = new Point3D(it.getMax().y(), it.getMin().x(), 0, 1);
			Point3D upLeft = new Point3D(it.getMin().y(), it.getMax().x(), 0, 2);
			Point3D upRight = new Point3D(it.getMax().y(), it.getMax().x(), 0, 3);

			downLeft = new Point3D(downLeft.y(), downLeft.x(), 0, 0);
			downRight = new Point3D(downRight.y(), downRight.x(), 0, 1);
			upLeft = new Point3D(upLeft.y(), upLeft.x(), 0, 2);
			upRight = new Point3D(upRight.y(), upRight.x(), 0, 3);

			//checks if the corner down right is in another box.
			isIn = this.PointIn(downRight, it, bList);
			if (!isIn) {
				outPoints.add(downRight);
			}

			//checks if the corner down left is in another box.
			isIn = this.PointIn(downLeft, it, bList);
			if (!isIn) {
				outPoints.add(downLeft);
			}

			//checks if the corner up left is in another box.
			isIn = this.PointIn(upLeft, it, bList);
			if (!isIn) {
				outPoints.add(upLeft);
			}

			//checks if the corner up right is in another box.
			isIn = this.PointIn(upRight, it, bList);
			if (!isIn) {
				outPoints.add(upRight);
			}
		}

		for(Point3D it: outPoints) {
			Pixel temp = map.Point2Pixel(it.y(), it.x());
			outPixels.add(temp);
		}
		
		for (int i = 0; i < outPixels.size(); i = i + 4) {

			Pixel p1 = new Pixel(outPixels.get(i).getX() + 10, outPixels.get(i).getY() + 10);
			outPixels.set(i, p1);
			
			Pixel p2 = new Pixel(outPixels.get(i + 1).getX() - 10, outPixels.get(i + 1).getY() + 10);
			outPixels.set(i + 1, p2);
			
			Pixel p3 = new Pixel(outPixels.get(i + 2).getX() - 10, outPixels.get(i + 2).getY() - 10);
			outPixels.set(i + 2, p3);
			
			Pixel p4 = new Pixel(outPixels.get(i + 3).getX() + 10, outPixels.get(i + 3).getY() - 10);
			outPixels.set(i + 3, p4);
			
		}
		
		for (int i = 0; i < outPixels.size(); i++) {
			Point3D temp = map.Pixel2Point(outPixels.get(i));
			outPoints.set(i, new Point3D(temp.y(), temp.x()));
		}
		
	}

	/*
	 * This is the main algorithm of the class,
	 * which computes the shortest path from the source to the target.
	 */
	public ArrayList<Point3D> MAIN(Point3D a, Point3D b, Game game) {

		bList.clear();
		outPixels.clear();
		outPoints.clear();
		pathLines.clear();
		shortestPath.clear();

		this.game = new Game(game);
		this.mainAlgo2();

		int size = outPixels.size();
		System.out.println(outPoints);
		Graph G = new Graph(); 
		String source = "a"; //the player.
		String target = "b"; //the closest fruit.
		G.add(new Node(source)); // Node "a"
		for(int i = 1 ; i < size + 1 ; i++) {
			Node d = new Node("" + i);
			G.add(d);
		}

		G.add(new Node(target)); // Node "b"

		ArrayList<Point3D> see = new ArrayList<Point3D>();

		Coords C = new  Coords();
		for (int i = 0; i < outPixels.size(); i++) {
			if(SeePoints(map.Point2Pixel(a.x(), a.y()), outPixels.get(i))) {
				String s = "" + (i + 1);
				System.out.println("a" + " >> " + s );
				G.addEdge("a", s,map.Point2Pixel(a.x(), a.y()).distance(outPixels.get(i)));
				G.addEdge(s, "a",map.Point2Pixel(a.x(), a.y()).distance(outPixels.get(i)));
			}
		}
		if(SeePoints(map.Point2Pixel(a.x(), a.y()), map.Point2Pixel(b.x(), b.y()))) {
			G.addEdge("a", "b", map.Point2Pixel(a.x(), a.y()).distance(map.Point2Pixel(b.x(), b.y())));
			G.addEdge("b", "a", map.Point2Pixel(a.x(), a.y()).distance(map.Point2Pixel(b.x(), b.y())));
		}

		for (int i = 0; i < outPixels.size(); i++) {
			for (int j = i; j < outPixels.size(); j++) {
				if(SeePoints(outPixels.get(i), outPixels.get(j))) {
					String s = "" + (j + 1);
					String s2 = "" + (i + 1);
					System.out.println(s2 + " >> " + s );
					G.addEdge(s2, s, outPixels.get(i).distance(outPixels.get(j)));
					G.addEdge(s, s2, outPixels.get(i).distance(outPixels.get(j)));
				}
			}
		}

		for (int i = 0; i < outPixels.size(); i++) {
			if(SeePoints(map.Point2Pixel(b.x(), b.y()), outPixels.get(i))) {
				String s = "" + (i + 1);
				System.out.println("b" + " >> " + s );
				G.addEdge("b", s,map.Point2Pixel(b.x(), b.y()).distance(outPixels.get(i)));
				G.addEdge(s, "b",map.Point2Pixel(b.x(), b.y()).distance(outPixels.get(i)));
			}
		}

		// This is the main call for computing all the shortest path from node 0 ("a")
		//Graph_Algo.dijkstra(G, source);

		Graph_Algo.dijkstra(G, source);

		Node bb = G.getNodeByName(target);
		System.out.println("** Graph Demo for OOP_Ex4 **");
		System.out.println("Dist: " + bb.getDist());
		ArrayList<String> shortPath = bb.getPath();
		shortPath.add("b");
		for (int i = 0; i < shortPath.size(); i++) {
			System.out.print(shortPath.get(i) + ",");
		}

		for (int j = 1; j < shortPath.size() - 1; j++) {

			Point3D p = new Point3D(outPoints.get(Integer.parseInt(shortPath.get(j)) - 1).y(), outPoints.get(Integer.parseInt(shortPath.get(j)) - 1).x());
			shortestPath.add(p);
		}
		
//		for (int i = 0; i < shortestPath.size(); i++) {
//			
//			Pixel p = map.Point2Pixel(shortestPath.get(i).x(), shortestPath.get(i).y());
//			
//		}
		
		
		
//		for (int i = 0; i < shortestPath.size(); i++) 
//		{
//
//			switch (outPoints.get(i).value() {
//			case 1:
//				outPixels.get(shortestPath.get(i)).set_PixelX(PixelInclude.get(i).get_PixelX()-10);
//				PixelInclude.get(i).set_PixelY(PixelInclude.get(i).get_PixelY()+10);
//				break;
//			case 2:
//				PixelInclude.get(i).set_PixelX(PixelInclude.get(i).get_PixelX()+10);
//				PixelInclude.get(i).set_PixelY(PixelInclude.get(i).get_PixelY()-10);
//				break;
//			case 3:
//				PixelInclude.get(i).set_PixelX(PixelInclude.get(i).get_PixelX()+10);
//				PixelInclude.get(i).set_PixelY(PixelInclude.get(i).get_PixelY()+10);
//				break;
//			case 0:
//				PixelInclude.get(i).set_PixelX(PixelInclude.get(i).get_PixelX()-10);
//				PixelInclude.get(i).set_PixelY(PixelInclude.get(i).get_PixelY()-10);
//				break;
//			}
		shortestPath.add(b);
		
		return shortestPath;
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

//		 value: 0- down left, 1- down right, 2- up left, 3-up right.
		Point3D downLeft = new Point3D(box.getMin().y(), box.getMin().x(), 0, 0);
		corners.add(c.add(downLeft, meter));
		Point3D downRight = new Point3D(box.getMax().y(), box.getMin().x(), 0, 1);
		corners.add(c.add(downRight, meter));
		Point3D upLeft = new Point3D(box.getMin().y(), box.getMax().x(), 0, 2);
		corners.add(c.add(upLeft, meter));
		Point3D upRight = new Point3D(box.getMax().y(), box.getMax().x(), 0, 3);
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

	/*
	 * This function finds the points the player "see".
	 */
	public boolean SeePoints(Pixel a , Pixel b) {
		boolean ans = true; 

		Line2D Temp = new Line2D.Double(a.getX(),a.getY(),b.getX(),b.getY());
		for (int i = 0; i < pathLines.size(); i++) {
			boolean tempAns = true ; 
			Pixel pixel1Line = new Pixel(pathLines.get(i).getX1(),pathLines.get(i).getY1());
			Pixel pixel2Line = new Pixel(pathLines.get(i).getX2(),pathLines.get(i).getY2());

			if(a.equals(pixel1Line) || a.equals(pixel2Line) || b.equals(pixel1Line) || b.equals(pixel2Line)) tempAns = false;
			if(linesCut(Temp, pathLines.get(i)) && tempAns  ) ans = false;
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

		Game game = new Game("C:\\Users\\מעיין\\eclipse-workspace\\PacmanGame\\data\\Ex4_OOP_example4.csv");
		Algorithm algo = new Algorithm();
		Point3D A = new Point3D(game.getPackman(0).getLocation().y(),game.getPackman(0).getLocation().x());
		Point3D B = new Point3D(game.getTarget(5).getLocation().y(),game.getTarget(5).getLocation().x());
		algo.MAIN(A, B, game);
		System.out.println(algo.shortestPath);
	}

}
