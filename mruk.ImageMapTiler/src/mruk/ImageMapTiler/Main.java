package mruk.ImageMapTiler;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
    public void start(final Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
	    
        Scene scene = new Scene(root, 720, 560);
    
        primaryStage.setTitle("Simple Map Tiler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}