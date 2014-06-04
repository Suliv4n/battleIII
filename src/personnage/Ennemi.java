package personnage;

import game.Combat;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import skill.Skill;

public class Ennemi implements IBattle
{
	private String id;
	private String nom;
	private String description;
	private Image image;
	
	
	//Stats
	private int attaque_physique;
	private int attaque_magique;
	private int defense_physique;
	private int defense_magique;
	private int dexterite;
	private int soin;
	private int pv_maximum;
	private int pv;
	
	
	private int experience;
	private int argent;
	
	private Object action;
	private ArrayList<IBattle> cibles;
	
	//Objets dropés

	private ArrayList<Skill> skills;

	
	
	public Ennemi(String id, String nom, String description, int atq_p, int atq_m, int def_p, int def_m, int dext, int soin, int pv_max,int xp, int argent, Image image)
	{
		this.id = id;
		this.nom = nom;
		this.description = description;
		attaque_physique = atq_p;
		attaque_magique = atq_m;
		defense_physique = def_p;
		defense_magique = def_m;
		dexterite = dext;
		this.soin = soin;
		pv_maximum = pv_max;
		experience = xp;
		this.argent = argent;
		this.image = image;
		pv = pv_max;
		
		cibles = new ArrayList<IBattle>();
	}

	public Image getImage()
	{
		return image;
	}

	public int getDexterite() 
	{
		return dexterite;
	}
	
	//_-_-_-_-_-_-COMBAT-_-_-_-_-_-_
	
	
	/**
	 * 
	 */
	public void doIA(Combat combat)
	{
		//TODO : stratégie ennemi
		Object action = "Attaquer";
		
		
		cibles.add((IBattle) combat.getEquipe().getPersonnageAlea());
		
		
		setAction(action);
	}
	
	/**
	 * Annule l'action en cours
	 */
	public void annulerAction()
	{
		action = null;
	}

	public boolean estPret() 
	{
		return action != null;
	}
	
	public boolean estVivant()
	{
		return pv > 0;
	}
	
	/**
	 * Sélectionne la cible suivante de la cible en cours.
	 * Ne fonctionne que pour des actions mono-cibles.
	 * Si la nouvelle cible selectionnée est de même type que l'ancienne cible.
	 * (Si l'ancienne cible est un Personnage la nouvelle cible sera un Personnage...)
	 */
	public void selectionnerCibleSuivante(Combat combat)
	{		
		if(cibles.size() == 1)
		{
			if(cibles.get(0) instanceof Ennemi)
			{
					if(!((Ennemi)cibles.get(0)).estVivant())
					{
					EquipeEnnemis ennemis = combat.getEnnemis();
					Ennemi currentCible = ennemis.getEnnemis().get(cibles.get(0));
					if(ennemis.getEnnemis().containsValue(currentCible))
					{
						for(Ennemi e : ennemis.getEnnemis().values())
						{
							if(e.estVivant())
							{
								cibles.set(0,e);
								break;
							}
						}
					}
				}
			}
			
			else //Personnage
			{
				if(!cibles.get(0).estVivant())
				{
					Equipe equipe = combat.getEquipe();
					int indexCible = combat.getEquipe().indexOf((Personnage)cibles.get(0));
					
					for(int i = 0; i<equipe.nbPersonnages(); i++)
					{
						if(combat.getEquipe().get((i + indexCible) % equipe.nbPersonnages()).estVivant())
						{
							cibles.set(0, combat.getEquipe().get((i + indexCible) % equipe.nbPersonnages()));
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
	 * @return
	 */
	public ArrayList<IBattle> getCibles() 
	{
		return cibles;
	}

	public Object getAction() 
	{
		return action;
	}

	public int getDefensePhysique() 
	{
		return defense_physique;
	}

	public int getAttaquePhysique() 
	{
		return attaque_physique;
	}
		
	public int getAttaqueMagique()
	{
		return attaque_magique;
	}
	public int getDefenseMagique()
	{
		return defense_magique;
	}
	
	public int getPVMaximum()
	{
		return pv_maximum;
	}	
	
	public int getPV()
	{
		return pv;
	}
	

	public String getNom()
	{
		return nom;
	}
	

	
	public String getID() 
	{
		return id;
	}


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

	@Override
	public void setCibles(ArrayList<IBattle> cibles) 
	{
		this.cibles = cibles;
	}

	@Override
	public int getSoin() 
	{
		return soin;
	}

	@Override
	public void setAction(Object action) 
	{
		this.action = action;
	}

	public int getXP() 
	{
		return experience;
	}
	
}
