package game.system;

import org.newdawn.slick.Color;

public class Configurations {
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
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(255,255,255);
	
	/**
	 * Couleur de bordure par défaut.
	 */
	public static final Color DEFAULT_BORDER_COLOR = new Color(255,255,255);
	
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
