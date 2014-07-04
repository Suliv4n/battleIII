package game;

import org.newdawn.slick.Color;

/**
 * Configuration du jeu : dimension de la fenêtre, FPS,
 * id des GameStates et couleur des menus.
 * 
 * @author Darklev
 *
 */
public class Config 
{

	//#region ----ANTICONSTRUCTEUR----
	//Classe statique
	private Config(){};
	//#endregion
	
	//#region --------DIMENSION-------
	//Taille de l'écran
	public static final int LONGUEUR = 640;
	public static final int LARGEUR = 480;
	//#endregion
	
	//#region -----------FPS----------
	//FPS
	public static final int FPS = 30;
	//#endregion

	//#region ----ID GAME STATES-------
	//id des GameStates
	public static final int EXPLORATION = 1;
	public static final int MENU = 2;
	public static final int GESTIONNAIRE_EQUIPE = 3;
	public static final int CONFIGURATION = 4;
	public static final int COMBAT = 5;
	public static final int SAISIE = 6;
	public static final int GAME_OVER = 7;
	public static final int SAC = 8;
	public static final int COMBATRESULT = 9;
	public static final int TITLE_SCREEN = 0;
	//#endregion
	
	
	//#region -------COULEURS CADRE----
	//Couleur des menus
	/**
	 * Couleur principale (fond)
	 */
	public static Color couleur1 = new Color(150,20,0);
	/**
	 * Couleur secondaire (cadre)
	 */
	public static Color couleur2 = new Color(200,150,0);
	
	public static Color couleurXP = new Color(0,150,200);
	//#endregion
	
	//#region -----VOLUMES------------
	//Volume du jeu
	public static int musique = 0;
	public static int son = 100;
	//#endregion
}
