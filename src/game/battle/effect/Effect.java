package game.battle.effect;

import game.battle.IBattle;

public class Effect {
	
	private IBattle target;
	private EffectType type;
	private int value = 1;
	
	public Effect(IBattle target, EffectType type){
		this.target = target;
		this.type = type;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	
	
	
	
	public static void takeDamage(IBattle target, int value){
		target.updateHealthPoints(value);
	}
}
