package game.scene;

import game.system.GameStep;
import game.system.application.Application;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Introduction extends Scene{

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		Application.application().drawStringWithoutGameFont("Introduction", 10, 10, Color.white);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(container.getInput().isKeyDown(Input.KEY_RETURN)){
			finish();
		}
	}
	
	@Override
	public void onFinish(){
		Application.application().getGame().getParty().getGameStep().addStep(GameStep.SEE_INTRO);
	}

}
