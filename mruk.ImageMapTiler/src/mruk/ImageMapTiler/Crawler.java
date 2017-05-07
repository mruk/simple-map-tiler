package mruk.ImageMapTiler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Crawler {
	BufferedImage image;
	//TODO - przejść ze String na ConfigReader
	String config;
	static int TILE_SIZE = 256;

	Crawler (BufferedImage image, ConfigReader config){
		this.image = image;
	}

	static void crawle(BufferedImage image, int zoomLvl, ImageView tileImage, Label actTileStatus, ProgressBar lvlProgressBar)
			throws IOException {
		int rows = tileAutoRes(image.getHeight());
		int cols = tileAutoRes(image.getWidth());

		File zoomLvlFolder = makeChildFolder(new File("resources"), zoomLvl);
		
		BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, image.getType());
		Graphics2D g2d = tile.createGraphics();
		
		// y-folders with x-tiles
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				File rowDir = makeChildFolder(zoomLvlFolder, x);
				if (rowDir.exists()) {
					g2d.drawImage(image, 0, 0, TILE_SIZE, TILE_SIZE, TILE_SIZE * x, TILE_SIZE * y, 
							TILE_SIZE * (x + 1), TILE_SIZE * (y + 1), null);
					
					Image fxImage = SwingFXUtils.toFXImage(tile, null);
					tileImage.setImage(fxImage);
					
					String outUrl = zoomLvlFolder + "/" + x + "/" + y;
					guiUpdate(actTileStatus, lvlProgressBar, rows, cols, x, y, outUrl);
					saveImage(tile, outUrl, "png");
				}
			}
		}
		g2d.dispose();
	}

	private static void guiUpdate(Label actualTileStatus, ProgressBar lvlProgressBar, int rows, int cols, int x, int y,
			String outUrl) {
		Double val = (double) (x * rows + y)/(cols * rows);
		Platform.runLater(new Runnable() {
			@Override 
		    public void run() {
				actualTileStatus.setText(outUrl);
				lvlProgressBar.setProgress(val);
				}
		});
	}

	private static File makeChildFolder(File source, int subFolder) {
		File newFile = new File(source + "/" + subFolder);
		//makeConcreteFolder(newFile);
		if (!newFile.exists()) {
			if (newFile.mkdir()) {
				ConsoleTrace.log("- "+ newFile.getPath() + " is created.");
			} else {
				ConsoleTrace.log("mkdir() Failed: "+ newFile.getPath());
			}
		}
		return newFile;
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
