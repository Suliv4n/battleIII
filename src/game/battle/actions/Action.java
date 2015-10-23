package game.battle.actions;

import game.battle.IBattle;
import game.battle.effect.Effect;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import animation.BattleAnimation;
import animation.BattleEffectsDisplayer;

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
	
	protected BattleEffectsDisplayer effectsDisplayer;
	
	protected ArrayList<Effect> effects;
	private boolean effectsCalculated = false;
	
	public Action(IBattle caster, ArrayList<IBattle> targets)
	{
		this.caster = caster;
		this.targets = targets;
		
		this.effects = new ArrayList<Effect>();
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
	public abstract ArrayList<Effect> getEffectsAction();

	/**
	 * Modifie les cibles.
	 * 
	 * @param targets
	 * 		Nouvelles cibles de l'action.
	 */
	public void setTargets(ArrayList<IBattle> targets) {
		this.targets = targets;
	}
	
	public BattleEffectsDisplayer getEffectsAnimation(){
		
		if(effectsDisplayer == null){
			effectsDisplayer = new BattleEffectsDisplayer(effects);		
		}
		return effectsDisplayer;
	}
	
	public abstract void render() throws SlickException;
	public abstract void update(int delta);

	public abstract boolean isRenderFisnished();
	public abstract void calculateEffects();
	
	public final void calculateEffectsOnce(){
		if(!effectsCalculated){
			calculateEffects();
		}
		effectsCalculated = true;
	};

	public final void applyEffects() {
		for(Effect e : effects){
			e.apply();
		}
	}

	public IBattle getCaster() {
		return caster;
	}

}
