package animation.death;

import game.system.application.Application;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

import characters.Ennemy;
import animation.DeathAnimation;

public class StandardDeathAnimation extends DeathAnimation{

	private static final int DURATION = 600;
	
	private int time = 0;
	private Point coord;
	
	public StandardDeathAnimation(Ennemy ennemy) {
		super(ennemy);
		coord = Application.application().getGame().getCurrentBattle().getCoords(ennemy);
	}

	@Override
	public void render() {
		Image img = super.getEnnemy().getImageForBattle().copy();
		float r = 0.8f;
		float g = 0.3f;
		float b = 0.2f;
		float a = Math.max(0, 1 - (float)time / (float)DURATION);
		img.setAlpha(a);
		img.setColor(Image.BOTTOM_LEFT, r, g, b, a);
		img.setColor(Image.BOTTOM_RIGHT, r, g, b, a);
		img.setColor(Image.TOP_LEFT, r, g, b, a);
		img.setColor(Image.TOP_RIGHT, r, g, b, a);
		
		img.drawCentered(coord.getCenterX(), coord.getCenterY());
	}

	@Override
	public void update(int delta) {
		time += delta;
	}
	
	public boolean isFinished(){
		return time >= DURATION;
	}
	

}
