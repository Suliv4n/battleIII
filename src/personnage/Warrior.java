package personnage;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import bag.item.stuff.*;


/**
 * 
 * Personnage guerrier.
 * 
 * @author Darklev
 *
 */
public class Warrior extends Character 
{

	private int furie = 100;
	
	/**
	 * Constructeur
	 * 
	 * @param name
	 * 		Nom du guerrier.
	 */
	public Warrior(String name)
	{
		super(name);		
		statistics.put("endurance",20);
		statistics.put("force",20);
		statistics.put("dexterite",10);
		statistics.put("intelligence",5);
		statistics.put("sagesse",5);
		
		statisticsUP.put("endurance",5);
		statisticsUP.put("force",5);
		statisticsUP.put("dexterite",2);
		statisticsUP.put("intelligence",1);
		statisticsUP.put("sagesse",1);
		
		healthPoints=statistics.get("endurance")*5;
		
		mainWeaponTypes.add(Weapon.SWORD);
		mainWeaponTypes.add(Weapon.AXE);
		mainWeaponTypes.add(Weapon.HAMMER);

		secondWeaponTypes.add(Weapon.SHIELD);
		
		armorTypes.add(Armor.CHAINMAIL);
		armorTypes.add(Armor.LEATHER);
		

		try {
			sprites = new SpriteSheet("ressources/spritesheet/guerrier.png", 32,32,new Color(255,0,255));
		} catch (SlickException e) {
			e.printStackTrace();
		}
		for(int i = 0; i<5; i++)
		{
			animations[i] = new Animation(sprites, 0, i, 2,i ,true, 100, true);
		}

	}	
	
	
	@Override
	public int getMaximumEnergy() 
	{
		return 100;
	}

	public int getEnergy() 
	{
		return furie;
	}
	
	@Override
	public void renderCharacterPanel(Graphics g, int x, int y, boolean selectionne) 
	{
		super.renderCharacterPanel(g, x, y, selectionne);
		
		g.setColor(new Color(255,255,255));
		g.drawString("Furie", x+47, y+86);
		
		g.setColor(energyColor());
		g.fillRect(x+96, y+91, furie/getMaximumEnergy()*149, 9);
		
		
	}

	public Color energyColor()
	{
		return new Color(150,50,0);
	}


	@Override
	public String getEnergyLabel() {
		return "Furie";
	}

}
