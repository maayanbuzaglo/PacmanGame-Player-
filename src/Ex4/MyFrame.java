package Ex4;

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
import Pacman_game.Map;
import Pacman_game.Pixel;
import Robot.Game;
import Robot.Packman;
import Robot.Play;

/*
 * This class represents the game frame.
 */
public class MyFrame extends JFrame implements MouseListener {

	Play play;
	Robot.Game g;
	public Map m;
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
	public ArrayList<Pixel> boxPixel1; //boxes pixels list for point 1.
	public ArrayList<Pixel> boxPixel2; //boxes pixels list for point 2.
	public ArrayList<Pixel> boxPixel3; //boxes pixels list for point 3.
	public ArrayList<Pixel> boxPixel4; //boxes pixels list for point 4.
	public ArrayList<Pixel> ghostPixel; //ghost pixels list.
	public ArrayList<Pixel> pacmanPixel; //pacmans pixels list.
	public ArrayList<Pixel> fruitPixel; //fruits pixel list.
	public Robot.Packman player;
	public int countPacman; //pacman id.
	public int countFruit; //fruit id.
	public String file;
	private boolean PlayerOn = false; //if true - draws player. else - nothing.
	private boolean ReadGameOn = false; //if true - draws player. else - need to read game.

	/*
	 * An empty constructor.
	 */
	public MyFrame() throws IOException {

		m = new Map();
		g = new Game();
		player = null;
		gList = new ArrayList<Robot.Packman>();
		pList = new ArrayList<Robot.Packman>();
		fList = new ArrayList<Robot.Fruit>();
		bList = new ArrayList<GeoBox>();
		playerPixel = new Pixel();
		ghostPixel = new ArrayList<Pixel>();
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		countPacman = 0;
		countFruit = 0;
		PlayerOn = false;
		play = new Play() ; 
		initGUI();		
		this.addMouseListener(this);
	}

	/*
	 * Constructor.
	 */
	public MyFrame(Robot.Game game) throws IOException {

		m = new Map();
		player = g.getPlayer();
		gList = g.getGhosts();
		pList =  g.getRobots();
		fList = g.getTargets();
		playerPixel = new Pixel();
		ghostPixel = new ArrayList<Pixel>();
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		PlayerOn = false;
		for (int i = 0; i < g.sizeB(); i++) {
			bList.add(g.getBox(i));
		}

		repaint();
		initGUI();		
		this.addMouseListener(this);
	}

	/*
	 * This function makes the frame.
	 */
	private void initGUI() {

		MenuBar menuBar = new MenuBar();
		Menu game = new Menu("Game"); //Game - Read game, Run.
		MenuItem readCSV = new MenuItem("Read game");
		MenuItem run = new MenuItem("Run");

		Menu options = new Menu("Options"); //Options - Clear.
		MenuItem clear = new MenuItem("Clear");

		menuBar.add(game);
		game.add(readCSV);
		game.add(run);

		menuBar.add(options);
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
				playerPixel = null;
				pacmanPixel.clear();
				fruitPixel.clear();
				boxPixel1.clear();
				boxPixel2.clear();
				boxPixel3.clear();
				boxPixel4.clear();
				String place = "";
				JButton open = new JButton();
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Choose A Pacman Game");
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					place = fc.getSelectedFile().getAbsolutePath();
				}
				file = place;
				Play play = new Play(file);

				play.setIDs(314882077, 322093311);

				String map_data = play.getBoundingBox();
				System.out.println("Bounding Box info: " + map_data);

				ArrayList<String> board_data = play.getBoard();
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa"+ board_data +   "aaaa");
				for(int i = 0; i < board_data.size(); i++) {
					System.out.println(board_data.get(i));
				}

				for(int i = 0; i < board_data.size(); i++) {

					//updates the player data.
					if(board_data.get(i).charAt(0) == 'M') {
						Robot.Packman player = new Robot.Packman(board_data.get(i));

						playerPixel = new Pixel(m.Point2Pixel(player.getLocation().y(), player.getLocation().x()));
					}

					//adds all the pacmans in the game to pacman list in this game.
					if(board_data.get(i).charAt(0) == 'P') {
						Robot.Packman pacman = new Robot.Packman(board_data.get(i));
						pList.add(pacman);

						Pixel p = new Pixel(m.Point2Pixel(pacman.getLocation().y(), pacman.getLocation().x()));
						pacmanPixel.add(p);
					}

					//adds all the fruits in the game to fruit list in this game.
					else if(board_data.get(i).charAt(0) == 'F') {
						Robot.Fruit fruit = new Robot.Fruit(board_data.get(i));
						fList.add(fruit);

						Pixel f = new Pixel(m.Point2Pixel(fruit.getLocation().y(), fruit.getLocation().x()));
						fruitPixel.add(f);
					}

					//adds all the ghosts in the game to ghost list in this game.
					else if(board_data.get(i).charAt(0) == 'G') {
						Robot.Packman ghost = new Robot.Packman(board_data.get(i));
						gList.add(ghost);

						Pixel g = new Pixel(m.Point2Pixel(ghost.getLocation().y(), ghost.getLocation().x()));
						ghostPixel.add(g);
					}

					//adds all the boxes in the game to ghost list in this game.
					else if(board_data.get(i).charAt(0) == 'B') {
						GeoBox box = new GeoBox(board_data.get(i));
						bList.add(box);

						Pixel b1 = new Pixel(m.Point2Pixel(box.getMin().y(), box.getMin().x()));
						boxPixel1.add(b1);
						Pixel b2 = new Pixel(m.Point2Pixel(box.getMin().y(), box.getMax().x()));
						boxPixel1.add(b2);
						Pixel b3 = new Pixel(m.Point2Pixel(box.getMax().y(), box.getMax().x()));
						boxPixel1.add(b3);
						Pixel b4 = new Pixel(m.Point2Pixel(box.getMax().y(), box.getMax().x()));
						boxPixel1.add(b4);
					}

				}

				ReadGameOn = true;
				PlayerOn = true;
				repaint();
			}
		});

		//listens to run key.
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//if a game was not chosen.
				if(ReadGameOn == false) {
					System.out.println("You must choose a game.");
				}
				else {
					play = new Play(file);
					play.setIDs(314882077, 322093311);

					String map_data = play.getBoundingBox();
					System.out.println("Bounding Box info: " + map_data);

					ArrayList<String> board_data = play.getBoard();
					System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa"+ board_data +   "aaaa1");
					for(int i = 0; i < board_data.size(); i++) {
						System.out.println(board_data.get(i));
					}

					//if a player was not chosen.
					if(player == null) {
						System.out.println("You must place the player.");
					}
					else {
						play.setInitLocation(player.getLocation().x() , player.getLocation().y());

						//starts the "server".
						play.start(); //default max time is 100 seconds (1000*100 ms).

						//plays as long as there are "fruits" and time.
						ThreadT S = new ThreadT() ; 
						S.start();
				
						//stops the server - not needed in the real implementation.
						//play1.stop();
						System.out.println("**** Done Game (user stop) ****");

						//prints the data & save to the course DB.
						String info = play.getStatistics();
						System.out.println(info);
						repaint();
						PlayerOn = false;
					}
				}
			}
		});

		//listens to clear key.
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				pList.clear();
				fList.clear();
				gList.clear();
				bList.clear();
				player = null;
				playerPixel = null;
				pacmanPixel.clear();
				fruitPixel.clear();
				ghostPixel.clear();
				boxPixel1.clear();
				boxPixel2.clear();
				boxPixel3.clear();
				boxPixel4.clear();

				countPacman = 0;
				countFruit = 0;
				ReadGameOn = false;
				PlayerOn = false;
				repaint();
			}
		});

		//gets the ghost image.
		try {
			ghostImage = ImageIO.read(new File("C:\\Users\\מעיין\\eclipse-workspace\\PacmanGame\\pictures\\ghost.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//gets the player image.
		try {
			playerImage = ImageIO.read(new File("C:\\Users\\מעיין\\eclipse-workspace\\PacmanGame\\pictures\\player.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//gets the pacman image.
		try {
			pacmanImage = ImageIO.read(new File("C:\\Users\\מעיין\\eclipse-workspace\\PacmanGame\\pictures\\pacman.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//gets the fruit image.
		try {
			fruitImage = ImageIO.read(new File("C:\\Users\\מעיין\\eclipse-workspace\\PacmanGame\\pictures\\fruit.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	int x = -1;
	int y = -1;

	/*
	 * This function paints pacmans, fruits, ghosts and boxes on the game frame.
	 * @see java.awt.Window#paint(java.awt.Graphics).
	 */
	public void paint(Graphics g) {

		g.drawImage(m.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		Pixel pFram = new Pixel(this.getWidth(), this.getHeight());
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		ghostPixel = new ArrayList<Pixel>();
		boxPixel1 = new ArrayList<Pixel>();
		boxPixel2 = new ArrayList<Pixel>();
		boxPixel3 = new ArrayList<Pixel>();
		boxPixel4 = new ArrayList<Pixel>();

		//uploads the game pixels if change the frame size.
		playerPixel = m.changeFrame(pFram, playerPixel, pacmanPixel, fruitPixel, ghostPixel, boxPixel1, boxPixel2, boxPixel3, boxPixel4);

		//changes points of pacmans in game to pixels.
		for (int i = 0; i < pList.size(); i++) {
			Pixel pix = m.Point2Pixel(pList.get(i).getLocation().y(), pList.get(i).getLocation().x());
			pacmanPixel.add(pix);
		}

		//changes points of fruits in game to pixels.
		for (int i = 0; i < fList.size(); i++) {
			Pixel pix = m.Point2Pixel(fList.get(i).getLocation().y(), fList.get(i).getLocation().x());
			fruitPixel.add(pix);
		}

		//changes points of ghost in game to pixels. 
		for (int i = 0; i < gList.size(); i++) {
			Pixel pix = m.Point2Pixel(gList.get(i).getLocation().y(), gList.get(i).getLocation().x());
			ghostPixel.add(pix);
		}

		//changes points of boxes in game to pixels.
		for (int i = 0; i < bList.size(); i++) {
			Pixel pix1 = m.Point2Pixel(bList.get(i).getMin().y(), bList.get(i).getMin().x());
			boxPixel1.add(pix1);
			Pixel pix2 = m.Point2Pixel(bList.get(i).getMin().y(), bList.get(i).getMax().x());
			boxPixel2.add(pix2);
			Pixel pix3 = m.Point2Pixel(bList.get(i).getMax().y(), bList.get(i).getMax().x());
			boxPixel3.add(pix3);
			Pixel pix4 = m.Point2Pixel(bList.get(i).getMax().y(), bList.get(i).getMin().x());
			boxPixel4.add(pix4);
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
			g.drawImage(playerImage, (int)playerPixel.getX(), (int)playerPixel.getY(), 60, 40, this);
		}

		//draws all the ghost on the list. 
		for (int i = 0; i < ghostPixel.size(); i++) {
			g.drawImage(ghostImage, (int)ghostPixel.get(i).getX(), (int)ghostPixel.get(i).getY(), 60, 40, this);
		}

	}

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
			Point3D temp = m.Pixel2Point(p);
			Coords.LatLonAlt point = new LatLonAlt(temp.y(), temp.x(), 0);
			System.out.println(point);
			this.player = new Packman(point, 2);
			System.out.println(point);
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

	
	public class ThreadT extends Thread
	{
		@Override
		public void run() {
			int i = 0 ; 
			while(play.isRuning()) {
				i++;
			
				//This is the main command to the player (on the server side).
				play.rotate(36*i); 
				System.out.println("***** Step " + i + " *****");

				//getS the current score of the game.
				String info = play.getStatistics();
				System.out.println(info);

				//getS the game-board current state.
				ArrayList<String> board_data = play.getBoard();
				for(int a = 0; a   < board_data.size(); a++) {
					System.out.println(board_data.get(a));
				}

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

						playerPixel = new Pixel(m.Point2Pixel(player.getLocation().y(), player.getLocation().x()));
					}

					//adds all the pacmans in the game to pacman list in this game.
					else if(board_data.get(i1).charAt(0) == 'P') {
						Robot.Packman pacman = new Robot.Packman(board_data.get(i1));
						pList.add(pacman);

						Pixel p = new Pixel(m.Point2Pixel(pacman.getLocation().y(), pacman.getLocation().x()));
						pacmanPixel.add(p);
					}

					//adds all the fruits in the game to fruit list in this game.
					else if(board_data.get(i1).charAt(0) == 'F') {
						Robot.Fruit fruit = new Robot.Fruit(board_data.get(i1));
						fList.add(fruit);

						Pixel f = new Pixel(m.Point2Pixel(fruit.getLocation().y(), fruit.getLocation().x()));
						fruitPixel.add(f);
					}

					//adds all the ghosts in the game to ghost list in this game.
					else if(board_data.get(i1).charAt(0) == 'G') {
						Robot.Packman ghost = new Robot.Packman(board_data.get(i1));
						gList.add(ghost);

						Pixel g = new Pixel(m.Point2Pixel(ghost.getLocation().y(), ghost.getLocation().x()));
						ghostPixel.add(g);
					}
				}
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa"+ board_data +   "aaaa");
				repaint();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		
	}	
}
