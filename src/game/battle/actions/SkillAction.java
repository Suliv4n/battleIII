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
}
