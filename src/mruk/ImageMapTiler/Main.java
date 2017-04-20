package mruk.ImageMapTiler;

import java.awt.image.BufferedImage;
import java.io.*;
//import javax.swing.*; 

public class Main {
	
	
	public static void main(String[] args) throws IOException {

        //SWING GUI
		//createAndShowGUI();
        //End of SWING GUI 
		ConfigReader configReader = new ConfigReader("resources/config.json");
		System.out.println("map slices: "+configReader.getZoomLvls());
		
		for (int i=0; i<configReader.getZoomLvls(); i++){
			int zoomLvl = configReader.getItyZoomLvl(i);
			System.out.println(zoomLvl);

			String imgPath = configReader.getItyImgPath(i);
			System.out.println(imgPath);
			// Crawler
			BufferedImage img = Crawler.loadImage(imgPath);
			Crawler.crawle(img, zoomLvl);
			// End of Crawler
		}	
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