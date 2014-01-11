package map;

import game.Jeu;
import personnage.Equipe;
import sac.IObjet;

public class Coffre
{
	
	//coordonnées du coffre.
	private String idMap;
	private int x;
	private int y;
	
	//Contenu du coffre.
	private IObjet contenu;
	private int quantite;
	private int po;
	
	public Coffre(String idMap, int x, int y, IObjet contenu,int qte, int po)
	{
		this.idMap = idMap;
		this.x = x;
		this.y = y;
		this.quantite = qte;
		
		this.contenu = contenu;
		this.po = po;
	}
	
	/**
	 * Coffre de comparaison.
	 * @param idMap
	 * @param x
	 * @param y
	 */
	protected Coffre(String idMap, int x, int y)
	{
		this.idMap = idMap;
		this.x = x;
		this.y = y;
	}
	
	public IObjet getContenu()
	{
		return contenu;
	}
	
	public int getQuantite()
	{
		return quantite;
	}
	
	public int getPO()
	{
		return po;
	}
	
	/**
	 * L'Equipe passé en paramètre récupère le contenu (dont PO) du coffre
	 * et le place dans le sac.
	 * 
	 * @param equipe
	 * 		Equipe de la session du jeu.
	 */
	public void recupererContenu(Equipe equipe)
	{
		if(contenu != null)
		{
			equipe.ajouterObjet(contenu, quantite);
		}
		else if(po > 0)
		{
			equipe.updatePO(po);
		}
		
		Jeu.ajouterCoffreOuvert(this);
	}
	
	/*
	public boolean equals(Coffre coffre)
	{		
		return
			coffre.x == x &&
			coffre.y == y &&
			coffre.idMap == idMap;
	}
	*/
	
	@Override
	public boolean equals(Object coffre)
	{
		if(!(coffre instanceof Coffre))
		{
			return false;
		}
		
		return
			((Coffre)coffre).x == x &&
			((Coffre)coffre).y == y &&
			((Coffre)coffre).idMap.equals(idMap);
	}

	public int getX() 
	{
		return x;
	}
	
	public int getY() 
	{
		return y;
	}
	
	public String toString()
	{
		return x + "  " + y +"   " + idMap;
	}
}
