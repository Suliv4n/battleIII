package bag.item;


import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Classe chargeant les icones pour les objets.
 * @author Darklev
 *
 */
public class IconLoader
{
	private static Image icones;
	
	/**
	 * Charge une icone selon son id.
	 * 
	 * @param id
	 * 		Id de l'icone à charger.
	 * @return	l'icone chargé.
	 * @throws SlickException
	 */
	public static Image loadIconItem(int id) throws SlickException
	{
		if(icones == null)
		{
			icones = new Image("ressources/images/objets.png", new Color(255,0,255));
		}
		return icones.getSubImage(1+(id%6)*21, 1 + ((int) id/6), 20, 20);
	}
	
}
