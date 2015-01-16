package game.system.log;

import java.util.ArrayList;

public class Log{

	private ArrayList<String> log;
	
	public Log(){
		log = new ArrayList<String>();
	}
	
	public void log(String message){
		log.add(message);
	}
	
}
