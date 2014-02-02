package sac.objet.stuff;

import java.util.ArrayList;

import personnage.Equipe;
import personnage.Personnage;

public class Armure implements Equipable 
{
	public static final int TETE=0;
	public static final int TORSE=1;
	public static final int PANTALON=2;
	public static final int MAIN=3;
	
	public static final int CUIR=0;
	public static final int TISSU=1;
	public static final int MAILLE=2;
	
	protected int defPhysique=0;
	protected int defMagique=0;
	
	protected ArrayList<String> bonus;
	private String nom;
	
	private String id;
	
	private int emplacement;
	private int typeArmure;
	
	/**
	 * Constructeur de Armure
	 * 
	 * @param nom
	 * 		Le nom de l'armure.
	 * @param emplacement
	 * 		Emplacement de l'armure (Armure.MAIN, Armure.PANTALON ...).
	 * @param typeArmure
	 * 		Type de l'armure (Armure.CUIR, Armure.TISSU, Armure.MAILLE).
	 * @param defPhy
	 * 		Défense physique conférée par l'armure.
	 * @param defMag
	 * 		Défense magique conférée par l'armure.
	 */
	public Armure(String id, String nom,int emplacement,int typeArmure,int defPhy, int defMag)
	{
		this.id = id;
		this.nom=nom;
		this.emplacement=emplacement;
		this.typeArmure=typeArmure;
		defPhysique=defPhy;
		defMagique=defMag;
	}
	
	
	
	/**
	 * Ajoute les bonus passés en paramètre à l'armure.
	 * 
	 * @param lesBonus sous forme de chaîne de caractères. Les bonus sont séparés par ";"
	 * @throws BonusException si le bonus n'est pas au bon format [bonus]:[val]
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
					double total = getBonus(tabB[0])+Integer.parseInt(tabB[1]);
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
	public double getBonus(String libBonus)
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
	 * Retourne le code de l'emplacement de l'armure.
	 * 
	 * @return code emplacement.
	 */
	public int getEmplacement()
	{
		return emplacement;
	}
	
	
	public int getType() 
	{
		return typeArmure;
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
	public static String emplacement(int code)
	{
		switch(code)
		{
		case(TETE): return "tête";
		case(TORSE): return "torse";
		case(PANTALON): return "pantalon";
		case(MAIN): return "main";
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
	public static String typeArmure(int code)
	{
		switch(code)
		{
		case(MAILLE):return"maille";
		case(TISSU):return"tissu";
		case(CUIR):return"cuir";
		default:return null;
		}
	}



	public int getDefensePhysique() 
	{
		return defPhysique;
	}



	public int getDefenseMagique() 
	{
		return defMagique;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Armure))
		{
			return false;
		}
		return ((Armure)o).id.equals(this.id);
	}



	@Override
	public Personnage quiEstEquipe(Equipe equipe)
	{
		for(Personnage p : equipe)
		{
			for(Armure a : p.getEquipement().values())
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
