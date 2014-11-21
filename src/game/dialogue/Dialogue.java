package game.dialogue;

import game.Launcher;

import java.util.ArrayList;
import java.util.regex.Pattern;

import personnage.Character;
import util.Regex;

/**
 * Classe permettant de créer un dialogue.
 * 
 * @author Darklev
 *
 */
public class Dialogue implements Cloneable
{
	private ArrayList<Line> dialogue;
	private int index;
	private Select select;
	private boolean notificationEnd = false; //vaut true si $end est rencontré dans la réplique en cours.
	
	public Dialogue(ArrayList<Line> dialogue)
	{
		this.dialogue = dialogue;
		index = -1;
	}
	
	/**
	 * Passe à la réplique suivante.
	 * @return
	 * 	Vrai s'il y a une réplique suivante ou faux s'il n'y en a pas ou une notification
	 * end a été rencotrée.
	 */
	public boolean suivant()
	{
		index++;
		if(index >= dialogue.size())
		{
			return false;
		}
		while(dialogue.get(index).getLine().matches(".*\\$if\\[.*\\].*"))
		{
			String line = dialogue.get(index).getLine();
			String condition = Regex.eval(line, ".*\\$if\\[(.*)\\].*")[1];
			if(!condition(condition))
			{
				index++;
			}
			else
			{
				break;
			}
		}

		//Si notifEnd vaut true alors mettre fin au dialogue
		if(notificationEnd == true){
			notificationEnd = false;
			return false;
		}
		
		if(dialogue.get(index).getLine().contains("\\$end"))
		{
			notificationEnd = true;
		}
		else{
			notificationEnd = false;
		}
		return index < dialogue.size();
	}
	
	/**
	 * Analyse une condition posée dans une réplique et retourne vraie
	 * si la condition est vraie, faux sinon.
	 * @param condition
	 * @return
	 * 		Vrai si la condition est vraie, faux sinon.
	 */
	private boolean condition(String condition)
	{
		boolean res = false;
		String[] eval = Regex.eval(condition,"([^>|<]*)(=|>=?|<=?)(.*)|(.*)"); 
		String[] membres = new String[2];
		String operator = null;
		if(eval.length > 1)
		{
			membres[0] = eval[1];
			
			if(condition.matches("([^>|<]*)(=|>=?|<=?)(.*)|(.*)"))
			{
				operator = eval[2];
				membres[1] = eval[3];
			}
		}
		
		
		Character character0 = null;
		Character character1 = null;
		for(String m : membres){
			if(m.matches("^equipe(\\d+)$")){
				character0 = Launcher.getParty().get(Integer.parseInt(Regex.eval(m, "^equipe(\\d+)$")[1]));
			}
			else if(m.equalsIgnoreCase("mage")){
				character1 = Launcher.getParty().getMage();
			}
			else if(m.equalsIgnoreCase("rodeur")){
				character1 = Launcher.getParty().getRanger();
			}
			else if(m.equals("guerrier")){
				character1 = Launcher.getParty().getWarrior();
			}
		}
		
		if(character0 != null && character1 != null)
		{
			if(operator.equals("=")){
				res = character0 == character1;
			}
		}
		
		return res;
	}
	
	/**
	 * Retoune la réplqiue courrante.
	 * 
	 * @return
	 * 	La réplique courante
	 */
	public Line get()
	{
		if(dialogue.get(index).getLine().matches(".*\\$select\\[.*\\].*"))
		{
			String rep = dialogue.get(index).getLine();
			select = createSelect(rep.substring(rep.indexOf("[",rep.indexOf("$select"))+1, rep.indexOf("]", rep.indexOf("$select"))));
		}
		return dialogue.get(index);
	}
	
	/**
	 * Retourne le select courant.
	 * @return
	 * 	Le select courant ou null s'il n'y en a pas.
	 */
	public Select getSelect()
	{
		return select;
	}
	
	/**
	 * Traduit une chaîne de caractères de type [choix1; choix2{1}] en selectionneur.
	 * 
	 * @param syntaxe
	 * 		Chaîne de caractères de type [choix1; choix2{n}] .
	 * 		
	 * @return
	 * 		Le sélectionneur correspondant à la chaîne de caractères passée en paramètre.
	 */
	private Select createSelect(String syntaxe)
	{
		ArrayList<String> choice = new ArrayList<String>();
		ArrayList<Integer> goTo = new ArrayList<Integer>();
		
		for(String c : syntaxe.split(";"))
		{
			if(c.matches(".*\\{([0-9]*|end)\\}"))
			{
				int n = 0;
				try
				{
					n = Integer.parseInt(c.substring((c.indexOf("{")+1),c.indexOf("}")));
				}
				catch (NumberFormatException e)
				{
					n = dialogue.size(); //end
				}
				goTo.add(n);
			}
			else
			{
				goTo.add(index + 1);
			}
			
			choice.add(c.replaceAll("\\{([0-9]*|end)\\}", ""));
		}
		
		return new Select(choice, goTo);
	}
	
	/**
	 * Valide le sélecteur. Exécute le goto associer au Select à l'index passer en paramètre.
	 * Le select n'existe plus.
	 * @param i
	 * 		Index à valider.
	 */
	public void validateSelect(int i)
	{
		if(select != null)
		{
			index = select.getGoTo(i);
			select = null;
		}
	}
	
	/**
	 * Retourne vrai si le dialogue est fini. Faux sinon.
	 * @return
	 * 		vrai si le dialogue est fini. Faux sinon.
	 */
	public boolean isEnd()
	{
		return index >= dialogue.size();
	}
	
	/**
	 * Clone le dialogue.
	 */
	@Override
	public Dialogue clone()
	{
		try 
		{
			return (Dialogue) super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
