package skill;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Random;

import personnage.Personnage;

public class Skill 
{
	public static final int ALLIE = 0;
	public static final int ENNEMI = 1;
	public static final int TOUS_ALLIES = 2;
	public static final int TOUS_ENNEMIS = 3;
	// public static final int TOUS = 4;
	
	public static final int PHYSIQUE = 0;
	public static final int MAGIQUE = 1;
	public static final int SOIN = 2;

	private String id;
	private String nom;
	private String description;
	
	private int puissance;
	private int precision;
	private int consommation;
	private int type;
	
	private int UPpuissance;
	
	/**
	 * Un effet est écrit : 
	 * effet:x
	 * x étant la probabilité/ ":x" facultatif => probabilté = 100%
	 */
	private ArrayList<String> effets;
	
	private int lvl = 1;
	private int lvlMax = 10;
	
	private int xp = 0;
	
	private int cibles;	
	
	/**
	 * Constructeur de Skill.
	 * 
	 * @param nom
	 * 		Le nom du Skill.
	 * @param description
	 * 		Description du Skill.
	 * @param puissance
	 * 		Puissance du Skill.
	 * @param precision
	 * 		Précision du Skill.
	 * @param conso
	 * 		Consommation du Skill.
	 * @param UP
	 * 		Nombre de points de puissance ajoutés à la puissance du Skill par monté de niveau.
	 * @param lvlMax
	 * 		Niveau maximum du Skill.
	 * @param effets
	 * 		Effets du skill (null ok)
	 * @param cible
	 * 		Cible du Skill (Skill.ALLIE, Skill.ENNEMI, Skill.TOUS_ALLIE, Skill.TOUS_ENNEMIS)
	 * @param type
	 * 		Physique, Magique 
	 */
	public Skill(String id, String nom, String description, int puissance, int precision, int conso, int UP ,int lvlMax, String effets, int cibles, int type)
	{
		this.id = id;
		this.nom = nom;
		this.description = description;
		
		
		this.cibles = cibles;
		
		this.puissance = puissance;
		this.precision = precision;
		this.consommation = conso;
		
		UPpuissance = UP;
		
		this.lvlMax = lvlMax;
		
		this.effets = new ArrayList<String>();
		String[] lesEffets = effets.split(";");
		
		for(String e : lesEffets)
		{
			this.effets.add(e);
		}
		
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	private void levelUp()
	{
		lvl++;
		puissance+=UPpuissance;
	}
	
	/**
	 * Genere les effets de la compétence, selon leur probabilité de se produire.
	 * Par exemple "poison:33" n'a que 33% de chance d'être généré.
	 * 
	 * @return les effets de la compétence.
	 */
	public ArrayList<String> genererEffets()
	{
		ArrayList<String> lesEffetsGeneres = new ArrayList<String>();
		for(String e : effets)
		{
			int r = (int) Math.random()*100;
			if(e.matches(".+:[1-9]{1,2}"))
			{
				String[] eff = e.split(":");
				int proba = Integer.parseInt(eff[1]);
				
				if(r<proba)
				{
					lesEffetsGeneres.add(eff[0]);
				}
			}
			else
			{
				lesEffetsGeneres.add(e);
			}
		}
		
		return lesEffetsGeneres;
	}
	
	/**
	 * Retourne la cible sous forme de chaine de caractères à partir du code de la cible.
	 * 
	 * @param cible
	 * 		Code de la cible (Skill.ENNEMI par exemple)
	 * @return
	 * 		La cible sous forme de chaine de caractères à partir du code de la cible
	 * 		ou -1 si le code n'est pas valide.
	 */
	public static int getCodeCible(String cible)
	{
		if(cible.toLowerCase().equalsIgnoreCase("ennemi"))
		{
			return ENNEMI;
		}
		else if(cible.toLowerCase().equalsIgnoreCase("tous_ennemis"))
		{
			return TOUS_ENNEMIS;
		}
		else if(cible.toLowerCase().equalsIgnoreCase("tous_allies"))
		{
			return TOUS_ALLIES;
		}
		else if(cible.toLowerCase().equalsIgnoreCase("allie"))
		{
			return ALLIE;
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * Retourne le nom du Skill.
	 * 
	 * @return le nom du Skill.
	 */
	public String getNom()
	{
		return nom;
	}
	
	/**
	 * Retourne la description du Skill.
	 * 
	 * @return la description du Skill.
	 */
	public String getDescription() 
	{
		return description;
	}

	/**
	 * Retourne la puissance du Skill.
	 * 
	 * @return la puissance du Skill.
	 */
	public int getPuissance()
	{
		return puissance;
	}
	
	/**
	 * Retourne la consommation du Skill.
	 * 
	 * @return la consommation du Skill.
	 */
	public int getConsommation()
	{
		return consommation;
	}
	
	/**
	 * Retourne le niveau du skill.
	 * 
	 * @return le niveau du skill.
	 */
	public int getNiveau() 
	{
		return lvl;
	}
	
	/**
	 * Retourne le niveau max du skill.
	 * 
	 * @return le niveau max du skill.
	 */
	public int getNiveauMax() 
	{
		return lvlMax;
	}
	
	/**
	 * Retourne le ciblage du Skill.
	 * 
	 * @return ciblage du Skill.
	 */
	public int getCible()
	{
		return cibles;
	}
	
	/**
	 * Retourne le libellé du ciblage en fonction de son code.
	 * 
	 * @param cible
	 * 		Le code du ciblage.
	 * @return
	 * 		Libelle du ciblage.
	 */
	public static String getLibCible(int cible)
	{
		switch(cible)
		{
		case(TOUS_ENNEMIS):
			return "Tous les ennemis";
		case(TOUS_ALLIES):
			return "Tous les alliés";
		case(ENNEMI):
			return "Ennemi";
		case(ALLIE):
			return "Allié";
		default:
			return "Cible inconnue";
		}
	}
	
	@Override
	public String toString()
	{
		String res = nom+" : "+description+"\nPuissance : "+puissance+"\nPrecision : "+precision+"\nConsommation : "+consommation +"\nNiveau : "+lvl+"/"+lvlMax;
		return res;
	}

	/**
	 * Retourne l'identifiant du Skill.
	 * 
	 * @return
	 * 	L'identifiant su Skill.
	 */
	public String getId() 
	{
		return id;
	}

	/**
	 * Retourne le libellé d'erreur si un personnage ne peut lancer un skill.
	 * Exemples : Pas assez de mana/concentration/furie, Arc requis...
	 * 
	 * @param personnage
	 * 		Personnage à tester.
	 * @return
	 * 		Le libellé d'erreur si le personnage ne peut pas lancer le skill,
	 * null sinon.
	 */
	public String libErreurLancer(Personnage personnage)
	{
	
		if(personnage.getEnergie() < consommation)
		{
			if(personnage.getClass().getName().equalsIgnoreCase("personnage.Mage"))
			{
				return "Pas assez de mana.";
			}
			if(personnage.getClass().getName().equalsIgnoreCase("personnage.Rodeur"))
			{
				return "Pas assez de concentration.";
			}
			if(personnage.getClass().getName().equalsIgnoreCase("personnage.Guerrier"))
			{
				return "Pas assez de furie.";
			}
			return "Pas assez d'énergie";
		}
		return null;
	}
}
