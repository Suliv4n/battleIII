package game.scene;

import game.system.application.Application;

import java.util.Random;

import org.newdawn.slick.state.BasicGameState;

public abstract class Scene extends BasicGameState
{

	
	private int existStateId;
	
	@Override
	public int getID() 
	{
		Random random = new Random(this.getClass().hashCode());
		
		return random.nextInt(Integer.MAX_VALUE);
	}

	public final void setExitStateId(int id) {
		existStateId = id;
	}
	
	public final void finish(){
		onFinish();
		Application.application().getGame().enterState(existStateId);
	}

	public void onFinish(){}
}
