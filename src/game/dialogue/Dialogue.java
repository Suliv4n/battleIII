package game.dialogue;

import game.Jeu;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Dialogue implements Cloneable
{
	private ArrayList<Replique> dialogue;
	private int index;
	private Selectionneur selectionneur;
	
	public Dialogue(ArrayList<Replique> dialogue)
	{
		this.dialogue = dialogue;
		index = -1;
	}
	
	public boolean suivant()
	{
		index++;
		if(index >= dialogue.size())
		{
			return false;
		}
		while(dialogue.get(index).getReplique().matches(".*\\$if\\[.*\\].*"))
		{
			String rep = dialogue.get(index).getReplique();
			if(!condition(rep.substring(rep.indexOf("[",rep.indexOf("$if"))+1, rep.indexOf("]", rep.indexOf("$select")))))
			{
				index++;
			}
			else
			{
				break;
			}
		}

		
		if(dialogue.get(index).getReplique().contains("$end"))
		{
			return false;
		}
		return index < dialogue.size();
	}
	
	private boolean condition(String condition)
	{
		System.out.println("-----------------------------------------------------------------------------------------");
		return false;
	}
	
	
	public Replique get()
	{
		if(dialogue.get(index).getReplique().matches(".*\\$select\\[.*\\].*"))
		{
			String rep = dialogue.get(index).getReplique();
			selectionneur = creerSelectionneur(rep.substring(rep.indexOf("[",rep.indexOf("$select"))+1, rep.indexOf("]", rep.indexOf("$select"))));
		}
		return dialogue.get(index);
	}
	
	public Selectionneur getSelectionneur()
	{
		return selectionneur;
	}
	
	/**
	 * Traduit une chaîne de caractères de type [choix1, choix2{1}] en selectionneur.
	 * 
	 * @param syntaxe
	 * 		Chaîne de caractères de type [choix1, choix2{n}] .
	 * 		
	 * @return
	 * 		Le sélectionneur correspondant à la chaîne de caractères passée en paramètre.
	 */
	private Selectionneur creerSelectionneur(String syntaxe)
	{
		ArrayList<String> choix = new ArrayList<String>();
		ArrayList<Integer> allerA = new ArrayList<Integer>();
		
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
				allerA.add(n);
			}
			else
			{
				allerA.add(index + 1);
			}
			
			choix.add(c.replaceAll("\\{([0-9]*|end)\\}", ""));
		}
		
		return new Selectionneur(choix, allerA);
	}
	
	public void validerSelectionneur(int i)
	{
		if(selectionneur != null)
		{
			index = selectionneur.getAllerA(i);
			selectionneur = null;
		}
	}
	
	public boolean estTermine()
	{
		return index >= dialogue.size();
	}
	
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
