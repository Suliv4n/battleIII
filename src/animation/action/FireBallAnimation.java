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

public class FireBallAnimation extends BattleAnimation{

	private ParticleSystem fireBall;
	private BattleWithATB battle;
	private double angle;
	private double xDistance;
	private double yDistance;
	private double speed = 10;
	private double x;
	private double y;
	
	public FireBallAnimation(IBattle caster, ArrayList<IBattle> targets) {
		super(caster, targets);
	}

	@Override
	public void render(){
		fireBall.render((float)x, (float)y);
	}

	@Override
	public void update(int delta){
		x += Math.cos(angle) * speed;
		y += Math.sin(angle) * speed;
		fireBall.update(delta);
	}

	@Override
	public void init() {
		int factorSize = 50;
		
		IBattle target = getTargets().get(0);
		IBattle caster = getCaster();
		battle = Application.application().getGame().getCurrentBattle();
		x = battle.getCoords(caster).getX();
		y = battle.getCoords(caster).getY();
		xDistance = battle.getCoords(target).getX() - battle.getCoords(caster).getX();
		yDistance = battle.getCoords(target).getY() - battle.getCoords(caster).getY();
		
		angle = Math.atan2(yDistance, xDistance);
		fireBall = Application.application().loadParticleSystem("fire", "particles/particle.png");
		((ConfigurableEmitter)fireBall.getEmitter(0)).windFactor.setValue((float) -(factorSize * Math.cos(angle)));
		((ConfigurableEmitter)fireBall.getEmitter(0)).gravityFactor.setValue((float) -(factorSize * Math.sin(angle)));
	}

	@Override
	public boolean isFinished() {
		return (  Math.abs(x - battle.getCoords(getCaster()).getX()) >= Math.abs(xDistance)
			&&  Math.abs(y - battle.getCoords(getCaster()).getY()) >= Math.abs(yDistance) ) ;
	}

}
