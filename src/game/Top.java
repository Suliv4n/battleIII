package game;

import java.util.Hashtable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;




public abstract class Top  extends BasicGameState
{
	private boolean console = false;
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException 
	{
		if(console){
			Launcher.console().render();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame arg1, int arg2)
			throws SlickException 
	{
		Input in = container.getInput();
		if(in.isKeyPressed(Input.KEY_0)){
			toggleConsole();
		}
		if(console){
			Launcher.console().setFocus(true);
			if(in.isKeyPressed(Input.KEY_ENTER)){
				Launcher.console().validate();
			}
			in.clearKeyPressedRecord();
		}
	}
	
	private void toggleConsole(){
		console = !console;
		Launcher.console().setFocus(console);
	}


}
