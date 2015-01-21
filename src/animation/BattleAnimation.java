package animation;

import game.battle.IBattle;

import java.util.ArrayList;

/**
 * Animation pour les combats.
 * 
 * @author Darklev
 *
 */
public abstract class BattleAnimation 
{
	private IBattle caster;
	private ArrayList<IBattle> targets;
	
	/**
	 * Les instances d'Animation sont créées à partir de 
	 * la classe AnimationFactory.
	 */
	public BattleAnimation(IBattle caster, ArrayList<IBattle> targets)
	{
		this.caster = caster;
		this.targets = targets;
		
		init();
	}
	
	/**
	 * Retourne les cibles de l'animation.
	 * 
	 * @return les cibles de l'animation.
	 */
	public ArrayList<IBattle> getTargets(){
		return targets;
	}
	
	/**
	 * Retourne le lanceur de l'animation.
	 * 
	 * @return le lanceur de l'animation
	 */
	public IBattle getCaster(){
		return caster;
	}
	
	/**
	 * Affiche l'animation (affichage et son) en cours de l'Animation 
	 */
	public abstract void render();
	
	/**
	 * Met à jour l'animation
	 * 
	 * @param delta
	 * 		Temps avant la dernière mise à jour en millisecondes.
	 */
	public abstract void update(int delta);
	
	/**
	 * Initialisation
	 */
	public abstract void init();
	
	/**
	 * Retourne vrai si l'animation est finie. Sinon retourne faux.
	 * 
	 * @return vrai si l'animation est finie. Sinon faux.
	 */
	public abstract boolean isFinished();
}
