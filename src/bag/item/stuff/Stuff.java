package bag.item.stuff;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import personnage.Character;
import personnage.Party;

import bag.IItems;

/**
 * Représente un objet Equipable.
 * @author Sulivan
 *
 */
public abstract class Stuff implements IItems {

	protected int physicDamage;
	protected int magicDamage;

	protected int physicDefense;
	protected int magicDefense;

	protected String id;
	protected String name;
	protected int type;
	
	private Image image;
	
	/**
	 * Liste des bonus du stuff:
	 * <table border=1>
	 * <tr><td> stat:x </td> <td>stat pouvant être force, endurance, intelligence, pv max,  attaque magique... x est la valeur du bonus</td></tr>
	 * 
	 * <tr><td> element:x </td> <td> element : feu, electrique ... x est la puissance de degat elementaire supplémentaire</td></tr>
	 * <tr><td> regen:x </td> <td> regenere x pv chaque tour</td></tr>
	 * <tr><td> energie:x </td> <td> regenere x energie(furie, concentration ou mana) chaque tour </td></tr>
	 * <tr><td> critique:x </td> <td> augmente le taux de critique de x</td></tr>
	 * <tr><td> precision:x  </td> <td> augmente le taux de précision de x</td></tr>
	 * 
	 * </table>
	 * Un bonus peut être un malus si x est négatif
	 */ 
	protected ArrayList<String> bonus;
	
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
	public Stuff(String id, String name,int type, int physicMagic, int magicDamage, int physicDefense, int magicDefense, Image image)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		this.physicDamage = physicMagic;
		this.magicDamage = magicDamage;
		this.physicDefense = physicDefense;
		this.magicDefense = magicDefense;
		this.image = image;
		
		bonus = new ArrayList<String>();
	}
	
	
	/**
	 * Ajoute les bonus passés en paramètre au stuff.
	 * 
	 * @param bonus 
	 * sous forme de chaîne de caractères. Les bonus sont séparés par ";"
	 * @throws BonusException si le bonus n'est pas au bon format [bonus]:[val]
	 */
	public void addBonus(String bonus) throws BonusException
	{
        
		String arrayBonus[] = bonus.split(";");
		
		for(String b : arrayBonus)
		{
			if(b.matches("[a-z]+:-?[0-9]+"))
			{
				String[] tabB = b.split(":");
				if(getBonus(tabB[0])!=0)
				{
					double total = getBonus(tabB[0])+Integer.parseInt(tabB[1]);
					this.bonus.set(bonus.indexOf(b),tabB[0]+":"+total);
				}
				else
				{
					this.bonus.add(b);
				}
			}
			else
			{
				throw new BonusException("Bad format bonus [bonus]:[val]  => "+b+" is not valid");
			}
		}
	}
	
	/**
	 * Récupère la valeur du bonus passé en paramètre ou 0 si le bonus n'existe pas.
	 * 
	 * @param lesBonus
	 */
	public int getBonus(String libBonus)
	{
		for(String b : bonus)
		{
			String[] tabB= b.split(":");
			if(tabB[0].equals(libBonus))
			{
				return Integer.parseInt(tabB[1]);
			}
		}

		return 0;
	}
	
	
	/**
	 * Retourne les dégât physiques du stuff.
	 * 
	 * @return les dégâts physiques du stuff.
	 */
	public int getPhysicDamage() 
	{
		return physicDamage;
	}

	/**
	 * Retourne la défense physqiue du stuff.
	 * 
	 * @return la défense physique du stuff.
	 */
	public int getPhysicDefense() 
	{
		return physicDefense;
	}

	/**
	 * Retourne la défense magique du stuff.
	 * 
	 * @return la défense magique du stuff.
	 */
	public int getMagicDefense()
	{
		return magicDefense;
	}

	/**
	 * Retourne la défense magique du stuff.
	 * 
	 * @return
	 */
	public int getMagicDamage() 
	{
		return magicDamage;
	}
	
	/**
	 * Retourne l'id du stuff.
	 * 
	 * @return l'id du stuff.
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Retourne l'image du stuff.
	 * 
	 * @return l'image du stuff.
	 */
	public Image getImage() 
	{
		return image;	
	}
	
	/**
	 * Retourne le type du stuff.
	 * 
	 * @return le type du stuff.
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Retourne le personnage qui est équipé de l'équipable.
	 * 
	 * @return le personnage qui est équipé de l'équipable.
	 */
	public abstract Character whoIsEquipedOfThis(Party equipe);
		
	//-----------------------------------------------------------------------------
	//---------------------------Interface IItem-----------------------------------
	//-----------------------------------------------------------------------------
	
	@Override
	public Image getIcon() throws SlickException
	{
		return image;
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public boolean isUsableInBattle() 
	{
		return false;
	}

	@Override
	public boolean isRare() 
	{
		return false;
	}

	@Override
	public ArrayList<String> getEffects()
	{
		return new ArrayList<String>();
	}

	@Override
	public String getDescription() 
	{
		return name +"\n"+"Dégât physique : " + physicDamage + "" +
					"\nDégât magique : " + magicDamage +
					"\nDéfense physique : " + physicDefense + "" +
					"\nDéfense physique : " + magicDefense;
	}
	
	
}
