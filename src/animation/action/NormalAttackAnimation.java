package animation.action;

import game.BattleWithATB;
import game.battle.IBattle;

import game.system.ConsoleLine;
import game.system.application.Application;


import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.particles.ConfigurableEmitter;

import org.newdawn.slick.particles.ParticleSystem;

import animation.BattleAnimation;

public class NormalAttackAnimation extends BattleAnimation{

	private ParticleSystem particles;
	private BattleWithATB battle;
	private int x;
	private int y;
	private float time = 0.0f;
	private float duration;
	
	public NormalAttackAnimation(IBattle caster, ArrayList<IBattle> targets) {
		super(caster, targets);
		battle = Application.application().getGame().getCurrentBattle();
		x = (int) battle.getCoords(targets.get(0)).getX();
		y = (int) battle.getCoords(caster).getY();
		
	}

	@Override
	public void render(){
		particles.render((float)x, (float)y);
	}

	@Override
	public void update(int delta){
		particles.update(delta);
		time += delta;
	}

	@Override
	public void init() {
		particles = Application.application().loadParticleSystem("normal_attack", "particles/star.png");
		particles.setRemoveCompletedEmitters(true);
		duration = ((ConfigurableEmitter)particles.getEmitter(0)).length.getMax();
	}

	@Override
	public boolean isFinished() {
		return time >= duration;
	}

}
