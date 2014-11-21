package personnage;

import java.util.TimerTask;

/**
 * L'ATB détermine quand un ennemi ou un personnage peut effectuer une action
 * durant le combat.
 * 
 * @author Darklev
 *
 */
public class ActiveTimeBattle  extends TimerTask{
	
	private boolean pause = false;
	private int current = 0;
	
	/**
	 * Crée une instance d'ActiveTimeBattle.
	 */
	public ActiveTimeBattle(){
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
	
	/**
	 * Met en pause l'ATB si pause vout true, sinon continue l'ATB.
	 * @param pause
	 * 		Vrai si l'ATB doit être en pause, faut sinon.
	 */
	public void pause(boolean pause){
		this.pause = pause;
	}
	
	
	/**
	 * Retourne une valeur entre 0 et 100 qui détermine l'avancée de l'ATB.
	 * A 100, l'ATB est au maximum.
	 * 
	 * @return l'avancée de l'ATB.
	 */
	public int getCurrent(){
		return current;
	}
}
