package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

public class _TransitionCombat implements Transition
{

	//#region ----------PROPRIETES---------
	private int i=0;
	private int j=0;
	//#endregion
	
	//#region ---OVERRIDE BASICSTATEGAME---
	@Override
	public void init(GameState firstState, GameState secondState) 
	{
		
	}

	@Override
	public boolean isComplete() 
	{
		return i == 9 && j==7;
	}

	@Override
	public void postRender(StateBasedGame game, GameContainer container,
			Graphics g) throws SlickException 
	{
		g.setColor(Color.black);
		for(int cpt=0; cpt<j ; cpt++)		
			g.fillRect(0, 64*cpt, 640, 64);
		g.fillRect(0, j*64, 64*i, 64);
	}

	@Override
	public void preRender(StateBasedGame game, GameContainer container,
			Graphics g) throws SlickException 
	{
		
	}

	@Override
	public void update(StateBasedGame game, GameContainer container, int delta)
			throws SlickException
	{
		i++;
		if(i%20==0)
		{
			i=0;
			j++;
		}
		container.getInput().clearKeyPressedRecord();
	}
	
	//#endregion

}
