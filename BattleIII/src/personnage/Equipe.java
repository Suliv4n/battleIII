package personnage;


import game.Config;

import java.util.ArrayList;
import java.util.Iterator;
import map.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import sac.IObjet;
import sac.Sac;


import donnees.GenerateurDonnees;



/**
 * Une équipe est composée de personnages.
 * 
 * Une équipe ne peut comporter qu'un certain nombre de personnages.
 * 
 * @author Darklev
 *
 */
public class Equipe implements Iterable<Personnage>
{
	public static final int BAS = 0;
	public static final int HAUT = 1;
	public static final int DROITE = 2;
	public static final int GAUCHE = 3;
	
	
	//COORDONNEES
	private double relativeX; // par rapport à l'écran
	private double relativeY;
	
	private double absolueX; // par rapport à la Map
	private double absolueY;
	
	private Map map;
	//___________
	
	private ArrayList<String> recherches;
	private Sac sac;
	private int argent;
	
	/**
	 * Direction dans laquelle regarde l'équipe.
	 */
	private int direction;
	
	
	private Personnage[] equipe;
	private int nbPlaces;
	
	
	/**
	 * Constructeur de Equipe.
	 * 
	 * @param nbSlots
	 * 		Le nombre de personnages autorisé dans l'équipe
	 */
	public Equipe(int nbSlots)
	{
		equipe = new Personnage[nbSlots];
		nbPlaces = nbSlots;
		
		relativeX = Config.LONGUEUR/2;
		relativeY = Config.LARGEUR/2;
		
		absolueX = 100;
		absolueY = 100;
				
		recherches = new ArrayList<String>();
		
		map = GenerateurDonnees.genererMap("test1", - absolueX + relativeX, - absolueY + relativeY);
		argent = 10;
		
		sac = new Sac();
		
		
		//Correcteur de coordonnées relatives initiales
		/*
		if(absolueX<Map.LONG/2)
		{
			relativeX = absolueX;
		}
		if(absolueY<Map.LARG/2)
		{
			relativeY = absolueY;
		}
		*/
		//Fin correcteur de coordonnées relatives initiales
	}
	 

	/**
	 * Ajoute un Personnage dans l'équipe s'il y a assez de place dans l'équipe.
	 * 
	 * @param Le personnage à ajouter.
	 * @return Vrai si le personnage a pu être ajouté, sinon faux.
	 * 
	 */
	public boolean ajouter(Personnage personnage)
	{
		int i=0;
		while(i<nbPlaces && equipe[i]!=null)
		{
			i++;
		}
		
		if(i < nbPlaces)
		{
			equipe[i]=personnage;
			return true;
		}
		return false;
	}
	
	/**
	 * Retourne l'animation du premier personnage de l'équipe en fonction
	 * de la direction dans laquelle il regarde.
	 */
	public Animation getAnimation()
	{
		return equipe[0].getAnimation(direction);
	}
	
	/**
	 * Retourne la direction dans la quelle regarde l'équipe.
	 * @return la direction dans la quelle regarde l'équipe.
	 */
	public int getDirection()
	{
		return direction;
	}
	
	/**
	 * Iterator permet de parcourir le tableau de personnages equipe
	 * avec une boucle foreach.
	 */
	@Override
	public Iterator<Personnage> iterator() 
	{
		ArrayList<Personnage> col = new ArrayList<Personnage>();
		for(Personnage p : equipe)
		{
			col.add(p);
		}		
		
		return col.iterator();
	}

	/**
	 * Retourne la map sur laquelle se trouve l'equipe.
	 * @return
	 */
	public Map getMap() 
	{
		return map;
	}
	
	public Sac getSac()
	{
		return sac;
	}

	public int getArgent()
	{
		return argent;
	}
	
	public double vitesse()
	{
		//Modifier vitesse 10 default 
		return 15 + equipe[0].getVitesse();
	}

	/**
	 * Met à jour la position relative (coordonnées par rapport à la fenêtre) sur l'axe X de l'équipe.
	 * 
	 * @param dx
	 * 		Déplacement horizontal.
	 * 
	 */
	public void setRelativeX(double dx)
	{	
		relativeX += dx;
	}
	
	/**
	 * Met à jour la position relative (coordonnées par rapport à la fenêtre) sur l'aye Y de l'équipe.
	 * 
	 * @param dy
	 * 		Déplacement vertical
	 * 
	 */
	public void setRelativeY(double dy)
	{		
		relativeY += dy;		
	}
	
	/**
	 * Change la direction dans laquelle regarde l'équipe.
	 * 
	 * @param la nouvelle direction (Equipe.GAUCHE par exemple)
	 */
	public void setDirection(int direction) 
	{
		this.direction = direction;
	}

	/**
	 * Retourne la position relative (par rapport à la fenetre) de l'équipe sur l'axe X.
	 * 
	 * @return la position relative (par rapport à la fenetre) de l'équipe sur l'axe X.
	 */
	public double getRelativeX() 
	{
		return relativeX;
	}
	
	/**
	 * Retourne la position relative (par rapport à la fenetre) de l'équipe sur l'axe Y.
	 * 
	 * @return la position relative (par rapport à la fenetre) de l'équipe sur l'axe Y.
	 */
	public double getRelativeY() 
	{
		return relativeY;
	}
	
	/**
	 * Retourne la position absolue (par rapport à la map) de l'équipe sur l'axe X.
	 * 
	 * @return la position absolue (par rapport à la map) de l'équipe sur l'axe X.
	 */
	public double getAbsolueX()
	{
		return absolueX;
	}

	/**
	 * Met à jour la position absolue (coordonnées par rapport à la map) sur l'aye X de l'équipe.
	 * 
	 * @param dx
	 * 		Déplacement sur l'axe X
	 * 
	 */
	public void setAbsolueX(double dx) 
	{
		this.absolueX += dx;
	}
	
	
	/**
	 * Retourne la position absolue (par rapport à la map) de l'équipe sur l'axe Y.
	 * 
	 * @return la position absolue (par rapport à la map) de l'équipe sur l'axe Y.
	 */	
	public double getAbsolueY() 
	{
		return absolueY;
	}


	/**
	 * Met à jour la position absolue (coordonnées par rapport à la map) sur l'aye Y de l'équipe.
	 * 
	 * @param dy
	 * 		Déplacement sur l'axe Y
	 */
	public void setAbsolueY(double dy) 
	{
		this.absolueY += dy;
	}

	
	/**
	 * Retourne le personnage à la position passé en paramètre.
	 * 
	 * @param i
	 * 		Index du personnage à retourner.
	 * @return
	 * 		le personnage de l'équipe à l'index i.
	 */
	public Personnage get(int i) 
	{
		return equipe[i];
		
	}	
	
	
	public void setValRelativeY(double i) 
	{
		this.relativeY = i;
		
	}



	public void setValRelativeX(int i) {
		this.relativeX = i;
		
	}


	/**
	 * Affiche sur la fenêtre un résumé de chaque personnage de l'équipe (nom, xp, pv ...)
	 *  
	 * @param g
	 * 		Graphics
	 * @param selection
	 * 		Index du personnage sélectionné par le joueur.
	 */
	public void afficherListeEquipe(Graphics g, int selection)
	{
		for(int i=0;i<equipe.length;i++)
		{
			equipe[i].afficherGestionPerso(g,0,160*i,selection == i);
		}
		
	}


	/**
	 * Retourne le nombre de personnages dans l'équipe.
	 * 
	 * @return
	 * 		Le nombre de personnage dans l'équpe.
	 */
	public int nbPersonnages() 
	{
		int total = 0;
		for(Personnage p : equipe)
		{
			if(p!=null)
			{
				total++;
			}
		}
		return total;
	}


	/**
	 * Permutte la position d'un personnage de l'équipe avec celle d'un autre.
	 * 
	 * @param p0
	 * 		Position initiale du personnage.
	 * @param pF
	 * 		Nouvelle poistion du personnage.
	 */
	public void swap(int p0, int pF) 
	{
		Personnage temp = equipe[p0];
		equipe[p0] = equipe[pF];
		equipe[pF] = temp;
	}
	
	/**
	 * Retourne l'index du personnage passé en paramètre ou -1 si le personnage n'est pas dans l'équipe.
	 * @param personnage
	 * 		Personnage dont l'index est à retourner.
	 * @return
	 * 		l'index du personnage passé en paramètre ou -1 si le personnage n'est pas dans l'équipe.
	 */
	public int indexOf(Personnage personnage)
	{
		for(int i = 0; i < nbPersonnages(); i++)
		{
			if(personnage.equals(equipe[i]))
			{
				return i;
			}
		}
		return -1;
	}



	public int nombrePersonnagesVivants() 
	{
		int total = 0;
		for(Personnage p : equipe)
		{
			if(p.estVivant())
			{
				total++;
			}
		}
		return total;
	}
	
	public boolean estPrete()
	{
		for(Personnage p : equipe)
		{
			if(!p.estActionPreparee());
			{
				return false;
			}
		}
		return true;
	}



	public Object getPersonnageAlea() 
	{
		if(nombrePersonnagesVivants() == 0)
		{
			return null;
		}
		else
		{
			int a = (int) (Math.random() * nombrePersonnagesVivants());
			while (!equipe[a].estVivant())
			{
				a = (a + 1) % nbPersonnages();
				System.out.println("Test b2 oo : " + a + " - "  + !equipe[a].estVivant());
			}
			return equipe[a];
		}
	}


	/**
	 * Retourne vrai si l'équipe a déjà analysé l'ennemi dont l'id est passé en paramètre, sinon faux.
	 * @param id
	 * 		L'id de l'ennemi.
	 * @return
	 * 		 vrai si l'équipe a déjà analysé l'ennemi dont l'id est passé en paramètre, sinon faux.
	 */
	public boolean estAnalyse(String id) 
	{
		return recherches.contains(id);
	}

	/**
	 * Retourne le mage de l'équipe ou null s'il n'y a pas de mage.
	 * 
	 * @return 
	 * 		le mage de l'équipe ou null s'il n'y a pas de mage.
	 */
	public Mage getMage()
	{
		for(Personnage p : equipe)
		{
			if(p instanceof Mage)
			{
				return (Mage)p;
			}
		}
		return null;
	}

	/**
	 * Retourne le rodeur de l'équipe ou null s'il n'y a pas de rodeur.
	 * 
	 * @return 
	 * 		le rodeur de l'équipe ou null s'il n'y a pas de rodeur.
	 */
	public Rodeur getRodeur()
	{		
		for(Personnage p : equipe)
		{
			if(p instanceof Rodeur)
			{
				return (Rodeur)p;
			}
		}
		return null;
	}
	
	
	/**
	 * Retourne le guerrier de l'équipe ou null s'il n'y a pas de guerrier.
	 * 
	 * @return 
	 * 		le guerrier de l'équipe ou null s'il n'y a pas de guerrier.
	 */
	public Guerrier getGuerrier()
	{		
		for(Personnage p : equipe)
		{
			if(p instanceof Guerrier)
			{
				return (Guerrier)p;
			}
		}
		return null;
	}
	
	public void ajouterObjet(IObjet objet,int qte)
	{
		sac.ajouter(objet, qte);
	}

	/**
	 * Ajoute un ennemi à la liste des ennemis analysés.
	 * (Compétence "Recherche" du mage)
	 * 
	 * @param id
	 *		L'id de l'ennemi analysé.
	 */
	public void analyser(String id)
	{
		recherches.add(id);
	}


	public void setMap(Map map) 
	{
		this.map = map;
	}


	public void setValAbsolueX(double x)
	{
		absolueX = x;	
	}
	
	public void setValAbsolueY(double y)
	{
		absolueY = y;	
	
	}


	public void updatePO(int po)
	{
		argent += po;
	}
}
