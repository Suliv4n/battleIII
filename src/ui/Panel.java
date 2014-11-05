package ui;

import game.Jeu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Panel {
	
	private int width;
	private int height;
	private Color color;
	private Color colorFrame;
	private int frame;
	
	public Panel(int width, int height, int frame, Color color, Color colorFrame){
		this.width = width;
		this.height = height;
		this.color = color;
		this.colorFrame = colorFrame;
		this.frame = frame;
	}
	
	public void render(int x, int y){
		Graphics g = Jeu.getAppGameContainer().getGraphics();
		
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
		g.setColor(colorFrame);
		for(int i = 0; i<frame; i++){
			g.drawRect(x+i, y+i, width-i*2-1, height-i*2-1);
		}
	}
	
	
	
}
