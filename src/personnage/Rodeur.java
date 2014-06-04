package personnage;


import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import sac.objet.stuff.Arme;
import sac.objet.stuff.Armure;


public class Rodeur extends Personnage 
{

	int concentration = 100;
	
	public Rodeur(String nom) 
	{
		super(nom);		
		stats.put("endurance",15);
		stats.put("force",15);
		stats.put("dexterite",20);
		stats.put("intelligence",5);
		stats.put("sagesse",5);
		
		statsUP.put("endurance",4);
		statsUP.put("force",4);
		statsUP.put("dexterite",5);
		statsUP.put("intelligence",1);
		statsUP.put("sagesse",1);
		
		pv=stats.get("endurance")*5;
		
		typesArmesPrincipales.add(Arme.EPEE);
		typesArmesSecondaires.add(Arme.ARC);
		
		typesArmures.add(Armure.TISSU);
		
		try 
		{
			sprites = new SpriteSheet("ressources/spritesheet/rodeur.png", 32,32,new Color(255,0,255));
			for(int i = 0; i<5; i++)
			{
				animations[i] = new Animation(sprites, 0, i, 2,i ,true, 100, true);
			}
			
		} 
		
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		

	}

	
	@Override
	public int getEnergie() 
	{
		return concentration;
	}

	@Override
	public int getEnergieMax() 
	{
		return 100;
	}
	
	@Override
	public void afficherGestionPerso(Graphics g, int x, int y, boolean selectionne) 
	{
		super.afficherGestionPerso(g, x, y, selectionne);
		
		g.setColor(new Color(255,255,255));
		g.drawString("Conc.", x+50, y+86);
		
		g.setColor(couleurEnergie());
		g.fillRect(x+96, y+91, concentration/getEnergieMax()*149, 9);
		
	}
	
	@Override
	public Color couleurEnergie()
	{
		return new Color(0,150,0);
	}
}
