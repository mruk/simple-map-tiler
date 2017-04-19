package mruk.ImageMapTiler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

class Crawler {
	
	BufferedImage image;
	//TODO - przejść ze String na JSONObject
	String config;
	static int TILE_SIZE = 256;
	
	
	Crawler (BufferedImage image, String config){
		this.image = image;
	}
	
	static void crawle(BufferedImage image, String config) throws IOException{
		int rows = tileAutoRes(image.getHeight());
		int cols = tileAutoRes(image.getWidth());
		System.out.println("Image read...");

		
		BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, image.getType());
		Graphics2D gr = tile.createGraphics();
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				gr.drawImage(image, 0, 0, TILE_SIZE, TILE_SIZE, TILE_SIZE * x, TILE_SIZE * y, 
						TILE_SIZE * (x + 1), TILE_SIZE * (y + 1), null);

				File directory = new File("resources/" + x);
				if (!directory.exists()) {
					if (directory.mkdir()) {
						System.out.println("- resources/" + x + " is created.");
					} else {
						System.out.println("Failed to create directory!");
					}
				} 
				if (directory.exists()){
					String outUrl = "resources/" + x + "/" + y;
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
