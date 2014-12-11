package ui;

import game.ControllerInput;
import game.launcher.Launcher;
import game.system.application.Application;

import java.util.ArrayList;
import java.util.Collections;

import javax.xml.crypto.Data;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;


import personnage.Character;

import ui.listController.ListController;
import ui.listRenderer.CursorRenderer;
import ui.listRenderer.ElementRenderer;

public class GUIList<T> {
	private int relativeIndex = 0; //Index où le curseur doit s'afficher
	private int absoluteIndex = 0; //Index réel
	private int width;
	private int height;
	
	private int count; //nombre d'éléments à afficher.
	private int step = 20;
	
	private ElementRenderer elementRenderer;
	private CursorRenderer cursorRenderer;
	private ListController listController;
	
	private ArrayList<T> list;
	
	private int frame = 2; //epaisseur du cadre.
	private Color frameColor;
	private Color undergroundColor;
	
	private boolean drawGUI = true;
	
	private int cursorMargeX = 0;
	private int cursorMargeY = 0;
	
	private boolean renderCursor = true;
	private boolean automaticWidth = true;
	
	public GUIList(int count, Color underground, Color frame, boolean drawGUI){
		list = new ArrayList<T>();
		this.count = count;
		this.step = Application.application().getGame().getGameFont().getHeight("E") ;
		this.height = count * step + 15;
		this.undergroundColor = underground == null ? Color.black : underground;
		this.frameColor = frame == null ? Color.white : frame;
		
		this.drawGUI = drawGUI;
		
		//Renderer par défaut;
		elementRenderer = new ElementRenderer() {
			@Override
			public void render(int x, int y, Object element, int index){
				Application.application().drawString(element == null ? "null" : element.toString(), x + 15, y );
			}
		};
		
		cursorRenderer = new CursorRenderer() {
			@Override
			public void render(int x, int y) {
				Image cursor = Application.application().getGame().getArrow(0);
				cursor.draw(x+1, y + cursor.getWidth());
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
		Graphics g = Application.application().getGraphics();
		if(drawGUI){
			g.setColor(undergroundColor);
			g.fillRect(x, y, width, height);
			g.setColor(frameColor);
			for(int i=0; i<frame; i++){
				g.drawRect(x+i, y+i, width - 2*i, height - 2*i);
			}
			new Panel(width, height, frame, undergroundColor, frameColor);
		}
		g.setColor(Color.white);
		int n = 0;
		for(int i = absoluteIndex - relativeIndex; i < Math.min(list.size() ,absoluteIndex - relativeIndex + count); i++){
			elementRenderer.render(x, y + 5 + n * step, list.get(i),i);
			n++;
		}
		if(renderCursor){
			cursorRenderer.render(x + (drawGUI ? frame : 0) + cursorMargeX, y + relativeIndex * step + (drawGUI ? frame : 0) + cursorMargeY);
		}
	}
	
	public void update(Input in){
		if(list.size() > 0){
			if(listController.isDown(in)){
				absoluteIndex = absoluteIndex + 1 == list.size() ? 0 : absoluteIndex + 1;
				relativeIndex = relativeIndex + 1 < count && relativeIndex + 1 < list.size() ?  relativeIndex + 1 : absoluteIndex == 0 ? 0 : count-1;
			}
			else if(listController.isUp(in)){
				absoluteIndex = absoluteIndex  == 0 ? list.size() - 1: absoluteIndex - 1;
				relativeIndex = relativeIndex > 0 ?  relativeIndex - 1 : absoluteIndex ==  list.size() - 1  ? Math.min(list.size() - 1, count - 1) : 0;
			}
		}
	}
	
	public void setData(ArrayList<T> list){
		this.list = list;
	}
	
	public void setData(T[] list){
		this.list = new ArrayList<>();
		int longestString = 0;
		for(T t : list){
			this.list.add(t);
			int width = Application.application().getGame().getGameFont().getWidth(t == null ? "null" : t.toString());
			if(width > longestString){
				longestString = width;
			}
		}
		if(automaticWidth){
			this.width = longestString + 30;
		}
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
	
	public void setStep(int step){
		this.step = step;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public void setCursorMarges(int x, int y){
		cursorMargeX = x;
		cursorMargeY = y;
	}
	
	public int getSelectedIndex(){
		return absoluteIndex;
	}
	
	public void setRenderCursor(boolean renderCursor){
		this.renderCursor = renderCursor;
	}

	
	/**
	 * Sélectionne le premier item passé en paramètre de la liste trouvé 
	 * (ne fait rien si l'item n'est pas dans la liste).
	 * 
	 * @param item
	 * 		L'item à sélectionner.
	 */
	public void select(T item) {
		if(list.contains(item)){
			relativeIndex = list.indexOf(item);
			absoluteIndex = list.indexOf(item);
		}
	}
	
	/**
	 *  Modifie la longueur automatique de la liste.
	 *  Si la longueur automatique est activée alors la list change de longueur
	 *  automatiquement selon la chaîne de caractères la plus longue.
	 *  
	 *  @param auto
	 *  	Vrai si la longueur automatique doit être activée, sinon faux.
	 */
	public void setAutomaticWidth(boolean auto){
		automaticWidth = auto;
	}
}
