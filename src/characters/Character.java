/**
 * @author Darklev
 */

package characters;

import game.Combat;
import game.Config;
import game.battle.IBattle;
import game.battle.actions.Action;
import game.battle.atb.ActiveTimeBattleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import characters.skin.Skin;
import bag.IItems;
import bag.item.stuff.*;
import skill.Skill;
import ui.BarUI;
import ui.TypeBarre;


/**
 * Classe reprénsentant un personnage jouable.
 * 
 * @author Darklev
 *
 */
public abstract class Character extends IBattle
{
	
	public static final int MAIN_WEAPON=0;
	public static final int SECOND_WEAPON=1;
	
	private String name;
	
	protected int level=1;
	protected int healthPoints;
	protected int experience=0;
	
	protected Weapon mainWeapon;
	protected Weapon secondWeapon;
	
	protected HashMap<String, Integer> statistics;
	
	/**
	 * A chaque monter de level les stats augmente selon la HashMap statsUP
	 */
	protected HashMap<String, Integer> statisticsUP;
	
	protected ArrayList<Integer> mainWeaponTypes;
	protected ArrayList<Integer> secondWeaponTypes;
	protected ArrayList<Integer> armorTypes;
	
	protected HashMap<Integer,Armor> armors;
	
	protected ArrayList<Skill> skills;
	
	protected Skin skin;
	
	//Action en cours => state combat
	private Action action;
	private ArrayList<IBattle> targets;
	private ActiveTimeBattleManager activeTimeBattleManager;
	
	/**
	 * Constructeur de joueur. A la construction, le nom est initialisé.
	 * Les ArrayList et HashMap sont instanciées.
	 * 
	 * @param name
	 * 			Le nom du personnage
	 */
	public Character(String name)
	{
		this.name=name;
		statistics=new HashMap<String,Integer>();
		statisticsUP = new HashMap<String, Integer>();
		
		mainWeaponTypes=new ArrayList<Integer>();
		secondWeaponTypes=new ArrayList<Integer>();
		armorTypes=new ArrayList<Integer>();
		
		armors = new HashMap<Integer, Armor>();
		
		armors.put(Armor.HANDS, null);
		armors.put(Armor.LEGS, null);
		armors.put(Armor.HEAD, null);
		armors.put(Armor.CHEST, null);
		
		skills = new ArrayList<Skill>();
		targets = new ArrayList<IBattle>();
		activeTimeBattleManager = new ActiveTimeBattleManager(100);
	}
	

	/**
	 * Retourne le bonus d'énergie du personnage.
	 * 
	 * @return le bonus d'énergie du perosnnage.
	 */
	public int getBonusEnergy() 
	{

		int total = 0;
		if(mainWeapon != null)
		{
			total+=mainWeapon.getBonus("energie");
		}
		if(secondWeapon != null)
		{
			total+=secondWeapon.getBonus("energie");
		}
		
		
		for(Armor a : armors.values())
		{
			if(a != null)
			{
				total+=a.getBonus("energie");
			}
		}
		
		return total;
	}
	
	/**
	 * Retourne la valeur d'une statistique avec les bonus compris.
	 * (Bonus de stuff...)
	 * 
	 * @param statistique
	 * 		Nom de la statistique à retourner.
	 * @return valeur de la statistique mawimale.
	 */
	public int getMaximumStatistic(String statistique) 
	{
		int total = 0;
		if(statistics.containsKey(statistique))
		{
			total=statistics.get(statistique);
		}
		if(mainWeapon != null)
		{
			total+=mainWeapon.getBonus(statistique);
		}
		if(secondWeapon != null)
		{
			total+=secondWeapon.getBonus(statistique);
		}
		
		
		for(Armor a : armors.values())
		{
			if(a != null)
			{
				total+=a.getBonus(statistique);
			}
		}
		
		return total;
	}
	

	

	
	/**
	 * Retourne le nombre de PV maximum en prenant en compte les bonus d'équipements.
	 * 
	 * @return PV maximum
	 */
	public int getMaximumHealthPoints()
	{
		int bonusPV = 0;
		if(mainWeapon != null)
		{
			bonusPV=mainWeapon.getBonus("pvmax");
			
		}
		if(secondWeapon != null)
		{
			bonusPV+=secondWeapon.getBonus("pvmax");
		}

		
		for(Armor a : armors.values())
		{
			if(a != null)
			{
				bonusPV+=a.getBonus("pvmax");
			}
		}

		
		
		return (int) 5*getMaximumStatistic("endurance")+bonusPV;
	}
	
	public int getHealtPoints() 
	{
		return healthPoints;
	}
	
	/**
	 * Retourne l'équipement du personnage (armures).
	 * @return
	 * 	Pièces de l'armure du personnage.
	 */
	public HashMap<Integer, Armor> getArmors()
	{
		return armors;
	}

	/**
	 * Augmente l'expérience du joueur qui monte de level après avoir obtenu assez 
	 * d'expérience.
	 *  
	 * @param xp
	 * 		La quantité de poinst d'expérience gagnés.
	 */
	public void gainExperience(int xp)
	{
		for(int i=1;i<=xp;i++)
		{
			experience++;
			if(experience == experienceRequiredForNextLevel())
			{
				levelUP();
			}
		}
	}
	
	/**
	 * Retourne l'expérience nécessaire pour atteindre le prochain niveau.
	 * @return l'expérience nécessaire pour atteindre le prochain niveau.
	 */
	public int experienceRequiredForNextLevel() 
	{
		int res = 10;
		for(int i = 1; i<level ; i++){
			res += (int) (10 + i*i*0.5);
		}
		return res;
	}
	
	
	/**
	 * Incrémente le level du Personnage et améliore ses stats en conséquence.
	 */
	private void levelUP() 
	{
		level++;		
		for(String k : statistics.keySet())
		{
			statistics.put(k,statisticsUP.get(k)+statistics.get(k));
			healthPoints = getMaximumHealthPoints();
		}
	}

	/**
	 * Retourne le nom du personnage.
	 * 
	 * @return nom
	 * 		Le nom du personnage
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Retourne le level du personnage.
	 * @return
	 * 		le level du personnage.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Met à jour le nombre de pv restant du personnage en ajoutant 
	 * la valeur passée en paramètre. Le nombre de pv est toujours >= 0 et <= pvMax
	 * 
	 * @param HP 
	 * 		nombre de pv à ajouter (>0) 
	 *      ou à supprimer (<0)
	 */
	public void updateHealthPoints(int HP)
	{
		if(HP >= 0)
		{
			healthPoints = Math.min(getMaximumHealthPoints(), healthPoints + HP);
		}
		else
		{
			healthPoints = Math.max(0, healthPoints + HP);
		}
	}

	/**
	 * Equipe une arme au personnage. Retourne vrai si le personnage peut s'équiper de l'arme,
	 * sinon retourne faux.
	 * 
	 * @param 
	 * 		weaponPosition : ARME_PRINCIPALE ou ARME_SECONDAIRE
	 * @param weapon 
	 * 		L'arme à équiper.
	 * @return vrai si le personnage peut s'équiper de l'arme. SInon retourne faux.
	 */
	public boolean equipeArme(int weaponPosition, Weapon weapon)
	{

		switch(weaponPosition)
		{
			case(MAIN_WEAPON):

				if(mainWeaponTypes.contains(weapon.getType()))
				{
					mainWeapon=weapon;
					return true;
				}
			break;
			
			case(SECOND_WEAPON):

				if(secondWeaponTypes.contains(weapon.getType()))
				{
					mainWeapon=weapon;
					return true;
				}
			break;
		}
		
		return false;
	}
	
	/**
	 * Equipe une armure au personnage. Retourne vrai si le personnage peut s'équiper de l'arme,
	 * sinon retourne false.
	 * 
	 * @param armor
	 * 		Armure à équiper.
	 * @return vrai si le personnage peut s'équiper de l'arme, sinon faux.
	 */
	public boolean equipeArmor(Armor armor)
	{
		if(armorTypes.contains(armor.getType()))
		{
			armors.get(armor.getPosition());
			armors.put(armor.getPosition(),armor);
			return true;
		}
		
		return false;
	}
	
	
	public Animation getAnimation(int direction) 
	{
		return skin.getAnimation((int)(Math.log(direction)/Math.log(2)));
	}
	
	/**
	 * Retourne l'énergie (furie/mana/concentration)
	 * @return l'énergie (furie/mana/concentration) du personnage.
	 */
	public abstract int getEnergy();
	
	/**
	 * Retourne le nom de l'énergie du personnage.
	 * 
	 * @return nom de l'énergie du personnage.
	 */
	public abstract String getEnergyLabel();

	/**
	 * Retourne la vitesse de marche du personnage en fonction des bonus
	 * des équipements et armes.
	 * 
	 * @return la vitesse de marche du personnage en fonction des bonus
	 * des équipements.
	 */
	public double getSpeed() 
	{
		double som = 0;
		for(Armor a : armors.values())
		{
			if(a!=null)
			{
				som += a.getBonus("vitesse");
			}
			
		}
		
		if(mainWeapon != null)
		{
			som += mainWeapon.getBonus("vitesse");
		}
			
		if(secondWeapon != null)
		{
			som += secondWeapon.getBonus("vitesse");
		}
			
		return som;
	}
	
	/**
	 * Retourne l'énergie maximale du personnage.
	 * 
	 * @return l'énergie maximale du personnage.
	 */
	public abstract int getMaximumEnergy();
	
	/**
	 * Le personnage apprend une nouvelle compétence(Skill)
	 * passée en paramètre.
	 * 
	 * @param skill
	 * 		La compétence que le personnage doit apprendre.
	 */
	public void learnSkill(Skill skill)
	{
		skills.add(skill);
	}
	
	/**
	 * Retourne une ArrayList des compétences connues
	 * par le Personnage.
	 * 
	 * @return Les skills connues par le Personnage.
	 */
	public ArrayList<Skill> getSkills()
	{
		return skills;
	}
	
	/**
	 * Affiche les informations du personages sur le graphique passé en paramètre.
	 * 
	 * @param g
	 * 		Graphics où afficher les informations.
	 * @param x
	 * 		Abscisse d'affcihage.
	 * @param y
	 * 		Ordonnée d'affichage.
	 * @param selectionne
	 * 		Vrai si le personnage est sélectionné, faux sinon.
	 */
	public void renderCharacterPanel(Graphics g, int x, int y, boolean selectionne) 
	{
		g.setColor(Config.couleur2);
		g.drawRect(x, y, 256, 160);
		g.drawRect(x+1, y+1, 254,158);
		
		g.setColor(Config.couleur1);
		g.fillRect(x+2, y+2, 253, 157);
		
		Animation perso = getAnimation(Party.SOUTH);
		perso.setLooping(true);
		if(selectionne)
		{
			g.drawAnimation(perso, 15+x, y+80-perso.getHeight()/2);		
		}
		else
		{
			g.drawImage(perso.getImage(2), 15+x, y+80-perso.getHeight()/2);
		}
		g.setColor(new Color(255,255,255));
		g.drawString(name, 25, y+10);
		g.drawString("N."+level, 140, y+10);
		g.drawString("PV", x+73, y+46);
		g.drawString(healthPoints+"/"+getMaximumHealthPoints(),x+100,y+60);

		g.setColor(Config.couleur2);
		g.drawRect(x+95, y+50, 150, 10);
		g.drawRect(x+95, y+90, 150, 10);
		
		
		g.setColor(Color.black);
		g.fillRect(x+96, y+51, 149, 9);
		g.fillRect(x+96, y+91, 149, 9);
		
		//Remplissage bar pv
		g.setColor(new Color(0,100,0));
		g.fillRect(x+96, y+51, (float) (healthPoints/((double)getMaximumHealthPoints()))*149, 9);
		
		g.setColor(new Color(255,255,255));
		g.drawString(getEnergy()+"/"+getMaximumEnergy(),x+100,y+100);
		
		//XP
		g.drawString("EXP", x+5, y+120);
		g.drawString("EXP : "+experience+"/"+experienceRequiredForNextLevel(), x+5, y+140);
		new BarUI(Config.couleurXP, Color.black, 210, 4, experience, experienceRequiredForNextLevel(), TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2).render(g, x+35, y+127);

		

	}

	

	/**
	 * Afficher les stats du personnage.
	 * @param g
	 * 		Graphics où afficher les statistiques.
	 */
	public void drawStatistiques(Graphics g)
	{
		g.setColor(Config.couleur2);
		g.drawRect(256, 0, 383, 160);
		g.drawRect(257, 1, 382, 159);
		
		g.setColor(Config.couleur1);
		g.fillRect(258, 2, 381, 158);
		
		g.setColor(new Color(255,255,255));
		g.drawString("STATS", 420, 10);
		
		g.setColor(Color.white);
		
		int i=0;
		for(String s : statistics.keySet())
		{
			g.drawString(s.substring(0,1).toUpperCase()+s.substring(1),270,40+25*i);
			g.drawString(String.valueOf(getMaximumStatistic(s)),400,40+25*i);
			
			i++;
		}
	}

	/**
	 * Affiche l'équipement du personnage.
	 * 
	 * @param g
	 * 		Graphics où afficher l'équipement du personnage.
	 */
	public void drawStuff(Graphics g)
	{
		g.setColor(Config.couleur2);
		g.drawRect(256, 160, 383, 200);
		g.drawRect(257, 161, 382, 199);
		
		g.setColor(Config.couleur1);
		g.fillRect(258, 162, 381, 198);
		
		g.setColor(new Color(255,255,255));
		g.drawString("EQUIPEMENTS", 400, 170);
		
		g.drawString("Arme principale", 270, 210);
		if(mainWeapon != null)
		{
			g.drawString(mainWeapon.toString(), 410, 210);
		}
		
		g.drawString("Arme Secondaire", 270, 235);
		if(secondWeapon != null)
		{
			g.drawString(mainWeapon.toString(), 410, 210);
		}
		
		int i=1;
		for(Integer k : armors.keySet())
		{
			g.drawString(Armor.getLabelPosition(k).substring(0,1).toUpperCase()+Armor.getLabelPosition(k).substring(1), 270, 235+i*25);
			if(armors.get(k)!=null)
			{
				g.drawString(armors.get(k).toString(), 410, 235+i*25);
			}
			i++;
		}
	}

	/**
	 * Retourne l'image du personnage en position de combat.
	 * 
	 * @return
	 * 	l'image du personnage en position de combat.
	 */
	public Image getImagePositionAttaque() 
	{
		return getAnimation(Party.WEST).getImage(2);
	}
	
	/**
	 * Retourne l'image du personnage KO pendant le combat.
	 * 
	 * @return
	 * 	l'image du personnage KO.
	 */
	public Image getImageKO() 
	{
		return getAnimation(4).getImage(1);
	}

	/**
	 * Teste si le personnage est en vie ou non en le châtouillant pour
	 * voir s'il bouge.
	 * 
	 * @return
	 * 		Vrai si le personnage est en vie faux sinon.
	 */
	public boolean isAlive() 
	{
		return healthPoints > 0;
	}

	public void setNom(String nom) 
	{
		this.name = nom;
	}

	
	//-_-_-_-_-_-_-COMBAT-_-_-_-_-_-_-_
	/**
	 * Prépare l'action du personnage
	 * @param action
	 */
	public void setAction(Action action)
	{
		this.action = action;
	}
	
	/**
	 * Retourne vrai si le personnage a une action préparée, sinon faux.
	 * 
	 * @return
	 * 	vrai si le personnage a une action préparée, sinon faux.
	 */
	public boolean estActionPreparee() 
	{
		return action != null;
	}

	/**
	 * Annule l'action du personnage.
	 */
	public void cancelAction()
	{
		action = null;
		targets.clear();
	}



	/**
	 * Retourne l'action l'action préparé.
	 * 
	 * @return
	 * 	Retourne l'action prépapré.
	 */
	public Action getAction() 
	{
		return action;
	}

	/**
	 * Modifie les cibles de l'action préparée.
	 * 
	 * @param targets
	 * 		Nouvelles cibles.
	 */
	public void setTargets(ArrayList<IBattle> targets)
	{
		this.targets = targets; 
	}


	/**
	 * Ajoute une cible pour l'action préparée.
	 * 
	 * @param target
	 * 		Cible à ajouter.
	 */
	public void ajouterCible(IBattle target) 
	{
		targets.add(target);
	}
	
	/**
	 * Retourne vrai si le personnage à une cible pour son action, sinon retourne faux.
	 * 
	 * @return vrai si le personnage a sélectionné une cible.
	 */
	public boolean estCibleSelectionnee()
	{
		return !targets.isEmpty();
	}

	/**
	 * Retourne les cibles du personnage pour l'action préparée.
	 * 
	 * @return les cibles du personnages.
	 */
	public ArrayList<IBattle> getTargets() 
	{
		return targets;
	}


	/**
	 * Retourne l'arme principale du personnage.
	 *
	 * @return
	 * 		L'arme principale du personnage.
	 */
	public Weapon getMainWeapon() 
	{
		return mainWeapon;
	}

	/**
	 * Retourne l'arme secondaire du personnage.
	 *
	 * @return
	 * 		L'arme secondaire du personnage.
	 */
	public Weapon getSecondWeapon()
	{
		return secondWeapon;
	}
	
	/**
	 * Retourne l'attaque physique maximal du Personnage
	 * @return
	 */
	public int getPhysicAttack() 
	{
		return (int) (getMaximumStatistic("force") * 2.5 + getMaximumStatistic("atqPhy"));
	}

	/**
	 * Sélectionne la cible suivante de la cible en cours (si l'ancienne cible est morte).
	 * Ne fonctionne que pour des actions mono-cibles.
	 * Si la nouvelle cible selectionnée est de même type que l'ancienne cible.
	 * (Si l'ancienne cible est un Personnage la nouvelle cible sera un Personnage...)
	 */
	public void chooseNextTarget(Combat combat)
	{
		
		if(targets.size() == 1)
		{

			if(targets.get(0) instanceof Ennemy)
			{
				if(!targets.get(0).isAlive())
				{
					EnnemisParty ennemis = combat.getEnnemis();
					ennemis.getEnnemis().get(targets.get(0));
					
					
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
			
			else
			{
				/*
				if(!((Personnage)cibles.get(0)).estVivant())
				{
					Equipe equipe = combat.getEquipe();
					int indexCible = combat.getEquipe().indexOf((Personnage)cibles.get(0));
					for(int i = 0; i<equipe.nbPersonnages(); i++)
					{
						if(combat.getEquipe().get((i + indexCible) % equipe.nbPersonnages()).estVivant())
						{
							cibles.set(0, combat.getEquipe().get((i + indexCible) % equipe.nbPersonnages()));
							break;
						}
					}
				}
				*/
			}
		}
	}

	/**
	 * Retourne la défense physique totale du personnage
	 *  
	 * @return
	 * 		défense physique totale du personnage.
	 */
	public int getPhysicDefense()
	{
		int def = 0;
		
		for(Armor a : armors.values())
		{
			if(a!=null)
			{
				def += a.getPhysicDefense();
			}
		}
		
		if(mainWeapon != null)
		{
			def += mainWeapon.getPhysicDefense();
		}
		if(secondWeapon != null)
		{
			def += secondWeapon.getPhysicDefense();
		}
		
		return (int) (getMaximumStatistic("endurance") * 1.2 + getMaximumStatistic("defPhy") + def);
	}
	
	/**
	 * Retourne la dextérité du personnage.
	 * 
	 * @return la dextérité de l'ennemi.
	 */
	public int getAgility()
	{
		return getMaximumStatistic("dexterite");
	}
	
	/**
	 * Retourne l'attaque magique du personnage.
	 * 
	 * @return l'attaque magique du personnage.
	 */
	public int getMagicAttack()
	{
		return (int) (getMaximumStatistic("intelligence") * 1.2 + getMaximumStatistic("attMag"));
	}
	
	/**
	 * Retourne le pouvoir de soin du personnage.
	 * 
	 * @return le pouvoir de soin du personnage.
	 */
	public int getHealth()
	{
		return (int) (getMaximumStatistic("sagesse") * 1.2 + getMaximumStatistic("soin"));
	}
	
	/**
	 * Retourne la défense magique du personnage.
	 * 
	 * @return la défense magique du personnage.
	 */
	public int getMagicDefense()
	{	
		return (int) (getMaximumStatistic("sagesse") * 1.2 + getMaximumStatistic("defMag"));
	}


	/**
	 * Retourne la couleur de la barre d'énergie du personnage.
	 * 
	 * @return
	 * 		La couleur de la barre d'énergie du personnage.
	 */
	abstract public Color energyColor();



	/**
	 * Retourne le nombre de point d'expérience que posséde le personnage.
	 * @return
	 */
	public int getExperience() 
	{
		return experience;
	}


	/**
	 * Utilise l'objet passé en paramètre sur le personnage pour lui appliquer ses effets.
	 * Retourne vrai si l'objet à un effet sur le personnage, faux sinon.
	 * @param item
	 * 		Objet à utliser.
	 * @return
	 * 		Vrai si l'objet à eu un effet. Faux sinon.
	 */
	public boolean useItem(IItems item) 
	{
		boolean hasEffect = false;
		for(String effect : item.getEffects())
		{
			String[] paire = effect.split(":");
			if(paire[0].equalsIgnoreCase("soin"))
			{
				hasEffect |= healthPoints != getMaximumHealthPoints();
				updateHealthPoints(Integer.parseInt(paire[1]));
			}
		}
		return hasEffect;
	}


	/**
	 * Equipe le personnage del'objet passé en paramètre.
	 * Renvoie false si il peut s'en équipe, faux sinon.
	 * 
	 * @param equipable
	 * 		Objet equipable.
	 * @return Vrai si le personnage peur porter le stuff, faux sinon.
	 */
	public boolean equipStuff(Stuff equipable) 
	{
		if(equipable instanceof Armor)
		{
			return equipeArmor((Armor)equipable);
		}
		else if(equipable instanceof Weapon)
		{
			Weapon arme = (Weapon)equipable;
			if(arme.getType() == Weapon.SHIELD || arme.getType() == Weapon.BOW)
			{
				return equipeArme(SECOND_WEAPON,(Weapon)equipable);
			}
			else
			{
				return equipeArme(MAIN_WEAPON,(Weapon)equipable);
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return name;
	}


	/**
	 * Déséquipe le personnage de l'équipable passé en paramètre.
	 * 
	 * @param objetSelectionne
	 * 		L'équipable à déséquiper.
	 */
	public void desequipe(Stuff equipable) 
	{
		if(mainWeapon.equals(equipable))
		{
			mainWeapon = null;
		}
		else if(secondWeapon.equals(equipable))
		{
			secondWeapon = null;
		}
		else
		{
			for(Integer a : armors.keySet())
			{
				if(armors.get(a).equals(equipable))
				{
					armors.put(a, null);
					break;
				}
			}
		}
	}

	/**
	 * Retourne l'image du personnage pour les combats.
	 * 
	 * @return l'image du personnage pour les combats.
	 */
	@Override
	public Image getImageForBattle() {
		return getImagePositionAttaque().getScaledCopy(2);
	}
	
	/**
	 * Retourne l'ATBManager du personnage.
	 * 
	 * @return l'ATBManager du personnage.
	 */
	public ActiveTimeBattleManager getActiveTimeBattleManager(){
		return activeTimeBattleManager;
	}


	/**
	 * Réduit les pv du personnage à 0.
	 */
	public void kill() {
		healthPoints = 0;
	}

	/**
	 * Recréé un ATB Manager et annule l'ancien.
	 */
	public void resetActiveTimeBattleManager() {
		activeTimeBattleManager.stop();
		activeTimeBattleManager = new ActiveTimeBattleManager(100);
	}


	public void launchActiveTime() {
		activeTimeBattleManager.launch(util.Random.randInt(20,60));
	}


	public Skin getSkin() {
		return skin;
	}
}
