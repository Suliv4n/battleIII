package sac.objet.stuff;

import java.util.ArrayList;

import org.newdawn.slick.Image;



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
 * @see Arme#bonus 	
 */
public class Arme implements Equipable
{
	public static final int EPEE=0;
	public static final int MARTEAU=1;
	public static final int BATON=2;
	public static final int HACHE=3;
	public static final int BOUCLIER=4;
	public static final int ARC=5;

	protected int degatPhysique;
	protected int degatMagique;

	protected int defPhysique;
	protected int defMagique;

	protected String nom;
	protected int typeArme;

	/**
	 * Liste des bonus d'arme:
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

	private Image image;
	
	/**
	 * Constructeur de Arme.
	 * 
	 * @param nom
	 * 		Le nom de l'arme.
	 * @param typeArme
	 * 		Le type de l'arme (Arme.EPEE, Arme.ARC ...)
	 * @param degP
	 * 		Degat Physique de l'arme.
	 * @param degMag
	 * 		Degat magique de l'arme.
	 * @param defPhy
	 * 		Defense physique de l'arme.
	 * @param defMag
	 * 		Defense Magique de l'arme.
	 */
	public Arme(String nom,int typeArme,int degP, int degMag, int defPhy, int defMag, Image image)
	{
		this.nom=nom;
		this.typeArme=typeArme;
		degatPhysique=degP;
		degatMagique=degMag;
		degatPhysique=defPhy;
		degatMagique=defMag;
		this.image = image;
		
		bonus = new ArrayList<String>();
	}

	/**
	 * Retourne le code du type de l'arme.
	 * 
	 * @return le code du type de l'arme.
	 */
	public int getType() 
	{
		return typeArme;
	}

	/**
	 * Ajoute les bonus passés en paramètre à l'arme.
	 * 
	 * @param lesBonus sous forme de chaîne de caractères. Les bonus sont séparés par ";"
	 * @throws BonusException si le bonus n'est pas au bon format [bonus]:[val] sans majuscules
	 */
	public void ajouterBonus(String lesBonus) throws BonusException
	{

		String tabBonus[] = lesBonus.split(";");
		System.out.println(tabBonus[1]);
		for(String b : tabBonus)
		{
			if(b.matches("[a-z]+:-?[0-9]+"))
			{
				String[] tabB = b.split(":");
				if(getBonus(tabB[0])!=0)
				{
					int total = getBonus(tabB[0])+Integer.parseInt(tabB[1]);
					bonus.set(bonus.indexOf(b),tabB[0]+":"+total);
				}
				else
				{
					bonus.add(b);
				}
			}
			else
			{
				throw new BonusException("Mauvais format de bonus [bonus]:[val]  => "+b+" n'est pas valide");
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
	 * Retourne le type de l'arme sous forme de chaine de caractère.
	 * 
	 * @return le type de l'arme sous forme de chaine de caractère.
	 */
	public String typeArme()
	{
		return Arme.typeArme(typeArme);
	}

	/**
	 * Retourne le type d'arme qui correspond au code passé en paramètre.
	 * 
	 * @param code
	 * @return
	 */
	public static String typeArme(int code)
	{
		switch(code)
		{
		case(EPEE): return "épée";
		case(ARC): return "arc";
		case(HACHE): return "hache";
		case(MARTEAU): return "marteau";
		case(BATON): return "baton";
		case(BOUCLIER): return "bouclier";
		default:return null;
		}
	}


	@Override
	public String toString()
	{
		return nom;
	}

	/**
	 * Retourne l'image de l'arme.
	 */
	public Image getImage() 
	{
		return image;	
	}

	public int getDegatPhysique() 
	{
		return degatPhysique;
	}

	public int getDefensePhysique() 
	{
		return defPhysique;
	}

	public int getDefenseMagique()
	{
		return defMagique;
	}

	public int getDegatMagique() 
	{
		return degatMagique;
	}

}
