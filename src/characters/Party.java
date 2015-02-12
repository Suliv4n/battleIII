package characters;


import game.Config;
import game.battle.IBattle;
import game.system.Configurations;
import game.system.application.Application;

import java.util.ArrayList;
import java.util.Iterator;

import map.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import util.Random;
import bag.Bag;
import bag.IItems;
import bag.item.stuff.Stuff;



import data.DataManager;



/**
 * Une équipe est composée de personnages.
 * 
 * Une équipe ne peut comporter qu'un certain nombre de personnages.
 * 
 * @author Darklev
 *
 */
public class Party implements Iterable<Character>
{
	public static final int SOUTH = 0;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	
	
	//COORDONNEES
	private double relativeX; // par rapport à l'écran
	private double relativeY;
	
	private double absoluteX; // par rapport à la Map
	private double absoluteY;
	
	private Map map;
	//___________

	//liste des id des ennemis analysés
	private ArrayList<String> analysedEnnemis;
	private Bag sac;
	private int money;
	
	/**
	 * Direction dans laquelle regarde l'équipe.
	 */
	private int direction;
	private boolean moving = false;
	
	private Character[] party;
	private int slots;
	
	
	/**
	 * Constructeur de Equipe.
	 * 
	 * @param slots
	 * 		Le nombre de personnages autorisé dans l'équipe
	 * @throws SlickException 
	 */
	public Party(int slots) throws SlickException
	{
		party = new Character[slots];
		this.slots = slots;
		
		relativeX = Configurations.SCREEN_WIDTH/2;
		relativeY = Configurations.SCREEN_HEIGHT/2;
		
		absoluteX = 100;
		absoluteY = 100;
				
		analysedEnnemis = new ArrayList<String>();
		
		map = DataManager.loadMap("test1", - absoluteX + relativeX, - absoluteY + relativeY, true);
		money = 10;
		
		sac = new Bag();
		
		
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
	public boolean add(Character character)
	{
		int i=0;
		while(i<slots && party[i]!=null)
		{
			i++;
		}
		
		if(i < slots)
		{
			party[i]=character;
			return true;
		}
		return false;
	}
	
	/**
	 * Retourne l'animation du premier personnage vivant de l'équipe en fonction
	 * de la direction dans laquelle il regarde.
	 * 
	 * @return l'animation de l'équipe.
	 */
	public Animation getAnimation()
	{
		return firstAliveCharacter().getAnimation(direction);
	}
	
	/**
	 * Retourne le premier personnage vivant de l'équipe.
	 * @return
	 */
	private Character firstAliveCharacter() {
		for(Character c : this)
		{
			if(c.isAlive()){
				return c;
			}
		}
		return null;
	}


	/**
	 * Retourne la direction dans la quelle regarde l'équipe.
	 * 
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
	public Iterator<Character> iterator() 
	{
		ArrayList<Character> col = new ArrayList<Character>();
		for(Character p : party)
		{
			col.add(p);
		}		
		
		return col.iterator();
	}

	/**
	 * Retourne la map sur laquelle se trouve l'equipe.
	 * @return la map sur laquelle se trouve l'équipe.
	 */
	public Map getMap() 
	{
		return map;
	}
	
	/**
	 * Retourne le sac de l'équipe où se trouve les objets 
	 * de l'équipe.
	 * 
	 * @return le sac de l'équipe.
	 */
	public Bag getBag()
	{
		return sac;
	}

	/**
	 * Retourne la monaie de l'équipe.
	 * 
	 * @return la monaie de l'équipe.
	 */
	public int getMoney()
	{
		return money;
	}
	
	/**
	 * Retourne la citesse de déplacement en pixel/frame de l'équipe.
	 * Il s'agit de la vitesse du personnage à l'index 0.
	 * 
	 * @return la vitesse de l'équipe.
	 */
	public double speed()
	{
		//Modifier vitesse 10 default 
		return 15 + party[0].getSpeed();
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
		party[0].getSkin().moveX((float) dx);
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
		party[0].getSkin().moveY((float) dy);
		relativeY += dy;		
	}
	
	/**
	 * Change la direction dans laquelle regarde l'équipe.
	 * 
	 * @param la nouvelle direction (NORTH,SOUTH,EAST,WEST)
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
	public double getAbsoluteX()
	{
		return absoluteX;
	}

	/**
	 * Met à jour la position absolue (coordonnées par rapport à la map) sur l'aye X de l'équipe.
	 * 
	 * @param dx
	 * 		Déplacement sur l'axe X
	 * 
	 */
	public void setAbsoluteX(double dx) 
	{
		this.absoluteX += dx;
	}
	
	
	/**
	 * Retourne la position absolue (par rapport à la map) de l'équipe sur l'axe Y.
	 * 
	 * @return la position absolue (par rapport à la map) de l'équipe sur l'axe Y.
	 */	
	public double getAbsoluteY() 
	{
		return absoluteY;
	}


	/**
	 * Met à jour la position absolue (coordonnées par rapport à la map) sur l'aye Y de l'équipe.
	 * 
	 * @param dy
	 * 		Déplacement sur l'axe Y
	 */
	public void setAbsoluteY(double dy) 
	{
		this.absoluteY += dy;
	}

	
	/**
	 * Retourne le personnage à la position passé en paramètre.
	 * 
	 * @param i
	 * 		Index du personnage à retourner.
	 * @return
	 * 		le personnage de l'équipe à l'index i.
	 */
	public Character get(int i) 
	{
		return party[i];
		
	}
	
	/**
	 * Retourne le personnage à l'index passé en paramètre ou retourne
	 * null si le personnage n'existe pas.
	 * 
	 * @param i
	 * 	Index du personnage.
	 * @return
	 * 	le personnage à l'indexe i sauf, ou null s'il n'existe pas.
	 */
	public Character getIfExists(int i) {
		Character res;
		try{
			res = party[i];
		}
		catch(IndexOutOfBoundsException e){
			res = null;
		}

		return res;
	}
	
	/**
	 * Modifie l'ordonnée relative de l'équipe (par raport à l'écran).
	 * 
	 * @param y
	 * 		Nouvelle ordonnée relative.
	 */
	public void setValRelativeY(double y) 
	{
		this.relativeY = y;
		party[0].getSkin().setY((float) y);
	}


	/**
	 * Modifie l'abscisse relative de l'équipe (par raport à l'écran).
	 * 
	 * @param x
	 * 		Nouvelle abscisse relative.
	 */
	public void setValRelativeX(double x) {
		this.relativeX = x;
		party[0].getSkin().setX((float) x);
	}


	/**
	 * Affiche sur la fenêtre un résumé de chaque personnage de l'équipe (nom, xp, pv ...)
	 *  
	 * @param g
	 * 		Graphics
	 * @param selection
	 * 		Index du personnage sélectionné par le joueur.
	 */
	public void renderTeamList(Graphics g, int selection)
	{
		for(int i=0;i<party.length;i++)
		{
			party[i].renderCharacterPanel(g,0,160*i,selection == i);
		}
		
	}


	/**
	 * Retourne le nombre de personnages dans l'équipe.
	 * 
	 * @return
	 * 		Le nombre de personnage dans l'équpe.
	 */
	public int numberOfCharacters() 
	{
		int total = 0;
		for(Character p : party)
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
		Character temp = party[p0];
		party[p0] = party[pF];
		party[pF] = temp;
	}
	
	/**
	 * Retourne l'index du personnage passé en paramètre ou -1 si le personnage n'est pas dans l'équipe.
	 * @param personnage
	 * 		Personnage dont l'index est à retourner.
	 * @return
	 * 		l'index du personnage passé en paramètre ou -1 si le personnage n'est pas dans l'équipe.
	 */
	public int indexOf(Character personnage)
	{
		for(int i = 0; i < numberOfCharacters(); i++)
		{
			if(personnage.equals(party[i]))
			{
				return i;
			}
		}
		return -1;
	}



	/**
	 * Retourne le nombre de personnage dont les PV sont >= 1.
	 * 
	 * @return le nombre de personnages vivants dans l'équipe.
	 */
	public int numberOfAliveCharacters() 
	{
		int total = 0;
		for(Character p : party)
		{
			if(p.isAlive())
			{
				total++;
			}
		}
		return total;
	}
	
	/**
	 * Retourne vrai si tous les membres de l'équipe préparent une action 
	 * lors d'un combat, sinon retourne faux.
	 * 
	 * @return vrai si tous les personnages de l'équipe sont prêts, faux sinon.
	 */
	public boolean isReady()
	{
		for(Character p : party)
		{
			if(!p.estActionPreparee());
			{
				return false;
			}
		}
		return true;
	}


	/**
	 * Retourne un personnage aléatoire parmis les personnages vivants de l'équipe.
	 * 
	 * @return un personnage aléatoire vivant de l'équipe.
	 */
	public Object getRandomCharacter() 
	{
		if(numberOfAliveCharacters() == 0)
		{
			return null;
		}
		else
		{
			int a = (int) (Math.random() * numberOfAliveCharacters());
			while (!party[a].isAlive())
			{
				a = (a + 1) % numberOfCharacters();
			}
			return party[a];
		}
	}


	/**
	 * Retourne vrai si l'équipe a déjà analysé l'ennemi dont l'id est passé en paramètre, sinon faux.
	 * @param id
	 * 		L'id de l'ennemi.
	 * @return
	 * 		 vrai si l'équipe a déjà analysé l'ennemi dont l'id est passé en paramètre, sinon faux.
	 */
	public boolean isAnalysed(String id) 
	{
		return analysedEnnemis.contains(id);
	}

	/**
	 * Retourne le mage de l'équipe ou null s'il n'y a pas de mage.
	 * 
	 * @return 
	 * 		le mage de l'équipe ou null s'il n'y a pas de mage.
	 */
	public Mage getMage()
	{
		for(Character p : party)
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
	public Ranger getRanger()
	{		
		for(Character p : party)
		{
			if(p instanceof Ranger)
			{
				return (Ranger)p;
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
	public Warrior getWarrior()
	{		
		for(Character p : party)
		{
			if(p instanceof Warrior)
			{
				return (Warrior)p;
			}
		}
		return null;
	}
	
	
	/**
	 * Retourne vrai si l'equipable passé en paramètre est équipé
	 * par l'un des personnage de l'équipe, sinon faux.
	 * 
	 * @param equipable
	 * 		Stuff à tester
	 * 
	 * @return vrai si l'equipable passé en paramètre est équipé
	 * par l'un des personnage de l'équipe, sinon faux.
	 */
	public boolean isEquiped(Stuff equipable)
	{
		for(Character p : this)
		{
			for(Stuff e : p.armors.values())
			{
				if(e != null)
				{
					if(e.equals(equipable))
					{
						return true;
					}
				}
			}
			
			if(p.mainWeapon != null)
			{
				if(p.mainWeapon.equals(equipable))
				{
					return true;
				}
			}
			
			if(p.secondWeapon != null)
			{
				if(p.secondWeapon.equals(equipable))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Ajoute un objet en quantité passé en paramètre dans le sac de l'équipe.
	 * 
	 * @param item
	 * 		Objet à ajouter.
	 * @param quantity
	 * 		Quantité de l'objet à ajouter.
	 */
	public void addItemInBag(IItems item,int quantity)
	{
		sac.add(item, quantity);
	}
	
	/**
	 * Retourne un tableau avec tous les personnages de l'équipe.
	 * 
	 * @return un tableau des personnages de l'équipe.
	 */
	public Character[] getCharacters()
	{
		return party;
	}

	/**
	 * Ajoute un ennemi à la liste des ennemis analysés.
	 * (Compétence "Recherche" du mage)
	 * 
	 * @param id
	 *		L'id de l'ennemi analysé.
	 */
	public void analyse(String id)
	{
		analysedEnnemis.add(id);
	}

	/**
	 * CHange la map où se trouve l'équipe.
	 * 
	 * @param map
	 * 		Nouvelle map où se trouve l'équipe.
	 */
	public void setMap(Map map) 
	{
		this.map = map;
	}

	/**
	 * Modifie l'absisse absolue de l'équipe. (par rapport à la map)
	 * 
	 * @param x
	 * 		Nouvelle abscisse absolue
	 */
	public void setValAbsoluteX(double x)
	{
		absoluteX = x;	
	}
	
	/**
	 * Modifie l'ordonnée absolue de l'équipe. (par rapport à la map)
	 * 
	 * @param x
	 * 		Nouvelle ordonnée absolue
	 */
	public void setValAbsoluteY(double y)
	{
		absoluteY = y;	
	
	}

	/**
	 * Met à jour la monaie de l'équipe en ajoutant la valeur passé en paramètre.
	 * @param money
	 * 		Valeur à ajouter à la monaie de l'équipe.
	 */
	public void updateMoney(int money)
	{
		this.money += money;
	}

	
	/**
	 * Dessine l'équipe.
	 */
	public void draw() {
		draw(1, false);
	}
	
	/**
	 * Affiche l'équipe avec une certiane opacité
	 * 	
	 * @param alpha
	 * 		Opacité entre 0 et 1
	 * @param absolute
	 * 		Vrai s'il faut utiliser les coordonnées absolues. Sinon faux.
	 */
	public void draw(float alpha, boolean absolute) {
		Animation animation =  get(0).getAnimation(direction).copy();
		animation.setAutoUpdate(true);
		for(int i=0; i<animation.getFrameCount() ;i++){
			animation.getImage(i).setAlpha(alpha);
		}
		
		int x = (int)(relativeX - animation.getWidth()/2);
		int y = (int)(relativeY - animation.getHeight()/2);
		
		if(absolute)
		{
			x = (int) (Application.application().getGame().getParty().getMap().getX() + absoluteX);
			y = (int) (Application.application().getGame().getParty().getMap().getY() + absoluteY);

		}
		
		if(moving){
			animation.draw(x, y);
		}
		else{
			animation.getImage(2).draw(x, y);
		}
	}
	
	public void update(Input in){
		
	}


	/**
	 * Indique si l'équipe est en mouvement ou non.
	 * @param moving
	 * 		Vrai si l'équipe est en mouvement, sinon faux.
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}


	/**
	 * Retourne un personnage aléatoire pouvant être ciblé.
	 * 
	 * @return un personnage valide de façon aléatoire.
	 */
	public IBattle getRandomValidTarget() {
		
		ArrayList<IBattle> validTargets = getValidTargets();
		
		return validTargets.get(Random.randInt(0, validTargets.size() - 1));
	}


	/**
	 * Retourne les personnages pouvant être ciblé par une compétence ou un objet ...
	 * 
	 * @return les personnages pouvant être ciblés.
	 */
	public ArrayList<IBattle> getValidTargets() {
		
		ArrayList<IBattle> validTargets = new ArrayList<IBattle>();
		for(Character c : this){
			if(c.isAlive()){
				validTargets.add(c);
			}
		}
		return validTargets;
	}
	
	public void drawHitbox(Color color){
		party[0].getSkin().drawHitbox(color);
	}
}
