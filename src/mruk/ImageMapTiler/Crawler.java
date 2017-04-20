package mruk.ImageMapTiler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

class Crawler {

	BufferedImage image;
	//TODO - przejść ze String na ConfigReader
	String config;
	static int TILE_SIZE = 256;


	Crawler (BufferedImage image, String config){
		this.image = image;
	}

	static void crawle(BufferedImage image, int zoomLvl) throws IOException{
		int rows = tileAutoRes(image.getHeight());
		int cols = tileAutoRes(image.getWidth());
		System.out.println("Image read...");

		// folder containing one zoom lvl
		File mainFolder = new File("resources/" + zoomLvl);
		if (!mainFolder.exists()) {
			if (mainFolder.mkdir()) {
				System.out.println("- "+ mainFolder + " is created.");
			} else {
				System.out.println("mkdir() Failed: "+ mainFolder.getPath());
			}
		}
		
		BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, image.getType());
		Graphics2D gr = tile.createGraphics();
		
		// y folders with x tiles
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				File directory = new File(mainFolder + "/" + x);
				if (!directory.exists()) {
					if (directory.mkdir()) {
						System.out.println("- "+ directory.getPath() + " is created.");
					} else {
						System.out.println("mkdir() Failed: "+ directory.getPath());
					}
				}
				if (directory.exists()){
					gr.drawImage(image, 0, 0, TILE_SIZE, TILE_SIZE, TILE_SIZE * x, TILE_SIZE * y, 
							TILE_SIZE * (x + 1), TILE_SIZE * (y + 1), null);
					
					String outUrl = mainFolder + "/" + x + "/" + y;
					saveImage(tile, outUrl, "png");
				}
			}
		}
		gr.dispose();
	}

	BufferedImage getTile(){
		BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, 0);
		// TODO - getTile logic
		return tile;
	}

	static BufferedImage loadImage(String url) throws IOException{
        File file = new File(url);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis);
		return image;
	}

	static void saveImage(BufferedImage tile, String url, String fileFormat) throws IOException{
        ImageIO.write(tile, fileFormat, new File(url+"."+fileFormat));
        System.out.println(url+" Saved...");
	}

	static int tileAutoRes(int x){
		/**
		 * tile resolution is the size of the image in tiles
		 *
		 */
		double out = Math.ceil( ((100f*x)/TILE_SIZE)/100f );
		return (int) (out);
	}

}
