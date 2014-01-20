package sac;

import java.util.ArrayList;

import org.newdawn.slick.Image;

public class Categorie 
{
	private ArrayList<IObjet> objets;
	private ArrayList<Integer> quantites;

	public Categorie()
	{
		objets = new ArrayList<IObjet>();
		quantites = new ArrayList<Integer>();
	}
	
	public void ajouter(IObjet obj, int qte)
	{
		if(objets.contains(obj))
		{
			int index = -1;
			for(int i = 0; i<objets.size();i++)
			{
				if(objets.get(i).equals(obj))
				{
					index = i;
					break;
				}
			}
			quantites.set(index, quantites.get(index) + qte);
		}
		else
		{
			objets.add(obj);
			quantites.add(qte);
		}
	}
	
	public void supprimer(IObjet obj, int qte)
	{
		if(objets.contains(obj))
		{
			int index = -1;
			for(int i = 0; i<objets.size();i++)
			{
				if(objets.get(i).equals(obj))
				{
					index = i;
					break;
				}
			}
			if(quantites.get(index) - qte <= 0)
			{
				quantites.remove(index);
				objets.remove(index);
			}
			else
			{
				quantites.set(index, quantites.get(index) - qte);
			}
		}
	}
	
	public int getTaille()
	{
		return objets.size();
	}

	public IObjet getObjet(int i) 
	{
		return objets.get(i);
	}
	
	/**
	 * Retourne la quantité restante de l'objet passé en paramètre.
	 * @param objet
	 * 		Objet dont la quantité restante est passée en paramètre.
	 * @return
	 * 		La quantité restante de l'objet passé en paramètre.
	 */
	public int getQuantite(IObjet objet) 
	{
		int i = 0;
		for(IObjet o : objets)
		{
			if(o == objet)
			{
				return quantites.get(i);
			}
			i++;
		}
		return 0;
	}
	
	/**
	 * Retourne la quantité restante de l'objet dont l'indexe est passé en paramètre.
	 * @param i
	 * 		Index de l'objet dont la quantité est à retourner.
	 * @return
	 * 		La quantité restante de l'objet à l'indexe i.
	 */
	public int getQuantite(int i) 
	{
		return quantites.get(i);
	}



}
