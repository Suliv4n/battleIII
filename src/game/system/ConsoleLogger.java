package game.system;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.newdawn.slick.SlickException;

public class ConsoleLogger extends Console{
	
	private ArrayList<ConsoleLine> allLines;
	private String filter = "";
	private static ConsoleLogger logger;
	
	protected ConsoleLogger(){
		super();
		allLines = new ArrayList<ConsoleLine>();
	}
	public void validate() throws SlickException{//nothing}

	}
	
	public void print(ConsoleLine line){
		super.print(line);
		allLines.add(line);
	}
	
	public void filter(){
		filter = super.getText();
		if(filter.isEmpty()){
			lines = allLines;
		}
		else{
			lines = (ArrayList<ConsoleLine>) allLines.stream().filter( l -> l.getLine().contains(filter)).collect(Collectors.toList());
		}
	}
	
	
	public static ConsoleLogger logger(){
		if(logger == null){
			logger = new ConsoleLogger();
		}
		return logger; 
	}
}