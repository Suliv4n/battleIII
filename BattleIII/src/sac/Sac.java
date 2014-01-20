package sac;

import sac.objet.stuff.Arme;
import sac.objet.stuff.Armure;


public class Sac 
{

	private Categorie divers; //objets divers (ingrédient ..)
	private Categorie combat; //objets utilisables en combat (potions, ...)
	private Categorie equipements; //armes et armures
	private Categorie rares; //objet de quête  
	
	public Sac()
	{
		divers = new Categorie();
		combat = new Categorie();
		equipements = new Categorie();
		rares = new Categorie();
	}
	
	public Categorie getDivers()
	{
		return divers;
	}

	public Categorie getCombat() 
	{
		return combat;
	}

	public Categorie getEquipements() 
	{
		return equipements;
	}

	public Categorie getRares()
	{
		return rares;
	}
	
	public void ajouter(IObjet objet, int qte)
	{
		getCategorieObjet(objet).ajouter(objet, qte);
	}
	
	public void supprimer(IObjet objet,int qte)
	{
		getCategorieObjet(objet).supprimer(objet, qte);
	}
	
	private Categorie getCategorieObjet(IObjet objet)
	{
		if(objet instanceof Arme || objet instanceof Armure)
		{
			return equipements;
		}
		else if(objet.utilisableCombat())
		{
			return combat;
		}
		else if(objet.estRare())
		{
			return rares;
		}
		else
		{
			return divers;
		}
	}
	
	/**
	 * Retourne la quatité restante d'un objet.
	 * 
	 * @param objet
	 * 		Objet dont la quantité restante est à retourner.
	 * @return
	 */
	public int getQuantite(IObjet objet)
	{
		return getCategorieObjet(objet).getQuantite(objet);
	}
}
