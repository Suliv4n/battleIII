package game.scene;

import org.newdawn.slick.state.BasicGameState;

public abstract class Scene extends BasicGameState
{

	@Override
	public int getID() 
	{
		try {
			return SceneId.class.getField(this.getClass().getName()).getInt(SceneId.class);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
