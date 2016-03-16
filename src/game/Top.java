package game;

import game.settings.Settings;
import game.system.Configurations;
import game.system.KeyboardControlsConfigurations;
import game.system.SystemInformations;
import game.system.application.Application;

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
	private static boolean showSystemInfo = false;
	private boolean directionKeyPressedOrDown = false;
	private boolean logger = false;
	private boolean showHitboxes = false;
	
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
		if(logger){
			Application.application().logger().render();
		}
		if(showHitboxes && Application.application().getGame().getCurrentStateID() == StatesId.EXPLORATION){
			Application.application().getGame().getParty().getMap().drawHitbox(new Color(200,0,0,0.5f));
			Application.application().getGame().getParty().drawHitbox(new Color(0,200,100,0.5f));
		}
	}

	
	@Override
	public void update(GameContainer container, StateBasedGame arg1, int delta)
			throws SlickException 
	{
		Input in = container.getInput();

		
		if(Settings.MULTIPLAYER && Application.application().getGame().getCurrentSave() != null){
			Application.application().getConnector().sendInformationsToServer();
		}
		
		directionKeyPressedOrDown = false;
		
		if(Configurations.DEBUG){
			SystemInformations.getInstance().log(delta);
		}
		
		if(in.isKeyPressed(Input.KEY_F1) && Configurations.DEBUG){
			toggleConsole();
		}
		if(in.isKeyPressed(Input.KEY_F2) && Configurations.DEBUG){
			showSystemInfo = !showSystemInfo;
		}
		if(in.isKeyPressed(Input.KEY_F3) && Configurations.DEBUG){
			toggleLogger();
		}
		if(in.isKeyPressed(Input.KEY_F4) && Configurations.DEBUG){
			showHitboxes = !showHitboxes;
		}
		
		if(console && Configurations.DEBUG){
			Application.application().console().setFocus(true);
			if(in.isKeyPressed(Input.KEY_ENTER)){
				Application.application().console().validate();
			}
			in.clearKeyPressedRecord();
		}
		if(logger){
			Application.application().logger().setFocus(true);
			Application.application().logger().filter();
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
		if(in.isKeyPressed(KeyboardControlsConfigurations.DOWN_KEY) || ControllerInput.isControllerDownPressed(0, in)){
			onDown();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.UP_KEY) || ControllerInput.isControllerUpPressed(0, in)){
			onUp();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.LEFT_KEY) || ControllerInput.isControllerLeftPressed(0, in)){
			onLeft();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.RIGHT_KEY) || ControllerInput.isControllerDownPressed(0, in)){
			onRight();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.START_KEY)){
			onStart();
		}
		//KEY DOWN
		if((in.isKeyDown(KeyboardControlsConfigurations.DOWN_KEY) || ControllerInput.isControllerDownDown(0, in)) && !console){
			onHoldDown();
			directionKeyPressedOrDown = true;
		}
		if((in.isKeyDown(KeyboardControlsConfigurations.UP_KEY) || ControllerInput.isControllerUpDown(0, in)) && !console){
			onHoldUp();
			directionKeyPressedOrDown = true;
		}
		if((in.isKeyDown(KeyboardControlsConfigurations.LEFT_KEY) || ControllerInput.isControllerLeftDown(0, in)) && !console){
			onHoldLeft();
			directionKeyPressedOrDown = true;
		}
		if((in.isKeyDown(KeyboardControlsConfigurations.RIGHT_KEY) || ControllerInput.isControllerRightDown(0, in)) && !console){
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
		logger = false;
		Application.application().console().setFocus(console);
	}

	/**
	 * Affiche ou cache la console de logs.
	 */
	private void toggleLogger(){
		logger = !logger;
		console = false;
		Application.application().logger().setFocus(logger);
	}
	
	//----------------------------------------------------------------
	//----------------------------EVENTS------------------------------
	//----------------------------------------------------------------
	
	public final void controllerButtonPressed(int controller, int button){

		switch(button){
		case(ControllerInput.START):
			onStart();
			break;
		case(ControllerInput.VALIDATE):
			onValidate();
			break;
		case(ControllerInput.BACK):
			onBack();
			break;
		}
		
		/*if(Configurations.DEBUG){
			System.out.println(button);
		}*/
	}
	
	
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
