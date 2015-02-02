package game.system;

import java.util.ArrayList;

import game.system.command.Command;
import game.system.command.CommandParseException;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;


/**
 * Console permettant de saisir des commandes.
 * 
 * @author Darklev
 *
 */
public class Console {
	
	protected ArrayList<ConsoleLine> lines;
	protected static Console self;
	private ConsoleRenderer renderer;
	public static final Color ERROR_COLOR_LINE = new Color(200,0,0);
	
	/**
	 * Constructeur 
	 */
	protected Console(){
		lines = new ArrayList<ConsoleLine>();
		this.renderer = new ConsoleRenderer(new Color(0,0,0,0.75f) , Color.white);
	}
	
	/**
	 * Affiche un message dans la console.
	 * 
	 * @param message 
	 * 		Le message à afficher.
	 */
	public void print(ConsoleLine line){
		lines.add(line);
	}
	
	/**
	 * Retourne la sortie de la console.
	 * 
	 * @return la sortie de la console.
	 */
	public ArrayList<ConsoleLine> out(){
		return lines;
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
		try {
			new Command(renderer.clearTextField()).execute();
		} catch (CommandParseException e) {
			print(new ConsoleLine(e.getMessage(), ERROR_COLOR_LINE));
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

	/**
	 * Retourne le texte saisi.
	 * 
	 * @return le texte saisi.
	 */
	public String getText(){
		return renderer.getText();
	}
	
	protected ConsoleRenderer getRenderer(){
		return renderer;
	}
	
}
