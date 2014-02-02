package sac.objet;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import sac.IObjet;



public class Objet implements IObjet
{
	private String id;
	private Image icone;
	private String description;
	private String nom;
	private boolean combat;
	private boolean rare;
	private ArrayList<String> effets;
	
	

	public Objet(String id, Image icone, String description, String nom, boolean combat,	boolean rare, ArrayList<String> effets) 
	{
		this.id = id;
		this.icone = icone;
		this.description = description;
		this.nom = nom;
		this.combat = combat;
		this.rare = rare;
		this.effets = effets;
	}

	public Image getIcone() throws SlickException
	{
		return icone;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getNom()
	{
		return nom;
	}
	
	public boolean utilisableCombat()
	{
		return combat;
	}
	
	public ArrayList<String> getEffets()
	{
		return effets;
	}

	public boolean estRare() 
	{
		return rare;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Objet)
		{
			return ((Objet)o).id.equals(id);
		}
		else
		{
			return false;
		}
	}
	
}
