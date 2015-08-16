package game.battle.actions;

import game.battle.IBattle;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;

import animation.AnimationFactory;
import animation.BattleAnimation;

public class AttackAction extends Action{

	public AttackAction(IBattle caster, ArrayList<IBattle> targets) {
		super(caster, targets);
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