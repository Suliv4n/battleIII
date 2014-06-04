package game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import donnees.Formatage;

import personnage.Equipe;
import personnage.Personnage;

import sac.Categorie;
import sac.IObjet;
import sac.Sac;
import sac.objet.stuff.Arme;
import sac.objet.stuff.Armure;
import sac.objet.stuff.Equipable;

/**
 * Game State permettant d'avoir un aperçu sur les objets du joueur 
 * et d'intéragir avec (utilisation, jeter ...)
 * 
 * @author Darklev
 *
 */
public class InterfaceSac extends Top
{


	//#region -------CODES CATEGORIE OBJET-----------
	
	/*
	 * Catégories :
	 * - Tous : tous les objets 
	 * - Divers : ingrédients ...
	 * - Combat : objet utilisable en combat (potions, antidotes ...)
	 * - Equipement : armes et armures
	 * - Rares : objets de quête.
	 */
	
	public static final int DIVERS = 0;
	public static final int EQUIPEMENT = 1;
	public static final int COMBAT = 2;
	public static final int RARE = 3;
	
	//#endregion

	//#region ------CODES ACTION---------------------
	
	public static final int UTILISER = 0;
	public static final int EQUIPER = 1;
	public static final int JETER = 2;
	public static final int DEPLACER = 3;
	public static final int DESEQUIPER = 4;
	
	//#endregion

	//#region -----CODES CONFIRMATION----------------
	
	private static final int  NON = 0;
	private static final int OUI = 1;
	
	//#endregion

	//#region ----------PROPRIETES-------------------
	private int curseurCategorie = 0; // curseur de catégorie
	private ArrayList<Integer> curseursAbsolus; //curseurs des objets de chaque catégories
	private ArrayList<Integer> curseursRelatifs;
	private int curseurAction;
	
	private Sac sac;
	private Equipe equipe;
	
	private boolean categorieValidee;
	private boolean confirmation = false; 
	private String message;

	private IObjet objetSelectionne;
	private ArrayList<Integer> listeActions;
	private int actionSelectionnee = -1;
	
	private int curseurPersonnage = 0;
	private Personnage personnageSelectionne; 
	
	private int curseurDeplacement = 0;
	private int curseurConfirmation = 0;
	private int boiteComptage = 1;
	
	//#endregion
	
	//#region ----OVERRIDE BASICGAMESTATE------------
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException 
	{
		equipe = Jeu.getEquipe();
		sac = equipe.getSac();
	
		categorieValidee = false;
		curseursAbsolus = new ArrayList<Integer>();
		curseursRelatifs = new ArrayList<Integer>();
		listeActions = new ArrayList<Integer>();
		
		for(int i = 0; i<5; i++)
			curseursAbsolus.add(0);
		for(int i = 0; i<5; i++)
			curseursRelatifs.add(0);
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		dessinerInterface(g);
		if(!categorieValidee)
		{
			//Affichage curseur catégorie
			g.drawImage(Jeu.getFleche(0), 37 + curseurCategorie*160, 55);
			if(getCategorie().getTaille() != 0)
			{
				afficherListeObjets(g);
			}
		}
		else
		{
			afficherListeObjets(g);
			afficherCurseurObjets(g);
			if(getCategorie().getTaille() != 0)
			{				
				if(objetSelectionne != null)
				{
					if(actionSelectionnee == -1)
					{
						afficherListeActions(g);
					}
					else
					{
						switch(actionSelectionnee)
						{
						case(JETER):
							if(getCategorie().getQuantite(curseursAbsolus.get(curseurCategorie)) > 1 && !confirmation)
							{
								afficherQuantiteAJeter(g);
							}
							else
							{
								afficherConfirmation(g);
							}
							break;
						case(UTILISER):
						case(EQUIPER):
							afficherEquipePourUtiliser(g);
							break;
						}
					}
				}
				else
				{
					afficherDecriptionObjet(g);
				}
			}
		}
		if(message != null && message != "")
		{
			afficherMessage(g);
		}
		
		super.render(container, game, g);
		
	}




	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		if(message == null || message == "")
		{
			if(!categorieValidee)
			{
				gererChoixCategorie(container.getInput());
				if(container.getInput().isKeyPressed(Input.KEY_ESCAPE))
				{
					game.enterState(Config.MENU);
				}
				container.getInput().clearKeyPressedRecord();
			}
			else if(actionSelectionnee == -1)
			{
				if(objetSelectionne == null)
				{
					gererChoixObjet(container.getInput());
				}
				else
				{
					gererChoixAction(container.getInput());
				}	
			}
			else
			{
				switch(actionSelectionnee)
				{
				case(JETER):
					if(getCategorie().getQuantite(curseursAbsolus.get(curseurCategorie)) > 1 && !confirmation)
					{
						gererQuantiteAJeter(container.getInput());
					}
					else
					{
						gererCurseurConfirmation(container.getInput());
					}
					break;
				case(EQUIPER):
					if(personnageSelectionne == null)
					{
						gererCurseurSelectionPersonnage(container.getInput());
					}
					else
					{
						if(personnageSelectionne.equipeStuff((Equipable)objetSelectionne))
						{
							message = personnageSelectionne + " est équipé de  "+objetSelectionne+".";
							actionSelectionnee = -1;
							objetSelectionne = null;
						}
						else
						{
							message = personnageSelectionne + " ne peut pas s'équiper de "+objetSelectionne+".";
						}
						personnageSelectionne = null;
					}
					break;
				case(DESEQUIPER):
					if(personnageSelectionne == null)
					{
						message="Déséquiper";
						actionSelectionnee = -1;
						Personnage perso = ((Equipable)objetSelectionne).quiEstEquipe(equipe);
						perso.desequipe((Equipable)objetSelectionne);
						objetSelectionne = null;
					}
					break;
				case(UTILISER):
					if(personnageSelectionne == null)
					{
						gererCurseurSelectionPersonnage(container.getInput());
					}
					else
					{
						if(personnageSelectionne.utiliserObjet(objetSelectionne))
						{
							sac.supprimer(objetSelectionne, 1);
							if(sac.getQuantite(objetSelectionne) == 0)
							{
								objetSelectionne = null;
							}
							actionSelectionnee = -1;
						}
						else
						{
							message = "Ca n'aura aucun effet!";

						}
						personnageSelectionne = null;
					}
					break;
				}
			}
		}
		else
		{
			if(container.getInput().isKeyPressed(Input.KEY_RETURN) || container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			{
				message = null;
			}
		}
		
		super.update(container, game, delta);
		
		container.getInput().clearKeyPressedRecord();
	}





	@Override
	public int getID() 
	{
		return Config.SAC;
	}

	//#endregion

	//#region --------AFFICHAGE----------------------
	
	/**
	 * Dessine l'interface de la vue sac. 
	 * @param g
	 */
	private void dessinerInterface(Graphics g)
	{
		g.setColor(Config.couleur1);
		g.fillRect(2, 2, 638, 478);
		
		g.setColor(Config.couleur2);
		g.drawRect(0, 0, 639, 479);
		g.drawRect(1, 1, 637, 477);
		g.drawLine(0, 40, 640, 40);
		g.drawLine(0, 41, 640, 41);
		
		g.drawLine(0, 390, 640, 390);
		g.drawLine(0, 389, 640, 389);
		
		for(int i = 0; i<4; i++)
		{
			g.drawRect(160*i,40, 160, 40);
			g.drawRect(160*i+1,41, 159, 40);
		}
		
		g.setColor(Color.white);
		g.drawString("SAC", 290,10);
		
		g.drawString("Divers", 50, 50);
		g.drawString("Equip.", 210, 50);
		g.drawString("Combat", 370, 50);
		g.drawString("Rares", 530, 50);
		
		g.drawString(Jeu.getEquipe().getArgent() + " PO" , 450, 10);
		
		
		
		//effet onglet (cache)
		g.setColor(Config.couleur1);
		g.fillRect(2 + curseurCategorie * 160, 80, 158, 2);
	}
	
	/**
	 * Affiche le curseur de sélection d'objet.
	 * 
	 * @param g
	 */
	private void afficherCurseurObjets(Graphics g) 
	{
		int index = curseursRelatifs.get(curseurCategorie);
		g.drawImage(Jeu.getFleche(0), 5, 93 + index * 25);
	}
	
	/**
	 * Affiche la descritption de l'objet en face du curseur.
	 * @param g
	 */
	private void afficherDecriptionObjet(Graphics g)
	{
		g.setColor(Color.white);
		g.drawString(Formatage.multiLignes(getCategorie().getObjet(curseursAbsolus.get(curseurCategorie)).getDescription(), 70), 5, 395);
	}
	
	
	private void afficherListeActions(Graphics g)
	{
		g.setColor(Color.white);
		

		for(int i = 0 ; i<listeActions.size(); i++)
		{
			g.drawString(libelleAction(listeActions.get(i)), 17, 395 + i * 20);
		}
		
		//affichage du curseur :
		g.drawImage(Jeu.getFleche(0), 5, 400 + curseurAction * 20);
	}
	
	/**
	 * 3
	 * 
	 * Affiche interface pour jeter un objet.
	 * (message + boîte compteur)
	 * 
	 * @param g
	 */
	private void afficherQuantiteAJeter(Graphics g)
	{
		g.setColor(Color.white);
		g.drawString(Formatage.multiLignes("Jeter combien de : " + getCategorie().getObjet(curseursAbsolus.get(curseurCategorie)).getNom() + " ?", 70), 5, 395);
	
		//affichage de la boite de comptage :
		{
			g.setColor(Config.couleur2);
			g.drawRect(280, 415, 80, 25);
			g.drawRect(279, 414, 82, 27);
			
			g.setColor(Color.white);
			g.drawString(String.valueOf(boiteComptage), 285, 420);
		}
	}
	
	/**
	 * 4
	 * 
	 * Affiche message de confirmation
	 * 
	 * @param g
	 */
	private void afficherConfirmation(Graphics g)
	{
		g.setColor(Color.white);
		g.drawString(Formatage.multiLignes( getCategorie().getObjet(curseursAbsolus.get(curseurCategorie)).getNom()  + " : " + " en jeter " + boiteComptage +" ?", 70), 5, 395);
		
		g.drawString("Non", 100, 415);
		g.drawString("Oui", 180, 415);
		
		//desinerCurseur
		g.drawImage(Jeu.getFleche(0), 87 + 80 * curseurConfirmation, 418);
		
	}
	
	/**
	 * Affiche la liste des objets de la catégorie sélectionnée.
	 * 
	 * @param g
	 * @throws SlickException
	 */
	private void afficherListeObjets(Graphics g) throws SlickException
	{
			int index = 0;
			for(int i = curseursAbsolus.get(curseurCategorie) - curseursRelatifs.get(curseurCategorie);
					i<getCategorie().getTaille() && index<12; i++)
			{
				
				g.drawImage(getCategorie().getObjet(i).getIcone(), 20, 90 + 25*index);
				g.setColor(Color.white);
				g.drawString(getCategorie().getObjet(i).getNom(), 45 ,90 + 25*index);
				g.drawString("x"+getCategorie().getQuantite(i), 590 ,90 + 25*index);
				index++;
			}
	}
	
	/**
	 * Affiche les membres de l'équipe pour pouvoir sélectionner une personnage
	 * et a lui appliquer un objet.
	 * @param g
	 */
	private void afficherEquipePourUtiliser(Graphics g)
	{
		int i = 0;
		for(Personnage p : equipe)
		{
			p.afficherGestionPerso(g,128, i*160, false);
			i++;
		}
		
		Image fleche = Jeu.getFleche(0);
		g.drawImage(fleche, 130, 80 + curseurPersonnage*160);
		
	}
	
	/**
	 * Affiche le message.
	 * @param g
	 */
	private void afficherMessage(Graphics g) 
	{
		g.setColor(Color.white);
		g.drawString(Formatage.multiLignes(message, 70), 5, 395);
	}
	
	
	//#endregion

	//#region ---------GESTION INPUT-----------------
	
	
	/**
	 * 0
	 * Gère le curseur du choix de la catégorie.
	 * 
	 * @param in
	 * 		Input
	 */
	private void gererChoixCategorie(Input in) 
	{

		if(in.isKeyPressed(Input.KEY_LEFT))
		{
			curseurCategorie = (curseurCategorie + 3) % 4;
		}
		
		
		else if(in.isKeyPressed(Input.KEY_RIGHT))
		{
			curseurCategorie = (curseurCategorie + 1) % 4;
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			categorieValidee = true;
		}
		
	}
	
	/**
	 * 1
	 * Gère le curseur pour le choix de l'objet
	 * @param in
	 */
	private void gererChoixObjet(Input in) 
	{
		if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			categorieValidee = false;
			in.clearKeyPressedRecord();
		}
		else if(in.isKeyPressed(Input.KEY_UP )  && getCategorie().getTaille() != 0)
		{
			int indexA = curseursAbsolus.get(curseurCategorie);
			curseursAbsolus.set(curseurCategorie, (indexA - 1 + getCategorie().getTaille()) % getCategorie().getTaille());
			indexA = curseursAbsolus.get(curseurCategorie);
			int indexR = curseursRelatifs.get(curseurCategorie);
			
			System.out.println(curseursAbsolus.get(curseurCategorie));
			
			if(indexA + 1 == getCategorie().getTaille())
			{
				curseursRelatifs.set(curseurCategorie, Math.min(getCategorie().getTaille()-1, 11));
			}
			else
			{
				curseursRelatifs.set(curseurCategorie, Math.max(0, indexR-1));
			}
		}
		else if(in.isKeyPressed(Input.KEY_DOWN)  && getCategorie().getTaille() != 0)
		{
			int indexA = curseursAbsolus.get(curseurCategorie);
			curseursAbsolus.set(curseurCategorie, (indexA + 1) % getCategorie().getTaille());
			indexA = curseursAbsolus.get(curseurCategorie);
			int indexR = curseursRelatifs.get(curseurCategorie);
			
			
			if(indexA == 0)
			{
				curseursRelatifs.set(curseurCategorie, 0);
			}
			else
			{
				curseursRelatifs.set(curseurCategorie, Math.min(indexR+1, 11));
			}
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			if(getCategorie().getTaille() > 0)
			{
				objetSelectionne = getCategorie().getObjet(curseursAbsolus.get(curseurCategorie));
				updateListeAtions();
			}
		}
	}
	
	/**
	 * 2
	 * Gère le choix de l'action pour l'objet sélectionné.
	 * 
	 * @param in
	 */
	private void gererChoixAction(Input in)
	{
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			curseurAction = (curseurAction + 1) % listeActions.size();
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			curseurAction = (curseurAction - 1 + listeActions.size()) % listeActions.size();
		}
		else if(in.isKeyPressed(Input.KEY_ENTER))
		{
			actionSelectionnee = listeActions.get(curseurAction);
		}
		else if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			objetSelectionne = null;
		}
	}
	
	/**
	 * 3
	 * 
	 * Gère quantité d'objet à jeter.
	 * 
	 * @param in
	 */
	private void gererQuantiteAJeter(Input in)
	{
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			boiteComptage = Math.max(1, boiteComptage - 1);
		}
		else if(in.isKeyPressed(Input.KEY_LEFT))
		{
			boiteComptage = Math.max(1, boiteComptage - 10);
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			boiteComptage = Math.min(getCategorie().getQuantite(curseursAbsolus.get(curseurCategorie)), boiteComptage + 1);
		}
		else if(in.isKeyPressed(Input.KEY_RIGHT))
		{
			boiteComptage = Math.min(getCategorie().getQuantite(curseursAbsolus.get(curseurCategorie)), boiteComptage + 10);
		}
		else if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			actionSelectionnee = -1;
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{

			confirmation = true;
		}
	}
	
	/**
	 * Met à jour la liste d'actions en fonction de l'objet sélectionné.
	 */
	private void updateListeAtions()
	{
		listeActions.clear();
		listeActions.add(UTILISER);		
		if(objetSelectionne instanceof Arme || objetSelectionne instanceof Armure)
		{
			Equipable equipable = (Equipable) objetSelectionne;
			if(equipe.estEquipeDe(equipable))
			{
				listeActions.set(0, DESEQUIPER);
			}
			else
			{
				listeActions.set(0, EQUIPER);
			}
		}
		listeActions.add(DEPLACER);
		if(!objetSelectionne.estRare())
		{
			listeActions.add(JETER);
		}
	}

	
	

	
	/**
	 * 4
	 * 
	 * Gére le curseur de confirmation pour la suppression d'objet.
	 * 
	 * @param in
	 */
	private void gererCurseurConfirmation(Input in)
	{
		if(in.isKeyPressed(Input.KEY_LEFT))
		{
			curseurConfirmation = NON;
		}
		else if(in.isKeyPressed(Input.KEY_RIGHT))
		{
			curseurConfirmation = OUI;
		}
		else if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			confirmation = false;
		}
		else if (in.isKeyPressed(Input.KEY_RETURN))
		{
			if(curseurConfirmation == OUI)
			{
				getCategorie().supprimer(objetSelectionne, boiteComptage);
				curseursAbsolus.set(curseurCategorie, Math.min(curseursAbsolus.get(curseurCategorie), getCategorie().getTaille() - 1));
				
				actionSelectionnee = -1;
				objetSelectionne = null;
				boiteComptage = 1;
				confirmation = false;
				
				//correction curseur Relatif :
				
				if(curseursRelatifs.get(curseurCategorie) > getCategorie().getTaille() - 1 && getCategorie().getTaille() != 0)
				{
					curseursRelatifs.set(curseurCategorie, getCategorie().getTaille() - 1);
				}
				
				if(getCategorie().getTaille() == 0)
				{
					curseursAbsolus.set(curseurCategorie, 0);
					curseursRelatifs.set(curseurCategorie, 0);
					categorieValidee = false;
				}
			}
			
			confirmation = false;
			curseurConfirmation = NON;
			
			if(getCategorie().getTaille() > 0)
			{
				if(getCategorie().getQuantite(curseursAbsolus.get(curseurCategorie)) == 1)
				{
					actionSelectionnee = -1;
				}
			}
		}
		
		in.clearKeyPressedRecord();
	}
	
	/**
	 * Permet de gérer le curseur de sélection d'un personnage.
	 */
	private void gererCurseurSelectionPersonnage(Input in)
	{
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			curseurPersonnage = (curseurPersonnage + 1) % equipe.nbPersonnages();
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			curseurPersonnage = (curseurPersonnage + equipe.nbPersonnages() - 1) % equipe.nbPersonnages();
		}
		else if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			actionSelectionnee = -1;
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			personnageSelectionne = equipe.get(curseurPersonnage);
		}
	}
	//#endregion

	//#region ---------AUTRES METHODES---------------
	
	/**
	 * Retourne la catégorie en cours.
	 * 
	 * @return
	 *  	la catégorie en cours.
	 */
	private Categorie getCategorie()
	{
		switch(curseurCategorie)
		{
		case(DIVERS):
			return sac.getDivers();
		case(EQUIPEMENT):
			return sac.getEquipements();
		case(COMBAT):
			return sac.getCombat();
		case(RARE):
			return sac.getRares();
		default:
			return null;
		}
	}
	
	//#endregion
	
	//#region --------METHODES STATIQUES-------------
	
	/**
	 * Retourne le libelle de l'action dont le code est passé en parmaètre.
	 * 
	 * @param code
	 * 		Code de l'action dont le libellé est à retourner.
	 * @return
	 * 		Le libelle de l'action dont le code est passé en parmaètre.
	 */
	private static String libelleAction(int code)
	{
		switch(code)
		{
		case(UTILISER):
			return "Utiliser";
		case(EQUIPER):
			return "Equiper";
		case(DEPLACER):
			return "Déplacer";
		case(JETER):
			return "Jeter";
		case(DESEQUIPER):
			return "Déséquiper";			
		default:
			return "";
		}
	}
	
	//#endregion
}
