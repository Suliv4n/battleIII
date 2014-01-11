package sac.objet;


import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Classe chargeant les icones pour les objets.
 * @author Darklev
 *
 */
public class IconesObjets 
{
	private static Image icones;
	
	
	public static Image getIconeObjet(int id) throws SlickException
	{
		if(icones == null)
		{
			icones = new Image("ressources/images/objets.png", new Color(255,0,255));
		}
		return icones.getSubImage(1+(id%6)*21, 1 + ((int) id/6), 20, 20);
	}
	
}
