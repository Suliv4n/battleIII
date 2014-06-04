/*
				  ___    _  _____  _____  _     ___          ___  ___  ___                                 
				 | _ )  /_\|_   _||_   _|| |   | __|        |_ _||_ _||_ _|                                
				 | _ \ / _ \ | |    | |  | |__ | _|          | |  | |  | |                                 
				 |______/_\__|____  |___ |____|____| _____  ____|___________  _       _    _  _  ___       
				 |_   _|| || || __| | __|/_\  | \| ||_   _|/_\  / __|\ \ / / | |     /_\  | \| ||   \      
				   | |  | __ || _|  | _|/ _ \ | .` |  | | / _ \ \__ \ \ V /  | |__  / _ \ | .` || |) |     
				   |_|  |____||_____|__/__ \____|\____|____/ \_\|___/____|___|____|/_____\|____|_____  ___ 
				  / _ \ | __| |_   _|| || || __|  / __|/ _ \ | \| | / __|| __|| _ \|_   _|| __|| | | || _ \
				 | (_) || _|    | |  | __ || _|  | (__| (_) || .` || (__ | _| |  _/  | |  | _| | |_| ||   /
				  \___/ |_|     |_|  |_||_||___|  \___|\___/ |_|\_| \___||___||_|    |_|  |___| \___/ |_|_\

 */

package game;

import java.util.ArrayList;
import java.util.HashMap;

import game.saisie.ActionSaisie;
import game.saisie.Saisie;
import map.Coffre;
import map.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import animation.RessourceAnimationLoader;
import audio.GestionnaireMusique;

import personnage.*;
import sac.objet.stuff.Arme;

import donnees.GenerateurDonnees;



public class Jeu extends StateBasedGame 
{
	//#region --------PROPRIETES-------------
	
	 /**//**//**//**//**//**//**//**//**//**//**/
	/**/private static AppGameContainer app;/**/
   /**//**//**//**//**//**//**//**//**//**//**/
	
	//Les GameStates :
	private Exploration exploration;
	private Menu menu;
	private GestionEquipe gestionEquipe;
	private Configuration configuration;
	private Saisie saisie;
	private GameOver gameOver;
	private InterfaceSac interfaceSac;
	private static Combat combat;
	private CombatResult combatResult;
	
	private static Image fleches;

	//AppGameContainer container;


	private static Equipe equipe;
	private static ArrayList<Coffre> coffresOuverts;
	
	//#endregion
	
	//#region --------CONSTRUCTEUR-----------
	public Jeu(String name)	
	{		
		super(name);
	}
	//#endregion
	
	//#region -------GETTERS-----------------
	public static Equipe getEquipe()
	{
		return equipe;
	}
	
	/**
	 * Retourne la fléche dont l'id est passé en paramètre.
	 * @param id
	 * @return
	 */
	public static Image getFleche(int id)
	{
		return fleches.getSubImage(id*11, 0, 11, 9);
	}
	
	public Jeu getJeu()
	{
		return this;
	}
	
	//#endregion

	//#region ------OVERRIDE STATEBASEDGAME--
	@Override
	public void initStatesList(GameContainer container) throws SlickException 
	{
		fleches = new Image("ressources/images/fleches.png",new Color(255,0,255));
		
		equipe = new Equipe(3);
		equipe.ajouter(new Rodeur("Tell"));
		equipe.ajouter(new Guerrier("Chrysaor"));
		equipe.ajouter(new Mage("Yensid"));	
		
		GestionnaireMusique.chargerMusique("victory");		
		GestionnaireMusique.chargerMusique("battle");

		
		//TEST
		equipe.get(2).apprendreCompetence(GenerateurDonnees.genererSkill("debug"));
		
		Arme test = GenerateurDonnees.genererArme("epee_test");
		
		equipe.ajouterObjet(test, 1);
		equipe.ajouterObjet(GenerateurDonnees.genererArme("arc_test"), 1);
		equipe.ajouterObjet(GenerateurDonnees.genererArme("bouc_test"), 1);
		equipe.ajouterObjet(GenerateurDonnees.genererArme("baton_test"), 1);
		
		System.out.println(test);
		
		//F_TEST
		
		if (app instanceof AppGameContainer) 
		{  
			app = (AppGameContainer) container;
		}  

		//_-_-_-_-GAME STATES-_-_-_-_\\
		exploration = new Exploration();
		menu = new Menu();
		gestionEquipe = new GestionEquipe();
		configuration = new Configuration();
		combat = new Combat();
		saisie = new Saisie();
		gameOver = new GameOver();
		interfaceSac = new InterfaceSac();
		combatResult = new CombatResult();
		
		addState(exploration);
		addState(saisie);
		addState(menu);
		addState(gestionEquipe);
		addState(configuration);
		addState(interfaceSac);
		addState(combat);
		addState(gameOver);
		addState(combatResult);
		
		interfaceSac.ajouterMeta("equipable", test);
		

	}
	
	//#endregion

	//#region ---INIT=>CHANGEMENT DE SBG-----
	public static void saisir(Image icone,String message, String defaut, int sortie, StateBasedGame sbg, ActionSaisie operations, HashMap<String, Object> param)
	{
		Saisie.init(icone, message, defaut, sortie, operations, param);
		sbg.enterState(Config.SAISIE);
		app.pause();
	}
	
	
	/**
	 * Entre dans la state Combat.
	 * Lance le combat Joueur contre ennemis.
	 * 
	 * @param joueur
	 * 		L'équipe du joueur.
	 * @param ennemis
	 * 		L'équipe ennemis adverse.
	 * @param sbg
	 * 		State en cours.
	 * @throws SlickException 
	 */
	public static void entrerCombat(Equipe joueur, EquipeEnnemis ennemis, StateBasedGame sbg) throws SlickException
	{
		Combat.init(joueur, ennemis);
		combat.init(null, sbg);
		sbg.enterState(Config.COMBAT, new _TransitionCombat() ,null);
		GestionnaireMusique.jouerEnBoucle(ennemis.getMusique());
		
	}
	//#endregion
	
	//#region ------AUTRES METHODES----------
	public static void setPaused(boolean b)
	{
		app.setPaused(b);
	}
	

	public static boolean estPleinEcran()
	{
		return app.isFullscreen();
	}

	public static void switchModeAffichage() throws SlickException
	{
		app.setFullscreen(!app.isFullscreen());
		//app.setMouseGrabbed(app.isFullscreen());
	}
	
	public static ArrayList<Coffre> getCoffresOuverts()
	{
		return coffresOuverts;
	}
	
	public static void ajouterCoffreOuvert(Coffre coffre)
	{
		coffresOuverts.add(coffre);
	}
	
	public static boolean estCoffreOuvert(Coffre coffre)
	{
		/*
		for(Coffre c : coffresOuverts)
		{
			if(c.equals(coffre))
			{
				System.out.print("true");
				return true;
			}
		}
		System.out.print("false");
		return false;
		*/
		return coffresOuverts.contains(coffre);
	}

	//#endregion

	
	//#region ++++++++++[[MAIN]]+++++++++++++
	public static void main(String[] args) {
		try 
		{
			//test zone
			
			//fin test zone
			Saisie.initSaisie();
			GestionnaireMusique.init();
			
			RessourceAnimationLoader.init();
			
			//lib.LibUtil.addToJavaLibraryPath("../lib");
			
			Map.init(Config.LONGUEUR, Config.LARGEUR);
			
			coffresOuverts = new ArrayList<Coffre>();

			app = new AppGameContainer(new Jeu("Battle III : The fantasy land of the Concepteur"));
			
			app.setDisplayMode(Config.LONGUEUR, Config.LARGEUR, false);
			app.setTargetFrameRate(Config.FPS);
			app.setShowFPS(false);
			app.setMouseGrabbed(app.isFullscreen());
			
			app.start();
			
		} 

		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		
	}
	//#endregion


}