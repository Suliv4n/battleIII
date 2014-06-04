package personnage;

import java.util.HashMap;

public class EquipeEnnemis 
{
	private HashMap<Integer,Ennemi> ennemis;
	private int rarete;
	
	private String musique;
	
	public EquipeEnnemis(int rarete, String musique, HashMap<Integer,Ennemi> ennemis)
	{
		this.rarete = rarete;
		this.ennemis = ennemis;
		this.musique = musique;
	}
	
	/*
	public void ajouter(int place, Ennemi ennemi)
	{
		ennemis.put(place,ennemi);
	}
	 */
	public HashMap<Integer, Ennemi> getEnnemis() 
	{
		return ennemis;
	}

	public boolean prets() 
	{
		for(Ennemi e : ennemis.values())
		{
			if(!e.estPret())
			{
				return false;
			}
		}
		return true;
	}

	public String getMusique()
	{
		return musique;
	}
	
	/**
	 * Retourne vrai si la cible est vivante et existante, faux sinon.
	 * 
	 * @param i
	 * 		Index de l'ennemi a testé.
	 * @return
	 * 		vrai si la cible est vivante et existante, faux sinon.
	 */
	public boolean estCibleValable(int i) 
	{
		if(ennemis.containsKey(i))
		{
			if(ennemis.get(i).estVivant())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Retourne le nombre d'ennemis vivants dans le groupe.
	 * 
	 * @return
	 * 
	 * 		Le nombre d'ennemis vivants dans le groupe.
	 */
	public int nombreEnnemisVivants() 
	{
		int tot = 0;
		for(Ennemi e : ennemis.values())
		{
			if(e.estVivant())
				tot++;
		}
		return tot;
	}
}
