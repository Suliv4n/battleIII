package animation;

import java.math.BigDecimal;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.openal.Audio;

/**
 * Charge les images et les sons pour les IAnimation.
 *  
 * @author Darklev
 *
 */
public class RessourceAnimationLoader 
{
	private static HashMap<String, Audio> sons;
	private static HashMap<String, Image> images;
	
	private RessourceAnimationLoader(){}
	
	public static void init()
	{
		sons = new HashMap<String, Audio>();
		images = new HashMap<String, Image>();
	}
	
	protected static Audio getSon(String son)
	{
		return sons.get(son);
	}
	
	protected static Image getImage(String image) 
	{
		return images.get(image);
	}
	
	protected static void ajouterImage(String nom, Image image)
	{
		images.put(nom,image);
	}
	//TODO ajouterSon, clear
	protected static void ajouterSon(String nom, Audio son)
	{
		sons.put(nom, son);
	}
	
	protected static void clear()
	{
		sons.clear();
		images.clear();
	}
}
