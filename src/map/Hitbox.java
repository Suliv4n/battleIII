package map;

import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Vector2f;

public class Hitbox {
	private ArrayList<Polygon> shapes;
	
	public Hitbox(){
		shapes = new ArrayList<Polygon>();
	}
	
	public Hitbox(Polygon shape){
		shapes = new ArrayList<Polygon>();
		shapes.add(shape);
	}
	
	public void addShape(Polygon shape){
		shapes.add(shape);
	}
	
	public boolean intersects(Hitbox other){
		for(Shape s : shapes){
			for(Shape o : other.shapes){
				if(s.intersects(o)){
					Application.application().debug("INTERSECT");
					return true;
				}
			}
		}
		Application.application().debug("NO INTERSECT");
		return false;
	}

	public void moveX(double d) {
		Application.application().debug("dx = "+d);
		for(Shape s : shapes){
			s.setX((float) (s.getX() + d));
		}
	}
	
	public void moveY(double d) {
		Application.application().debug("dy = "+d);
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
	
	public void setCenterX(float x) {
		for(Shape s : shapes){
			s.setCenterX(x);
		}
	}
	
	public void setCenterY(float y) {
		for(Polygon s : shapes){
			s.setCenterY(y);
		}
	}

	/**
	 * Retourne une copie de la hitbox avec un décalage.
	 * 
	 * @param dx
	 * 	Décalage abscisse
	 * @param dy
	 * 	Décalage ordonée
	 * @return
	 */
	public Hitbox copy(double dx, double dy) {
		Hitbox copy = new Hitbox();
		
		for(Polygon s : shapes){
			Polygon shapeCopy = s.copy();
			copy.addShape(shapeCopy);
		}
		copy.moveX(dx);
		copy.moveY(dy);
		return copy;
	}
	
}
