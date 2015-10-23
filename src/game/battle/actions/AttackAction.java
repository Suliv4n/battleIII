package game.battle.actions;

import game.battle.IBattle;
import game.battle.effect.Effect;
import game.battle.effect.EffectType;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;

import animation.AnimationFactory;
import animation.BattleAnimation;
import animation.action.NormalAttackAnimation;

public class AttackAction extends Action{

	public AttackAction(IBattle caster, ArrayList<IBattle> targets) {
		super(caster, targets);
	}

	@Override
	public BattleAnimation getBattleAnimation() {
		if(currentAnimation == null){
			currentAnimation = new NormalAttackAnimation(caster, targets);
		}
		return currentAnimation;
	}

	@Override
	public ArrayList<Effect> getEffectsAction() {
		return null;
	}

	@Override
	public void render() throws SlickException{
		getBattleAnimation().render();
	}

	@Override
	public void update(int delta) {
		getBattleAnimation().update(delta);
	}

	@Override
	public boolean isRenderFisnished() {
		return getBattleAnimation().isFinished();
	}

	@Override
	public void calculateEffects() {
		Effect e = new Effect(targets.get(0), EffectType.PHYSIC_DAMAGE);
		e.setValue(50);
		super.effects.add(e);
	}

}
