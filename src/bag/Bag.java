package bag;

import bag.item.stuff.Weapon;
import bag.item.stuff.Armor;

/**
 * Classe représentant un sac permettant de contenir
 * des objets et de les trier.
 * 
 * @author Darklev
 *
 */
public class Bag 
{

	private Category miscellaneous; //objets divers (ingrédient ..)
	private Category battle; //objets utilisables en combat (potions, ...)
	private Category stuff; //armes et armures
	private Category rares; //objet de quête  
	
	/**
	 * Constructeur
	 */
	public Bag()
	{
		miscellaneous = new Category();
		battle = new Category();
		stuff = new Category();
		rares = new Category();
	}
	
	/**
	 * Retourne la catégorie "divers".
	 * 
	 * @return la catégorie "divers".
	 * 
	 */
	public Category getMiscellaneous()
	{
		return miscellaneous;
	}

	/**
	 * Retourne la catégorie "battle".
	 * 
	 * @return la catégorie "battle".
	 */
	public Category getCombat() 
	{
		return battle;
	}

	/**
	 * Retourne la catégorie "stuff".
	 * 
	 * @return la catégorie "stuff".
	 */
	public Category getStuff() 
	{
		return stuff;
	}
	
	/**
	 * Retourne la catégorie "rare".
	 * 
	 * @return la catégorie "rare".
	 */
	public Category getRares()
	{
		return rares;
	}
	
	/**
	 * Ajoute une objet en le classant automatique dans une catégorie.
	 * 
	 * @param item
	 * 		Objet à ajouter.
	 * @param quantity
	 * 		Quantité de l'objet à ajouter.
	 */
	public void add(IItems item, int quantity)
	{
		getItemCategory(item).add(item, quantity);
	}
	
	/**
	 * Supprimer un objet du sac.
	 * 
	 * @param item
	 * 		Objet à supprimer.
	 * @param quantity
	 * 		Quantité à supprimer.
	 */
	public void supprimer(IItems item,int quantity)
	{
		getItemCategory(item).remove(item, quantity);
	}
	
	/**
	 * Retourne la catégorie d'un objet.
	 * 
	 * @param item
	 * 		L'objet dont la catégorie est à retourner.
	 * @return
	 * 		La catégorie dont l'objet passé en paramètre appartient.
	 */
	private Category getItemCategory(IItems item)
	{
		if(item instanceof Weapon || item instanceof Armor)
		{
			return stuff;
		}
		else if(item.isUsableInBattle())
		{
			return battle;
		}
		else if(item.isRare())
		{
			return rares;
		}
		else
		{
			return miscellaneous;
		}
	}
	
	/**
	 * Retourne la quatité restante d'un objet.
	 * 
	 * @param item
	 * 		Objet dont la quantité restante est à retourner.
	 * @return la quantité de l'objet dans le sac.
	 */
	public int getQuantity(IItems item)
	{
		return getItemCategory(item).getQuantity(item);
	}
}
