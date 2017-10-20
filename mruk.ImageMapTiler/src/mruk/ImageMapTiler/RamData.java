package mruk.ImageMapTiler;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class RamData {
	
	private volatile static RamData ramData;
	static ConfigReader cr;
	private Double overallProgressValue, lvlProgressValue;
	private static String MAIN_CONFIG_PATH = "resources/config.json";
	
	private Observable projectOutput;
	private ObservableList<Slice> slices = FXCollections.observableArrayList();
	
	private RamData(){
		cr = new ConfigReader(MAIN_CONFIG_PATH);
		setOutputPath(cr);
		populateSlices(cr);

		overallProgressValue = 0D;
		lvlProgressValue = 0D;
	}

	/**
     * Constructor, or external class, can invoke a rebuild of Slices data. 
	 * @param c 
     * 
     */
	void populateSlices(ConfigReader c){
		for (int i=0 ; i<c.getAmountOfZoomLvls(); i++){
			Slice slice = new Slice();
			slice.setZoomLvl(c.getItyZoomLvl(i));
			slices.add(slice);
		}
	}
	
	/**
     * Returns the data as an observable list of Slices. 
     * @return
     */
    public ObservableList<Slice> getSlices() {
        return slices;
    }
    
	/**
     * Returns the project output path. 
     * @return
     */
    public Observable getOutputPath() {
        return projectOutput;
    }

	/**
     * Sets the project output path. 
     * @return
     */
    public void setOutputPath(ConfigReader c) {
    	// TODO: define a getter in ConfigReader
    	String str = "resources";
    	projectOutput = new SimpleStringProperty(str);
    }
    
	String getMainConfigPath() {
		return MAIN_CONFIG_PATH;
	}
	
	String getConfigStatus(){
		
		if (cr==null) return "Config not loaded.";
		if (cr!=null) return "Config ready: "+MAIN_CONFIG_PATH;
		
		return "ERR: unexpected Config state";
	}

	void setMainConfigPath(String mainConfigPath) {
		RamData.MAIN_CONFIG_PATH = mainConfigPath;
	}

	Double getOverallProgress() {
		return overallProgressValue;
	}

	void setOverallProgress(Double val) {
		this.overallProgressValue = val;
	}

	Double getLvlProgress() {
		return lvlProgressValue;
	}

	void setLvlProgress(Double val) {
		this.lvlProgressValue = val;
		ConsoleTrace.log("RamData.setLvlProgressValue: " + lvlProgressValue);
	}

	public static RamData getRamData() {
        if(ramData==null) {
            synchronized(RamData.class){
                if(ramData == null) {
                	ramData = new RamData();
                }
            }
        }
        return ramData;
    }
	

	/**
	 * Model class containing one zoom-level information.
	 *
	 * @author Mruk
	 */
	private class Slice{
		private IntegerProperty zoom;
		private StringProperty inPath;
		private BooleanProperty fileExist;
		
		private IntegerProperty totalRows;
		private IntegerProperty totalCols;
		private IntegerProperty currentRow;
		private IntegerProperty currentCol;
		
	    /**
	     * Constructor with some initial dummy data.
	     * 
	     */
		Slice(){
			this.zoom = new SimpleIntegerProperty(0);
			this.inPath = new SimpleStringProperty("");
			this.fileExist = new SimpleBooleanProperty(false);
			
			this.totalCols = new SimpleIntegerProperty(1);
			this.totalRows = new SimpleIntegerProperty(1);
			this.currentRow = new SimpleIntegerProperty(0);
			this.currentCol = new SimpleIntegerProperty(0);

		}

	    public int getZoomLvl() {
	        return zoom.get();
	    }

	    public void setZoomLvl(int i) {
	        this.zoom.set(i);
	    }
	    
	    public IntegerProperty zoomLevelProperty() {
	        return zoom;
	    }
	    
	    public String getInputPath() {
	        return inPath.get();
	    }

	    public void setInputPath(String fileString) {
	        this.inPath.set(fileString);
	    }

	    public StringProperty inputPathProperty() {
	        return inPath;
	    }
	}
}
