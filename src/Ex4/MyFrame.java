package Ex4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import Geom.Point3D;
import Pacman_game.Fruit;
import Pacman_game.Game;
import Pacman_game.Ghost;
import Pacman_game.Line;
import Pacman_game.Map;
import Pacman_game.Pacman;
import Pacman_game.Pixel;
import Pacman_game.Player;
import Robot.Play;
import javafx.scene.shape.Box;

/*
 * This class represents the game frame.
 */
public class MyFrame extends JFrame implements MouseListener {

	public Map m;
	public BufferedImage background; //game background image.
	public BufferedImage ghostImage; //ghost icon.
	public BufferedImage playerImage; //player icon.
	public BufferedImage pacmanImage; //pacman icon.
	public BufferedImage fruitImage; //fruit icon.
	public ArrayList<Ghost> gList; //list of Ghost.
	public ArrayList<Pacman> pList; //list of pacmans.
	public ArrayList<Fruit> fList; //list of fruits.
	public ArrayList<Pacman_game.Box> bList; //list of boxes.
	public ArrayList<Pixel> boxPixel1; //boxes pixels list for point 1.
	public ArrayList<Pixel> boxPixel2; //boxes pixels list for point 2.
	public ArrayList<Pixel> boxPixel3; //boxes pixels list for point 3.
	public ArrayList<Pixel> boxPixel4; //boxes pixels list for point 4.
	public ArrayList<Pixel> ghostPixel; //ghost pixels list.
	public Pixel playerPixel; //player pixel.
	public ArrayList<Pixel> pacmanPixel; //pacmans pixels list.
	public ArrayList<Pixel> fruitPixel; //fruits pixel list.
	public Player player;
	public int countPacman; //pacman id.
	public int countFruit; //fruit id.
	public String file;
	private boolean Player; //if true - draws player. else - selects direction.

	/*
	 * An empty constructor.
	 */
	public MyFrame() throws IOException {

		m = new Map();
		player = new Player();
		gList = new ArrayList<Ghost>();
		pList = new ArrayList<Pacman>();
		fList = new ArrayList<Fruit>();
		bList = new ArrayList<Pacman_game.Box>();
		playerPixel = new Pixel();
		ghostPixel = new ArrayList<Pixel>();
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		countPacman = 0;
		countFruit = 0;
		Player = false;

		initGUI();		
		this.addMouseListener(this);
	}

	/*
	 * Constructor.
	 */
	public MyFrame(Game game) throws IOException {

		m = new Map();
		player = this.player;
		gList = game.Ghost_list;
		pList =  game.Pacman_list;
		fList = game.Fruit_list;
		playerPixel = new Pixel();
		ghostPixel = new ArrayList<Pixel>();
		pacmanPixel = new ArrayList<Pixel>();
		fruitPixel = new ArrayList<Pixel>();
		Player = false;

		repaint();
		initGUI();		
		this.addMouseListener(this);
	}

	/*
	 * This function makes the frame.
	 */
	private void initGUI() {

		MenuBar menuBar = new MenuBar();
		Menu game = new Menu("Game"); //Icons - Pacman, Fruit.
		MenuItem readCSV = new MenuItem("Read game");
		MenuItem run = new MenuItem("Run");

		Menu options = new Menu("Options"); //Options - Run, Create kml file, Read game, Save game, Clear.
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

				Game g = new Game();
				//clears all before read a new game.
				pList.clear();
				fList.clear();
				bList.clear();
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
				g.readCsv(place);

				//adds all the pacmans in the game to pacman list in this game.
				for(Pacman it: g.Pacman_list) {
					pList.add(it);

					Pixel p = new Pixel(m.Point2Pixel(it.getLocation().x(),it.getLocation().y()));
					pacmanPixel.add(p);
				}

				//adds all the fruits in the game to fruit list in this game.
				for(Fruit it: g.Fruit_list) {
					fList.add(it);

					Pixel f = new Pixel(m.Point2Pixel(it.getLocation().x(), it.getLocation().y()));
					fruitPixel.add(f);
				}

				//adds all the ghosts in the game to ghost list in this game.
				for(Ghost it: g.Ghost_list) {
					gList.add(it);

					Pixel gh = new Pixel(m.Point2Pixel(it.getLocation().x(),it.getLocation().y()));
					ghostPixel.add(gh);
				}

				//adds all the boxes in the game to ghost list in this game.
				for(Pacman_game.Box it: g.Box_list) {
					bList.add(it);

					Pixel b1 = new Pixel(m.Point2Pixel(it.getLocationStart().x(),it.getLocationStart().y()));
					boxPixel1.add(b1);
					Pixel b2 = new Pixel(m.Point2Pixel(it.getLocationStart().x(),it.getLocationEnd().y()));
					boxPixel1.add(b2);
					Pixel b3 = new Pixel(m.Point2Pixel(it.getLocationEnd().x(),it.getLocationEnd().y()));
					boxPixel1.add(b3);
					Pixel b4 = new Pixel(m.Point2Pixel(it.getLocationEnd().x(),it.getLocationEnd().y()));
					boxPixel1.add(b4);
				}

				Player = true;
				repaint();
			}
		});

		//listens to run key.
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Player = false;
				Play play = new Play(file);
				play.setIDs(314882077, 322093311);

				String map_data = play.getBoundingBox();
				System.out.println("Bounding Box info: " + map_data);

				ArrayList<String> board_data = play.getBoard();
				for(int i = 0; i < board_data.size(); i++) {
					System.out.println(board_data.get(i));
				}

				System.out.println();
				System.out.println("Init Player Location should be set using the bounding box info");

				play.setInitLocation(player.getLocation().x(), player.getLocation().y());

				play.start();

				repaint();
			}
		});

		//listens to clear key.
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pList.clear();
				fList.clear();
				bList.clear();
				pacmanPixel.clear();
				fruitPixel.clear();
				boxPixel1.clear();
				boxPixel2.clear();
				boxPixel3.clear();
				boxPixel4.clear();

				countPacman = 0;
				countFruit = 0;
				repaint();
			}
		});

		//gets the box image.
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
	 * This function paints pacmans and fruits on the game frame.
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

		//upload the game pixels if change the frame size.
		m.changeFrame(pFram, playerPixel, pacmanPixel, fruitPixel, ghostPixel, boxPixel1, boxPixel2, boxPixel3, boxPixel4);

		//changes points of player in game to pixels.
		Pixel playtemp = m.Point2Pixel(player.getLocation().x(), player.getLocation().y());
		playerPixel = new Pixel(playtemp);

		//changes points of pacmans in game to pixels.
		for (int i = 0; i < pList.size(); i++) {
			Pixel pix = m.Point2Pixel(pList.get(i).getLocation().x(), pList.get(i).getLocation().y());
			pacmanPixel.add(pix);
		}

		//changes points of fruits in game to pixels.
		for (int i = 0; i < fList.size(); i++) {
			Pixel pix = m.Point2Pixel(fList.get(i).getLocation().x(), fList.get(i).getLocation().y());
			fruitPixel.add(pix);
		}

		//changes points of ghost in game to pixels. 
		for (int i = 0; i < gList.size(); i++) {
			Pixel pix = m.Point2Pixel(gList.get(i).getLocation().x(), gList.get(i).getLocation().y());
			ghostPixel.add(pix);
		}

		//changes points of lines in game to pixels (point 1).
		for (int i = 0; i < bList.size(); i++) {
			Pixel pix1 = m.Point2Pixel(bList.get(i).getLocationStart().x(), bList.get(i).getLocationStart().y());
			boxPixel1.add(pix1);
			Pixel pix2 = m.Point2Pixel(bList.get(i).getLocationStart().x(), bList.get(i).getLocationEnd().y());
			boxPixel2.add(pix2);
			Pixel pix3 = m.Point2Pixel(bList.get(i).getLocationEnd().x(), bList.get(i).getLocationEnd().y());
			boxPixel3.add(pix3);
			Pixel pix4 = m.Point2Pixel(bList.get(i).getLocationEnd().x(), bList.get(i).getLocationStart().y());
			boxPixel4.add(pix4);
		}

		//draws all the boxes on the list.
		for (int i = 0; i < boxPixel1.size(); i++) {
			double height = boxPixel2.get(i).distance(boxPixel1.get(i));
			double width = boxPixel4.get(i).distance(boxPixel1.get(i));
//			g.drawRect((int)boxPixel1.get(i).getX(), (int)boxPixel3.get(i).getY(), (int)width, (int)height);
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
		g.drawImage(playerImage, (int)playerPixel.getX(), (int)playerPixel.getY(), 60, 40, this);

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
		if(Player) { //draws pacmans where mouse clicked.
			this.playerPixel = new Pixel(p);
			System.out.println(playerPixel);
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


	public  class ThreadPacks extends Thread
	{
		/**
		 * This class run the thread of the pacman.
		 */
		@Override
		public void run() {
			double max =0;
			for (int i = 0; i < pList.size(); i++) {
				if (pList.get(i).getTime() > max) {
					max = pList.get(i).getTime();
				}
			}
			for (int i = 1; i < 1000; i++) {
				repaint();
				for(Pacman it: pList)
				{
					Point3D p = it.When(i, m);

					if (p != null) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for (int j = 0; j < fList.size(); j++) {
							if(fList.get(j).getEndTime() < i) {
								fList.remove(j);
								System.out.println("ok");
							}
						}
					} 
					else if (it.getTime() == max && !fList.isEmpty())
					{
						fList.remove(0);
					}
				}
			}
			repaint();
		}
	}
}
