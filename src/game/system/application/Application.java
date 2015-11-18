package game.system.application;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import game.input.TextInput;
import game.settings.Settings;
import game.system.Configurations;
import game.system.Console;
import game.system.ConsoleLine;
import game.system.ConsoleLogger;
import game.system.command.CommandsManager;
import map.Map;
import multiplayer.ConnectorManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import animation.ResourcesAnimitationManager;
import audio.MusicManager;
import audio.SoundsManager;

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
	private ConnectorManager server;
	
	/**
	 * Constructeur.
	 * Application est un singleton.
	 * 
	 * @throws SlickException
	 */
	private Application() throws SlickException{
		TextInput.initSaisie();
		CommandsManager.init();
		MusicManager.init();
		ResourcesAnimitationManager.init();
		Map.init(Configurations.SCREEN_WIDTH, Configurations.SCREEN_HEIGHT);
		game = new Game(Configurations.GAME_TITLE);
		this.container = new AppGameContainer(game);
		if(Settings.MULTIPLAYER){
			server = new ConnectorManager();
			server.connect();
		}
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
		
		SoundsManager.init();
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
	 * Retourne le connector manager pour le multiplayer.
	 */
	public ConnectorManager getConnector(){
		return server;
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
	 * Charge un particle system avec l'emmiter et l'image passé en paramètre.
	 * 
	 * @param emmiter
	 * 		Fichier emmiter sans l'extension xml (depuis le dossier particles).
	 * @param image
	 * 		Image depuis le dossier des images.
	 * @return le particle system avec l'emmiter et l'image passé en paramètre.
	 */
	public ParticleSystem loadParticleSystem(String emmiter, String image){
		
		emmiter = Configurations.PARTICLE_EMMITERS_FOLDER + emmiter + ".xml";
		image = Configurations.IMAGES_FOLDER + image;
		
		File xmlFile = new File(emmiter);
		ParticleSystem particle = new ParticleSystem(image);
		
		ConfigurableEmitter ce;
		try {
			ce = ParticleIO.loadEmitter(xmlFile);
			particle.addEmitter(ce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return particle;
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

	/**
	 * Retourne la console de log.
	 * 
	 * @return la console de log.
	 */
	public ConsoleLogger logger() {
		return (ConsoleLogger) ConsoleLogger.logger();
	}
	
	
	
	//-----------LOG-----------------
	
	public void debug(String message){
		logger().print(new ConsoleLine("[" + new SimpleDateFormat("yyyy MMM dd HH:mm:ss").format(new Date()) +  "]" + "[debug]" + message, new Color(0,200,30)));
	}
	
}
