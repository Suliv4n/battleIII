package skill;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Random;

import characters.Character;

/**
 * Cette classe représente une compétence.
 * 
 * @author Darklev
 *
 */
public class Skill 
{
	public static final int ALLY = 0;
	public static final int ENNEMY = 1;
	public static final int ALL_ALLIES = 2;
	public static final int ALL_ENNEMIS = 3;
	// public static final int TOUS = 4;
	
	public static final int PHYSIC = 0;
	public static final int MAGIC = 1;
	public static final int HEALTH = 2;

	private String id;
	private String name;
	private String description;
	
	private int power;
	private int accuracy;
	private int consommation;
	private int type;
	
	//Valeur ajouté à la puissance lors de monté de niveau.
	private int UPPower;
	
	/**
	 * Un effet est écrit : 
	 * effet:x
	 * x étant la probabilité/ ":x" facultatif => probabilté = 100%
	 */
	private ArrayList<String> effects;
	
	private int level = 1;
	private int levelMax = 10;
	
	//Expérience dans la compétence
	private int experience = 0;
	
	//Type de cible.
	private int targets;	
	
	/**
	 * Constructeur de Skill.
	 * 
	 * @param name
	 * 		Le nom du Skill.
	 * @param description
	 * 		Description du Skill.
	 * @param power
	 * 		Puissance du Skill.
	 * @param accuracy
	 * 		Précision du Skill.
	 * @param consomation
	 * 		Consommation du Skill.
	 * @param UPPower
	 * 		Nombre de points de puissance ajoutés à la puissance du Skill par monté de niveau.
	 * @param levelMax
	 * 		Niveau maximum du Skill.
	 * @param effects
	 * 		Effets du skill (null ok)
	 * @param cible
	 * 		Cible du Skill (Skill.ALLIE, Skill.ENNEMI, Skill.TOUS_ALLIE, Skill.TOUS_ENNEMIS)
	 * @param type
	 * 		Physique, Magique 
	 */
	public Skill(String id, String name, String description, int power, int accuracy, int consomation, int UPPower ,int levelMax, String effects, int targets, int type)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		
		
		this.targets = targets;
		
		this.power = power;
		this.accuracy = accuracy;
		this.consommation = consomation;
		
		this.UPPower = UPPower;
		
		this.levelMax = levelMax;
		
		this.effects = new ArrayList<String>();
		String[] lesEffets = effects.split(";");
		
		for(String e : lesEffets)
		{
			this.effects.add(e);
		}
		
		this.type = type;
	}
	
	/**
	 * Retourne le type de compétence (Physic, magic ou health).
	 * @return le type de compétence (Physic, magic ou health).
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * Monte la compétence d'un niveau.
	 */
	private void levelUp()
	{
		level++;
		power+=UPPower;
	}
	
	
	/**
	 * Retourne le niveau du skill.
	 * 
	 * @return le niveau du skill.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Retourne le level max du skill.
	 * @return le level max du skill.
	 */
	public int getLevelMax(){
		return levelMax;
	}
	
	/**
	 * Genere les effets de la compétence, selon leur probabilité de se produire.
	 * Par exemple "poison:33" n'a que 33% de chance d'être généré.
	 * 
	 * @return les effets de la compétence.
	 */
	public ArrayList<String> generateEffects()
	{
		ArrayList<String> effects = new ArrayList<String>();
		for(String e : this.effects)
		{
			int r = (int) Math.random()*100;
			if(e.matches(".+:[1-9]{1,2}"))
			{
				String[] eff = e.split(":");
				int proba = Integer.parseInt(eff[1]);
				
				if(r<proba)
				{
					effects.add(eff[0]);
				}
			}
			else
			{
				effects.add(e);
			}
		}
		
		return effects;
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
			return ENNEMY;
		}
		else if(cible.toLowerCase().equalsIgnoreCase("tous_ennemis"))
		{
			return ALL_ENNEMIS;
		}
		else if(cible.toLowerCase().equalsIgnoreCase("tous_allies"))
		{
			return ALL_ALLIES;
		}
		else if(cible.toLowerCase().equalsIgnoreCase("allie"))
		{
			return ALLY;
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
	public String getName()
	{
		return name;
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
	public int getPower()
	{
		return power;
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
	 * Retourne le niveau max du skill.
	 * 
	 * @return le niveau max du skill.
	 */
	public int getNiveauMax() 
	{
		return levelMax;
	}
	
	/**
	 * Retourne le ciblage du Skill.
	 * 
	 * @return ciblage du Skill.
	 */
	public int getTargets()
	{
		return targets;
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
		case(ALL_ENNEMIS):
			return "Tous les ennemis";
		case(ALL_ALLIES):
			return "Tous les alliés";
		case(ENNEMY):
			return "Ennemi";
		case(ALLY):
			return "Allié";
		default:
			return "Cible inconnue";
		}
	}
	
	@Override
	public String toString()
	{
		String res = name+" : "+description+"\nPuissance : "+power+"\nPrecision : "+accuracy+"\nConsommation : "+consommation +"\nNiveau : "+level+"/"+levelMax;
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
	public String getErrorCastMessage(Character personnage)
	{
	
		if(personnage.getEnergy() < consommation)
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

	/**
	 * Paramètre le level de la compétence.
	 * 
	 * @param level
	 * 		Nouveu lvel de la compétence.
	 */
	public void setLevel(int level) {
		
		//retour à la puissance de base :
		power = power - (this.level - 1)* UPPower;
		
		
		if(level < 1){
			level = 1;
		}
		else if(level > levelMax){
			level = levelMax;
		}
		
		this.level = level;
		//nouvelle puissance
		power = power + UPPower * (level-1);
	}


}
