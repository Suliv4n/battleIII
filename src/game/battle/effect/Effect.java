package game.battle.effect;

import game.battle.IBattle;
import game.system.application.Application;

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

	public void apply() {
		Application.application().debug(target.getName() + " take " + value + " damage" );
		type.apply(target, value);
	}
}
