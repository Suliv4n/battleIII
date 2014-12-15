package game;


import game.system.application.Application;

import java.io.FileNotFoundException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.xml.SlickXMLException;


import data.Save;

import personnage.Party;
import personnage.Character;
import ui.GUIList;
import ui.listRenderer.ElementRenderer;


public class TitleScreen extends Top{

	private Image fond;
	private String[] menu;
	private int curseurMenu = 0;
	
	//Selection du menu
	private int selectionMenu = -1;
	
	private GUIList<Party> listSaves;
	
	private StateBasedGame game;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame game)
			throws SlickException {
		fond = new Image("ressources/images/title_screen.png");
		menu = new String[]{"Nouveau jeu",
							"Charger"};
		this.game = game;
		
		this.listSaves = new GUIList<Party>(4, null, null, false);
		listSaves.setWidth(250);
		listSaves.setHeight(390);
		listSaves.setAutomaticWidth(false);
		//listSaves
		listSaves.setStep(70);
		listSaves.setCursorMarges(-15, 30);
		listSaves.setElementRenderer(new ElementRenderer() {
			
			@Override
			public void render(int x, int y, Object element, int index) {
				Party save = (Party) element;
				Graphics g = Application.application().getGraphics();
				g.setColor(new Color(132,105,55));
				g.drawString("Sauvegarde " + (index+1) , x, y );
					
					if(save != null)
					{
						int j=0;
						for(Character p : save)
						{
							if(p != null)
							{
								g.drawImage(p.getAnimation(Party.SOUTH).getImage(2), x + 40 * j, y+20);
								j++;
							}
						}
						g.drawString(save.get(0).getName(), 510, 15 + y);
						g.drawString("Niv. "+save.get(0).getLevel(), 510, 35 + y);
						g.drawString(save.getMap().getName(), 390, 55 + y);
					}
					else
					{
						g.drawString("Vide", 500, 35 + y);
					}
					
					g.drawLine(380,70 + y, 630, 70 + y);
				}});
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		
		g.drawImage(fond,0,0);
		
		g.setColor(new Color(132,105,55));
		
		for(int i=0; i<menu.length; i++){
			g.drawString(menu[i], 50, 200 + 20*i);
		}
		
		if(selectionMenu == 1){
			listSaves.render(390, 50);
		}
		

		g.drawImage(Application.application().getGame().getArrow(0), 30, 205+20*curseurMenu);
		
		super.render(container, sbg, g);
	}



	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		
		//--------------------SAUVEGARDES-----------------------
		if(selectionMenu == 1){
			listSaves.update(container.getInput());
		}
		
		super.update(container, sbg, delta);
	}

	@Override
	public int getID() {
		return Config.TITLE_SCREEN;
	}
	
	
	@Override
	public void controllerButtonPressed(int controller, int button){
		if(button == ControllerInput.VALIDATE){
			onValidate();
		}
	}
	
	//-----------------------------------------------------------
	//-------------------------EVENTS----------------------------
	//-----------------------------------------------------------
	
	@Override
	public void onValidate(){
		if(selectionMenu == -1){
			selectionMenu = curseurMenu;
			if(curseurMenu == 1){
				try {
					listSaves.setData(Save.getAllSauvegardes());
				} catch (FileNotFoundException | SlickException e) {
					e.printStackTrace();
				}
			}
		}
		else if(selectionMenu == 1){
			if(listSaves.getObject() != null)
			{
				try {
					Save.loadSave("save"+(listSaves.getSelectedIndex()+1));
				} catch (SlickException e) {
					e.printStackTrace();
				}
				game.enterState(Config.EXPLORATION);
			}
		}
	}
	
	@Override
	public void onBack(){
		selectionMenu = -1;
	}
	
	@Override
	public void onUp(){
		if(selectionMenu == -1){
			curseurMenu = (curseurMenu - 1 + menu.length) % menu.length;
		}
	}
	
	@Override
	public void onDown(){
		if(selectionMenu == -1){
			curseurMenu = (curseurMenu + 1) % menu.length;
		}
	}
}
