package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOver extends BasicGameState
{

	//#region ---------OVERRIDE BASICGAMESTATE--------
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException 
	{
		
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
			throws SlickException 
	{
		g.drawString("Game Over", 0, 0);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException
	{
		
	}

	@Override
	public int getID() 
	{
		return Config.GAME_OVER;
	}
	//#endregion
}
