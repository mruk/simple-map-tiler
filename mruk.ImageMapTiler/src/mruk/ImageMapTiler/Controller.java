package mruk.ImageMapTiler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

public class Controller implements Initializable{
	@FXML
    private Button startCrawleButton;
	@FXML
	private ImageView tileImageView;
	@FXML
	private Label actualTileStatus;
	@FXML
	private Label labelMapSource;
	@FXML	
	private ProgressBar lvlProgressBar;
	
	@FXML
	void runImageCrawler(ActionEvent event){
		startImgProcessingTask();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
        System.out.println("GUI Controller started.");
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
		int PASSED_CHARS = 7;
  	    setControlLabeledText(startCrawleButton, "Working", PASSED_CHARS);

  		ConfigReader configReader = new ConfigReader("resources/config.json");

  		iterateOverZoomLvls(configReader);
  		
  		ConsoleTrace.log("Mini images saved. EOP");
        Platform.runLater(new Runnable() {
        	@Override 
            public void run() {
        		startCrawleButton.setText("Start");
        		}
        });
	}

	/**
	 * @param configReader
	 */
	private void iterateOverZoomLvls(ConfigReader configReader) {
		for (int i=0; i<configReader.getZoomLvls(); i++){
  			iterationOverOneZoomLvl(configReader, i);
  		}
	}

	/**
	 * @param configReader
	 * @param i
	 */
	private void iterationOverOneZoomLvl(ConfigReader configReader, int i) {
		int zoomLvl = configReader.getItyZoomLvl(i);
		String imgPath = configReader.getItyImgPath(i);
		
		ConsoleTrace.log("zoom lvl: "+zoomLvl);
		ConsoleTrace.log("img path: "+imgPath);

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
		    setControlLabeledText(labelMapSource, imgPath, 42);
		    
			Crawler.crawle(sourceMap, zoomLvl, tileImageView, actualTileStatus, lvlProgressBar);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param txt
	 */
	void setControlLabeledText(Control control, String txt, int slicedChars) {
		Platform.runLater(new Runnable() {
			@Override 
		    public void run() {
				String slicedText = txt.substring( txt.length()-slicedChars , txt.length());
				((Labeled) control).setText(slicedText);
				}
		});
	}

}
