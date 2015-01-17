package game.system.command;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

public class Command {
	
	
	private String command;
	private String[] parameters;
	
	/**
	 * Constructuer
	 * 
	 * @param command
	 * 		La ligne de commande
	 * @throws CommandParseException
	 * 		Si une mauvaise syntaxe
	 */
	public Command(String command) throws CommandParseException{
		String[] elements = parse(command);
		if(elements.length == 1){
			this.command = elements[0];
			parameters = new String[0];
		}
		else if(elements.length > 0){
			this.command = elements[0];
			parameters = new String[elements.length-1];
			for(int i=1; i<elements.length;i++){
				parameters[i-1] = elements[i];
			}
		}
	}
	
	/**
	 * Ananlyse une chaîne de caractère.
	 * 
	 * @param line
	 * 		La ligne de caractère à analyser.
	 * @throws CommandParseException 
	 * 		Si mauvaise syntaxe.
	 */
	public String[] parse(String line) throws CommandParseException{
		String res[] = line.trim().split(" ");
		String quote = "";
		boolean findQuote = false;
		ArrayList<String> parse = new ArrayList<String>();
		if(res.length == 0){
			throw new CommandParseException("Empty command");
		}
		for(String s : res){
			if(!s.contains("\"")){ //pas de quote
				if(findQuote){
					quote += s + " ";
				}
				else{
					parse.add(s);
				}
			}
			else if(s.matches("$\"(.*)\"^")){ //quotes sans espace
				parse.add(s);
			}
			else if(s.matches("^\".*") && !findQuote){ // Une quote detecté
				quote += s.replace("\"","") + " ";
				findQuote = true;
			}
			else if(s.matches(".*\"$") && findQuote){ //Fin de quote
				quote += s.replace("\"","");
				findQuote = false;
				parse.add(quote);
				quote="";
			}
		}
		return parse.toArray(new String[parse.size()]);
	}
	
	/**
	 * Retourne le paramètre d'index i.
	 * 
	 * @param i
	 * 		Index du paramètre à retourner.
	 * @return le paramètre d'index i de type string ou null s'il n'existe pas.
	 */
	public String getParameter(int i) {
		return i >= 0 && i < parameters.length ? parameters[i] : null;
	}
	
	/**
	 * Retourne le paramètre d'index i de type float.
	 * 
	 * @param i
	 * 		Index du paramètre à retourner.
	 * @return le paramètre d'index i de type float ou null s'il n'existe pas.
	 */
	public Float getFloatParameter(int i) {
		return i >= 0 && i < parameters.length ? Float.parseFloat(parameters[i]) : null;
	}
	
	/**
	 * Retourne le paramètre d'index i de type entier.
	 * 
	 * @param i
	 * 		Index du paramètre à retourner.
	 * @return le paramètre d'index i de type entier ou null s'il n'existe pas.
	 */
	public Integer getIntegerParameter(int i) {
		return i >= 0 && i < parameters.length ? Integer.parseInt(parameters[i]) : null;
	}
	
	/**
	 * Retoutne la nom de la commande.
	 * 
	 * @return le nom de la commande.
	 */
	public String getCommand(){
		return command;
	}
	
	/**
	 * Execute la commande
	 * @throws SlickException 
	 */
	public void execute() throws SlickException{
		CommandsManager.execute(this);
	}
	
}
