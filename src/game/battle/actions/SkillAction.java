package game.battle.actions;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;

import animation.AnimationFactory;
import animation.BattleAnimation;
import game.battle.IBattle;
import game.battle.effect.Effect;
import game.battle.effect.EffectType;
import game.system.Ratio;
import skill.Skill;
import util.Random;

public class SkillAction extends Action{
	Skill skill;
	
	public SkillAction(IBattle caster, ArrayList<IBattle> targets, Skill skill)
	{
		super(caster, targets);
		this.skill = skill;
	}

	@Override
	public BattleAnimation getBattleAnimation() {
		if(currentAnimation == null){
			currentAnimation = AnimationFactory.createAnimation(caster, targets);
		}
		return currentAnimation;
	}

	@Override
	public HashMap<IBattle, ArrayList<String>> getEffectsAction() {
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
	
	public void calculateEffects(){
		double power = skill.getPower();
		EffectType type = null;
		
		super.effects = new ArrayList<Effect>();
		
		for(IBattle target : targets){
			switch(skill.getType()){
				case(Skill.MAGIC):
					power *= Ratio.MAGIC_SKILL_POWER_RATIO;
					power += Ratio.MAGIC_SKILL_POWER_RATIO * caster.getMagicAttack();
					power -= Ratio.MAGIC_DEFENSE * target.getMagicDefense(); 
					power *= Random.rand(Ratio.MAGIC_RANDOM_MIN, Ratio.MAGIC_RANDOM_MAX);
					power = Math.max(1, power);
					type = EffectType.MAGIC_DAMAGE;
					break;
						
				case(Skill.PHYSIC):
					power *= Ratio.PHYSIC_SKILL_POWER_RATIO;
					power += Ratio.PHYSIC_SKILL_POWER_RATIO * caster.getPhysicAttack();
					power -= Ratio.PHYSIC_DEFENSE * target.getPhysicDefense(); 
					power *= Random.rand(Ratio.PHYSIC_RANDOM_MIN, Ratio.PHYSIC_RANDOM_MAX);
					power = Math.max(1, power);
					type = EffectType.PHYSIQUE_DAMAGE;
					break;	
			}
			
			Effect e = new Effect(target, type);
			e.setValue((int)(Math.floor(power)));
			super.effects.add(e);
		}
	}
}
