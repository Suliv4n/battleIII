package bag.item.stuff;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import characters.Character;
import characters.Party;
import bag.IItems;




/**
 * Les armes peuvent être équipés par les Personnages.
 * Une arme est caractérisée par :
 * <ul>
 * <li> Ses degats physiques at magiques </li>
 * <li> Defense physique et magique conférées (cas bouclier) </li>
 * <li> Son type (Hache, Bâton, épée ...)</li>
 * <li> Son nom</li>
 * <li> Eventuellement un ou plusieurs bonus/malus </li>
 * </ul>
 * 
 * @author Darklev
 *
 * @see Weapon#bonus 	
 */
public class Weapon extends Stuff
{
	public static final int SWORD = 0;
	public static final int HAMMER = 1;
	public static final int STAFF = 2;
	public static final int AXE = 3;
	public static final int SHIELD = 4;
	public static final int BOW = 5;


	
	/**
	 * Constructeur de Arme.
	 * 
	 * @param name
	 * 		Le nom de l'arme.
	 * @param type
	 * 		Le type de l'arme (Arme.EPEE, Arme.ARC ...)
	 * @param physicMagic
	 * 		Degat Physique de l'arme.
	 * @param magicDamage
	 * 		Degat magique de l'arme.
	 * @param physicDefense
	 * 		Defense physique de l'arme.
	 * @param magicDefense
	 * 		Defense Magique de l'arme.
	 */
	public Weapon(String id, String name,int type, int physicMagic, int magicDamage, int physicDefense, int magicDefense, Image image)
	{
		super(id, name, type, physicMagic, magicDamage, physicDefense, magicDefense, image);
	}

	/**
	 * Retourne le code du type de l'arme.
	 * 
	 * @return le code du type de l'arme.
	 */
	public int getType() 
	{
		return type;
	}






	/**
	 * Retourne le type de l'arme sous forme de chaine de caractère.
	 * 
	 * @return le type de l'arme sous forme de chaine de caractère.
	 */
	public String getTypeLabel()
	{
		return Weapon.getTypeLabel(type);
	}

	/**
	 * Retourne le type d'arme qui correspond au code passé en paramètre.
	 * 
	 * @param code
	 * @return
	 */
	public static String getTypeLabel(int code)
	{
		switch(code)
		{
		case(SWORD): return "épée";
		case(BOW): return "arc";
		case(AXE): return "hache";
		case(HAMMER): return "marteau";
		case(STAFF): return "baton";
		case(SHIELD): return "bouclier";
		default:return null;
		}
	}

	/**
	 * Retourne le code du type de l'arme dont le nom de type est passé
	 * en paramètre.
	 * 
	 * @param type 
	 * 		Nom du type de l'arme.
	 * @return
	 * 		Code du type.
	 */
	public static int codeWeapon(String type)
	{
		if(type.equalsIgnoreCase("epee") || type.equalsIgnoreCase("épée"))
		{
			return SWORD;
		}
		else if(type.equalsIgnoreCase("arc"))
		{
			return BOW;
		}
		else if(type.equalsIgnoreCase("hache"))
		{
			return AXE;
		}
		else if(type.equalsIgnoreCase("marteau"))
		{
			return HAMMER;
		}
		else if(type.equalsIgnoreCase("baton"))
		{
			return STAFF;
		}
		else if(type.equalsIgnoreCase("bouclier"))
		{
			return SHIELD;
		}
		
		return 0;
	}

	@Override
	public String toString()
	{
		return name;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Weapon))
		{
			return false;
		}
		return ((Weapon)o).id.equals(this.id);
	}

	@Override
	public Character whoIsEquipedOfThis(Party equipe) 
	{
		
		for(Character p : equipe)
		{
			if(p.getMainWeapon() != null)
			{
				if(p.getMainWeapon().equals(this))
				{
					return p;
				}
			}
			if(p.getSecondWeapon() != null)
			{
				if(p.getSecondWeapon().equals(this))
				{
					return p;
				}
			}
		}
		
		return null;
	}

}
