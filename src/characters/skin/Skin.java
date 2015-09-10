package characters.skin;


import map.Hitbox;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

public class Skin {
	
	private Animation[] animations;
	private Hitbox hitbox;
	
	public Skin(SpriteSheet sprites, int x, int y, int width, int height){
		animations = new Animation[5];
		for(int i = 0; i<5; i++)
		{
			animations[i] = new Animation(sprites, 0, i, 2,i ,true, 100, true);
			hitbox = new Hitbox(new Polygon(new float[]{x,y,x,y+height,x+width,y+height,x+width,y}));
			animations[i].setAutoUpdate(true);
		}
	}
	
	public void moveX(float dx){
		hitbox.moveX(dx);
	}

	public void moveY(float dy){
		hitbox.moveY(dy);
	}
	
	public void setX(float x){
		hitbox.setCenterX(x);
	}

	public void setY(float y){
		hitbox.setCenterY(y);
	}
	
	public void drawHitbox(Color color){
		hitbox.draw(new Color(0,200,100,0.5f));
	}

	public Animation getAnimation(int direction) {
		return animations[direction];
	}

	public Hitbox getHitbox() {
		return hitbox;
	}

	
}
