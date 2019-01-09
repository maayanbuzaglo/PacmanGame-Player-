package Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Geom.Point3D;
import Pacman_game.Map;
import Pacman_game.MyFrame;
import Pacman_game.Pixel;
import Robot.Game;

/*
 * Main class.
 */
public class Main {

	public static void main(String[] args) throws IOException {
		
		MyFrame window = new MyFrame();
		window.setTitle("Pacman Game");
		BufferedImage image = ImageIO.read(new File("pacman.png"));
		window.setIconImage(image);
		window.setVisible(true);
		window.setSize(1433, 642);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Map m = new Map();
		Pixel p = new Pixel(662, 545);
		Point3D ans = m.Pixel2Point(p);
		Point3D poi = new Point3D(35.20751574428877, 32.10206734509563, 0);
	}

}
