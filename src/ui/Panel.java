package ui;


import game.launcher.Launcher;
import game.system.application.Application;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


/**
 * Représente une zone.
 * 
 * @author Darklev
 *
 */
public class Panel {
	
	private int width;
	private int height;
	private Color color;
	private Color colorFrame;
	private int frame;
	
	/**
	 * Constructeur.
	 * 
	 * @param width
	 * 		Longueur du panel.
	 * @param height
	 * 		Hauteur du panel.
	 * @param frame
	 * 		Epaisseur de la bordure.
	 * @param color
	 * 		Couleur du fond.
	 * @param colorFrame
	 * 		Couleur de la bordure.
	 */
	public Panel(int width, int height, int frame, Color color, Color colorFrame){
		this.width = width;
		this.height = height;
		this.color = color;
		this.colorFrame = colorFrame;
		this.frame = frame;
	}
	
	/**
	 * Affiche le panel aux coordonnées passées en paramètre.
	 * 
	 * @param x
	 * 		Abscisse d'affichae.
	 * @param y
	 * 		Ordonnées d'affichage.
	 */
	public void render(int x, int y){
		Graphics g = Application.application().getGraphics();
		
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
		g.setColor(colorFrame);
		for(int i = 0; i<frame; i++){
			g.drawRect(x+i, y+i, width-i*2-1, height-i*2-1);
		}
	}
	
	
	
}
