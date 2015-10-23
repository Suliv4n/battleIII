package animation;

import game.battle.effect.Effect;




public abstract class BattleEffectRenderer 
{
	private Effect effect;
	
	public BattleEffectRenderer(Effect effect){
		this.effect = effect;
	}
	
	public Effect getEffect(){
		return effect;
	}
	
	public abstract boolean isFinished();
	public abstract void render();
	public abstract void update(int delta);
	
	
}
