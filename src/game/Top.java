package game;

import java.util.ArrayList;

import game.system.Configurations;
import game.system.KeyboardControlsConfigurations;
import game.system.SystemInformations;
import game.system.application.Application;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;





public abstract class Top  extends BasicGameState
{
	private boolean console = false;
	private static boolean showSystemInfo = false;
	private boolean directionKeyPressedOrDown = false;
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException 
	{
		if(showSystemInfo){
			SystemInformations.getInstance().render();
		}
		if(console){
			Application.application().console().render();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame arg1, int delta)
			throws SlickException 
	{
		Input in = container.getInput();
		directionKeyPressedOrDown = false;
		
		if(Configurations.DEBUG){
			SystemInformations.getInstance().log(delta);
		}
		
		if(in.isKeyPressed(Input.KEY_0) && Configurations.DEBUG){
			toggleConsole();
		}
		if(in.isKeyPressed(Input.KEY_9) && Configurations.DEBUG){
			showSystemInfo = !showSystemInfo;
		}
		if(console && Configurations.DEBUG){
			Application.application().console().setFocus(true);
			if(in.isKeyPressed(Input.KEY_ENTER)){
				Application.application().console().validate();
			}
			in.clearKeyPressedRecord();
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.VALIDATE_KEY)){
			onValidate();
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.BACK_KEY)){
			onBack();
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.R_KEY)){
			onR();
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.L_KEY)){
			onL();
		}
		//KEY PRESSED
		if(in.isKeyPressed(KeyboardControlsConfigurations.DOWN_KEY)){
			onDown();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.UP_KEY)){
			onUp();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.LEFT_KEY)){
			onLeft();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.RIGHT_KEY)){
			onRight();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.START_KEY)){
			onStart();
		}
		//KEY DOWN
		if(in.isKeyDown(KeyboardControlsConfigurations.DOWN_KEY) && !console){
			onHoldDown();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfigurations.UP_KEY) && !console){
			onHoldUp();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfigurations.LEFT_KEY) && !console){
			onHoldLeft();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfigurations.RIGHT_KEY) && !console){
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
		Application.application().console().setFocus(console);
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
	/**
	 * Evénement "start"
	 */
	public void onStart() {}
	/**
	 * Evénement "R"
	 */
	public void onR() {}
	/**
	 * Evénement "L"
	 */
	public void onL() {}
}
