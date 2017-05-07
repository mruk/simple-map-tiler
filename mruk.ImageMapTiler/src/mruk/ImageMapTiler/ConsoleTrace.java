package mruk.ImageMapTiler;

class ConsoleTrace {

	static void log(String message){
		if (Main.IS_TRACEING_ON){
			System.out.println(message);
		}
	}
	
	static void log(int number){
		if (Main.IS_TRACEING_ON){
			System.out.println(number);
		}
	}
	
}
