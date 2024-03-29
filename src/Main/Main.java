package Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import Pacman_game.MyFrame;

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
	}

}
