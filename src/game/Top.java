package game;

import game.system.Configurations;
import game.system.KeyboardControlsConfiguration;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;




public abstract class Top  extends BasicGameState
{
	private boolean console = false;
	private boolean directionKeyPressedOrDown = false;
	
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
		directionKeyPressedOrDown = false;
		if(in.isKeyPressed(Input.KEY_0)){
			toggleConsole();
		}
		if(console && Configurations.DEBUG){
			Launcher.console().setFocus(true);
			if(in.isKeyPressed(Input.KEY_ENTER)){
				Launcher.console().validate();
			}
			in.clearKeyPressedRecord();
		}
		if(in.isKeyPressed(KeyboardControlsConfiguration.VALIDATE_KEY)){
			onValidate();
		}
		if(in.isKeyPressed(KeyboardControlsConfiguration.BACK_KEY)){
			onBack();
		}
		//KEY PRESSED
		if(in.isKeyPressed(KeyboardControlsConfiguration.DOWN_KEY)){
			onDown();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfiguration.UP_KEY)){
			onUp();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfiguration.LEFT_KEY)){
			onLeft();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfiguration.RIGHT_KEY)){
			onRight();
			directionKeyPressedOrDown = true;
		}
		//KEY DOWN
		if(in.isKeyDown(KeyboardControlsConfiguration.DOWN_KEY)){
			onHoldDown();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfiguration.UP_KEY)){
			onHoldUp();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfiguration.LEFT_KEY)){
			onHoldLeft();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfiguration.RIGHT_KEY)){
			onHoldRight();
			directionKeyPressedOrDown = true;
		}
		if(!directionKeyPressedOrDown){
			onNoDirectionKeyPressedOrDown();
		}
	}
	
	/**
	 * Affiche ou cache la console de débuggage.
	 */
	private void toggleConsole(){
		console = !console;
		Launcher.console().setFocus(console);
	}

	//----------------------------------------------------------------
	//----------------------------EVENTS------------------------------
	//----------------------------------------------------------------
	
	
	/**
	 * Evénement "validate"
	 */
	public void onValidate(){}
	/**
	 * Evénement "back"
	 */
	public void onBack(){}
	/**
	 * Evénement "left"
	 */
	public void onLeft(){}
	/**
	 * Evénement "right"
	 */
	public void onRight(){}
	/**
	 * Evénement "up"
	 */
	public void onUp(){}
	/**
	 * Evénement "down"
	 */
	public void onDown(){}
	/**
	 * Evénement "hold left"
	 */
	public void onHoldLeft(){}
	/**
	 * Evénement "hold right"
	 */
	public void onHoldRight(){}
	/**
	 * Evénement "hold up"
	 */
	public void onHoldUp(){}
	/**
	 * Evénement "hold down"
	 */
	public void onHoldDown(){}
	/**
	 * Pas de direction.
	 */
	public void onNoDirectionKeyPressedOrDown(){}
}
