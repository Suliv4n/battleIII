package game.battle.actions;

import java.util.ArrayList;
import java.util.HashMap;

import animation.AnimationFactory;
import animation.BattleAnimation;
import game.battle.IBattle;
import skill.Skill;

public class SkillAction extends Action{
	Skill skill;
	
	public SkillAction(IBattle caster, ArrayList<IBattle> targets, Skill skill)
	{
		super(caster, targets);
		this.skill = skill;
	}

	@Override
	public BattleAnimation getBattleAnimation() {
		return AnimationFactory.createAnimation("bouleDeFeu");
	}

	@Override
	public HashMap<IBattle, ArrayList<String>> getEffectsAction() {
		return null;
	}
}
