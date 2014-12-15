package game.system;

import game.Config;
import game.system.application.Application;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;



import util.Regex;




/**
 * Console permettant de saisir des commandes.
 * 
 * @author Darklev
 *
 */
public class Console {
	
	private class ConsoleRenderer{
		
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
			textField = new TextField(Application.application().getContainer(), Application.application().getContainer().getDefaultFont(), 0, Config.LARGEUR - 25, Config.LONGUEUR, 20);
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
			g.fillRect(0, 0, Config.LONGUEUR, Config.LARGEUR);
			g.setColor(defaultTextColor);
			GUIContext context = Application.application().getContainer();
			int lines = console.out().split("\n").length;
			g.drawString(console.out(), 3, Config.LARGEUR - 20 - 20 * lines);
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

		public void setFocus(boolean focus) {
			textField.setFocus(focus);
		}
	}
	
	
	private String out;
	private static Console self;
	private ConsoleRenderer renderer;
	
	/**
	 * Constructeur 
	 */
	private Console(){
		this.out = "";
		this.renderer = new ConsoleRenderer(new Color(0,0,0,0.75f) , Color.white);
	}
	
	/**
	 * Affiche un message dans la console.
	 * 
	 * @param message 
	 * 		Le message à afficher.
	 */
	public void print(String message){
		out += message + "\n";
	}
	
	/**
	 * Retourne la sortie de la console.
	 * 
	 * @return la sortie de la console.
	 */
	public String out(){
		return out;
	}
	
	/**
	 * Retourne l'instance de la console.
	 * 
	 * @return l'instance de la console.
	 */
	public static Console console(){
		if(self == null){
			self = new Console();
		}
		return self;
	}
	
	/**
	 * Affiche la console.
	 */
	public void render(){
		renderer.render(this);
	}
	
	/**
	 * Valide le texte entré dans la console.
	 * @throws SlickException 
	 */
	public void validate() throws SlickException{
		//print(renderer.clearTextField());
		try {
			print(new Command(renderer.clearTextField()).execute());
		} catch (CommandParseException e) {
			print(e.getMessage());
		}
	}

	/**
	 * Focus la console si le paramètre vaut vrai, sinon supprime le focus
	 * de la console.
	 * 
	 * @param focus
	 * 		Vrai si la console doit être focus, sinon faux.
	 */
	public void setFocus(boolean focus) {
		renderer.setFocus(focus);
	}

}
