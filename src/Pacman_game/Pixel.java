package Pacman_game;

/*
 * This class represents a pixel in the game.
 * @author maayan
 * @author nahama
 */
public class Pixel {

	private double x;
	private double y;
	public int value;

	/*
	 * An empty constructor.
	 */
	public Pixel() {
		
		this.x = 0;
		this.y = 0;
		this.value = 0;
	}
	
	/*
	 * Constructor.
	 */
	public Pixel(double dx, double dy) {
		
		this.x = dx;
		this.y = dy;
		this.value = 0;

	}
	
	/*
	 * Constructor.
	 */
	public Pixel(double dx, double dy, int dvalue) {
		
		this.x = dx;
		this.y = dy;
		this.value = dvalue;

	}

	/*
	 * Constructor.
	 */
	public Pixel(Pixel p) {
		
		this.x = p.x;
		this.y = p.y;
		this.value = p.value;

	}

	/*
	 * This function checks if 2 pixels are equal.
	 */
	public boolean equals(Pixel arg0) {
		
		if (this.getX() != arg0.getX() || this.getY() != arg0.getY())
			return false;
		return true;
	}
	
	/*
	 * This function calculate the distance between 2 pixels.
	 */
	public double distance(Pixel p) {
		
		double ans = Math.pow(this.getX() - p.getX(), 2) + Math.pow(this.getY() - p.getY(), 2);
		return Math.sqrt(ans);
	}
	
	@Override
	public String toString() {
		return "Pixel [X = " + x +
				", Y = " + y + "]\n";
	}

	public double getX() {
		return this.x;
	}

	public double getY() {

		return this.y;
	}
	
	public void setX(double d) {
		this.x = d;
	}

	public void setY(double d) {
		this.y = d;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	
}