package personnage;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import sac.objet.stuff.*;


public class Guerrier extends Personnage 
{

	private int furie = 100;
	
	public Guerrier(String nom)
	{
		super(nom);		
		stats.put("endurance",20);
		stats.put("force",20);
		stats.put("dexterite",10);
		stats.put("intelligence",5);
		stats.put("sagesse",5);
		
		statsUP.put("endurance",5);
		statsUP.put("force",5);
		statsUP.put("dexterite",2);
		statsUP.put("intelligence",1);
		statsUP.put("sagesse",1);
		
		pv=stats.get("endurance")*5;
		
		typesArmesPrincipales.add(Arme.EPEE);
		typesArmesPrincipales.add(Arme.HACHE);
		typesArmesPrincipales.add(Arme.MARTEAU);

		typesArmesSecondaires.add(Arme.BOUCLIER);
		
		typesArmures.add(Armure.MAILLE);
		typesArmures.add(Armure.CUIR);
		

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
	public int getEnergieMax() 
	{
		return 100;
	}

	public int getEnergie() 
	{
		return furie;
	}
	
	@Override
	public void afficherGestionPerso(Graphics g, int x, int y, boolean selectionne) 
	{
		super.afficherGestionPerso(g, x, y, selectionne);
		
		g.setColor(new Color(255,255,255));
		g.drawString("Furie", x+47, y+86);
		
		g.setColor(couleurEnergie());
		g.fillRect(x+96, y+91, furie/getEnergieMax()*149, 9);
		
		
	}

	public Color couleurEnergie()
	{
		return new Color(150,50,0);
	}


	@Override
	public String getLibelleEnergie() {
		return "Furie";
	}

}
