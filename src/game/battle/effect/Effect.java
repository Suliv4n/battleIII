package game.battle.effect;

import org.newdawn.slick.SlickException;

import animation.BattleEffectRenderer;
import animation.effect.UpdateHealthRenderer;
import game.battle.IBattle;
import game.system.application.Application;

public class Effect {
	
	private IBattle target;
	private EffectType type;
	private int value = 1;
	
	private BattleEffectRenderer renderer;
	
	public Effect(IBattle target, EffectType type){
		this.target = target;
		this.type = type;
	}
	
	
	public IBattle getTarget(){
		return target;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}

	public void apply() {
		type.apply(target, value);
	}
	
	public BattleEffectRenderer getRenderer() throws SlickException{
		if(renderer == null){
			dispatchRenderer();
		}
		return renderer;
	}
	
	private void dispatchRenderer() throws SlickException{
		switch(type){
			case HEAL:
			case MAGIC_DAMAGE:
			case PHYSIC_DAMAGE:
				renderer = new UpdateHealthRenderer(this);
				break;
			default:
				throw new SlickException("Cannot dispatch renderer for type "+type.name());
		}
	}
}
