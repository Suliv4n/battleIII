package audio;

import game.system.Configurations;

import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;





public class SoundsManager
{
	
	private static HashMap<String, Sound> sounds;
	
	public static void init() throws SlickException{
		sounds = new HashMap<String, Sound>();
		for(String name : new String[]{}){
			sounds.put("select", new Sound(Configurations.SOUNDS_FOLDER+name+".ogg"));
		}
	}
	
	public static void play(String id){
		if(sounds.get(id) != null){
			sounds.get(id).play();
		}
	}
}
