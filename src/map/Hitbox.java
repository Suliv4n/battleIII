package map;

import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Vector2f;

public class Hitbox {
	private ArrayList<Shape> shapes;
	
	public Hitbox(){
		shapes = new ArrayList<Shape>();
	}
	
	public Hitbox(Shape shape){
		shapes = new ArrayList<Shape>();
		shapes.add(shape);
	}
	
	public void addShape(Shape shape){
		shapes.add(shape);
	}
	
	public boolean intersects(Hitbox other){
		for(Shape s : shapes){
			for(Shape o : other.shapes){
				if(s.intersects(o)){
					return true;
				}
			}
		}
		
		return false;
	}

	public void moveX(double d) {
		for(Shape s : shapes){
			s.setX((float) (s.getX() + d));
		}
	}
	
	public void moveY(double d) {
		for(Shape s : shapes){
			s.setY((float) (s.getY() + d));
		}
	}
	
	public void draw(Color color){
		Graphics g = Application.application().getGraphics();
		Color current = g.getColor();
		g.setColor(color);
		for(Shape s : shapes){
			ShapeRenderer.fill(s);
			g.draw(s);
		}
		g.setColor(current);
	}

	public void setX(float x) {
		for(Shape s : shapes){
			s.setX(x);
		}
	}
	
	public void setY(float y) {
		for(Shape s : shapes){
			s.setY(y);
		}
	}
}
