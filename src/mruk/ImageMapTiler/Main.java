package mruk.ImageMapTiler;

import java.awt.image.BufferedImage;
import java.io.*;
//import javax.swing.*; 

public class Main {

	public static void main(String[] args) throws IOException {

        //SWING GUI
		//createAndShowGUI();
        //End of SWING GUI        
		
		// TODO - make ConfigLoader
		// End of ConfigLoader

		// ImageLoader
		BufferedImage image = Crawler.loadImage("/Users/makpiec/Dropbox/lightwork/a-map/MapaRosyjska_1.5k.png");
		// End of ImageLoader

		// TODO - make Crawler
		Crawler.crawle(image, "config");
		// End of Crawler
		
		System.out.println("Mini images saved. EOP");
	}
	
	/*
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TileGeneratorMain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Display the window.
        frame.setSize(500, 300);
        frame.setVisible(true);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Tile gen v0.001");
        frame.getContentPane().add(label);
        
        frame.setVisible(true);

    }
    */
}