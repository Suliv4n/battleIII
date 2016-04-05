package game.battle.actions;

import game.battle.IBattle;
import game.battle.effect.Effect;
import game.battle.effect.EffectType;
import game.system.Ratio;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;

import util.Random;
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
		
		for(IBattle target : targets){
			Effect e = new Effect(target, EffectType.PHYSIC_DAMAGE);
			
			int power = Ratio.ATTACK_ACTION_BASE;
			power *= Ratio.PHYSIC_SKILL_POWER_RATIO;
			power += Ratio.PHYSIC_SKILL_POWER_RATIO * caster.getPhysicAttack();
			power -= Ratio.PHYSIC_DEFENSE * target.getPhysicDefense(); 
			power *= Random.rand(Ratio.PHYSIC_RANDOM_MIN, Ratio.PHYSIC_RANDOM_MAX);
			power = Math.max(1, power);
			
			e.setValue(power);
			
			super.effects.add(e);
		}
	}

}
