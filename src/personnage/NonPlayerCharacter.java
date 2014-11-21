package personnage;

import game.dialogue.Dialogue;

import org.newdawn.slick.Animation;

/**
 * Classe représentant un NPC.
 * 
 * @author Darklev
 *
 */
public class NonPlayerCharacter 
{
	private String id;
	private String name;
	private Animation[] animations;
	
	private int x;
	private int y;
	
	private int direction;
	
	private Dialogue dialogue;
	
	private boolean moving = false;
	
	/**
	 * Constructeur
	 * 
	 * @param id
	 * 		Id du NPC
	 * @param name
	 * 		Nom du NPC
	 * @param animations
	 * 		Animations du NPC.
	 */
	public NonPlayerCharacter(String id, String name, Animation[] animations)
	{
		this.id = id;
		this.name = name;
		this.animations = animations;
	}
	
	/**
	 * Modifie l'absisse du NPC.
	 * 
	 * @param x
	 * 		Nouvelle abscisse.
	 * 
	 * @return
	 * 		Cette instance.
	 */
	public NonPlayerCharacter setX(int x)
	{
		this.x = x;
		return this;
	}
	
	/**
	 * Modifie l'ordonnée du NPC.
	 * 
	 * @param x
	 * 		Nouvelle ordonnée.
	 * 
	 * @return
	 * 		Cette instance.
	 */
	public NonPlayerCharacter setY(int y)
	{
		this.y = y;
		return this;
	}
	
	/**
	 * Retourne l'absisse du NPC.
	 * 
	 * @return l'abscisse du NPC.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Retourne l'ordonnée du NPC.
	 * 
	 * @return l'ordonnée du NPC.
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Retourne vrai se le NPC est en mouvement sinon retourne faux.
	 * 
	 * @return vrai si le NPC est en mouvement sinon faux.
	 */
	public boolean isMoving()
	{
		return moving;
	}
	
	/**
	 * Modifie la direction dans laquelle regarde le NPC.
	 * @param direction
	 * 		Nouvelle direction.
	 * @return
	 * 		Cette instance.
	 */
	public NonPlayerCharacter setPosition(int direction)
	{
		this.direction = direction;
		return this;
	}
	
	/**
	 * Modifie le dialogue du personnage.
	 * 
	 * @param dialogue
	 * 		Nouveau dialogue.
	 * @return
	 * 		Cette instance.
	 */
	public NonPlayerCharacter setDialogue(Dialogue dialogue)
	{
		this.dialogue = dialogue;
		return this;
	}
	
	/**
	 * Retourne l'animation du NPC.
	 * 
	 * @return l'animation du NPC.
	 */
	public Animation getAnimation()
	{
		return  animations[direction];
	}
	
	/**
	 * Retourne le dialogue du NPC.
	 * 
	 * @return le dialogue du NPC.
	 */
	public Dialogue getDialogue()
	{
		return  dialogue.clone();
	}
}
