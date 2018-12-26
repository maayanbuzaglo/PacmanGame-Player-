package Pacman_game;

import java.util.ArrayList;

/*
 * This class represents a game that mades of pacmans and fruits.
 * @author maayan
 * @author nahama
 */
public class Game {

	public ArrayList<Pacman> Pacman_list; //Pacman list.
	public ArrayList<Fruit> Fruit_list; //Fruit list.
	public ArrayList<Ghost> Ghost_list; //Ghost list.
	public ArrayList<Pacman_game.Box> Box_list; //Box list.

	/*
	 * An empty constructor.
	 */
	public Game() {

		this.Pacman_list = null;
		this.Fruit_list = null;
		this.Ghost_list = null;
		this.Box_list = null;
	}

	/*
	 * Constructor.
	 */
	public Game(ArrayList<Pacman> pacman_list, ArrayList<Fruit> fruit_list, ArrayList<Ghost> ghost_list, ArrayList<Box> box_list) {

		this.Pacman_list = pacman_list;
		this.Fruit_list = fruit_list;
		this.Ghost_list = ghost_list;
		this.Box_list = box_list;
	}

	/*
	 * This function read a csv game file.
	 */
	public void readCsv(String file) {

		Box b = new Box();
		this.Box_list = b.ReadCsvFile(file); //reads the ghosts on the csv file.
		Ghost g = new Ghost();
		this.Ghost_list = g.ReadCsvFile(file); //reads the ghosts on the csv file.
		Pacman p  = new Pacman();
		this.Pacman_list = p.ReadCsvFile(file); //reads the fruits on the csv file.
		Fruit f = new Fruit();
		this.Fruit_list = f.ReadCsvFile(file); //reads the pacmans on the csv file.
	}

	public ArrayList<Pacman> getPacman_list() {
		return Pacman_list;
	}

	public void setPacman_list(ArrayList<Pacman> pacman_list) {
		Pacman_list = pacman_list;
	}

	public ArrayList<Fruit> getFruit_list() {
		return Fruit_list;
	}

	public void setFruit_list(ArrayList<Fruit> fruit_list) {
		Fruit_list = fruit_list;
	}
	
	public ArrayList<Ghost> getGhost_list() {
		return Ghost_list;
	}

	public void setGhost_list(ArrayList<Ghost> ghost_list) {
		Ghost_list = ghost_list;
	}
		
	public ArrayList<Box> getBox_list() {
		return Box_list;
	}

	public void setBox_list(ArrayList<Box> box_list) {
		Box_list = box_list;
	}

}