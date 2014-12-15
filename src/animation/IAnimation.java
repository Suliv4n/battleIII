package animation;

import game.Combat;
import game.battle.IBattle;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public interface IAnimation 
{
	public boolean playFrame(Graphics g, Point casterPosition, ArrayList<Point> targetsPositions, 
			IBattle caster, ArrayList<IBattle> targets , Combat battle,  int frame)  throws SlickException ;
}
