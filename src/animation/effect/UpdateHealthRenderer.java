package animation.effect;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import game.battle.effect.Effect;
import game.system.Configurations;
import game.system.application.Application;
import animation.BattleEffectRenderer;

public class UpdateHealthRenderer extends BattleEffectRenderer{

	private static final int DELTA = 20;
	private static final int DURATION = 500;
	
	private int deltaHealth;
	private Color color;
	private Point coords;
	
	private int time = 0;
	
	public UpdateHealthRenderer(Effect effect) {
		super(effect);
		deltaHealth = effect.getValue();
		if(deltaHealth > 0){
			color = Configurations.DAMAGE_LABEL_COLOR;
		}
		else{
			color = Configurations.HEAL_LABEL_COLOR;
		}
		
		coords = Application.application().getGame().getCurrentBattle().getCoords(effect.getTarget());
	}

	@Override
	public boolean isFinished() {
		return time >= DURATION;
	}

	@Override
	public void render() {
		if(time < DURATION){
			Application.application().drawString(
					String.valueOf(Math.abs(deltaHealth)), 
					(int)coords.getCenterX(),
					(int)coords.getCenterY() - (int)(time*DELTA/(DURATION/2)),
					color
				);
		}
		
		Application.application().debug("Rendering update health on "+coords.getCenterX()+";"+coords.getCenterY()+" => "+deltaHealth);
	}

	@Override
	public void update(int delta) {
		this.time += delta;
	}

}
