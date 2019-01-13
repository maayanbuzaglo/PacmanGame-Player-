package Pacman_game;

import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import Coords.GeoBox;
import Coords.LatLonAlt;
import Geom.Point3D;
import Robot.Fruit;
import Robot.Game;
import Robot.Packman;
import Robot.Play;

/*
 * This class represents the game frame.
 */
public class MyFrame extends JFrame implements MouseListener {

	public Map map;
	public Play play;
	public Robot.Game game; //a game.
	public Robot.Packman player; //the player.
	public String file; //game file.
	public double azi; //the azimuth.
	public BufferedImage background; //game background image.
	public BufferedImage ghostImage; //ghost icon.
	public BufferedImage playerImage; //player icon.
	public BufferedImage pacmanImage; //pacman icon.
	public BufferedImage fruitImage; //fruit icon.
	public ArrayList<Robot.Packman> gList; //list of ghosts.
	public ArrayList<Robot.Packman> pList; //list of pacmans.
	public ArrayList<Robot.Fruit> fList; //list of fruits.
	public ArrayList<GeoBox> bList; //list of boxes.
	public Pixel playerPixel; //player pixel.
	public ArrayList<Pixel> ghostPixel; //ghosts pixels list.
	public ArrayList<Pixel> pacmanPixel; //pacmans pixels list.
	public ArrayList<Pixel> fruitPixel; //fruits pixel list.
	public ArrayList<Pixel> boxPixel1; //boxes pixels list for point 1.
	public ArrayList<Pixel> boxPixel2; //boxes pixels list for point 2.
	public ArrayList<Pixel> boxPixel3; //boxes pixels list for point 3.
	public ArrayList<Pixel> boxPixel4; //boxes pixels list for point 4.
	public int countPacman; //pacman id.
	public int countFruit; //fruit id.
	public Algorithm algo;//the algorithm for the auto run.

	private boolean PlayerOn = false; //if true - draws player. else - nothing.
	private boolean AzimuthOn = false; //if true - the player moves to mouse click.
	private boolean ReadGameOn = false; //if true - draws player. else - need to read game.


	/*
	 * An empty constructor.
	 */
	public MyFrame() throws IOException {

		map = new Map();
		play = new Play() ; 
		game = new Game();
		player = null;
		file = null;
		azi = 0;
		gList = new ArrayList<Robot.Packman>();
		pList = new ArrayList<Robot.Packman>();
		fList = new ArrayList<Robot.Fruit>();
		bList = new ArrayList<GeoBox>();
		playerPixel = null;
		ghostPixel = new ArrayList<Pixel>();
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		boxPixel1 = new ArrayList<Pixel>();
		boxPixel2 = new ArrayList<Pixel>();
		boxPixel3 = new ArrayList<Pixel>();
		boxPixel4 = new ArrayList<Pixel>();
		countPacman = 0;
		countFruit = 0;
		PlayerOn = false;
		algo = new Algorithm();

		initGUI();		
		this.addMouseListener(this);
	}

	/*
	 * Constructor.
	 */
	public MyFrame(Robot.Game game) throws IOException {

		map = new Map();
		player = game.getPlayer();
		gList = game.getGhosts();
		pList =  game.getRobots();
		fList = game.getTargets();
		playerPixel = new Pixel();
		ghostPixel = new ArrayList<Pixel>();
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		boxPixel1 = new ArrayList<Pixel>();
		boxPixel2 = new ArrayList<Pixel>();
		boxPixel3 = new ArrayList<Pixel>();
		boxPixel4 = new ArrayList<Pixel>();
		PlayerOn = false;
		algo = new Algorithm();
		//gets the boxes in the game.
		for (int i = 0; i < game.sizeB(); i++) {
			bList.add(game.getBox(i));
		}

		repaint();
		initGUI();		
		this.addMouseListener(this);
	}

	/*
	 * This function makes the frame.
	 */
	private void initGUI() {

		//		MyPanel grafic = new MyPanel(); //For clear moves, but does problems.
		//		add(grafic);

		MenuBar menuBar = new MenuBar();
		Menu gameOn = new Menu("Game"); //Game - Read game, Control run, Automatic run.
		MenuItem readCSV = new MenuItem("Read game");
		MenuItem contRun = new MenuItem("Control run");
		MenuItem autoRun=new MenuItem("Automatic run");

		Menu options = new Menu("Options"); //Options - Stop, Clear.
		MenuItem stop = new MenuItem("Stop");
		MenuItem clear = new MenuItem("Clear");

		menuBar.add(gameOn);
		gameOn.add(readCSV);
		gameOn.add(contRun);
		gameOn.add(autoRun);

		menuBar.add(options);
		options.add(stop);
		options.add(clear);

		this.setMenuBar(menuBar);

		//listens to read game key.
		readCSV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//clears all before read a new game.
				pList.clear();
				fList.clear();
				bList.clear();
				player = null;
				pacmanPixel.clear();
				fruitPixel.clear();
				boxPixel1.clear();
				boxPixel2.clear();
				boxPixel3.clear();
				boxPixel4.clear();
				playerPixel = null;

				String place = "";
				JButton open = new JButton();
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Choose A Pacman Game");
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					place = fc.getSelectedFile().getAbsolutePath();
				}
				file = place;
				Play play = new Play(file); //plays the game on this file.
				play.setIDs(314882077, 322093311); //sets the group id.

				//gets the GPS coordinates of the "arena".
				String map_data = play.getBoundingBox();
				System.out.println("Bounding Box info: " + map_data);

				//gets the game-board data.
				ArrayList<String> board_data = play.getBoard();

				for(int i = 0; i < board_data.size(); i++) {

					//updates the player data.
					if(board_data.get(i).charAt(0) == 'M') {
						Robot.Packman player = new Robot.Packman(board_data.get(i));

						playerPixel = new Pixel(map.Point2Pixel(player.getLocation().y(), player.getLocation().x()));
					}

					//adds all the pacmans in the game to pacman list in this game.
					if(board_data.get(i).charAt(0) == 'P') {
						Robot.Packman pacman = new Robot.Packman(board_data.get(i));
						pList.add(pacman);

						Pixel p = new Pixel(map.Point2Pixel(pacman.getLocation().y(), pacman.getLocation().x()));
						pacmanPixel.add(p);
					}

					//adds all the fruits in the game to fruit list in this game.
					else if(board_data.get(i).charAt(0) == 'F') {
						Robot.Fruit fruit = new Robot.Fruit(board_data.get(i));
						fList.add(fruit);
						game.add(fruit);

						Pixel f = new Pixel(map.Point2Pixel(fruit.getLocation().y(), fruit.getLocation().x()));
						fruitPixel.add(f);
					}

					//adds all the ghosts in the game to ghost list in this game.
					else if(board_data.get(i).charAt(0) == 'G') {
						Robot.Packman ghost = new Robot.Packman(board_data.get(i));
						gList.add(ghost);
						game.addGhost(ghost);

						Pixel g = new Pixel(map.Point2Pixel(ghost.getLocation().y(), ghost.getLocation().x()));
						ghostPixel.add(g);
					}

					//adds all the boxes in the game to box list in this game.
					else if(board_data.get(i).charAt(0) == 'B') {
						GeoBox box = new GeoBox(board_data.get(i));
						bList.add(box);
						game.add(box);

						Pixel downLeft = new Pixel(map.Point2Pixel(box.getMin().y(), box.getMin().x()));
						boxPixel1.add(downLeft);
						Pixel upLeft = new Pixel(map.Point2Pixel(box.getMin().y(), box.getMax().x()));
						boxPixel2.add(upLeft);
						Pixel upRight = new Pixel(map.Point2Pixel(box.getMax().y(), box.getMax().x()));
						boxPixel3.add(upRight);
						Pixel downRight = new Pixel(map.Point2Pixel(box.getMax().y(), box.getMin().x()));
						boxPixel4.add(downRight);
					}
				}

				ReadGameOn = true;
				PlayerOn = true;
				repaint();
			}
		});

		//listens to control run key.
		contRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//if a game was not chosen.
				if(ReadGameOn == false) {
					System.out.println("You must choose a game.");
				}
				else {
					play = new Play(file); //plays the game on this file.
					play.setIDs(314882077, 322093311); //sets the group id.

					//gets the GPS coordinates of the "arena".
					String map_data = play.getBoundingBox();
					System.out.println("Bounding Box info: " + map_data);

					//gets the game-board data.
					ArrayList<String> board_data = play.getBoard();

					//if a player was not chosen.
					if(player == null) {
						System.out.println("You must place the player.");
					}
					else {
						play.setInitLocation(player.getLocation().x() , player.getLocation().y()); //sets the "player" init location.

						//starts the "server".
						play.start(); //default max time is 100 seconds (1000*100 ms).

						System.out.println("******************** Game Over ********************");

						//prints the data & save to the course DB.
						String info = play.getStatistics();
						System.out.println(info);
						repaint();
						PlayerOn = false;
						AzimuthOn = true;

						ThreadT S = new ThreadT();
						S.start();
					}
				}
			}
		});

		//listens to automatic run key.
		autoRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//if a game was not chosen.
				if(ReadGameOn == false) {
					System.out.println("You must choose a game.");
				}
				else {
					play = new Play(file); //plays the game on this file.
					play.setIDs(314882077, 322093311); //sets the group id.

					//gets the GPS coordinates of the "arena".
					String map_data = play.getBoundingBox();
					System.out.println("Bounding Box info: " + map_data);

					//gets the game-board data.
					ArrayList<String> board_data = play.getBoard();

					//if a player was not chosen.
					if(player == null) {
						System.out.println("You must place the player.");
					}
					else {
						play.setInitLocation(player.getLocation().x() , player.getLocation().y()); //sets the "player" init location.

						//starts the "server".
						play.start(); //default max time is 100 seconds (1000*100 ms).

						System.out.println("******************** Game Over ********************");

						//prints the data & save to the course DB.
						String info = play.getStatistics();
						System.out.println(info);
						repaint();
						PlayerOn = false;
						AzimuthOn = false;

						Fruit closetFruit = new Fruit(Algorithm.closetFruit(fList, player)); //finds the closet fruit.

						Point3D A = new Point3D(player.getLocation().y(), player.getLocation().x());
						Point3D B = new Point3D(closetFruit.getLocation().y(), closetFruit.getLocation().x());
						algo.MAIN(A, B, game);

						//computes the azimuth the player should go to eat the closet fruit.
						azimuth(player.getLocation().x(), player.getLocation().y(), algo.shortestPath.get(0).y(), algo.shortestPath.get(0).x());

						ThreadT2 S = new ThreadT2();
						S.start();
					}
				}
			}
		});

		//listens to stop key.
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				play.stop(); //stop the game.
				PlayerOn = false;
				AzimuthOn = false;
				repaint();
			}
		});

		//listens to clear key.
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				play.stop();
				pList.clear();
				fList.clear();
				gList.clear();
				bList.clear();
				player = null;
				pacmanPixel.clear();
				fruitPixel.clear();
				ghostPixel.clear();
				boxPixel1.clear();
				boxPixel2.clear();
				boxPixel3.clear();
				boxPixel4.clear();
				playerPixel = null;
				play.getBoard().clear();

				countPacman = 0;
				countFruit = 0;
				ReadGameOn = false;
				PlayerOn = false;
				AzimuthOn = false;

				repaint();
			}
		});

		//gets the player image.
		try {
			playerImage = ImageIO.read(new File("player.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//gets the ghost image.
		try {
			ghostImage = ImageIO.read(new File("ghost.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//gets the pacman image.
		try {
			pacmanImage = ImageIO.read(new File("pacman.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//gets the fruit image.
		try {
			fruitImage = ImageIO.read(new File("fruit.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	int x = -1;
	int y = -1;

	/*
	 * This function handles mouse clicks events.
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg) {

		x = arg.getX();
		y = arg.getY();
		Pixel p = new Pixel(x, y);
		if(PlayerOn) { //draws a player where mouse clicked.
			this.playerPixel = new Pixel(p);
			Point3D temp = map.Pixel2Point(p);
			Coords.LatLonAlt point = new LatLonAlt(temp.y(), temp.x(), 0);
			this.player = new Packman(point, 0);
		}
		else if(AzimuthOn){ //makes the pacman go the mouse click.
			Point3D po = map.Pixel2Point(p);
			Point3D playerPoint = map.Pixel2Point(playerPixel);
			azi = playerPoint.north_angle(po);
		}		
		repaint();

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	//	public class MyPanel extends JPanel { //for clear moves.

	/*
	 * This function paints pacmans, fruits, ghosts and boxes on the game frame.
	 * @see java.awt.Window#paint(java.awt.Graphics).
	 */
	public void paint(Graphics g) {

		g.drawImage(map.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		Pixel pFram = new Pixel(this.getWidth(), this.getHeight());
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		ghostPixel = new ArrayList<Pixel>();
		boxPixel1 = new ArrayList<Pixel>();
		boxPixel2 = new ArrayList<Pixel>();
		boxPixel3 = new ArrayList<Pixel>();
		boxPixel4 = new ArrayList<Pixel>();

		//uploads the game pixels if change the frame size.
		playerPixel = map.changeFrame(pFram, playerPixel, pacmanPixel, fruitPixel, ghostPixel, boxPixel1, boxPixel2, boxPixel3, boxPixel4);

		//changes points of pacmans in game to pixels.
		for (int i = 0; i < pList.size(); i++) {
			Pixel pix = map.Point2Pixel(pList.get(i).getLocation().y(), pList.get(i).getLocation().x());
			pacmanPixel.add(pix);
		}

		//changes points of fruits in game to pixels.
		for (int i = 0; i < fList.size(); i++) {
			Pixel pix = map.Point2Pixel(fList.get(i).getLocation().y(), fList.get(i).getLocation().x());
			fruitPixel.add(pix);
		}

		//changes points of ghost in game to pixels. 
		for (int i = 0; i < gList.size(); i++) {
			Pixel pix = map.Point2Pixel(gList.get(i).getLocation().y(), gList.get(i).getLocation().x());
			ghostPixel.add(pix);
		}

		//changes points of boxes in game to pixels.
		for (int i = 0; i < bList.size(); i++) {
			Pixel downLeft = map.Point2Pixel(bList.get(i).getMin().y(), bList.get(i).getMin().x());
			boxPixel1.add(downLeft);
			Pixel upLeft = map.Point2Pixel(bList.get(i).getMin().y(), bList.get(i).getMax().x());
			boxPixel2.add(upLeft);
			Pixel upRight = map.Point2Pixel(bList.get(i).getMax().y(), bList.get(i).getMax().x());
			boxPixel3.add(upRight);
			Pixel downRight = map.Point2Pixel(bList.get(i).getMax().y(), bList.get(i).getMin().x());
			boxPixel4.add(downRight);
		}

		//draws all the boxes on the list.
		for (int i = 0; i < boxPixel1.size(); i++) {
			double height = boxPixel2.get(i).distance(boxPixel1.get(i));
			double width = boxPixel4.get(i).distance(boxPixel1.get(i));
			g.fillRect((int)boxPixel1.get(i).getX(), (int)boxPixel3.get(i).getY(), (int)width, (int)height);
		}

		//draws all the fruits on the list.
		for (int i = 0; i < fruitPixel.size(); i++) {
			g.drawImage(fruitImage, (int)fruitPixel.get(i).getX(), (int)fruitPixel.get(i).getY(), 40, 30, this);
		}

		//draws all the pacmans on the list.
		for (int i = 0; i < pacmanPixel.size(); i++) {
			g.drawImage(pacmanImage, (int)pacmanPixel.get(i).getX(), (int)pacmanPixel.get(i).getY(), 30, 30, this);
		}

		//draws the player. 
		if (playerPixel != null) {
			g.drawImage(playerImage, (int)playerPixel.getX(), (int)playerPixel.getY(), 30, 30, this);
		}

		//draws all the ghost on the list. 
		for (int i = 0; i < ghostPixel.size(); i++) {
			g.drawImage(ghostImage, (int)ghostPixel.get(i).getX(), (int)ghostPixel.get(i).getY(), 60, 40, this);
		}
	}
	//}

	public class ThreadT extends Thread {

		@Override
		public void run() {

			int i = 0 ; 
			while(play.isRuning()) {
				i++;

				//This is the main command to the player (on the server side).
				play.rotate(azi); 
				System.out.println("***** Step " + i + " *****");

				//getS the current score of the game.
				String info = play.getStatistics();
				System.out.println(info);

				//getS the game-board current state.
				ArrayList<String> board_data = play.getBoard();

				//clears all before read a new game (except the boxes).
				pList.clear();
				fList.clear();
				gList.clear();
				player = null;
				playerPixel = null;
				pacmanPixel.clear();
				fruitPixel.clear();
				ghostPixel.clear();

				for(int i1 = 0; i1 < board_data.size(); i1++) {

					//updates the player data.
					if(board_data.get(i1).charAt(0) == 'M') {
						Robot.Packman player = new Robot.Packman(board_data.get(i1));

						playerPixel = new Pixel(map.Point2Pixel(player.getLocation().y(), player.getLocation().x()));
					}

					//adds all the pacmans in the game to pacman list in this game.
					else if(board_data.get(i1).charAt(0) == 'P') {
						Robot.Packman pacman = new Robot.Packman(board_data.get(i1));
						pList.add(pacman);

						Pixel p = new Pixel(map.Point2Pixel(pacman.getLocation().y(), pacman.getLocation().x()));
						pacmanPixel.add(p);
					}

					//adds all the fruits in the game to fruit list in this game.
					else if(board_data.get(i1).charAt(0) == 'F') {
						Robot.Fruit fruit = new Robot.Fruit(board_data.get(i1));
						fList.add(fruit);

						Pixel f = new Pixel(map.Point2Pixel(fruit.getLocation().y(), fruit.getLocation().x()));
						fruitPixel.add(f);
					}

					//adds all the ghosts in the game to ghost list in this game.
					else if(board_data.get(i1).charAt(0) == 'G') {
						Robot.Packman ghost = new Robot.Packman(board_data.get(i1));
						gList.add(ghost);

						Pixel g = new Pixel(map.Point2Pixel(ghost.getLocation().y(), ghost.getLocation().x()));
						ghostPixel.add(g);
					}
				}
				repaint();
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}

	public class ThreadT2 extends Thread {

		@Override
		public void run() {

			while(play.isRuning()) {

				int FruitList = fList.size();
				int i = 0 ;
				int k = 0;

				Fruit closetFruit = new Fruit(Algorithm.closetFruit(fList, player)); //finds the closet fruit.
				
				Point3D A = new Point3D(player.getLocation().y(), player.getLocation().x());
				Point3D B = new Point3D(closetFruit.getLocation().y(), closetFruit.getLocation().x());
				algo.MAIN(A, B, game);

				while(k < algo.shortestPath.size()) {
					i++;

					if(fList.isEmpty()) {
						play.stop();
					}
					else {
						closetFruit = new Fruit(Algorithm.closetFruit(fList, player)); //finds the closet fruit.

						//computes the azimuth the player should go to eat the closet fruit.
						azimuth(player.getLocation().x(), player.getLocation().y(), algo.shortestPath.get(k).y(), algo.shortestPath.get(k).x());

						play.rotate(azi); 	

						System.out.println("***** Step " + i + " *****");

						//getS the current score of the game.
						String info = play.getStatistics();
						System.out.println(info);

						//getS the game-board current state.
						ArrayList<String> board_data = play.getBoard();

						//clears all before read a new game (except the boxes).
						player = null;
						pList.clear();
						fList.clear();
						gList.clear();
						playerPixel = null;
						pacmanPixel.clear();
						fruitPixel.clear();
						ghostPixel.clear();

						for(int i1 = 0; i1 < board_data.size(); i1++) {

							//updates the player data.
							if(board_data.get(i1).charAt(0) == 'M') {
								player = new Robot.Packman(board_data.get(i1));

								playerPixel = new Pixel(map.Point2Pixel(player.getLocation().y(), player.getLocation().x()));
							}

							//adds all the pacmans in the game to pacman list in this game.
							else if(board_data.get(i1).charAt(0) == 'P') {
								Robot.Packman pacman = new Robot.Packman(board_data.get(i1));
								pList.add(pacman);

								Pixel p = new Pixel(map.Point2Pixel(pacman.getLocation().y(), pacman.getLocation().x()));
								pacmanPixel.add(p);
							}

							//adds all the fruits in the game to fruit list in this game.
							else if(board_data.get(i1).charAt(0) == 'F') {
								Robot.Fruit fruit = new Robot.Fruit(board_data.get(i1));
								fList.add(fruit);

								Pixel f = new Pixel(map.Point2Pixel(fruit.getLocation().y(), fruit.getLocation().x()));
								fruitPixel.add(f);
							}

							//adds all the ghosts in the game to ghost list in this game.
							else if(board_data.get(i1).charAt(0) == 'G') {
								Robot.Packman ghost = new Robot.Packman(board_data.get(i1));
								gList.add(ghost);

								Pixel g = new Pixel(map.Point2Pixel(ghost.getLocation().y(), ghost.getLocation().x()));
								ghostPixel.add(g);
							}
						}
						repaint();
						try {
							Thread.sleep(50);
						}
						catch (InterruptedException e) {

							e.printStackTrace();
						}
						Point3D temp = new Point3D(algo.shortestPath.get(k).y(), algo.shortestPath.get(k).x());
						double tempNum1 = temp.x() * 10000;
						tempNum1 = (int)tempNum1;
						tempNum1 = tempNum1 / 10000;
						double tempNum2 = player.getLocation().x() * 10000;
						tempNum2 = (int)tempNum2;
						tempNum2 = tempNum2 / 10000;


						if (tempNum1 == tempNum2 && k != algo.shortestPath.size() - 1) {
							k++;
						}

						if (fList.size() != FruitList) {
							k++;
						}
					}
				}
			}
		}
	} 

	/*
	 * This function computes the azimuth between 2 coordinates.
	 */
	public void azimuth(double lat1, double lon1, double lat2, double lon2) {

		double longitude1 = lon1;
		double longitude2 = lon2;
		double latitude1 = Math.toRadians(lat1);
		double latitude2 = Math.toRadians(lat2);
		double longDiff = Math.toRadians(longitude2 - longitude1);
		double y = Math.sin(longDiff) * Math.cos(latitude2);
		double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

		azi = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
	}

}
