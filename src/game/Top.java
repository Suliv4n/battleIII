package game;

import java.util.Hashtable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import sac.objet.stuff.Equipable;


public abstract class Top  extends BasicGameState
{
	
	private boolean debug = false;
	
	private Hashtable<String, Object> meta;
	
	
	public Top()
	{
		meta = new Hashtable<String, Object>();
	}
	
	public void ajouterMeta(String key, Object value)
	{
		meta.put(key, value);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException 
	{
		g.setColor(Color.white);
		if(debug)
		{
			Equipable test = (Equipable)meta.get("equipable");
			if(test != null)
			{
				g.drawString("Test arme équipé "  +  test +  " : " + Jeu.getEquipe().estEquipeDe(test),0,0);
				//g.drawString("Test null "  +  test ,0,0);
			}
		}
		
		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public void update(GameContainer container, StateBasedGame arg1, int arg2)
			throws SlickException 
	{
		if(container.getInput().isKeyPressed(Input.KEY_F3))
		{
			debug = !debug;
		}
	}


}
