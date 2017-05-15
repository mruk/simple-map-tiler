package mruk.ImageMapTiler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

class Crawler {
	BufferedImage image;
	static int TILE_SIZE = 256;

	Crawler (BufferedImage image, ConfigReader config){
		this.image = image;
	}

	BufferedImage getTile(){
		BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, 0);
		// TODO - getTile logic
		return tile;
	}

	static BufferedImage loadImage(String url) throws IOException{
        File file = new File(url);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = null;
		try {
			image = ImageIO.read(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}

	static void saveImage(BufferedImage tile, String url, String fileFormat) throws IOException{
        ImageIO.write(tile, fileFormat, new File(url+"."+fileFormat));
        ConsoleTrace.log(url+" Saved...");
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
