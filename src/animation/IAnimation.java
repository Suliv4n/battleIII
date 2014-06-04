package animation;

import game.Combat;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import personnage.IBattle;

public interface IAnimation 
{
	public boolean playFrame(Graphics g, Point coordonneesLanceur, ArrayList<Point> coordonneesCibles, 
			IBattle lanceur, ArrayList<IBattle> cibles , Combat combat,  int frame)  throws SlickException ;
}
