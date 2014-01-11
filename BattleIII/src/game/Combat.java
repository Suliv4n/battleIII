package game;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import donnees.Formatage;

import animation.AnimationFactory;
import animation.CombatAnimation;
import animation.EffectsDisplayer;
import audio.GestionnaireMusique;

import personnage.*;
import sac.IObjet;
import skill.Skill;



/**
 * 
 * _-_-_-_-[State Combat]-_-_-_-_-_
 * 
 *    o           o 
 *    +-  -      -+
 *    ^           ^
 * [xxx  ]     [xxxxx]
 * OO          [xxxx ]
 * 
 * @author Darklev
 *
 */
public class Combat extends BasicGameState
{
	//#region --------ATTRIBUTS--------------
	private static Equipe equipe;
	private static EquipeEnnemis ennemis;
	
	
	//Personnage sélectionné pour une action
	private Personnage selectionPersonnage;
	private HashMap<Personnage, Integer>curseurAction;
	private int curseurCible = 0;
	
	//curseurs Skills :
	private HashMap<Personnage, Integer> curseursAbsolusSkills;
	private HashMap<Personnage, Integer> curseursRelatifsSkills;
	
	
	
	private boolean showSkill = false;
	private String message = null;
	
	private int curseur = 6; // position du curseur
	/*
	 *  0 1    6
	 *  2 3    7
	 *  4 5    8
	 */

	//liste des personnages ayant préparé leur action
	private ArrayList<Personnage> personnagesPrets;
	
	private ArrayList<IBattle> ordrePassage;
	
	
	/**
	 * Index indiquant le personnage ou l'ennemi dont l'action est à éxécuter.
	 */
	private int indexAction;
	
	private boolean debutTour = false;
	
	/**
	 * 0 = combat non terminé
	 * 1 = equipe joueur annihilée
	 * 2 = ennemis annihilés
	 */
	private int issueDuCombat = 0;
	private CombatAnimation animEnCours; 
	private EffectsDisplayer effectsDisplayerEnCours;
	private int compteurFrame;
	
	
	/**
	 * Dictionnaire des effets en cours.
	 */
	private HashMap<IBattle, ArrayList<String>> effetsCurrent;
	private static HashMap<IBattle, Point> coords;

	/**
	 * XP cumulé au cours du combat.
	 */
	private int cumulXP;
	private int cumulPO;
	private ArrayList<IObjet> drops;
	
	//#endregion
	
	//#region -----------GETTERS-------------
	
	//----------AIDE A L'IA------------------
	public Equipe getEquipe()
	{
		return equipe;
	}	
	
	public EquipeEnnemis getEnnemis()
	{
		return ennemis;
	}
	//#endregion

	//#region ------------INIT-----------------
	public static void init(Equipe joueur, EquipeEnnemis ennemis)
	{
		//initialisation des coordonnees
		coords = new HashMap<IBattle, Point>();
		coords.put(joueur.get(0), new Point(482,182));
		coords.put(joueur.get(1), new Point(502,232));
		coords.put(joueur.get(2), new Point(522,282));
		
		
		for(int i : ennemis.getEnnemis().keySet())
		{
			Image img = ennemis.getEnnemis().get(i).getImage();
			Point p = null;
			if(i <= 1)
			{
				p = new Point(100+100*(i % 2)  - img.getWidth()/2, 200 - img.getHeight()/2);
			}
			else if(i <= 3)
			{
				p = new Point(115+100*(i%2) - img.getWidth()/2, 250 - img.getHeight()/2);
			}
			else if(i <= 5)
			{
				p = new Point(130+100*(i%2) - img.getWidth()/2, 300 - img.getHeight()/2);
			}
			coords.put(ennemis.getEnnemis().get(i), p);
		}
		
		equipe = joueur;
		Combat.ennemis = ennemis;
		
	}
	
	//#endregion
	
	//#region --------OVERRIDE BASICGAMESTATE-------
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		ordrePassage = new ArrayList<IBattle>();
		personnagesPrets = new ArrayList<Personnage>();
		curseursAbsolusSkills = new HashMap<Personnage, Integer>();
		curseursRelatifsSkills = new HashMap<Personnage, Integer>();
		curseurAction = new HashMap<Personnage, Integer>();
		drops = new ArrayList<IObjet>();
		
		issueDuCombat = 0;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		//Dessiner le terrain
		g.drawImage(equipe.getMap().getTerrain(), 0,100);
		
		//Placer les ennemis
		dessinerEnnemis(g);
		
		//Place l'équipe du joueur
		dessinerEquipe(g);
		
		//dessiner bande haut (info ennemis)
		dessinerInfoEnnemis(g);
		
		dessinerInfoJoueur(g);
		if(message == null)
		{
			if(issueDuCombat == 0)
			{
				if(!debutTour)
				{
					
					//Dessiner curseur
					if(selectionPersonnage == null)
					{
						dessinerCurseur(g);
					}
					
					//dessiner bande bas (info joueur / action ...)
					if(selectionPersonnage != null)
					{
						if(!selectionPersonnage.estCibleSelectionnee() && selectionPersonnage.estActionPreparee())
						{
							dessinerCurseurCible(g);
						}
						else if(showSkill)
						{
							dessinerTableauSkills(g);
						}
						else
						{
							dessinerLesActions(g);
							dessinerCurseurAction(g);
						}
						
					}
				}
				else if(indexAction < ordrePassage.size())
				{
					executerAction(g);
				}
			}
			else if(issueDuCombat == 1)//Défaite
			{
				afficherTexte(g, "Game Over");
			}
			else if(issueDuCombat == 2)//Victoire
			{
				afficherTexte(g, "Victoire !");
			}
		}
		
		else
		{
			//affichage du message
			afficherTexte(g, message);
			compteurFrame++;
			if(compteurFrame == 60)
			{
				compteurFrame = 0;
				message = null;
			}
		}
		
	}



	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException			
	{
		
		if(message == null)
		{
			if(issueDuCombat == 0)
			{
				if(!debutTour)
				{
					if(!ennemis.prets())
					{
						preparerActionsEnnemis();
					}
					
					//gérer curseur
					if(selectionPersonnage == null)
					{
						gererCurseurPrincipal(container.getInput());
					}
					else
					{
						if(!selectionPersonnage.estCibleSelectionnee() && selectionPersonnage.estActionPreparee())
						{
							gererCurseurCible(convertInputToInt(container.getInput())); //2. Gestion sélection cible(s).
						}
						else
						{
							if(showSkill)
							{
								gererCurseurSkill(container.getInput()); //3. Selection Skill
							}
							else
							{
								gererCurseurAction(container.getInput()); //1. Gestion sélection de l'action.
							}
						}
					}
				}
			}
			else if(issueDuCombat == 1)
			{
				if(container.getInput().isKeyPressed(Input.KEY_RETURN))
				{
					container.getInput().clearKeyPressedRecord();
					game.enterState(Config.GAME_OVER);
				}
			}
			else if(issueDuCombat == 2)
			{
				GestionnaireMusique.jouerEnBoucle("victory");
				if(container.getInput().isKeyPressed(Input.KEY_RETURN))
				{
					container.getInput().clearKeyPressedRecord();
					CombatResult.init( cumulPO , cumulXP, drops);
					game.enterState(Config.COMBATRESULT);
				}
			}
		}
		else
		{
			gererInputMessage(container.getInput());
		}
		
		container.getInput().clearKeyPressedRecord();
	}
	
	@Override
	public int getID() 
	{
		return Config.COMBAT;
	}
	
	//#endregion
	
	//#region -------------AFFICHAGE--------------
	private void executerAction(Graphics g) throws SlickException 
	{
		ArrayList<Point> coordonneesCibles = new ArrayList<Point>(); //ok
		ArrayList<IBattle> cibles; //ok
		IBattle lanceur = ordrePassage.get(indexAction); //ok
		Point coordonneesLanceur = coords.get(lanceur); //ok

		Object action;
		action = lanceur.getAction();
		lanceur.selectionnerCibleSuivante(this);
		cibles = lanceur.getCibles();
		
		for(Object o : lanceur.getCibles())
		{
			coordonneesCibles.add(coords.get(o));
		}


		if(animEnCours == null)
		{
			if(action instanceof String)
			{
				message = lanceur.getNom() + " attaque!";
				if(((String) action).equalsIgnoreCase("Attaquer"))
				{
					animEnCours = AnimationFactory.creerAnimation("attaquer");
				}
			}
			else if(action instanceof Skill)
			{
				message = lanceur.getNom() + " utilise " + ((Skill)action).getNom() + " niv. " + ((Skill)action).getNiveau();
				animEnCours = AnimationFactory.creerAnimation(((Skill)action).getId());
			}
		}
		else
		{
			boolean b = false;
			if(effectsDisplayerEnCours == null)
			{
				b = animEnCours.playFrame(g, coordonneesLanceur, coordonneesCibles, lanceur, cibles, this);
				if(b)
				{

					effetsCurrent = getEffectsAction(lanceur, cibles, action);
					HashMap<Point, String> donneesED = new HashMap<Point,String>(); //
					
					for(IBattle ib : effetsCurrent.keySet())
					{
						//ArrayList => HashMap
						/*
						 * getEffetsAction()
						 *   Key             Value
						 *  IBattleCible   ArrayList : effet0 effet1 effet2
						 */
						String effetsFormat = ""; //effet1;effet2;
						for(String e : effetsCurrent.get(ib))
						{
							effetsFormat += e +";";
						}								
						donneesED.put(coords.get(ib), effetsFormat);
					}
					
					effectsDisplayerEnCours = new EffectsDisplayer(donneesED);

					//----------------------------------------------------------------------------------------------------------------------------------------------------------
				}
			}
			else
			{
				b = effectsDisplayerEnCours.afficherEffets(g);
				//à la fin de l'animation de l'effet de l'action en cours :
				if(b)
				{
					//application des effets de l'action en cours
					appliquerEffets(effetsCurrent);
					
					if(equipe.nombrePersonnagesVivants() == 0) //Perdu
					{
						issueDuCombat = 1;
					}
					else if(ennemis.nombreEnnemisVivants() == 0) //Gagne
					{
						issueDuCombat = 2;
					}
					
					animEnCours = null;
					effectsDisplayerEnCours = null;
					indexAction++;
				}

			}



		}
		//nettoyage des données du tour si tout le monde est passé. 
		if(indexAction >= ordrePassage.size())
		{
			for(Ennemi e : ennemis.getEnnemis().values())
			{
				e.annulerAction();
			}
			for(Personnage p : equipe)
			{
				p.annulerAction();
			}
			ordrePassage.clear();
			personnagesPrets.clear();
			indexAction = 0;
			debutTour = false;
			animEnCours = null;
		}


	}

	//-----------------------------------------------------------------------
	private void barreHaut(Graphics g)
	{
		g.setColor(Config.couleur1);
		g.fillRect(0, 0, 640, 100);		
		g.setColor(Config.couleur2);
		g.drawRect(0,0,640,99);
		g.drawRect(1,1,638,97);
	}
	private void barreBas(Graphics g)
	{
		g.setColor(Config.couleur1);
		g.fillRect(0, 380, 640, 65100);		
		g.setColor(Config.couleur2);
		g.drawRect(0,380,640,99);
		g.drawRect(1,381,638,97);
	}
	//-----------------------------------------------------------------------
	
	/**
	 * Dessine les ennemis sur leur place respetive.
	 * 
	 * @param g
	 */
	private void dessinerEnnemis(Graphics g)
	{
		for(int place : ennemis.getEnnemis().keySet())
		{
			if(ennemis.getEnnemis().get(place).estVivant())
			{
				Image img = ennemis.getEnnemis().get(place).getImage();
				if(place <= 1)
				{
					g.drawImage(img , 100+100*(place%2)  - img.getWidth()/2, 200 - img.getHeight()/2);
				}
				else if(place <= 3)
				{
					g.drawImage(img, 115+100*(place%2) - img.getWidth()/2, 250 - img.getHeight()/2);
				}
				else if(place <= 5)
				{
					g.drawImage(img, 130+100*(place%2) - img.getWidth()/2, 300 - img.getHeight()/2);
				}
			}
		}
	}
	
	/**
	 * Dessine les personnages du joueur sur leur place respetive.
	 * 
	 * @param g
	 */
	private void dessinerEquipe(Graphics g)
	{
		int i = 0;
		for(Personnage p : equipe)
		{
			if(p.estVivant())
			{
				if(issueDuCombat == 0)
				{
					p.getImagePositionAttaque().draw(450+i*20 , 150+i*50, 2f); //normal
				}
				else
				{
					p.getAnimation(1).draw(450+i*20 , 150+i*50, 64, 64); //TODO : Animation victory
				}
			}
			else
			{
				p.getImageKO().draw(450+i*20 , 150+i*50, 2f);    //KO
			}
			
			if(p.estActionPreparee() && p.estCibleSelectionnee())
			{
				g.setColor(Color.green);
				g.fillRect(500+i*20, 150+i*50, 20, 20);
			}
			i++;
		}
	}
	
	/**
	 * Dessine le curseur selon sa position.
	 * 
	 * @param g
	 */
	private void dessinerCurseur(Graphics g) 
	{
		if(curseur>=6)
		{
			g.drawImage(Jeu.getFleche(0),430+(curseur-6) *20,175+(curseur-6) *50);
		}
		else if(curseur%2==0)
		{
			g.drawImage(Jeu.getFleche(0),50+5*curseur,200+50*((int)(curseur/2)));
		}
		else 
		{
			g.drawImage(Jeu.getFleche(0),150+5*curseur,200+50*((int)(curseur/2)));
		}
	}
	
	/**
	 * Affiche les infos du joueur en bas de l'écran
	 * 
	 * @param g
	 */
	private void dessinerInfoJoueur(Graphics g)
	{

		barreBas(g);
		
		for(int i = 0; i<equipe.nbPersonnages();i++)
		{
			g.setColor(Color.white);
			g.drawString(equipe.get(i).getNom(),10,385+i*30);
			
			
			//pv
			g.setColor(Config.couleur2);
			g.drawRect(99, 389+i*30, 150, 10);
			
			
			g.setColor(Color.black);
			g.fillRect(100, 390+i*30, 149, 9);
			
			g.setColor(new Color(0,100,0));
			g.fillRect(100, 390+i*30, (float) (equipe.get(i).getPV()/((double)equipe.get(i).getPVMaximum()))*149, 9);
			g.setColor(new Color(255,255,255));
			g.drawString((int)equipe.get(i).getPV()+"/"+equipe.get(i).getPVMaximum(),250,385+i*30);
			
			//Energie
			g.setColor(Config.couleur2);
			g.drawRect(369, 389+i*30, 150, 10);
			
			
			g.setColor(Color.black);
			g.fillRect(370, 390+i*30, 149, 9);
			
			g.setColor(equipe.get(i).couleurEnergie());
			g.fillRect(370, 390+i*30, (float) (equipe.get(i).getEnergie()/((double)equipe.get(i).getEnergieMax()))*149, 9);
			g.setColor(new Color(255,255,255));
			g.drawString((int)equipe.get(i).getEnergie()+"/"+equipe.get(i).getEnergieMax(),520,385+i*30);
		}
		
		g.setColor(Config.couleur2);
		if(curseur >= 6)
		{
			g.drawRect(5, 385+(curseur-6)*30, 630, 20);
		}
	}
	
	/**
	 * Affiche les infos du joueur en bas de l'écran
	 * 
	 * @param g
	 */
	private void dessinerInfoEnnemis(Graphics g) 
	{
		barreHaut(g);
		Ennemi ennemi = null;
		if(selectionPersonnage != null)
		{
			if(curseurCible < 6 && selectionPersonnage.getAction() != null)
			{
				ennemi = ennemis.getEnnemis().get(curseurAction);
			}
		}
		else
		{
			if(curseur < 6)
			{
				ennemi = ennemis.getEnnemis().get(curseurAction);
			}
		}
		
		if(ennemi != null)
		{
			if(equipe.estAnalyse(ennemi.getID()))
			{
				g.setColor(Color.white);
				g.drawString(ennemi.getNom(), 3, 2);
				
				g.drawString("Att phy : " + ennemi.getAttaquePhysique(), 3, 22);
				g.drawString("Att mag : " + ennemi.getAttaqueMagique(), 3, 42);
				g.drawString("Dexterité : " + ennemi.getDexterite(), 3, 62);
				
				g.drawString("Déf phy : " + ennemi.getDefensePhysique(), 320, 22);
				g.drawString("Déf mag : " + ennemi.getDefenseMagique(), 320, 42);
				g.drawString("Soin : " + ennemi.getDexterite(), 320, 62);

				g.setColor(Config.couleur2);
				g.drawRect(320, 3, 100, 15);
				g.setColor(Color.black);
				g.fillRect(321, 4, 99, 14);
				g.setColor(new Color(0,100,0));
				g.fillRect(321, 4, ennemi.getPV()/ennemi.getPVMaximum() * 99, 14);
				g.setColor(Color.white);
				g.drawString(ennemi.getPV() + "/" + ennemi.getPVMaximum(), 430, 3);
				
			}
			else
			{
				g.setColor(Color.white);
				g.drawString("????????", 3, 2);
			}
		}
	}
	
	private void dessinerLesActions(Graphics g)
	{ 
		barreBas(g);
		g.setColor(Color.white);
		g.drawString("Attaquer",30,385);
		g.drawString("Compétences",30,405);
		g.drawString("Objets",30,425);
		g.drawString("Fuir",30,445);
	}
	
	
	
	


	/**
	 * Dessine le curseur d'action.
	 * 
	 * @param g
	 */
	private void dessinerCurseurAction(Graphics g)
	{
		if(!curseurAction.containsKey(selectionPersonnage))
		{
			curseurAction.put(selectionPersonnage, 0);
		}
		g.drawImage(Jeu.getFleche(0), 15, 390 + curseurAction.get(selectionPersonnage) * 20);
	}
	
	
	
	private void dessinerCurseurCible(Graphics g)
	{
		Object action = selectionPersonnage.getAction();

		if(getCibleAction(action) == Skill.ENNEMI || getCibleAction(action) == Skill.ALLIE)
		{
			if(curseurCible>=6)
			{
				g.drawImage(Jeu.getFleche(0),430+(curseurCible-6) * 20,175+(curseurCible-6) * 50);
			}
			else if(curseurCible%2==0)
			{
				g.drawImage(Jeu.getFleche(0),50+5*curseurCible,200+50*((int)(curseurCible/2)));
			}
			else 
			{
				g.drawImage(Jeu.getFleche(0),150+5*curseurCible,200+50*((int)(curseurCible/2)));
			}
			

		}
		else if(getCibleAction(action) == Skill.TOUS_ENNEMIS)
		{
			for(int e : ennemis.getEnnemis().keySet())
			{
				if(ennemis.getEnnemis().get(e).estVivant())
				{
					if(e>=6)
					{
						g.drawImage(Jeu.getFleche(0),430+(e-6) * 20,175+(e-6) * 50);
					}
					else if(e%2==0)
					{
						g.drawImage(Jeu.getFleche(0),50+5*e,200+50*((int)(e/2)));
					}
					else 
					{
						g.drawImage(Jeu.getFleche(0),150+5*e,200+50*((int)(e/2)));
					}
				}
			}
		}
		else if(getCibleAction(action) == Skill.TOUS_ALLIES)
		{
			for(int i = 0; i<equipe.nbPersonnages() ;i++)
			{
				if(equipe.get(i).estVivant())
				{
					g.drawImage(Jeu.getFleche(0),430+(i) *20,175+(i) *50);
				}
			}
		}
	}
	
	private void dessinerTableauSkills(Graphics g)
	{
		//cadre :
		g.setColor(Config.couleur2);
		g.drawRect(8, 108, 353, 103);
		g.drawRect(9, 109, 351, 101);		
		
		g.setColor(Config.couleur1);
		g.fillRect(10, 110, 350, 100);
		
		

		for(int i = curseursAbsolusSkills.get(selectionPersonnage) - curseursRelatifsSkills.get(selectionPersonnage);
				i < selectionPersonnage.getSkills().size() && i < 6;
				i++)
		{
			
			
			
			Skill s = selectionPersonnage.getSkills().get(i);
			if(s.libErreurLancer(selectionPersonnage) == null)
			{
				g.setColor(Color.white);
			}
			else
			{
				g.setColor(Color.gray);
			}
			
			g.drawString(s.getNom(), 45, 112 + 20*i);
			g.drawString(" Niv." + s.getNiveau() + "/" + s.getNiveauMax() , 220, 112 + 20*i);
			g.drawString(String.valueOf(s.getConsommation()) , 300, 112 + 20*i);
		}
		
		if(!selectionPersonnage.getSkills().isEmpty())
		{
			Skill s = selectionPersonnage.getSkills().get(curseursAbsolusSkills.get(selectionPersonnage));
			
			g.drawString("Puissance : " + s.getPuissance(), 5,5);
			g.drawString("Consommation : " + s.getConsommation(), 5,25);
			g.drawString("Description : \n" + Formatage.multiLignes(s.getDescription(),150), 5,45);
		}
		
		//affichage du curseur :
		g.drawImage(Jeu.getFleche(0), 12, 115 + 20 * curseursRelatifsSkills.get(selectionPersonnage));
	}
	
	public void afficherTexte(Graphics g, String texte)
	{
		g.setColor(Config.couleur1);
		g.fillRect(0, 120, 640, 25);
		g.setColor(Config.couleur2);
		g.drawLine(0, 120, 640, 120);
		g.drawLine(0, 145, 640, 145);
		
		g.setColor(Color.white);
		
		g.drawString(texte, 640/2 - texte.length()*8, 123);
	}
	
	//#endregion
	
	//#region ------------GESTION INPUT--------
	

	
	/**
	 * Déplace logiquement le curseur en fonction de l'input. (curseur principal niveau 0)
	 * 
	 * @param input
	 * 		Input enregistré. 
	 */
	private void gererCurseurPrincipal(Input input) 
	{
		if(input.isKeyPressed(Input.KEY_DOWN))
		{
			if(curseur >= 6)
			{
				curseur = (curseur + 1)%3+6;
			}
			else
			{
				curseur = (curseur + 2)%6;
						
			}
		}
		else if(input.isKeyPressed(Input.KEY_UP))
		{
			if(curseur >= 6)
			{
				curseur = ((curseur - 1)+3)%3+6;
			}
			else
			{
				curseur = (curseur + 4)%6;
			}
		}
		
		else if(input.isKeyPressed(Input.KEY_RIGHT))
		{
			if(curseur == 0 || curseur == 2 || curseur == 4)
			{
				curseur++;
			}
			else if(curseur == 1)
			{
				curseur = 6;
			}
			else if(curseur == 3)
			{
				curseur = 7;
			}
			else if(curseur == 5)
			{
				curseur = 8;
			}
		}
		
		else if(input.isKeyPressed(Input.KEY_LEFT))
		{
			if(curseur == 1 || curseur == 3 || curseur == 5)
			{
				curseur--;
			}
			
			else if(curseur == 6)
			{
				curseur = 1;
			}
			else if(curseur == 7)
			{
				curseur = 3;
			}
			else if(curseur == 8)
			{
				curseur = 5;
			}
		}
		else if(input.isKeyPressed(Input.KEY_RETURN) && (curseur == 6 || curseur == 7 || curseur == 8))
		{
			input.clearKeyPressedRecord();
			if(!equipe.get(curseur-6).estActionPreparee() && equipe.get(curseur-6).estVivant())
			{
				selectionPersonnage = equipe.get(curseur-6);
			}
		}
		else if(input.isKeyPressed(Input.KEY_ESCAPE))
		{
			if(personnagesPrets.size()>0)
			{	
				Personnage p = personnagesPrets.remove(personnagesPrets.size() - 1);

				p.annulerAction();
				selectionPersonnage = p;
				curseur = equipe.indexOf(p) + 6;
			}
		}
	}
	
	private void gererCurseurAction(Input input) 
	{
		if(!curseurAction.containsKey(selectionPersonnage))
		{
			curseurAction.put(selectionPersonnage, 0);
		}
		
		if(input.isKeyPressed(Input.KEY_ESCAPE))
		{
			selectionPersonnage = null;
		}
		else if(input.isKeyPressed(Input.KEY_DOWN))
		{
			curseurAction.put(selectionPersonnage, (curseurAction.get(selectionPersonnage) + 1) % 4);
		}
		else if(input.isKeyPressed(Input.KEY_UP))
		{
			curseurAction.put(selectionPersonnage, (curseurAction.get(selectionPersonnage) + 3) % 4);
		}
		
		else if(input.isKeyPressed(Input.KEY_RETURN))
		{
			if(curseurAction.get(selectionPersonnage) == 0)
			{
				selectionPersonnage.setAction("Attaquer");	
			}
			else if(curseurAction.get(selectionPersonnage) == 1)
			{
				if(!curseursAbsolusSkills.containsKey(selectionPersonnage))
				{
					curseursAbsolusSkills.put(selectionPersonnage, 0);
					curseursRelatifsSkills.put(selectionPersonnage, 0);
				}
				
				showSkill = true;
			}
			

		}
		input.clearKeyPressedRecord();
	}
	
	
	/**
	 * @param in
	 * 	Input
	 * 
	 * @return
	 * 		Retourne la clé de la touche pressé pour les touches fleches, return et escape.
	 * 
	 */
	private int convertInputToInt(Input in)
	{
		if(in.isKeyPressed(Input.KEY_LEFT))
		{
			return Input.KEY_LEFT;
		}
		else if(in.isKeyPressed(Input.KEY_RIGHT))
		{
			return Input.KEY_RIGHT;
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			return Input.KEY_UP;
		}
		else if(in.isKeyPressed(Input.KEY_DOWN))
		{
			return Input.KEY_DOWN;
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			return Input.KEY_RETURN;
		}
		else if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			return Input.KEY_RETURN;
		}
		
		
		return -1;
	}
	
	/**
	 * Gère le curseur de sélection d'une cible / de cibles
	 * 
	 * @param input
	 * 		Input à gérer.
	 */
	private void gererCurseurCible(int input)
	{
		Object action = selectionPersonnage.getAction();
		int typeCible = getCibleAction(action);
		

		
		if(input == Input.KEY_ESCAPE)
		{
			selectionPersonnage.annulerAction();
		}
		
		if(typeCible == Skill.ENNEMI)
		{
			if(input == Input.KEY_DOWN)
			{
				curseurCible = (curseurCible + 2)%6;
			}
			else if(input == Input.KEY_UP)
			{
				curseurCible = (curseurCible + 4)%6;
			}
			
			else if(input == Input.KEY_RIGHT)
			{
				if(curseurCible == 0 || curseurCible == 2 || curseurCible == 4)
				{
					curseurCible++;
				}
				else if(curseurCible == 1)
				{
					curseurCible = 0;
				}
				else if(curseurCible == 3)
				{
					curseurCible = 2;
				}
				else if(curseurCible == 5)
				{
					curseurCible = 4;
				}
			}
			
			else if(input == Input.KEY_LEFT)
			{
				if(curseurCible == 1 || curseurCible == 3 || curseurCible == 5)
				{
					curseurCible--;
				}
				
				else if(curseurCible == 0)
				{
					curseurCible = 1;
				}
				else if(curseurCible == 2)
				{
					curseurCible = 3;
				}
				else if(curseurCible == 4)
				{
					curseurCible = 5;
				}
			}
			else if(input == Input.KEY_RETURN)
			{
				selectionPersonnage.ajouterCible(ennemis.getEnnemis().get(curseurCible));
				personnagesPrets.add(selectionPersonnage);
				selectionPersonnage = null;
				curseur = (curseur + 1)%3+6;
			}
			
			/*
			 * Si la cible n'est pas valable (inexistante ou non vivante) : recommencer.
			 */
			if(!ennemis.estCibleValable(curseurCible))
			{
				gererCurseurCible(input);
			}
			
		}
		
		else if(typeCible == Skill.ALLIE)
		{
			if(input == Input.KEY_UP)
			{
				curseurCible = ((curseurCible - 1)+3)%3+6;
			}
			else if(input == Input.KEY_DOWN)
			{
				curseurCible = (curseurCible + 1)%3+6;
			}
			else if(input == Input.KEY_RETURN)
			{
				selectionPersonnage.ajouterCible(equipe.get(curseurCible - 6));
				if(equipe.get(curseurCible - 6) == null)
				{
					throw new NullPointerException();
				}
				personnagesPrets.add(selectionPersonnage);
				selectionPersonnage = null;
				curseur = (curseur + 1)%3+6;
			}
		}
		
		else if(typeCible == Skill.TOUS_ENNEMIS)
		{
			if(input == Input.KEY_RETURN)
			{
				for(int e : ennemis.getEnnemis().keySet())
				{
					if(ennemis.getEnnemis().get(e).estVivant())
					{
						selectionPersonnage.ajouterCible(ennemis.getEnnemis().get(e));
					}
				}
				
				personnagesPrets.add(selectionPersonnage);
				selectionPersonnage = null;
				curseur = (curseur + 1)%3+6;
			}
		}
		else if(typeCible == Skill.TOUS_ALLIES)
		{
			if(input == Input.KEY_RETURN)
			{
				for(int i=0;i<equipe.nbPersonnages();i++)
				{
					if(equipe.get(i).estVivant())
					{
						selectionPersonnage.ajouterCible(equipe.get(i));
					}				
				}
				personnagesPrets.add(selectionPersonnage);
				selectionPersonnage = null;
				curseur = (curseur + 1)%3+6;
			}
		}
		
		//-----------------
		if(personnagesPrets.size() == equipe.nombrePersonnagesVivants())
		{
			trier();
			debutTour = true;
		}
	}
	
	
	private void gererCurseurSkill(Input in)
	{
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			if(curseursAbsolusSkills.get(selectionPersonnage) == selectionPersonnage.getSkills().size() - 1)
			{
				curseursAbsolusSkills.put(selectionPersonnage, 0);
				curseursRelatifsSkills.put(selectionPersonnage, 0);
			}
			else
			{
				curseursAbsolusSkills.put(selectionPersonnage, curseursAbsolusSkills.get(selectionPersonnage) + 1);
				if(curseursRelatifsSkills.get(selectionPersonnage) < 5)
				{
					curseursRelatifsSkills.put(selectionPersonnage, curseursRelatifsSkills.get(selectionPersonnage) + 1);
				}
			}
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			if(curseursAbsolusSkills.get(selectionPersonnage) == 0)
			{
				curseursAbsolusSkills.put(selectionPersonnage, selectionPersonnage.getSkills().size() - 1);
				curseursRelatifsSkills.put(selectionPersonnage, Math.min(4, selectionPersonnage.getSkills().size() - 1));
			}
			else
			{
				curseursAbsolusSkills.put(selectionPersonnage, curseursAbsolusSkills.get(selectionPersonnage) - 1);
				if(curseursRelatifsSkills.get(selectionPersonnage) > 0)
				{
					curseursRelatifsSkills.put(selectionPersonnage, curseursRelatifsSkills.get(selectionPersonnage) - 1);
				}
			}
		}
		else if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			showSkill = false;
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			if(!selectionPersonnage.getSkills().isEmpty())
			{
				if(selectionPersonnage.getSkills().get(curseursAbsolusSkills.get(selectionPersonnage)).libErreurLancer(selectionPersonnage) == null)
				{
					showSkill = false;
					selectionPersonnage.setAction(selectionPersonnage.getSkills().get(curseursAbsolusSkills.get(selectionPersonnage)));
					//si cible = allie, alors ajustement de curseurCible
					if(selectionPersonnage.getSkills().get(curseursAbsolusSkills.get(selectionPersonnage)).getCible() == Skill.ALLIE)
					{
						curseurCible = curseur;
					}
					//sinon si ennemi => curseurCible = premier ennemi vivant.
					else if(selectionPersonnage.getSkills().get(curseursAbsolusSkills.get(selectionPersonnage)).getCible() == Skill.ENNEMI)
					{
						for(int i : ennemis.getEnnemis().keySet())
						{
							if(ennemis.getEnnemis().get(i).estVivant())
							{
								curseurCible = i;
								break;
							}
						}
					}
				}
				else
				{
					message = selectionPersonnage.getSkills().get(curseursAbsolusSkills.get(selectionPersonnage)).libErreurLancer(selectionPersonnage);
				}
			}
		}

	}
	
	private void gererInputMessage(Input in)
	{
		if(in.isKeyPressed(Input.KEY_RETURN))
		{
			message = null;
			in.clearKeyPressedRecord();
		}
	}
	//#endregion
	
	//#region ----------AUTRES METHODES-------------
	private int calculerDegatsAttaquer(Object lanceur, Object cible) 
	{
		int defense = 0;
		double marge = Math.random() * (1.1 - 0.9 ) + 0.9;
		
		if(cible instanceof Personnage)
		{
			defense = ((Personnage)cible).getDefensePhysique();
		}
		else
		{
			defense = ((Ennemi)cible).getDefensePhysique();
		}

		if(lanceur instanceof Personnage)
		{
			int degatArme = ((Personnage)lanceur).getArmePrincipale() != null ? ((Personnage)lanceur).getArmePrincipale().getDegatPhysique() : 0;
			return Math.max( ((int) (((((Personnage)lanceur).getAttaquePhysique() + ((Personnage)lanceur).getAttaquePhysique()*0.1*degatArme) - defense) * marge)), 1);
		}
		else
		{
			return Math.max( (int) ((((Ennemi)lanceur).getAttaquePhysique() - defense) * marge), 1);
		}
	}
	
	private void preparerActionsEnnemis() 
	{
		for(Ennemi e : ennemis.getEnnemis().values())
		{
			if(e.estVivant())
			{
				e.doIA(this);
			}
		}
	}
	
	/**
	 * Renvoie une ArrayList d'ennemis et de personnages proganistes triés
	 * selon leur dextérité.
	 * <- DEXT+                DEXT- ->
	 * <- PRIO+                PRIO- ->
	 */
	private void trier() 
	{
		ArrayList<IBattle> priorites = new ArrayList<IBattle>();
		
		for(Personnage p1 : equipe)
		{
			int max = -1;
			Personnage persoMax = null;
			for(Personnage p2 : equipe)
			{
				if(p2.getStatMax("dexterite") > max && !priorites.contains(p2))
				{
					max = p2.getStatMax("dexterite");
					persoMax = p2;
				}
			}
			
			priorites.add(persoMax);
		}
		
		for(Ennemi e : ennemis.getEnnemis().values())
		{
			int i = 0;
			int dext = 0;
			
			boolean ajout = false;
			
			do
			{
				if(priorites.get(i) instanceof Personnage)
				{
					dext = ((Personnage)priorites.get(i)).getStatMax("dexterite");
				}
				else
				{
					dext = ((Ennemi)priorites.get(i)).getDexterite();
				}
				
				if(e.getDexterite() >= dext)
				{
					priorites.add(i,e);
					ajout = true;
				}
				
				
				i++;
				
			}while(i<priorites.size() && !ajout);
			
			if(!ajout)
			{
				priorites.add(e);
			}
		}
		
		//supprimer les ennemis et personnages non vivants :
		ArrayList<Object> suppressions = new ArrayList<Object>();
		for(Object e : priorites)
		{
			if(e instanceof Ennemi)
			{
				if(!((Ennemi) e).estVivant())
				{
					suppressions.add(e);
					((Ennemi) e).annulerAction();
				}
			}
			else
			{
				if(!((Personnage) e).estVivant())
				{
					suppressions.add(e);
					personnagesPrets.remove(e);
					((Personnage) e).annulerAction();
				}
			}
		}
		
		for(Object e : suppressions)
		{
			priorites.remove(e);
		}
		
		ordrePassage = priorites;
	}
	
	/**
	 * Calcul les effets d'une action lancer par le lanceur sur les cibles.
	 * 
	 * Retourne un dictionnaire enregistrant les résultats des calculs pour les effets d'une action (skill, objet ...)
	 * 
	 * Exemple : 
	 * <table>
	 * <tr>
	 * <td>IBattle (cibles)</td>
	 * <td>Effets</td>
	 * </tr>
	 * <tr>
	 * <td>Ennemi 1<td><td>129 - brulure</td>
	 * </tr>
	 * </tr>
	 * <tr>
	 * <td>Ennemi 2<td><td>132</td>
	 * </tr>
	 * </table>

	 * 
	 * @param lanceur
	 * 		Lanceur de l'action.
	 * @param cibles
	 * 		Cibles de l'action.
	 * @param action
	 * 		L'action dont les effets sont à calculés.
	 * @return
	 * 		un dictionnaire enregistrant les résultats des calculs pour les effets d'une action (skill, objet ...).
	 */
	private HashMap<IBattle, ArrayList<String>> getEffectsAction(IBattle lanceur, ArrayList<IBattle> cibles, Object action)
	{
		HashMap<IBattle,ArrayList<String>> effets = new HashMap<IBattle,ArrayList<String>>();
		
		if(action instanceof String)
		{
			ArrayList<String> listeEffets = new ArrayList<String>();
			if(((String) action).equalsIgnoreCase("attaquer"))
			{
				listeEffets.add(String.valueOf(calculerDegatsAttaquer(lanceur, cibles.get(0))));
				effets.put(cibles.get(0), listeEffets);
			}
			else if(((String) action).equalsIgnoreCase("fuir"))
			{
				
			}
		}
		else if (action instanceof Skill)
		{
			
			
			Skill skill = (Skill)action;

			for(IBattle ib : cibles)
			{
				if(ib.estVivant())
				{
					double marge = Math.random() * (1.1 - 0.9 ) + 0.9;
					
					ArrayList<String> listeEffets = new ArrayList<String>();
					int degatArmePhy = 0;
					int degatArmeMag = 0;
					if(lanceur instanceof Personnage)
					{
						if(((Personnage) lanceur).getArmePrincipale() != null)
						{
							degatArmePhy = ((Personnage) lanceur).getArmePrincipale().getDegatPhysique();
							degatArmeMag = ((Personnage) lanceur).getArmePrincipale().getDegatMagique();
						}
					}
					
					if(skill.getType() == Skill.PHYSIQUE && skill.getPuissance() != 0)
					{
						int deg = (int) ((lanceur.getAttaquePhysique() + skill.getPuissance() + lanceur.getAttaquePhysique() * 0.1 * degatArmePhy - ib.getDefensePhysique()) * marge);
						listeEffets.add(String.valueOf(deg));
						effets.put(ib, listeEffets);
					}
					else if(skill.getType() == Skill.MAGIQUE && skill.getPuissance() != 0)
					{
						int deg = (int) ((lanceur.getAttaqueMagique() + skill.getPuissance() + lanceur.getAttaqueMagique() * 0.1 * degatArmeMag - ib.getDefenseMagique()) * marge);
						listeEffets.add(String.valueOf(deg));
						effets.put(ib, listeEffets);
					}
					else if(skill.getType() == Skill.SOIN && skill.getPuissance() != 0)
					{
						int deg = (int) ((lanceur.getSoin() + skill.getPuissance() + lanceur.getAttaqueMagique() * 0.1 * degatArmeMag) * marge);
						listeEffets.add(String.valueOf(-deg));
						effets.put(ib, listeEffets);
					}
				}
			}
		}
		return effets;
	}
	
	
	private void appliquerEffets(HashMap<IBattle, ArrayList<String>> effets)
	{
		for(IBattle cible : effets.keySet())
		{
			for(String effet : effets.get(cible))
			{
				if(effet.matches("-?[0-9]+")) //nombre entier
				{
					int deg = Integer.parseInt(effet);
					cible.updatePV(-deg);
				}
				else if(effet.equalsIgnoreCase("analyse"))
				{
					if(cible instanceof Ennemi)
					{
						equipe.analyser(((Ennemi)cible).getID());
					}
				}
			}
			
			//si la cible est morte => suppression de la cble de l'ordrePassage(n'attaquera pas ce tour si elle n'a pas déjà attaquée)
			//ajout de l'xp de la cible dans cumulXP et po dans cumulPO si c'est un ennemi qui est mort.
			if(!cible.estVivant())
			{
				ordrePassage.remove(cible);
				cible.annulerAction();
				if(cible instanceof Ennemi)
				{
					cumulXP += ((Ennemi)cible).getXP();
				}
			}
		}
	}
	
	/**
	 * Retourne le code de ciblage d'une action.
	 * 
	 * @return
	 *		Le code de ciblage d'une action
	 */
	private int getCibleAction(Object action)
	{
		if(action instanceof String)
		{
			if(((String)action).equalsIgnoreCase("attaquer"))
			{
				return Skill.ENNEMI;
			}
		}
		
		else if(action instanceof Skill)
		{
			return ((Skill)action).getCible();
		}
		
		return Skill.ENNEMI;
	}
	
	//#endregion
	
}