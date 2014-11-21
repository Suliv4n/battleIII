package bag.item;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import bag.IItems;



/**
 * Classe représentant un objet.
 * 
 * @author Darklev
 *
 */
public class Item implements IItems
{
	private String id;
	private Image icon;
	private String description;
	private String name;
	private boolean usableInBattle;
	private boolean rare;
	private ArrayList<String> effects;
	
	
	/**
	 * Constructeur
	 * 
	 * @param id
	 * 		Id de l'item.
	 * @param icon
	 * 		Icone de l'item.
	 * @param description
	 * 		DEscription de l'item.
	 * @param name
	 * 		Nom de l'item.
	 * @param usableInBattle
	 * 		Vrai si l'objet est utilisable en combat, sinon faux.
	 * @param rare
	 * 		Vrai si l'objet ne peut pas être supprimé.
	 * @param effects
	 * 		Effets de l'objet.
	 */
	public Item(String id, Image icon, String description, String name, boolean usableInBattle,	boolean rare, ArrayList<String> effects) 
	{
		this.id = id;
		this.icon = icon;
		this.description = description;
		this.name = name;
		this.usableInBattle = usableInBattle;
		this.rare = rare;
		this.effects = effects;
	}

	@Override
	public Image getIcon() throws SlickException
	{
		return icon;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public boolean isUsableInBattle()
	{
		return usableInBattle;
	}
	
	@Override
	public ArrayList<String> getEffects()
	{
		return effects;
	}
	
	@Override
	public boolean isRare() 
	{
		return rare;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Item)
		{
			return ((Item)o).id.equals(id);
		}
		else
		{
			return false;
		}
	}
	
}
