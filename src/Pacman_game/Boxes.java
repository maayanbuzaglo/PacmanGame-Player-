package Pacman_game;

import java.util.ArrayList;

import Coords.GeoBox;

public class Boxes {
	/**
	 * This class represent a list of Boxes.
	 */

	public ArrayList<GeoBox> boxes; //list of boxes.
	
	/**
	 * Constructor of Boxes
	 */
	public Boxes() {
		boxes = new ArrayList<GeoBox>();
	}
	
	/**
	 * This function add a box to the Array List
	 * @param b represent a box
	 */
	public void add (GeoBox b) {
		this.add(b);
	}

	@Override
	public String toString() {
		return "Boxes [boxes=" + boxes + "]";
	}
	
	
}
