package personnage;

import java.util.Timer;

/**
 * Gestionnaire d'ATB.
 * 
 * @author Darklev
 *
 */
public class ActiveTimeBattleManager {
	private Timer atb;
	private ActiveTimeBattle onTick;
	private long speed;
	
	public ActiveTimeBattleManager(long speed){
		onTick = new ActiveTimeBattle();
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
	
	/**
	 * Retourne l'avancée de l'ATB géré (100 = terminé).
	 * @return
	 * 	Retourne l'avancée de l'ATB géré.
	 */
	public int getCurrent(){
		return onTick.getCurrent();
	}
	
	/**
	 * Démarre l'atb.
	 * 
	 * @param start
	 * 		Avancement atb au démarrage.
	 */
	public void launch(int start){
		onTick.setCurrent(start);
		atb.schedule(onTick,0,this.speed);
	}
	
	/**
	 * Arrête l'ATB.
	 */
	public void stop(){
		atb.cancel();
	}
}
