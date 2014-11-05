package personnage;

import java.util.Timer;

public class ATBManager {
	private Timer atb;
	private ATB onTick;
	private long speed;
	
	public ATBManager(long speed){
		onTick = new ATB();
		atb = new Timer();
		this.speed = speed;
		//atb.schedule(onTick, speed);
	}
	
	/**
	 * Met en pause l'ATB.
	 */
	public void pause(){
		onTick.pause(true);
	}
	
	/**
	 * Rejoue l'atb
	 */
	public void resume(){
		onTick.pause(false);
	}
	
	public int getCurrent(){
		return onTick.getCurrent();
	}
	
	public void launch(){
		atb.schedule(onTick,0,this.speed);
	}
	
	public void stop(){
		atb.cancel();
	}
}
