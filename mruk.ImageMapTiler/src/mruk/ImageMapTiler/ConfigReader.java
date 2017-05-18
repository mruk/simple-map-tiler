package mruk.ImageMapTiler;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class ConfigReader {

	JSONParser parser = new JSONParser();
	JSONObject config;
	
	ConfigReader(String t){
		config = new JSONObject();
		loadConfig(t);
		
  		ConsoleTrace.log("map slices: "+getAmountOfZoomLvls());
	}
	
	
	void loadConfig(String file) {
		try {
			config = (JSONObject) parser.parse(new FileReader(file));
			ConsoleTrace.log(config.toString());
		} catch (IOException | org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	int getAmountOfZoomLvls(){
		int out = 0;
		JSONArray slices = (JSONArray) config.get("slices");
		out = slices.size();
		return out;
	}
	
	int getItyZoomLvl(int i){
		JSONArray slices = (JSONArray) config.get("slices");		
		JSONObject slice = (JSONObject) slices.get(i);
		Long out = (Long)slice.get("zoom");
		return out.intValue();
	}
	
	String getItyImgPath(int i){
		JSONArray slices = (JSONArray) config.get("slices");		
		JSONObject slice = (JSONObject) slices.get(i);	
		String out = (String)slice.get("input-path");
		return out;
	}
	
	int getTileSize(){
		int pixelSize = 256; // world standard size
		JSONObject tile = (JSONObject) config.get("tile");
		pixelSize = (int)tile.get("pixelsize");
		return pixelSize;
	}
}
