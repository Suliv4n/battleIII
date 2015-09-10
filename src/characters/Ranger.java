package characters;


import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import characters.skin.Skin;
import data.DataManager;
import bag.item.stuff.Weapon;
import bag.item.stuff.Armor;


/**
 * Personnage Ranger.
 * 
 * @author Darklev
 *
 */
public class Ranger extends Character 
{

	int concentration = 100;
	
	/**
	 * Constructeur
	 * 
	 * @param name
	 * 		Nom du ranger.
	 * @throws SlickException 
	 */
	public Ranger(String name) throws SlickException 
	{
		super(name);		
		statistics.put("endurance",15);
		statistics.put("force",15);
		statistics.put("dexterite",20);
		statistics.put("intelligence",5);
		statistics.put("sagesse",5);
		
		statisticsUP.put("endurance",4);
		statisticsUP.put("force",4);
		statisticsUP.put("dexterite",5);
		statisticsUP.put("intelligence",1);
		statisticsUP.put("sagesse",1);
		
		healthPoints=statistics.get("endurance")*5;
		
		mainWeaponTypes.add(Weapon.SWORD);
		secondWeaponTypes.add(Weapon.BOW);
		
		armorTypes.add(Armor.FABRIC);
		
		skin = DataManager.loadSkin("2");

	}

	
	@Override
	public int getEnergy() 
	{
		return concentration;
	}

	@Override
	public int getMaximumEnergy() 
	{
		return 100;
	}
	
	@Override
	public void renderCharacterPanel(Graphics g, int x, int y, boolean selectionne) 
	{
		super.renderCharacterPanel(g, x, y, selectionne);
		
		g.setColor(new Color(255,255,255));
		g.drawString("Conc.", x+50, y+86);
		
		g.setColor(energyColor());
		g.fillRect(x+96, y+91, concentration/getMaximumEnergy()*149, 9);
		
	}
	
	@Override
	public Color energyColor()
	{
		return new Color(0,150,0);
	}


	@Override
	public String getEnergyLabel() {
		return "Conc.";
	}
}
