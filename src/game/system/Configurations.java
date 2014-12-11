package game.system;

import org.newdawn.slick.Color;

public class Configurations {
	
	/**
	 * Application name.
	 */
	public static final String GAME_TITLE = "BATTLE III";
	
	/**
	 * Longueur de la fenêtre de jeu.
	 */
	public static final int SCREEN_WIDTH = 640;
	
	/**
	 * Hauteur de la fenêtre de jeu.
	 */
	public static final int SCREEN_HEIGHT = 480;

	/**
	 * Couleur de fond par défaut.
	 */
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(100,0,0);
	
	/**
	 * Couleur de bordure par défaut.
	 */
	public static final Color DEFAULT_BORDER_COLOR = new Color(150,100,0);
	
	/**
	 * Couleur de barre pv
	 */
	public static final Color HEALTH_BAR_COLOR = new Color(50,200,50);
	
	/**
	 * Couleur de la police.
	 */
	public static final Color DEFAULT_FONT_COLOR = new Color(255,255,255);
	
	/**
	 * Nombre de FPS du jeu.
	 */
	public static final int FPS = 30;
	
	/**
	 * Vrai si les options de debugages doivent être activées.
	 * Sinon faux. 
	 */
	public static final boolean DEBUG = true;
	
}
