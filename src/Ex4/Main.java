package Ex4;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Coords.Coords;
import Geom.Point3D;

public class Main {

	public static void main(String[] args) throws IOException {
			
		MyFrame window = new MyFrame();
		window.setTitle("Pacman Game");
		BufferedImage image = ImageIO.read(new File("pacman.png"));
		window.setIconImage(image);
		window.setVisible(true);
		window.setSize(1433, 642);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Coords c = new Coords();
		Point3D p = new Point3D(32.104065774474904, 35.20645616220012, 0);
		Point3D meter = new Point3D(1, 1, 0);
		Point3D ans = c.add(p, meter);
		System.out.println(ans);
	}

}
