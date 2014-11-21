package bag;

import java.util.ArrayList;

import org.newdawn.slick.Image;

/**
 * Représente une catégorie d'objet pour le sac.
 * 
 * @author Darklev
 *
 */
public class Category 
{
	private ArrayList<IItems> items;
	private ArrayList<Integer> quantities;

	/**
	 * Constructeur
	 */
	public Category()
	{
		items = new ArrayList<IItems>();
		quantities = new ArrayList<Integer>();
	}
	
	/**
	 * Ajoute un objet en quantité passé en paramètre.
	 * 
	 * @param item
	 * 		Objet à ajouter.
	 * @param quantity
	 * 		Quantité de l'objet à ajouter.
	 */
	public void add(IItems item, int quantity)
	{
		if(items.contains(item))
		{
			int index = -1;
			for(int i = 0; i<items.size();i++)
			{
				if(items.get(i).equals(item))
				{
					index = i;
					break;
				}
			}
			quantities.set(index, quantities.get(index) + quantity);
		}
		else
		{
			items.add(item);
			quantities.add(quantity);
		}
	}
	
	/**
	 * Supprime un objet en quantité passé en paramètre.
	 * 
	 * @param item
	 * 		Objet à supprimer.
	 * @param quantity
	 * 		Quantité à supprimer.
	 */
	public void remove(IItems item, int quantity)
	{
		if(items.contains(item))
		{
			int index = -1;
			for(int i = 0; i<items.size();i++)
			{
				if(items.get(i).equals(item))
				{
					index = i;
					break;
				}
			}
			if(quantities.get(index) - quantity <= 0)
			{
				quantities.remove(index);
				items.remove(index);
			}
			else
			{
				quantities.set(index, quantities.get(index) - quantity);
			}
		}
	}
	
	/**
	 * Retourne le nombre d'objets différents stocker dans la catégorie.
	 * 
	 * @return	le nombre d'objets différents stockés dans la catégorie.
	 */
	public int size()
	{
		return items.size();
	}

	/**
	 * Retourne l'objet à l'index passé en paramètre.
	 * 
	 * @param i
	 * 		Indexe de l'objet à retourner.
	 * @return l'objet à l'index passé en paramètre.
	 */
	public IItems getItem(int i) 
	{
		return items.get(i);
	}
	
	/**
	 * Retourne la quantité restante de l'objet passé en paramètre.
	 * @param item
	 * 		Objet dont la quantité restante est passée en paramètre.
	 * @return
	 * 		La quantité restante de l'objet passé en paramètre.
	 */
	public int getQuantity(IItems item) 
	{
		int i = 0;
		for(IItems o : items)
		{
			if(o == item)
			{
				return quantities.get(i);
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
	public int getQuantity(int i) 
	{
		return quantities.get(i);
	}



}
