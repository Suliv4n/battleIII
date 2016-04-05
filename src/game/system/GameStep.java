package game.system;

import java.util.HashMap;

public class GameStep {
	
	/* Keys */
	
	public static final String SEE_INTRO = "SEE_INTRO";
	
	/*-------*/
	private HashMap<String, Boolean> steps;
	
	public GameStep(){
		steps = new HashMap<String, Boolean>();
	}
	
	
	public boolean has(String key){
		return steps.containsKey(key) ? steps.get(key) : false;
	}
	
	public void addStep(String key){
		steps.put(key, true);
	}
	
}
