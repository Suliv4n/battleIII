/**
 * @author Darklev
 */

package personnage;


import game.Combat;
import game.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;


import sac.IObjet;
import sac.objet.stuff.*;
import skill.Skill;



public abstract class Personnage implements IBattle
{
	
	public static final int ARME_PRINCIPALE=0;
	public static final int ARME_SECONDAIRE=1;
	
	private String nom;
	
	protected int level=1;
	protected int pv;
	protected int experience=0;
	
	protected Arme armePrincipale;
	protected Arme armeSecondaire;
	
	protected HashMap<String, Integer> stats;
	
	/**
	 * A chaque monter de level les stats augmente selon la HashMap statsUP
	 */
	protected HashMap<String, Integer> statsUP;
	
	protected ArrayList<Integer> typesArmesPrincipales;
	protected ArrayList<Integer> typesArmesSecondaires;
	protected ArrayList<Integer> typesArmures;
	
	protected HashMap<Integer,Armure> equipement;
	
	protected ArrayList<Skill> skills;
	
	protected SpriteSheet sprites;
	
	protected Animation animations[];
	
	//Action en cours => state combat
	private Object action;
	private ArrayList<IBattle> cibles;
	
	/**
	 * Constructeur de joueur. A la construction, le nom est initialisé.
	 * Les ArrayList et HashMap sont instanciées.
	 * 
	 * @param nom
	 * 			Le nom du personnage
	 */
	public Personnage(String nom)
	{
		this.nom=nom;
		stats=new HashMap<String,Integer>();
		statsUP = new HashMap<String, Integer>();
		
		typesArmesPrincipales=new ArrayList<Integer>();
		typesArmesSecondaires=new ArrayList<Integer>();
		typesArmures=new ArrayList<Integer>();
		
		equipement = new HashMap<Integer, Armure>();
		
		equipement.put(Armure.MAIN, null);
		equipement.put(Armure.PANTALON, null);
		equipement.put(Armure.TETE, null);
		equipement.put(Armure.TORSE, null);
		
		animations = new Animation[5];
		
		skills = new ArrayList<Skill>();
		cibles = new ArrayList<IBattle>();
	}
	

	
	public int getBonusEnergie() 
	{

		int total = 0;
		if(armePrincipale != null)
		{
			total+=armePrincipale.getBonus("energie");
		}
		if(armeSecondaire != null)
		{
			total+=armeSecondaire.getBonus("energie");
		}
		
		
		for(Armure a : equipement.values())
		{
			if(a != null)
			{
				total+=a.getBonus("energie");
			}
		}
		
		return total;
	}
	
	
	public int getStatMax(String stat) 
	{
		int total = 0;
		if(stats.containsKey(stat))
		{
			total=stats.get(stat);
		}
		if(armePrincipale != null)
		{
			total+=armePrincipale.getBonus(stat);
		}
		if(armeSecondaire != null)
		{
			total+=armeSecondaire.getBonus(stat);
		}
		
		
		for(Armure a : equipement.values())
		{
			if(a != null)
			{
				total+=a.getBonus(stat);
			}
		}
		
		return total;
	}
	

	

	
	/**
	 * Retourne le nombre de PV maximum en prenant en compte les bonus d'équipements.
	 * 
	 * @return PV maximum
	 */
	public int getPVMaximum()
	{
		int bonusPV = 0;
		if(armePrincipale != null)
		{
			bonusPV=armePrincipale.getBonus("pvmax");
			
		}
		if(armeSecondaire != null)
		{
			bonusPV+=armeSecondaire.getBonus("pvmax");
		}

		
		for(Armure a : equipement.values())
		{
			if(a != null)
			{
				bonusPV+=a.getBonus("pvmax");
			}
		}

		
		
		return (int) 5*getStatMax("endurance")+bonusPV;
	}
	
	public int getPV() 
	{
		return pv;
	}
	
	/**
	 * Retourne l'équipement du personnage (armures).
	 * @return
	 * 	Pièces de l'armure du personnage.
	 */
	public HashMap<Integer, Armure> getEquipement()
	{
		return equipement;
	}

	/**
	 * Augmente l'expérience du joueur qui monte de level après avoir obtenu assez 
	 * d'expérience.
	 *  
	 * <ul>
	 * <li>1 => 2 : 2 xp</li>
	 * <li>2 => 3 : 3 xp</li>
	 * <li>3 => 4 : 5 xp</li>
	 * <li>4 => 5 : 6 xp</li>
	 * </ul>
	 * @param xp
	 * 		La quantité de poinst d'expérience gagnés.
	 */
	public void gagnerXP(int xp)
	{
		for(int i=1;i<=xp;i++)
		{
			experience++;
			if(experience == prochainNiveau())
			{
				levelUP();
			}
		}
	}
	
	/**
	 * Retourne l'expérience nécessaire pour atteindre le prochain niveau.
	 * TODO à modifier description
	 * @return l'expérience nécessaire pour atteindre le prochain niveau.
	 */
	public int prochainNiveau() 
	{
		return (int) (experience + Math.round(level*1.5));
	}
	
	
	/**
	 * Incrémete le level du Personnage et améliorer ses stats en conséquence.
	 */
	private void levelUP() 
	{
		level++;		
		for(String k : stats.keySet())
		{
			stats.put(k,statsUP.get(k)+stats.get(k));
		}
	}

	/**
	 * Retourne le nom du personnage.
	 * 
	 * @return nom
	 * 		Le nom du personnage
	 */
	public String getNom()
	{
		return nom;
	}

	/**
	 * Met à jour le nombre de pv restant du personnage en ajoutant 
	 * la valeur passée en paramètre. Le nombre de pv est toujours >= 0 et <= pvMax
	 * 
	 * @param dPV 
	 * 		nombre de pv à ajouter (>0) 
	 *      ou à supprimer (<0)
	 */
	public void updatePV(int dPV)
	{
		if(dPV >= 0)
		{
			pv = Math.min(getPVMaximum(), pv + dPV);
		}
		else
		{
			pv = Math.max(0, pv + dPV);
		}
	}

	/**
	 * Equipe une arme au personnage. Retourne l'ancienne Arme si le personnage peut s'équiper.
	 * Retourne null sinon.
	 * 
	 * @param positionArme : ARME_PRINCIPALE ou ARME_SECONDAIRE
	 * @param arme L'arme à équiper.
	 * @return l'ancienne arme si le personnage peut s'équiper de l'arme, sinon null.
	 */
	public boolean equipeArme(int positionArme, Arme arme)
	{

		switch(positionArme)
		{
			case(ARME_PRINCIPALE):

				if(typesArmesPrincipales.contains(arme.getType()))
				{
					armePrincipale=arme;
					return true;
				}
			break;
			
			case(ARME_SECONDAIRE):

				if(typesArmesSecondaires.contains(arme.getType()))
				{
					armeSecondaire=arme;
					return true;
				}
			break;
		}
		
		return false;
	}
	
	/**
	 * Equipe une armure au personnage. Retourne l'ancienne armure si le personnage peut s'équiper.
	 * Retourne null sinon.
	 * 
	 * @param positionArme : 
	 * @return l'ancienne Armure si le personnage peut s'équiper de l'armure, sinon null.
	 */
	public boolean equipeArmure(Armure armure)
	{
		if(typesArmures.contains(armure.getType()))
		{
			equipement.put(armure.getEmplacement(),armure);
			return true;
		}
		
		return false;
	}
	
	
	public Animation getAnimation(int direction) 
	{
		return animations[direction];
	}
	
	/**
	 * Retourne l'énergie (furie/mana/concentration)
	 * @return l'énergie (furie/mana/concentration) du personnage.
	 */
	public abstract int getEnergie();

	/**
	 * Retourne la vitesse de marche du personnage en fonction des bonus
	 * des équipements et armes.
	 * 
	 * @return la vitesse de marche du personnage en fonction des bonus
	 * des équipements.
	 */
	public double getVitesse() 
	{
		double som = 0;
		for(Armure a : equipement.values())
		{
			if(a!=null)
			{
				som += a.getBonus("vitesse");
			}
			
		}
		
		if(armePrincipale != null)
		{
			som += armePrincipale.getBonus("vitesse");
		}
			
		if(armeSecondaire != null)
		{
			som += armeSecondaire.getBonus("vitesse");
		}
			
		return som;
	}
	
	public abstract int getEnergieMax();
	
	/**
	 * Le personnage apprend une nouvelle compétence(Skill)
	 * passée en paramètre.
	 * 
	 * @param skill
	 * 		La compétence que le personnage doit apprendre.
	 */
	public void apprendreCompetence(Skill skill)
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
	
	public void afficherGestionPerso(Graphics g, int x, int y, boolean selectionne) 
	{
		g.setColor(Config.couleur2);
		g.drawRect(x, y, 256, 160);
		g.drawRect(x+1, y+1, 254,158);
		
		g.setColor(Config.couleur1);
		g.fillRect(x+2, y+2, 253, 157);
		
		Animation perso = getAnimation(Equipe.BAS);
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
		g.drawString(nom, 25, y+10);
		g.drawString("N."+level, 140, y+10);
		g.drawString("PV", x+73, y+46);
		g.drawString(pv+"/"+getPVMaximum(),x+100,y+60);

		g.setColor(Config.couleur2);
		g.drawRect(x+95, y+50, 150, 10);
		g.drawRect(x+95, y+90, 150, 10);
		
		
		g.setColor(Color.black);
		g.fillRect(x+96, y+51, 149, 9);
		g.fillRect(x+96, y+91, 149, 9);
		
		//Remplissage bar pv
		g.setColor(new Color(0,100,0));
		g.fillRect(x+96, y+51, (float) (pv/((double)getPVMaximum()))*149, 9);
		
		g.setColor(new Color(255,255,255));
		g.drawString(getEnergie()+"/"+getEnergieMax(),x+100,y+100);
		
		//XP
		g.drawString("EXP", x+5, y+120);
		g.drawString("EXP : "+experience+"/"+prochainNiveau(), x+5, y+140);

		
		g.setColor(Config.couleur2);
		g.drawRect(x+35, y+127, 210, 4);
		
		g.setColor(new Color(0,0,0));
		g.fillRect(x+36, y+128, 209, 3);
		
		g.setColor(Config.couleurXP);
		g.fillRect(x+36, y+128, (int) (experience/prochainNiveau()*209) , 3);
		

	}

	


	public void afficherStats(Graphics g)
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
		for(String s : stats.keySet())
		{
			g.drawString(s.substring(0,1).toUpperCase()+s.substring(1),270,40+25*i);
			g.drawString(String.valueOf(getStatMax(s)),400,40+25*i);
			
			i++;
		}
	}

	
	public void afficherEquipements(Graphics g)
	{
		g.setColor(Config.couleur2);
		g.drawRect(256, 160, 383, 200);
		g.drawRect(257, 161, 382, 199);
		
		g.setColor(Config.couleur1);
		g.fillRect(258, 162, 381, 198);
		
		g.setColor(new Color(255,255,255));
		g.drawString("EQUIPEMENTS", 400, 170);
		
		g.drawString("Arme principale", 270, 210);
		if(armePrincipale != null)
		{
			g.drawString(armePrincipale.toString(), 410, 210);
		}
		
		g.drawString("Arme Secondaire", 270, 235);
		if(armeSecondaire != null)
		{
			g.drawString(armePrincipale.toString(), 410, 210);
		}
		
		int i=1;
		for(Integer k : equipement.keySet())
		{
			g.drawString(Armure.emplacement(k).substring(0,1).toUpperCase()+Armure.emplacement(k).substring(1), 270, 235+i*25);
			if(equipement.get(k)!=null)
			{
				g.drawString(equipement.get(k).toString(), 410, 235+i*25);
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
		return getAnimation(Equipe.GAUCHE).getImage(2);
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
	public boolean estVivant() 
	{
		return pv > 0;
	}

	public void setNom(String nom) 
	{
		this.nom = nom;
	}

	
	//-_-_-_-_-_-_-COMBAT-_-_-_-_-_-_-_
	/**
	 * Prépare l'action du personnage
	 * @param action
	 */
	public void setAction(Object action)
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
	public void annulerAction()
	{
		action = null;
		cibles.clear();
	}



	/**
	 * Retourne l'action l'action préparé.
	 * 
	 * @return
	 * 	Retourne l'action prépapré.
	 */
	public Object getAction() 
	{
		return action;
	}

	public void setCibles(ArrayList<IBattle> cibles)
	{
		this.cibles = cibles; 
	}



	public void ajouterCible(IBattle cible) 
	{
		cibles.add(cible);
	}

	public boolean estCibleSelectionnee()
	{
		return !cibles.isEmpty();
	}

	public ArrayList<IBattle> getCibles() 
	{
		return cibles;
	}


	/**
	 * Retourne l'arme principale du personnage.
	 *
	 * @return
	 * 		L'arme principale du personnage.
	 */
	public Arme getArmePrincipale() 
	{
		return armePrincipale;
	}

	/**
	 * Retourne l'arme secondaire du personnage.
	 *
	 * @return
	 * 		L'arme secondaire du personnage.
	 */
	public Arme getArmeSecondaire()
	{
		return armeSecondaire;
	}
	
	/**
	 * Retourne l'attaque physique maximal du Personnage
	 * @return
	 */
	public int getAttaquePhysique() 
	{
		return (int) (getStatMax("force") * 2.5 + getStatMax("atqPhy"));
	}

	/**
	 * Sélectionne la cible suivante de la cible en cours (si l'ancienne cible est morte).
	 * Ne fonctionne que pour des actions mono-cibles.
	 * Si la nouvelle cible selectionnée est de même type que l'ancienne cible.
	 * (Si l'ancienne cible est un Personnage la nouvelle cible sera un Personnage...)
	 */
	public void selectionnerCibleSuivante(Combat combat)
	{
		
		if(cibles.size() == 1)
		{
			System.out.println( "cibles.size() == 1" );
			if(cibles.get(0) instanceof Ennemi)
			{
				System.out.println( "cibles.get(0) instanceof Ennemi" + " - " + !cibles.get(0).estVivant());
				if(!cibles.get(0).estVivant())
				{
					EquipeEnnemis ennemis = combat.getEnnemis();
					Ennemi currentCible = ennemis.getEnnemis().get(cibles.get(0));
					
					
					for(Ennemi e : ennemis.getEnnemis().values())
					{
						System.out.println( "boucle : " + e );
						if(e.estVivant())
						{
							cibles.set(0,e);
							System.out.println( "NOUVELLE CIBLE : " + e );
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
	public int getDefensePhysique()
	{
		int def = 0;
		
		for(Armure a : equipement.values())
		{
			if(a!=null)
			{
				def += a.getDefensePhysique();
			}
		}
		
		if(armePrincipale != null)
		{
			def += armePrincipale.getDefensePhysique();
		}
		if(armeSecondaire != null)
		{
			def += armeSecondaire.getDefensePhysique();
		}
		
		return (int) (getStatMax("endurance") * 1.2 + getStatMax("defPhy") + def);
	}
	
	public int getDexterite()
	{
		return getStatMax("dexterite");
	}
	
	public int getAttaqueMagique()
	{
		return (int) (getStatMax("intelligence") * 1.2 + getStatMax("attMag"));
	}
	
	public int getSoin()
	{
		return (int) (getStatMax("sagesse") * 1.2 + getStatMax("soin"));
	}
	
	public int getDefenseMagique()
	{
		int defMag = 0;
		
		for(Armure a : equipement.values())
		{
			if(a!=null)
			{
				defMag += a.getDefenseMagique();
			}
		}
		
		if(armePrincipale != null)
		{
			defMag += armePrincipale.getDefenseMagique();
		}
		if(armeSecondaire != null)
		{
			defMag += armeSecondaire.getDefenseMagique();
		}
		
		return (int) (getStatMax("sagesse") * 1.2 + getStatMax("defMag"));
	}


	/**
	 * Retourne la couleur de la barre d'énergie du personnage.
	 * 
	 * @return
	 * 		La couleur de la barre d'énergie du personnage.
	 */
	abstract public Color couleurEnergie();



	public int getXP() 
	{
		return experience;
	}


	/**
	 * Utilise l'objet passé en paramètre sur le personnage pour lui appliquer ses effets.
	 * Retourne vrai si l'objet à un effet sur le personnage, faux sinon.
	 * @param objet
	 * 		Objet à utliser.
	 * @return
	 * 		Vrai si l'objet à eu un effet. Faux sinon.
	 */
	public boolean utiliserObjet(IObjet objet) 
	{
		boolean aEffet = false;
		for(String effet : objet.getEffets())
		{
			String[] paire = effet.split(":");
			if(paire[0].equalsIgnoreCase("soin"))
			{
				aEffet |= pv != getPVMaximum();
				updatePV(Integer.parseInt(paire[1]));
			}
		}
		return aEffet;
	}


	/**
	 * Equipe le personnage del'objet passé en paramètre.
	 * Renvoie false si il peut s'en équipe, faux sinon.
	 * 
	 * @param equipable
	 * 		Objet equipable.
	 * @return
	 * 		Vrais si le personnage peut s'équiper, faux sinon.
	 */
	public boolean equipeStuff(Equipable equipable) 
	{
		if(equipable instanceof Armure)
		{
			return equipeArmure((Armure)equipable);
		}
		else if(equipable instanceof Arme)
		{
			Arme arme = (Arme)equipable;
			if(arme.getType() == Arme.BOUCLIER || arme.getType() == Arme.ARC)
			{
				return equipeArme(ARME_SECONDAIRE,(Arme)equipable);
			}
			else
			{
				return equipeArme(ARME_PRINCIPALE,(Arme)equipable);
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return nom;
	}


	/**
	 * Déséquipe le personnage de l'équipable passé en paramètre.
	 * 
	 * @param objetSelectionne
	 * 		L'équipable à déséquiper.
	 */
	public void desequipe(Equipable equipable) 
	{
		if(armePrincipale.equals(equipable))
		{
			armePrincipale = null;
		}
		else if(armeSecondaire.equals(equipable))
		{
			armeSecondaire = null;
		}
		else
		{
			for(Integer a : equipement.keySet())
			{
				if(equipement.get(a).equals(equipable))
				{
					equipement.put(a, null);
					break;
				}
			}
		}
		
	}
	
}
