package bag.item.stuff;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import personnage.Party;
import personnage.Character;

/**
 * Représente une pièce d'armure.
 * 
 * @author Darklev
 *
 */
public class Armor extends Stuff
{
	public static final int HEAD=0;
	public static final int CHEST=1;
	public static final int LEGS=2;
	public static final int HANDS=3;
	
	public static final int LEATHER=0;
	public static final int FABRIC=1;
	public static final int CHAINMAIL=2;
	
	private int position;

	
	/**
	 * Constructeur de Armure
	 * 
	 * @param name
	 * 		Le nom de l'armure.
	 * @param position
	 * 		Emplacement de l'armure (Armure.MAIN, Armure.PANTALON ...).
	 * @param type
	 * 		Type de l'armure (Armure.CUIR, Armure.TISSU, Armure.MAILLE).
	 * @param physicDefense
	 * 		Défense physique conférée par l'armure.
	 * @param magicDefense
	 * 		Défense magique conférée par l'armure.
	 */
	public Armor(String id, String name,int position,int type,int physicDefense, int magicDefense, int physicDamage, int magicDamage, Image image)
	{
		super(id,name,type,physicDamage, magicDamage, physicDefense, magicDefense,image);
		this.position = position;
	}
	
	
	
	
	/**
	 * Retourne le code de l'emplacement de l'armure.
	 * 
	 * @return code emplacement.
	 */
	public int getPosition()
	{
		return position;
	}
	
	/**
	 * Donne le libéllé d'un emplacement d'armure à partir de son code.
	 * 
	 * Peut retourner :
	 * <ul>
	 * <li>tête</li>
	 * <li>torse</li>
	 * <li>pantalon</li>
	 * <li>main</li>
	 * </ul>
	 * @param code (Armure.TETE Armure.TORSE Armure.PANTALON Armure.MAIN)
	 * @return lib
	 */
	public static String getLabelPosition(int code)
	{
		switch(code)
		{
		case(HEAD): return "tête";
		case(CHEST): return "torse";
		case(LEGS): return "pantalon";
		case(HANDS): return "main";
		default: return null;
		}
	}	
	
	

	
	/**
	 * Donne le libéllé d'un type d'armure d'armure à partir de son code.
	 * 
	 * Peut retourner :
	 * <ul>
	 * <li>tissu</li>
	 * <li>maille</li>
	 * <li>cuir</li>
	 * </ul>
	 * @param code (Armure.MAILLE, Armure.TISSU ou Armure.CUIR)
	 * @return lib
	 */
	public static String getLabelType(int code)
	{
		switch(code)
		{
		case(CHAINMAIL):return"maille";
		case(FABRIC):return"tissu";
		case(LEATHER):return"cuir";
		default:return null;
		}
	}


	
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Armor))
		{
			return false;
		}
		return ((Armor)o).id.equals(this.id);
	}



	@Override
	public Character whoIsEquipedOfThis(Party equipe)
	{
		for(Character p : equipe)
		{
			for(Armor a : p.getArmors().values())
			{
				if(a != null)
				{
					if(a.equals(this))
					{
						return p;
					}
				}
			}
		}
		return null;
	}





}
