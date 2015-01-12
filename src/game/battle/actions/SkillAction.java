package game.battle.actions;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;

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
		if(currentAnimation == null){
			currentAnimation = AnimationFactory.createAnimation("bouleDeFeu");
		}
		return currentAnimation;
	}

	@Override
	public HashMap<IBattle, ArrayList<String>> getEffectsAction() {
		return null;
	}
	
	@Override
	public boolean render() throws SlickException{
		return getBattleAnimation().render(caster, targets);
	}

	@Override
	public void update() {
		
	}
}
