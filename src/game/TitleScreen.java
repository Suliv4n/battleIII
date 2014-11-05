package game;


import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.xml.SlickXMLException;

import donnees.Sauvegarde;

import personnage.Equipe;
import personnage.Personnage;
import ui.GUIList;
import ui.ListRenderer.ElementRenderer;


public class TitleScreen extends BasicGameState{

	private Image fond;
	private String[] menu;
	private int curseurMenu = 0;
	
	//Selection du menu
	private int selectionMenu = -1;
	
	//Sauvegardes
	//private int curseurRelatif = 0;
	//private int curseurAbsolu = 0;
	
	//private ArrayList<Equipe> saves;
	private GUIList<Equipe> listeSaves;
	private int time;
	
	private StateBasedGame game;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame game)
			throws SlickException {
		fond = new Image("ressources/images/title_screen.png");
		menu = new String[]{"Nouveau jeu",
							"Charger"};
		this.game = game;
		
		this.listeSaves = new GUIList<Equipe>(250, 390, 4, null, null, false);
		listeSaves.setStep(70);
		listeSaves.setCursorMarges(-15, 30);
		listeSaves.setElementRenderer(new ElementRenderer() {
			
			@Override
			public void render(int x, int y, Object element, int index) {
				Equipe save = (Equipe) element;
				Graphics g = Jeu.getAppGameContainer().getGraphics();
				g.setColor(new Color(132,105,55));
				g.drawString("Sauvegarde " + (index+1) , x, y );
					
					if(save != null)
					{
						int j=0;
						for(Personnage p : save)
						{
							if(p != null)
							{
								g.drawImage(p.getAnimation(Equipe.BAS).getImage(2), x + 40 * j, y+20);
								j++;
							}
						}
						g.drawString(save.get(0).getNom(), 510, 15 + y);
						g.drawString("Niv. "+save.get(0).getLevel(), 510, 35 + y);
						g.drawString(save.getMap().getNom(), 390, 55 + y);
					}
					else
					{
						g.drawString("Vide", 500, 35 + y);
					}
					
					g.drawLine(380,70 + y, 630, 70 + y);
				}});
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
			throws SlickException {
		g.drawImage(fond,0,0);
		
		g.setColor(new Color(132,105,55));
		
		for(int i=0; i<menu.length; i++){
			g.drawString(menu[i], 50, 200 + 20*i);
		}
		
		if(selectionMenu == 1){
			afficherSauvegardes(g);
		}
		

		g.drawImage(Jeu.getFleche(0), 30, 205+20*curseurMenu);
		
	}



	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		Input in = container.getInput();
		time += delta;
		if(selectionMenu == -1){ //------------------MENU PRINCIPAL-------------------------
			if(in.isKeyPressed(Input.KEY_UP) || ControllerInput.isControllerUpPressed(0, in)){
				curseurMenu = (curseurMenu - 1 + menu.length) % menu.length;
			}
			else if(in.isKeyPressed(Input.KEY_DOWN) || ControllerInput.isControllerDownPressed(0, in)){
				curseurMenu = (curseurMenu + 1) % menu.length;
			}
			else if(in.isKeyPressed(Input.KEY_RETURN)){
				onValidate();
			}
		}
		else if(selectionMenu == 1){//--------------------SAUVEGARDES-----------------------
			if(in.isKeyPressed(Input.KEY_ESCAPE)){
				selectionMenu = -1;
			}
			else if(in.isKeyPressed(Input.KEY_ENTER)){
				in.clearKeyPressedRecord();
				onValidate();
			}
			
			listeSaves.update(in);

		}
		
		in.clearKeyPressedRecord();
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
	
	private void onValidate(){
		if(selectionMenu == -1){
			selectionMenu = curseurMenu;
			if(curseurMenu == 1){
				try {
					//saves = Sauvegarde.getAllSauvegardes();
					listeSaves.setData(Sauvegarde.getAllSauvegardes());
				} catch (FileNotFoundException | SlickXMLException e) {
					e.printStackTrace();
				}
			}
		}
		else if(selectionMenu == 1){
			System.out.println("Validate on save"+(listeSaves.getSelectedIndex()+1));
			if(listeSaves.getObject() != null)
			{
				try {
					System.out.println("Execute load save"+(listeSaves.getSelectedIndex()+1));
					Sauvegarde.charger("save"+(listeSaves.getSelectedIndex()+1));
				} catch (SlickException e) {
					e.printStackTrace();
				}
				game.enterState(Config.EXPLORATION);
			}
		}
	}
	
	private void afficherSauvegardes(Graphics g) {
		listeSaves.render(390, 50);
		/*
		int pas = 90;
		for(int i = curseurAbsolu - curseurRelatif; 
				i - curseurAbsolu + curseurRelatif<4 && i<saves.size();
				i++){
			
			int x = i - curseurAbsolu + curseurRelatif;
			g.drawString("Sauvegarde " + (i+1) , 390,  50 + x * pas );
			
			if(saves.get(i) != null)
			{
				
				int j=0;
				for(Personnage p : saves.get(i))
				{
					if(p != null)
					{
						g.drawImage(p.getAnimation(Equipe.BAS).getImage(2), 390 + 40 * j, 70 + x*pas);
						j++;
					}
				}
				g.drawString(saves.get(i).get(0).getNom(), 510, 65 + x*pas);
				g.drawString("Niv. "+saves.get(i).get(0).getLevel(), 510, 85 + x*pas);
				g.drawString(saves.get(i).getMap().getNom(), 390, 105 + x*pas);
			}
			else
			{
				g.drawString("Vide", 500, 85 + x*pas);
			}
			
			g.drawLine(380,130 + x*pas, 630, 130 + x*pas);
		}
		
		g.drawImage(Jeu.getFleche(0), 380, 80 + curseurRelatif * pas);
		*/
	}

}
