package game.battle.actions;

import game.battle.IBattle;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;

import animation.BattleAnimation;

/**
 * Représente une action lors d'un combat.
 *
 * @author Darklev
 *
 */
public abstract class Action {
	
	protected IBattle caster;
	protected ArrayList<IBattle> targets;
	protected BattleAnimation currentAnimation;
	
	public Action(IBattle caster, ArrayList<IBattle> targets)
	{
		this.caster = caster;
		this.targets = targets;
	}
	
	/**
	 * Retourne l'animation de l'action pour le combat. 
	 * 
	 * @return l'animation pour le combat.
	 */
	public abstract BattleAnimation getBattleAnimation();
	/**
	 * Retourne les effets de l'action.
	 * 
	 * @return les effets de l'action par caster/targets.
	 */
	public abstract HashMap<IBattle, ArrayList<String>> getEffectsAction();

	/**
	 * Modifie les cibles.
	 * 
	 * @param targets
	 * 		Nouvelles cibles de l'action.
	 */
	public void setTargets(ArrayList<IBattle> targets) {
		this.targets = targets;
	}
	
	public abstract boolean render() throws SlickException;
	public abstract void update();
}
