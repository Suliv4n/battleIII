package game.system;

import org.newdawn.slick.Color;

public class Configurations {
	
	/**
	 * Application name.
	 */
	public static final String GAME_TITLE = "BATTLE III";
	
	/**
	 * Longueur de la fen�tre de jeu.
	 */
	public static final int SCREEN_WIDTH = 640;
	
	/**
	 * Hauteur de la fen�tre de jeu.
	 */
	public static final int SCREEN_HEIGHT = 480;

	/**
	 * Couleur de fond par d�faut.
	 */
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(100,0,0);
	
	/**
	 * Couleur de bordure par d�faut.
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
	 * Vrai si les options de debugages doivent �tre activ�es.
	 * Sinon faux. 
	 */
	public static final boolean DEBUG = true;
	
	/**
	 * Dossier contenant les ressouces du jeu.
	 */
	public static final String RESOURCES_FOLDER = "ressources/";

	/**
	 * Dosser contenant les donn�es.
	 */
	public static final String DATA_FOLDER = RESOURCES_FOLDER + "donnees/";
	
	/**
	 * Dosser contenant les emmiters pour les particle systemes.
	 */
	public static final String PARTICLE_EMMITERS_FOLDER = RESOURCES_FOLDER + "particles/";
	
	/**
	 * Dosser contenant les images.
	 */
	public static final String IMAGES_FOLDER = RESOURCES_FOLDER + "images/";
	
}
