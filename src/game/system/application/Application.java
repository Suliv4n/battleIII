package game.system.application;

import java.util.ArrayList;

import game.BattleWithATB;
import game.Combat;
import game.CombatResult;
import game.Config;
import game.Configuration;
import game.Exploration;
import game.GameOver;
import game.GestionEquipe;
import game.InterfaceSac;
import game.Menu;
import game.TitleScreen;
import game.input.TextInput;
import game.system.Configurations;
import game.system.Console;

import map.Chest;
import map.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import animation.ResourcesAnimitationManager;
import audio.MusicManager;

/**
 * Application Slick2d
 * 
 * @author Darklev
 *
 */
public class Application{
	
	private AppGameContainer container;
	private static Application application;
	private Game game;
	private boolean launching = false;

	
	/**
	 * Constructeur.
	 * Application est un singleton.
	 * 
	 * @throws SlickException
	 */
	private Application() throws SlickException{
		TextInput.initSaisie();
		MusicManager.init();
		ResourcesAnimitationManager.init();
		Map.init(Configurations.SCREEN_WIDTH, Configurations.SCREEN_HEIGHT);
		game = new Game(Configurations.GAME_TITLE);
		this.container = new AppGameContainer(game);
	}
	
	/**
	 * Lance le Jeu.
	 * @throws SlickException
	 */
	public void launch() throws SlickException{
		if(!launching){
			init();
			container.setDisplayMode(Configurations.SCREEN_WIDTH, Configurations.SCREEN_HEIGHT, false);
			container.setTargetFrameRate(Configurations.FPS);
			container.setShowFPS(false);
			container.start();
			launching = true;
		}
	}
	
	private void init() throws SlickException{
		MusicManager.loadMusic("victory");		
		MusicManager.loadMusic("battle");
	}
	
	/**
	 * Retourne l'instance courante de l'application.
	 * En crée une si elle n'existe pas.
	 * 
	 * @return application courante.
	 */
	public static Application application(){
		if(application == null){
			try {
				application = new Application();
			} catch (SlickException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return application;
	}
	
	/**
	 * Teste si l'application est en plein écran.
	 * 
	 * @return vrai si l'application est en plein écran, sinon 
	 * retourne faux.
	 */
	public boolean isFullScreen()
	{
		return container.isFullscreen();
	}

	/**
	 * Passe du mode plein écran au mode fenêtré en inversement.
	 * 
	 * @throws SlickException
	 */
	public void toggleFullScreen() throws SlickException
	{
		container.setFullscreen(!container.isFullscreen());
	}
	
	/**
	 * Retoutne le container du jeu en cours.
	 * 
	 * @return la container du jeu en cours.
	 */
	public AppGameContainer getContainer(){
		return container;
	}
	
	/**
	 * Retourne l'objet Graphics de l'application courante.
	 * 
	 * @return le graphics de l'application courante.
	 */
	public Graphics getGraphics(){
		return container.getGraphics();
	}
	
	
	/**
	 * Dessine la chaine de caractères passé en paramètre
	 * avec la police par défaut de slick. 
	 * 
	 * @param string
	 * 		Chaine de caractères à afficher.
	 * @param x
	 * 		Abscisse où afficher la chaîne.
	 * @param y
	 * 		Ordonnée où afficher la chaîne.
	 */
	public void drawStringWithoutGameFont(String string, int x, int y)
	{
		getGraphics().drawString(string, y, x);
	}
	
	/**
	 * Dessine la chaine de caractères passé en paramètre
	 * avec la police par défaut de slick. 
	 * 
	 * @param string
	 * 		Chaine de caractères à afficher.
	 * @param x
	 * 		Abscisse où afficher la chaîne.
	 * @param y
	 * 		Ordonnée où afficher la chaîne.
	 * @param color
	 * 		Couleur à utiliser.
	 */
	public void drawStringWithoutGameFont(String string, int x, int y, Color color)
	{
		Color temp = getGraphics().getColor();
		getGraphics().setColor(color);
		
		getGraphics().drawString(string, x, y);
		
		getGraphics().setColor(temp);
	}
	
	/**
	 * Dessine la chaine de caractères passé en paramètre
	 * avec la police par défaut. 
	 * 
	 * @param string
	 * 		Chaine de caractères à afficher.
	 * @param x
	 * 		Abscisse où afficher la chaîne.
	 * @param y
	 * 		Ordonnée où afficher la chaîne.
	 */
	public void drawString(String string, int x, int y)
	{
		game.getGameFont().drawString(x, y, string);
	}
	
	/**
	 * Dessine la chaine de caractères passé en paramètre. Avec la couleur
	 * passé en paramère.
	 * 
	 * @param string
	 * 		Chaine de caractères à afficher.
	 * @param x
	 * 		Abscisse où afficher la chaîne.
	 * @param y
	 * 		Ordonnée où afficher la chaîne.
	 * @param color
	 * 		Couleur à utiliser.
	 */
	public void drawString(String string, int x, int y, Color color)
	{
		game.getGameFont().drawString(x, y, string, color);
	}
	
	/**
	 * Retourne le jeu en cours.
	 * 
	 * @return le jeu en cours.
	 */
	public Game getGame(){
		return game;
	}
	
	/**
	 * Retourne la console.
	 * 
	 * @return la console.
	 */
	public Console console(){
		return Console.console();
	}
	
}
