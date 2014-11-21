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
public class ResourcesAnimitationManager 
{
	private static HashMap<String, Audio> sons;
	private static HashMap<String, Image> images;
	
	private ResourcesAnimitationManager(){}
	
	/**
	 * Initialise le ResourcesAniamtionManager.
	 */
	public static void init()
	{
		sons = new HashMap<String, Audio>();
		images = new HashMap<String, Image>();
	}
	
	/**
	 * Retourne le son dont le nom est passé en paramètre.
	 * 
	 * @param sound
	 * 		Nom du son.
	 * @return le son dont le nom est passé en paramètre.
	 */
	protected static Audio getSound(String sound)
	{
		return sons.get(sound);
	}
	
	/**
	 * Retourne l'image dont le nom est passé en paramètre.
	 * 
	 * @param image
	 * 		Nom de l'image.
	 * @return l'image dont le nom est passé en paramètre.
	 */
	protected static Image getImage(String image) 
	{
		return images.get(image);
	}
	
	/**
	 * Ajoute une image.
	 * 
	 * @param nom
	 * 		Le nom de l'image.
	 * @param image
	 * 		L'image à ajouter.
	 */
	protected static void addImage(String nom, Image image)
	{
		images.put(nom,image);
	}
	
	/**
	 * Ajouter un son.
	 * 
	 * @param nom
	 * 		Le nom du son.
	 * @param son
	 * 		Le son à ajouter.
	 */
	protected static void addSound(String nom, Audio son)
	{
		sons.put(nom, son);
	}
	
	/**
	 * Décharge tous les sons et toutes les images chargés du ResourcesAnimationManager.
	 */
	protected static void clear()
	{
		sons.clear();
		images.clear();
	}
}
