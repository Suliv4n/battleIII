package bag;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public interface IItems 
{
	/**
	 * Retourne l'icone de l'objet.
	 * 
	 * @return l'icone de l'objet.
	 */
	public Image getIcon() throws SlickException;
	
	/**
	 * Retourne le nom de l'objet.
	 * 
	 * @return le nom de l'objet.
	 */
	public String getName();
	
	/**
	 * Retourne vrai si l'objet est utilisable en combat, sinon
	 * retourne faux.
	 * 
	 * @return vrai si l'objet est utilisable en combat, sinon retourne faux.
	 */
	public boolean isUsableInBattle();
	
	/**
	 * Retourne vrai si l'objet peut être supprimé, sinon retourne faux.
	 * 
	 * @return vrai si l'objet peut être supprimé. Sinon retourne faux.
	 */
	public boolean isRare();
	public ArrayList<String> getEffects();
	
	/**
	 * Retourne la description de l'objet.
	 * 
	 * @return la description de l'objet.
	 */
	public String getDescription();
}

