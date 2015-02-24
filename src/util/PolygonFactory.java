package util;

import org.newdawn.slick.geom.Polygon;

public class PolygonFactory {
	
	public static Polygon createSquare(float x, float y, float width){
		return new Polygon(new float[]{x,y,x+width,y,x+width,y+width,x,y+width});
	}
	
	public static Polygon createRectangle(float x, float y, float width, float height){
		return new Polygon(new float[]{x,y,x+width,y,x+width,y+height,x,y+height});
	}
}
