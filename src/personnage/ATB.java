package personnage;

import java.util.TimerTask;

public class ATB  extends TimerTask{
	private boolean pause = false;
	private int current = 0;
	
	
	public ATB(){
		super();
	}

	@Override
	public void run() {
		if(!pause){
			if(current < 100){
				this.current++;
			}
		}
	}
	
	public void pause(boolean pause){
		this.pause = pause;
	}
	
	public int getCurrent(){
		return current;
	}
}
