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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

public class Controller implements Initializable{
	
	
	@FXML
    private Button mainButton;
	@FXML
	private ImageView tileImage;
	@FXML
	private Label actualTileStatus;
	@FXML
	private Label labelMapSource;
	@FXML	
	private ProgressBar lvlProgressBar;
	
	@FXML
	void runImageCrawler(ActionEvent event){
		startImageProcessingTask();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
        System.out.println("GUI Controller started.");
	}
	
	
	private void startImageProcessingTask() {
		Runnable task = new Runnable() {
			public void run() {
				runImageProcessingTask();
			}
		};
		Thread backgroundThread = new Thread(task);
		backgroundThread.setDaemon(true);
		backgroundThread.start();
	}	

	private void runImageProcessingTask() {
        System.out.println("Map tiling started.");
        Platform.runLater(new Runnable() {
        	@Override 
            public void run() {
        		mainButton.setText("Working");
        		}
        });

  		ConfigReader configReader = new ConfigReader("resources/config.json");
  		System.out.println("map slices: "+configReader.getZoomLvls());

  		for (int i=0; i<configReader.getZoomLvls(); i++){
  			int zoomLvl = configReader.getItyZoomLvl(i);
  			System.out.println(zoomLvl);
  			String imgPath = configReader.getItyImgPath(i);
  			System.out.println(imgPath);
  	        Platform.runLater(new Runnable() {
  	        	@Override 
  	            public void run() {
  	        		labelMapSource.setText(imgPath.substring(imgPath.length()-36, imgPath.length()));
  	        		}
  	        });

  			// Crawler
  			BufferedImage img;
				try {
					img = Crawler.loadImage(imgPath);
					Crawler.crawle(img, zoomLvl, tileImage, actualTileStatus, lvlProgressBar);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
       			// End of Crawler
  		}
  		System.out.println("Mini images saved. EOP");
        Platform.runLater(new Runnable() {
        	@Override 
            public void run() {
        		mainButton.setText("Start");
        		}
        });
	}

}
