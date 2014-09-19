package ui;

import game.ControllerInput;
import game.Jeu;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import ui.ListRenderer.CursorRenderer;
import ui.ListRenderer.ElementRenderer;
import ui.listController.ListController;

public class GUIList<T> {
	private int relativIndex = 0; //Index où le curseur doit s'afficher
	private int absoluteIndex = 0; //Index réel
	private int width;
	private int height;
	
	private int count; //nombre d'éléments à afficher.
	
	private ElementRenderer elementRenderer;
	private CursorRenderer cursorRenderer;
	private ListController listController;
	
	private ArrayList<T> list;
	
	private int frame = 2; //epaisseur du cadre.
	private Color frameColor;
	private Color undergroundColor;
	
	private boolean drawGUI = true;
	
	
	
	public GUIList(int width, int height, int count, Color underground, Color frame, boolean drawGUI){
		list = new ArrayList<T>();
		this.width = width;
		this.height = height;
		this.count = count;
		this.undergroundColor = underground == null ? Color.black : underground;
		this.frameColor = frame == null ? Color.white : frame;
		
		this.drawGUI = drawGUI;
		
		//Renderer par défaut;
		elementRenderer = new ElementRenderer() {
			@Override
			public void render(int x, int y, Object element){
				Graphics g = Jeu.getAppGameContainer().getGraphics();
				g.drawString(element.toString(), x + 15, y );
			}
		};
		
		cursorRenderer = new CursorRenderer() {
			@Override
			public void render(int x, int y) {
				Graphics g = Jeu.getAppGameContainer().getGraphics();
				Jeu.getFleche(0).drawCentered(x + 6, y + 12);
			}
		};
		
		listController = new ListController() {
			@Override
			public boolean isUp(Input in) {
				return in.isKeyPressed(Input.KEY_UP) || ControllerInput.isControllerUpPressed(0, in);
			}
			@Override
			public boolean isDown(Input in) {
				return in.isKeyPressed(Input.KEY_DOWN) ||  ControllerInput.isControllerDownPressed(0, in);
			}
		};
	}
	

	public void render(int x, int y){
		Graphics g = Jeu.getAppGameContainer().getGraphics();
		if(drawGUI){
			g.setColor(undergroundColor);
			g.fillRect(x, y, width, height);
			g.setColor(frameColor);
			for(int i=0; i<frame; i++){
				g.drawRect(x+i, y+i, width - 2*i, height - 2*i);
			}
		}
		g.setColor(Color.white);
		int n = 0;
		System.out.println(absoluteIndex + "  " + relativIndex);
		for(int i = absoluteIndex - relativIndex; i < Math.min(list.size() ,absoluteIndex - relativIndex + count); i++){
			elementRenderer.render(x, y + 5 + n *20, list.get(i));
			n++;
		}
		cursorRenderer.render(x + (drawGUI ? frame : 0), y + relativIndex * 20 + (drawGUI ? frame : 0));
	}
	
	public void update(Input in){
		if(list.size() > 0){
			if(listController.isDown(in)){
				absoluteIndex = absoluteIndex + 1 == list.size() ? 0 : absoluteIndex + 1;
				relativIndex = relativIndex + 1 < count && relativIndex + 1 < list.size() ?  relativIndex + 1 : absoluteIndex == 0 ? 0 : count-1;
				System.out.println("Check : " + relativIndex + " - " + count);
			}
			else if(listController.isUp(in)){
				absoluteIndex = absoluteIndex  == 0 ? list.size() - 1: absoluteIndex - 1;
				relativIndex = relativIndex > 0 ?  relativIndex - 1 : absoluteIndex ==  list.size() - 1  ? Math.min(list.size() - 1, count - 1) : 0;
			}
		}
	}
	
	public void setData(ArrayList<T> list){
		this.list = list;
	}
	
	public int size(){
		return list.size();
	}
	
	public T getObject(){
		return list.get(absoluteIndex);
	}
	
	public void setElementRenderer(ElementRenderer elementRenderer){
		this.elementRenderer = elementRenderer;
	}
	
	public void setCursorRenderer(CursorRenderer cursorRenderer){
		this.cursorRenderer = cursorRenderer;
	}
	
	public void setListController(ListController listController){
		this.listController = listController;
	}
}
