package mruk.ImageMapTiler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller implements Initializable {
	@FXML
	private Button loadConfigButton;
	@FXML
	private Button startCrawleButton;
	@FXML
	private ImageView tileImageView;
	@FXML
	private Label actualTileStatus;
	@FXML
	private Label labelMapSource;
	@FXML
	private Label labelConfigSource;
	@FXML
	private ProgressBar lvlProgressBar;
	@FXML
	private ProgressBar overallProgressBar;

	@FXML
	void runImageCrawler(ActionEvent event) {
		startImgProcessingTask();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("GUI Controller started.");
		
		String message = RamData.getRamData().getConfigStatus();
		setControlLabeledText(labelConfigSource, message, message.length());
	}

	private void startImgProcessingTask() {
		Runnable imgProcessingTask = new Runnable() {
			public void run() {
				runImgProcessingTask();
			}
		};
		Thread backgroundThread = new Thread(imgProcessingTask);
		backgroundThread.setDaemon(true);
		backgroundThread.start();
	}

	private void runImgProcessingTask() {
		ConsoleTrace.log("Map tiling started.");
		
		setControlLabeledText(startCrawleButton, "Working");
		iterateOverZoomLvls(RamData.getRamData().config);
		setControlLabeledText(startCrawleButton, "Start");
		
		ConsoleTrace.log("Mini images saved. EOP");
	}

	/**
	 * @param configReader
	 */
	private void iterateOverZoomLvls(ConfigReader configReader) {
		for (int i = 0; i < configReader.getAmountOfZoomLvls(); i++) {
			iterationOverOneZoomLvl(configReader, i);
			Double val = 0.01 * (double) (100*(i+1)/configReader.getAmountOfZoomLvls());
			overallProgressBar.setProgress(val);
		}
	}

	/**
	 * @param configReader
	 * @param i
	 */
	private void iterationOverOneZoomLvl(ConfigReader configReader, int i) {
		int zoomLvl = configReader.getItyZoomLvl(i);
		String imgPath = configReader.getItyImgPath(i);

		ConsoleTrace.log("zoom lvl: " + zoomLvl);
		ConsoleTrace.log("img path: " + imgPath);

		callingConcreteCrawler(zoomLvl, imgPath);
	}

	/**
	 * @param zoomLvl
	 * @param imgPath
	 */
	private void callingConcreteCrawler(int zoomLvl, String imgPath) {
		BufferedImage sourceMap;
		try {
			String message = "Loading lvl image...";
			setControlLabeledText(labelMapSource, message, message.length());
			sourceMap = Crawler.loadImage(imgPath);
			setControlLabeledText(labelMapSource, imgPath, 36);

			crawle(sourceMap, zoomLvl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void crawle(BufferedImage lvlImage, int zoomLvl) throws IOException {
		int rows = tileAutoRes(lvlImage.getHeight());
		int cols = tileAutoRes(lvlImage.getWidth());

		File zoomLvlFolder = makeChildFolder(new File("resources"), zoomLvl);

		BufferedImage tile = new BufferedImage(256, 256, lvlImage.getType());
		Graphics2D g2d = tile.createGraphics();

		// y-folders with x-tiles
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				File rowDir = makeChildFolder(zoomLvlFolder, x);
				if (rowDir.exists()) {
					g2d.drawImage(lvlImage, 0, 0, 256, 256, 256 * x, 256 * y, 256 * (x + 1), 256 * (y + 1), null);

					Image fxImage = SwingFXUtils.toFXImage(tile, null);
					tileImageView.setImage(fxImage);

					String outUrl = zoomLvlFolder + "/" + x + "/" + y;
					guiUpdate(rows, cols, x, y, outUrl);
					saveImage(tile, outUrl, "png");
				}
			}
		}
		g2d.dispose();
	}

	private File makeChildFolder(File source, int subFolder) {
		File newFile = new File(source + "/" + subFolder);
		// makeConcreteFolder(newFile);
		if (!newFile.exists()) {
			if (newFile.mkdir()) {
				ConsoleTrace.log("- " + newFile.getPath() + " is created.");
			} else {
				ConsoleTrace.log("mkdir() Failed: " + newFile.getPath());
			}
		}
		return newFile;
	}

	private void saveImage(BufferedImage tile, String url, String fileFormat) throws IOException {
		ImageIO.write(tile, fileFormat, new File(url + "." + fileFormat));
		ConsoleTrace.log(url + " Saved...");
	}

	private void guiUpdate(int rows, int cols, int x, int y, String outUrl) {
		Double val = (double) (x * rows + y) / (cols * rows);
		RamData.getRamData().setLvlProgress(val);

		updateLvlProgressBarValue(outUrl);
	}
	
	void updateLvlProgressBarValue(String outUrl) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				actualTileStatus.setText(outUrl);
				lvlProgressBar.setProgress(RamData.getRamData().getLvlProgress());
			}
		});

	}

	/**
	 * @param control
	 * @param txt
	 * @param slicedChars
	 */
	void setControlLabeledText(Control control, String txt, int slicedChars) {
		String slicedText = txt.substring(txt.length() - slicedChars, txt.length());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((Labeled) control).setText(slicedText);
			}
		});
	}
	void setControlLabeledText(Control control, String txt) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((Labeled) control).setText(txt);
			}
		});
	}
	
	private int tileAutoRes(int x) {
		/**
		 * tile resolution is the size of the image in tiles
		 *
		 */
		double out = Math.ceil(((100f * x) / 256) / 100f);
		return (int) (out);
	}

}
