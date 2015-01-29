package characters;

import game.BattleWithATB;
import game.Combat;
import game.battle.Artificialntelligences;
import game.battle.IBattle;
import game.battle.actions.Action;
import game.battle.atb.ActiveTimeBattleManager;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Function;

import org.newdawn.slick.Image;

import skill.Skill;

/**
 * Classe représentant un ennemi.
 * 
 * @author Darklev
 *
 */
public class Ennemy implements IBattle
{
	private String id;
	private String name;
	private String description;
	private Image image;
	
	
	//Stats
	private int physicAttack;
	private int magicAttack;
	private int physicDefense;
	private int magicDefense;
	private int agility;
	private int health;
	private int maximumHealthPoints;
	private int healthPoints;
	
	private int typeArtificialIntelligence;
	
	private int experience;
	private int money;
	
	private Action action;
	private ArrayList<IBattle> targets;
	
	private Function<BattleWithATB, Action> ia;
	
	//Objets dropés

	private ArrayList<Skill> skills;

	private ActiveTimeBattleManager activeTimeBattleManager;
	
	/**
	 * 
	 * @param id
	 * 		Id de l'ennemi.
	 * @param name
	 * 		Nom de l'ennemi.
	 * @param description
	 * 		Description de l'ennemi.
	 * @param physicAttack
	 * 		Attaque physique de l'ennemi.
	 * @param magicAttack
	 * 		Attaque magique de l'ennemi.
	 * @param physicDefense
	 * 		Défense physique de l'ennemi.
	 * @param magicDefense
	 * 		Défense magique de l'ennemi.
	 * @param agility
	 * 		Dectérité de l'ennemi.
	 * @param health
	 * 		Pouvoir de soin de l'ennemi.
	 * @param pv_max
	 * 		PV max de l'ennemi
	 * @param experience
	 * 		Expérience que donne l'ennemi après avoir été tué.
	 * @param money
	 * 		Argent que donne l'ennemi après avoir été tué.
	 * @param image
	 * 		Iamge de l'ennemi.
	 * @param typeAI 
	 */
	public Ennemy(String id, String name, String description, int physicAttack, int magicAttack, int physicDefense, int magicDefense, int agility, int health, int maximumHealthPoints,int experience, int money, Image image, int typeAI)
	{

		this.id = id;
		this.name = name;
		this.description = description;
		this.physicAttack = physicAttack;
		this.magicAttack = magicAttack;
		this.physicDefense = physicDefense;
		this.magicDefense = magicDefense;
		this.agility = agility;
		this.health = health;
		this.maximumHealthPoints = maximumHealthPoints;
		this.experience = experience;
		this.money = money;
		this.image = image;
		this.healthPoints = maximumHealthPoints;
		
		this.skills = new ArrayList<Skill>();
		
		targets = new ArrayList<IBattle>();
		activeTimeBattleManager = new ActiveTimeBattleManager(1000);
		
		this.ia = Artificialntelligences.createArtificialIntelligence(this, typeAI);
	}

	/**
	 * @deprecated use getImageForBattle instead
	 * @return
	 */
	@Deprecated
	public Image getImage()
	{
		return image;
	}
	
	/**
	 * Retourne l'image de l'ennemi.
	 * @return l'image de l'ennemi.
	 */
	public Image getImageForBattle()
	{
		return image;
	}

	/**
	 * Retourne la dextérité de l'ennemi.
	 * @return la dextérité de l'ennemi.
	 */
	public int getAgility() 
	{
		return agility;
	}
	
	//_-_-_-_-_-_-COMBAT-_-_-_-_-_-_
	
	
	/**
	 * L'ennemi calcule une action.
	 * @param battle
	 * 		Combat en cours.
	 * @return l'action résultat de l'ia.
	 */
	public Action doIA(BattleWithATB battle)
	{
		return ia.apply(battle);
	}
	
	/**
	 * Annule l'action en cours
	 */
	public void cancelAction()
	{
		action = null;
	}

	/**
	 * Retourne vraie si l'ennemie a préparé une action.
	 * Sinon retourne faux.
	 * @return vrai si l'ennemi a préparé une action sinon retourne faux.
	 */
	public boolean isReady() 
	{
		return action != null;
	}
	/**
	 * Retourne vrai si l'ennemi a 1 PV ou plus. Sinon retourne faux.
	 * @return vrai si l'ennemi a 1PV ou plus, sinon faux.
	 */
	public boolean isAlive()
	{
		return healthPoints > 0;
	}
	
	/**
	 * Sélectionne la cible suivante de la cible en cours.
	 * Ne fonctionne que pour des actions mono-cibles.
	 * Si la nouvelle cible selectionnée est de même type que l'ancienne cible.
	 * (Si l'ancienne cible est un Personnage la nouvelle cible sera un Personnage...)7
	 * @param combat
	 * 		Combat en cours.
	 */
	public void chooseNextTarget(Combat combat)
	{		
		if(targets.size() == 1)
		{
			if(targets.get(0) instanceof Ennemy)
			{
					if(!((Ennemy)targets.get(0)).isAlive())
					{
					EnnemisParty ennemis = combat.getEnnemis();
					Ennemy currentCible = ennemis.getEnnemis().get(targets.get(0));
					if(ennemis.getEnnemis().containsValue(currentCible))
					{
						for(Ennemy e : ennemis.getEnnemis().values())
						{
							if(e.isAlive())
							{
								targets.set(0,e);
								break;
							}
						}
					}
				}
			}
			
			else //Personnage
			{
				if(!targets.get(0).isAlive())
				{
					Party equipe = combat.getEquipe();
					int indexCible = combat.getEquipe().indexOf((Character)targets.get(0));
					
					for(int i = 0; i<equipe.numberOfCharacters(); i++)
					{
						if(combat.getEquipe().get((i + indexCible) % equipe.numberOfCharacters()).isAlive())
						{
							targets.set(0, combat.getEquipe().get((i + indexCible) % equipe.numberOfCharacters()));
							System.out.println("cible reseter");
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Retourne les cibles de l'action de l'ennemi
	 * @return les cibles de l'action préparée.
	 */
	public ArrayList<IBattle> getTargets() 
	{
		return targets;
	}

	/**
	 * Retourne l'action préparée par l'ennemi.
	 * 
	 * @return l'action préparée par l'ennemi.
	 */
	public Action getAction() 
	{
		return action;
	}

	/**
	 * Retourne la défense physique de l'ennemi.
	 * 
	 * @return la défense physique de l'ennemi.
	 */
	public int getPhysicDefense() 
	{
		return physicDefense;
	}

	/**
	 * Retourne l'attaque physique de l'ennemi.
	 * 
	 * @return l'attaque physique de l'ennemi.
	 */
	public int getPhysicAttack() 
	{
		return physicAttack;
	}
	
	/**
	 * Retourne l'attaque magique de l'ennemi.
	 * 
	 * @return l'attaque magique de l'ennemi.
	 */
	public int getMagicAttack()
	{
		return magicAttack;
	}
	
	/**
	 * Retourne la défense magique de l'ennemi.
	 * 
	 * @return la défense magique de l'ennemi.
	 */
	public int getMagicDefense()
	{
		return magicDefense;
	}
	
	/**
	 * Retourne les PV maximum de l'ennemi.
	 * 
	 * @return les PVs maximum de l'ennemi.
	 */
	public int getMaximumHealthPoints()
	{
		return maximumHealthPoints;
	}	
	
	/**
	 * Retourne les PV restants de l'ennemi.
	 * 
	 * @return les pv restants de l'ennemi.
	 */
	public int getHealtPoints()
	{
		return healthPoints;
	}
	
	/**
	 * Retourne le nom de l'ennemi.
	 * 
	 * @return le nom de l'ennemi.
	 */
	public String getName()
	{
		return name;
	}
	

	/**
	 * Retourne l'id de l'ennemi.
	 * 
	 * @return l'id de l'ennemi.
	 */
	public String getID() 
	{
		return id;
	}

	/**
	 * Modifie les PV de l'ennemi en lui ajoutant la valeur
	 * passée en paramètre.
	 * 
	 * @param dPV
	 * 		La valeur à ajouter aux PV (peut être négavive)
	 */
	public void updateHealthPoints(int dPV) 
	{
		if(dPV >= 0)
		{
			healthPoints = Math.min(getMaximumHealthPoints(), healthPoints + dPV);
		}
		else
		{
			healthPoints = Math.max(0, healthPoints + dPV);
		}
		
	}

	/**
	 * Modifie les cibles de l'action préparée.
	 * 
	 * @param cibles
	 * 		Les cibles de l'action préparée.
	 */
	@Override
	public void setTargets(ArrayList<IBattle> cibles) 
	{
		this.targets = cibles;
	}

	/**
	 * Retourne le pouvoir de soin de l'ennemi.
	 * 
	 * @return le pouvoir de soin de l'ennemi.
	 */
	@Override
	public int getHealth() 
	{
		return health;
	}

	/**
	 * Modifie l'action préparée.
	 * 
	 * @param action
	 * 		L'action préparée.
	 */
	@Override
	public void setAction(Action action) 
	{
		this.action = action;
	}

	/**
	 * Retourne les points d'expérience que l'ennemi en donne après sa mort.
	 * 
	 * @return nombre de points d'expérience que l'ennemi donne après avoir 
	 * été battu.
	 */
	public int getXP() 
	{
		return experience;
	}

	/**
	 * Retourne l'ATB de l'ennemi.
	 * 
	 * @return l'ATB de l'ennemi.
	 */
	@Override
	public ActiveTimeBattleManager getActiveTimeBattleManager() {
		return activeTimeBattleManager;
	}
	
	@Override
	public void launchActiveTime() {
		activeTimeBattleManager.launch(util.Random.randInt(20,60));
	}
	
	public int getTypeArtificialIntelligence(){
		return typeArtificialIntelligence;
	}

	/**
	 * Retourne les compétences de l'ennemi.
	 * 
	 * @return les compétences de l'ennemi.
	 */
	public ArrayList<Skill> getSkills() {
		return skills;
	}

	
}
