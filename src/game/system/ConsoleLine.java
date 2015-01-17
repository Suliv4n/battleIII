package game.system;

import org.newdawn.slick.Color;

/**
 * Ligne de console.
 * 
 * @author Sulivan
 *
 */
public class ConsoleLine {
	private Color color;
	private String line;
	
	/**
	 * Constructeur
	 * 
	 * @param line
	 * @param color
	 * 		Couleur de la ligne.
	 */
	public ConsoleLine(String line, Color color){
		this.line = line;
		this.color = color;
	}
	
	/**
	 * Retourne la ligne.
	 * @return la ligne.
	 */
	public String getLine(){
		return line;
	}
	
	/**
	 * Retourne la couleur de la ligne.
	 * @return
	 */
	public Color getColor(){
		return color;
	}
}
