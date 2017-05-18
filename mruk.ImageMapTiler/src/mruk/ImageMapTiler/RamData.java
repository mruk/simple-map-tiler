package mruk.ImageMapTiler;

class RamData {
	
	private volatile static RamData ramData;
	ConfigReader config;
	private Double overallProgressValue, lvlProgressValue;
	private String mainConfigPath;
	
	private RamData(){
		mainConfigPath = "resources/config.json";
		config = new ConfigReader(mainConfigPath);
		
		overallProgressValue = 0D;
		lvlProgressValue = 0D;
	}
	
	String getMainConfigPath() {
		return mainConfigPath;
	}
	
	String getConfigStatus(){
		
		if (config==null) return "Config not loaded.";
		if (config!=null) return "Config ready: "+mainConfigPath;
		
		return "ERR: unexpected Config state";
	}

	void setMainConfigPath(String mainConfigPath) {
		this.mainConfigPath = mainConfigPath;
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
	

}
