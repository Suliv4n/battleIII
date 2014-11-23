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

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import game.input.InputAction;
import game.input.TextInput;
import game.system.Console;
import map.Chest;
import map.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import animation.ResourcesAnimitationManager;
import audio.MusicManager;
import personnage.*;
import data.Save;



public class Launcher extends StateBasedGame 
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
	private TextInput saisie;
	private GameOver gameOver;
	private InterfaceSac interfaceSac;
	private static Combat combat;
	private static BattleWithATB battleWithATB;
	private CombatResult combatResult;
	private TitleScreen titleScreen;
	
	private static Image fleches;

	//AppGameContainer container;


	private static Party equipe;
	private static ArrayList<Chest> coffresOuverts;
	
	private static UnicodeFont font;
	
	private static int currentSave = -1 ;
	
	//#endregion
	
	//#region --------CONSTRUCTEUR-----------
	public Launcher(String name)	
	{		
		super(name);
	}
	//#endregion
	
	//#region -------GETTERS-----------------
	public static Party getParty()
	{
		return equipe;
	}
	
	public static void setEquipe(Party equipe) {
		Launcher.equipe = equipe;
	}
	
	public static void setCurrentSave(int save){
		currentSave = save;
	}
	
	public static int getCurrentSave(){
		return currentSave;
	}
	
	public static Console console(){
		return Console.console();
	}
	
	public static AppGameContainer getAppGameContainer(){
		return app;
	}
	
	/**
	 * Retourne la fléche dont l'id est passé en paramètre.
	 * @param id
	 * @return
	 */
	public static Image getArrow(int id)
	{
		return fleches.getSubImage(id*11, 0, 11, 9);
	}
	
	public static UnicodeFont getFont(){
		return font;
	}
	
	//#endregion

	//#region ------OVERRIDE STATEBASEDGAME--
	@Override
	public void initStatesList(GameContainer container) throws SlickException 
	{
		fleches = new Image("ressources/images/fleches.png",new Color(255,0,255));
		equipe = new Party(0);
		//FONT
	    try {
	        InputStream inputStream = ResourceLoader.getResourceAsStream("ressources/fonts/8bitoperator.ttf");

	        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	        awtFont.deriveFont(24);
	        font = new UnicodeFont(awtFont, 15, false, false);
	        font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
	        font.addAsciiGlyphs();
	        font.loadGlyphs();

	    } catch (Exception e) {
	    	System.out.println("FONT---------ERROR");
	        e.printStackTrace();
	    }
	    //!FONT
		
		MusicManager.loadMusic("victory");		
		MusicManager.loadMusic("battle");

		
		//TEST
		
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
		saisie = new TextInput();
		gameOver = new GameOver();
		interfaceSac = new InterfaceSac();
		combatResult = new CombatResult();
		titleScreen = new TitleScreen();
		battleWithATB = new BattleWithATB();
		
		
		addState(titleScreen);
		addState(exploration);
		addState(saisie);
		addState(menu);
		addState(gestionEquipe);
		addState(configuration);
		addState(interfaceSac);
		addState(combat);
		addState(battleWithATB);
		addState(gameOver);
		addState(combatResult);
	}
	
	//#endregion

	//#region ---INIT=>CHANGEMENT DE SBG-----
	public static void saisir(Image icone,String message, String defaut, int sortie, StateBasedGame sbg, InputAction operations, HashMap<String, Object> param)
	{
		TextInput.init(icone, message, defaut, sortie, operations, param);
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
	public static void entrerCombat(Party joueur, EnnemisParty ennemis, StateBasedGame sbg) throws SlickException
	{
		Combat.init(joueur, ennemis);
		combat.init(null, sbg);
		sbg.enterState(Config.COMBAT, new _TransitionCombat() ,null);
		MusicManager.playLoop(ennemis.getMusic());
		
	}
	
	public static void launchBattle(EnnemisParty ennemis, StateBasedGame game) {
		battleWithATB.launch(ennemis);
		game.enterState(Config.BATTLE_ATB);
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
	
	public static ArrayList<Chest> getCoffresOuverts()
	{
		return coffresOuverts;
	}
	
	public static void ajouterCoffreOuvert(Chest coffre)
	{
		coffresOuverts.add(coffre);
	}
	
	public static boolean estCoffreOuvert(Chest coffre)
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
			TextInput.initSaisie();
			MusicManager.init();
			
			ResourcesAnimitationManager.init();
			
			//Ajout des librairies
			lib.LibUtil.addToJavaLibraryPath("lib");
			
			Map.init(Config.LONGUEUR, Config.LARGEUR);
			
			coffresOuverts = new ArrayList<Chest>();
			app = new AppGameContainer(new Launcher("Battle III : The fantasy land of the Concepteur"));
			
			
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