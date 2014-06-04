package personnage;

import game.dialogue.Dialogue;

import org.newdawn.slick.Animation;

public class PNJ 
{
	private String id;
	private String nom;
	private Animation[] animations;
	
	private int x;
	private int y;
	
	private int position;
	
	private Dialogue dialogue;
	
	private boolean mouvement = false;
	
	
	public PNJ(String id, String nom, Animation[] animations)
	{
		this.id = id;
		this.nom = nom;
		this.animations = animations;
	}
	
	public PNJ setX(int x)
	{
		this.x = x;
		return this;
	}
	
	public PNJ setY(int y)
	{
		this.y = y;
		return this;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public boolean enMouvement()
	{
		return mouvement;
	}
	
	public PNJ setPosition(int position)
	{
		this.position = position;
		return this;
	}
	
	public PNJ setDialogue(Dialogue dialogue)
	{
		this.dialogue = dialogue;
		return this;
	}
	
	public Animation getAnimation()
	{
		return  animations[position];
	}
	
	public Dialogue getDialogue()
	{
		return  dialogue.clone();
	}
}
