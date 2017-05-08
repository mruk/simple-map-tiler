package mruk.ImageMapTiler;

class RamData {
	
	private volatile static RamData ramData;
	private Double overallProgressValue, lvlProgressValue;
	
	private RamData(){
		overallProgressValue = 0D;
		lvlProgressValue = 0D;
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
