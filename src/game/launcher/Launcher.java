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

package game.launcher;

import org.newdawn.slick.SlickException;

import game.system.application.Application;

/**
 * MAIN CLASS
 * 
 * @author Darklev
 *
 */
public class Launcher
{

	/*
	 * To do list : 
	 * TODO : supprimer fichier Config.java et mettre les données dans 
	 * Settings.java/Configurations.java les id des states dans un autre fichier.
	 * TODO : Méthodes render et update dans la classe dialogue. 
	 * TODO : Classe ConsoleLine et mise à jour de Consle.java.
	 * TODO : Graphics sur la mémoire pour le débuggage.
	 * TODO : Classe Logger pour gérer les logs.
	 * TODO : Hitbox pour les personnages et amélioration du systèmde collision
	 * TODO : Mettre à jour toutes les states (extends Top)
	 * TODO : Réfactoriser la classe DataManager.
	 * TODO : Gestion des tiles animées de type REVERSE.
	 * TODO : Sauvegardes des paramètrages.
	 */
	
	public static void main(String[] args) {
		lib.LibUtil.addToJavaLibraryPath("lib");
		try {
			Application.application().launch();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}