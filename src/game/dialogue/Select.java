package game.dialogue;

import java.awt.Dimension;
import java.util.ArrayList;

public class Select 
{
	private ArrayList<String> choix;
	private ArrayList<Integer> allerA;
	
	public Select(ArrayList<String> choix, ArrayList<Integer> allerA)
	{
		this.choix = choix;
		this.allerA = allerA;
	}
	
	public String getChoix(int i)
	{
		return choix.get(i);
	}
	
	public int getGoTo(int i)
	{
		return allerA.get(i);
	}
	
	public ArrayList<String> getChoix()
	{
		return choix;
	}
	
	public Dimension getDimensionBoite()
	{
		int max = 0;
		
		for(String c : choix)
		{
			if(c.length() > max)
			{
				max = c.length();
			}
		}
		
		return new Dimension(12 * max + 10, 20 * choix.size());
	}

	/**
	 * Retourne le nombre de choix que propose le sélectionneur.
	 * 
	 * @return
	 *		Le nombre de choix que propose le sélectionneur.
	 */
	public int nbChoix() 
	{
		return choix.size();
	}
	
}
