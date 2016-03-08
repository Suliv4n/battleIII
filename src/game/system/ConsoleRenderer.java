package game.system;

import game.StatesId;
import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class ConsoleRenderer{
	
	private TextField textField;
	private Color background;
	private Color defaultTextColor;
	
	/**
	 * Constructeur
	 * 
	 * @param background
	 * 		Couleur du fond.
	 * @param textColor
	 * 		Couleur du texte.
	 */
	public ConsoleRenderer(Color background, Color textColor ) {
		this.background = background;
		this.defaultTextColor = textColor;
		textField = new TextField(Application.application().getContainer(), Application.application().getContainer().getDefaultFont(), 0, Configurations.SCREEN_WIDTH - 25, Configurations.SCREEN_HEIGHT, 20);
		textField.setBackgroundColor(null);
		textField.setBorderColor(null);
	}
	
	/**
	 * Afficher la console;
	 * 
	 * @param console
	 * 		La console à afficher.
	 */
	public void render(Console console){
		Graphics g = Application.application().getGraphics();
		Color current = g.getColor();
		g.setColor(background);
		g.fillRect(0, 0, Configurations.SCREEN_WIDTH, Configurations.SCREEN_HEIGHT);
		g.setColor(defaultTextColor);
		GUIContext context = Application.application().getContainer();
		/*
		int lines = console.out().split("\n").length;
		g.drawString(console.out(), 3, Config.LARGEUR - 20 - 20 * lines);
		*/
		ArrayList<ConsoleLine> lines = console.out();
		int lineHeight = 15;
		int y = 20;
		for(int i=lines.size()-1; i>=0;i--){
			ConsoleLine line = lines.get(i);
			y += lineHeight * (line.getLine().split("\n").length) + 2;
			Application.application().drawStringWithoutGameFont(line.getLine(), 3, Configurations.SCREEN_HEIGHT - y, line.getColor());
		}
		textField.render(context, g);
		g.setColor(current);
	}

	/**
	 * Supprime le texte dans la textefield.
	 * Retourne le texte qu'il y avait à l'intérieur.
	 * 
	 * @return le texte saisie.
	 * 
	 */
	public String clearTextField() {
		String text = textField.getText();
		textField.setText("");
		return text;
	}

	/**
	 * Modifie le focus.
	 * 
	 * @param focus
	 * 		Vrai pour gagner le focus, faux pour perdre le focus.
	 */
	public void setFocus(boolean focus) {
		textField.setFocus(focus);
	}
	
	/**
	 * Retourne le texte saisi.
	 * 
	 * @return le texte saisi.
	 */
	public String getText(){
		return textField.getText();
	}
	

	public void addListener(ComponentListener listener){
		textField.addListener(listener);
	}

}