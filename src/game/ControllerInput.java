package game;

import org.newdawn.slick.Input;

public class ControllerInput {
	public static final int START = 8;
	public static final int VALIDATE = 1;
	public static final int BACK = 2;
	
	private static boolean buttonDown = false;
	private static boolean buttonUp = false;
	private static boolean buttonLeft = false;
	private static boolean buttonRight = false;
	
	public static boolean isControllerDownPressed(int controller, Input in){
		boolean res = in.isControllerDown(controller) && !buttonDown;
		buttonDown = in.isControllerDown(controller);
		return res;
	}
	
	public static boolean isControllerUpPressed(int controller, Input in){
		boolean res = in.isControllerUp(controller) && !buttonUp;
		buttonUp = in.isControllerUp(controller);
		return res;
	}
	
	public static boolean isControllerLeftPressed(int controller, Input in){
		boolean res = in.isControllerLeft(controller) && !buttonLeft;
		buttonLeft =  in.isControllerLeft(controller);
		return res;
	}
	
	public static boolean isControllerRightPressed(int controller, Input in){
		boolean res = in.isControllerRight(controller) && !buttonRight;
		buttonRight = in.isControllerRight(controller);
		return res;
	}
	
	
	public static boolean isControllerDownDown(int controller, Input in){
		return in.isControllerDown(controller);
	}
	
	public static boolean isControllerUpDown(int controller, Input in){
		return in.isControllerUp(controller);
	}
	
	public static boolean isControllerLeftDown(int controller, Input in){
		return in.isControllerLeft(controller);
	}
	
	public static boolean isControllerRightDown(int controller, Input in){
		return in.isControllerRight(controller);
	}

}
