package personnage;


import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import sac.objet.stuff.Arme;
import sac.objet.stuff.Armure;


public class Mage extends Personnage 
{
	private int mana;
	
	public Mage(String nom) 
	{
		super(nom);		
		stats.put("endurance",10);
		stats.put("force",5);
		stats.put("dexterite",10);
		stats.put("intelligence",20);
		stats.put("sagesse",15);
		
		statsUP.put("endurance",3);
		statsUP.put("force",1);
		statsUP.put("dexterite",2);
		statsUP.put("intelligence",5);
		statsUP.put("sagesse",4);
		
		pv=stats.get("endurance")*5-10;
		
		typesArmesPrincipales.add(Arme.BATON);
		
		typesArmures.add(Armure.TISSU);
		
		mana = stats.get("intelligence")*5;
		
		try 
		{
			sprites = new SpriteSheet("ressources/spritesheet/mage.png", 32,32,new Color(255,0,255));
			for(int i = 0; i<5; i++)
			{
				animations[i] = new Animation(sprites, 0, i, 2,i ,true, 100, true);
			}
		} catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public int getEnergie() 
	{
		return mana;
	}

	@Override
	public int getEnergieMax()
	{
		return getStatMax("intelligence")*5 + getBonusEnergie();
	}
	



	@Override
	public void afficherGestionPerso(Graphics g, int x, int y, boolean selectionne) 
	{
		super.afficherGestionPerso(g, x, y, selectionne);
		super.afficherGestionPerso(g, x, y, selectionne);
		
		g.setColor(new Color(255,255,255));
		g.drawString("Mana", x+54, y+86);
		
		g.setColor(couleurEnergie());
		g.fillRect(x+96, y+91, mana/getEnergieMax()*149, 9);
	}
	
	@Override
	public Color couleurEnergie()
	{
		return new Color(0,100,150);
	}

	@Override
	public String getLibelleEnergie() {
		return "Mana";
	}
	
}

