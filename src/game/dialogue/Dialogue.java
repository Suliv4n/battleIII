package game.dialogue;

import game.Jeu;

import java.util.ArrayList;
import java.util.regex.Pattern;

import personnage.Personnage;
import util.Regex;

public class Dialogue implements Cloneable
{
	private ArrayList<Replique> dialogue;
	private int index;
	private Selectionneur selectionneur;
	private boolean notifEnd = false; //vaut true si $end est rencontré dans la réplique en cours.
	
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
			String condition = Regex.eval(rep, ".*\\$if\\[(.*)\\].*")[1];
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
		if(notifEnd == true){
			notifEnd = false;
			return false;
		}
		
		if(dialogue.get(index).getReplique().contains("$end"))
		{
			notifEnd = true;
		}
		else{
			notifEnd = false;
		}
		return index < dialogue.size();
	}
	
	/**
	 * Analyse une condition posée dans une réplique et retourne vraie
	 * si la condition est vraie, faux sinon.
	 * @param condition
	 * @return
	 */
	private boolean condition(String condition)
	{
		boolean res = false;
		String[] eval = Regex.eval(condition,"([^>|<]*)(=|>=?|<=?)(.*)|(.*)"); 
		String[] membres = new String[2];
		String operateur = null;
		if(eval.length > 1)
		{
			membres[0] = eval[1];
			
			if(condition.matches("([^>|<]*)(=|>=?|<=?)(.*)|(.*)"))
			{
				operateur = eval[2];
				membres[1] = eval[3];
			}
		}
		
		
		Personnage perso0 = null;
		Personnage perso1 = null;
		for(String m : membres){
			if(m.matches("^equipe(\\d+)$")){
				perso0 = Jeu.getEquipe().get(Integer.parseInt(Regex.eval(m, "^equipe(\\d+)$")[1]));
			}
			else if(m.equalsIgnoreCase("mage")){
				perso1 = Jeu.getEquipe().getMage();
			}
			else if(m.equalsIgnoreCase("rodeur")){
				perso1 = Jeu.getEquipe().getRodeur();
			}
			else if(m.equals("guerrier")){
				perso1 = Jeu.getEquipe().getGuerrier();
			}
		}
		
		if(perso0 != null && perso1 != null)
		{
			if(operateur.equals("=")){
				res = perso0 == perso1;
			}
		}
		
		return res;
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
