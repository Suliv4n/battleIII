package game;

import game.launcher.Launcher;
import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


import data.Save;

/**
 * 
 * _-_-_-_-[State Menu]-_-_-_-_-_
 * 
 * @author Darklev
 *
 */
public class Menu extends Top 
{

	//#region --------PROPRIETES--------------
	private ArrayList<String> menu;
	private int cursor;
	private Image fleche;
	
	private StateBasedGame game;
	//#endregion
	
	//#region ----OVERRIDE BASICGAMESTATE-----
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		this.game = game;
				
		cursor = 0;
		menu = new ArrayList<String>();
		menu.add("Continuer");
		menu.add("Equipe");
		menu.add("Configuration");
		menu.add("Sac");
		menu.add("Sauvegarder");
		menu.add("Quitter");
		
		fleche = Application.application().getGame().getArrow(0);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		dessinerCadre(g);
		afficherMenu(g);
		dessinerCurseur(g);	
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		super.update(container, game, delta);
	}
	
	public void controllerButtonPressed(int controller, int button){
		if(button == ControllerInput.VALIDATE){
			onValidate();
		}
		else if(button == ControllerInput.START){
			game.enterState(Config.EXPLORATION);
		}
		
	}
	
	@Override
	public void onValidate(){
		switch(cursor)
		{
		case(0):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(Config.EXPLORATION);
			break;
		case(1):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(Config.GESTIONNAIRE_EQUIPE);
			
			break;
		case(2):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(Config.CONFIGURATION);
			
			break;
		case(3):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(Config.SAC);
			
			break;
		case(4):
			Save.sauvegarder("save1");
			break;
		case(5):
			//TODO
			System.exit(0);
		}
	}
	


	@Override
	public int getID() 
	{		
		return Config.MENU;
	}

	//#endregion
	
	//#region -------AFFICHAGE----------------
	
	private void dessinerCadre(Graphics g)
	{
		g.setColor(Config.couleur1);
		
		g.fillRect(170, 90, 280, 300);
		
		g.setColor(Config.couleur2);
		g.drawRect(169, 89, 281, 301);
		g.drawRect(168, 88, 282, 303);
		
		g.setColor(new Color(0,150,225));
		g.drawString("°~MENU~°", 270, 100);
	}
	
	private void afficherMenu(Graphics g)
	{
		g.setColor(new Color(255,255,255));
		for(int i=0; i<menu.size();i++)
		{
			g.drawString(menu.get(i), 210, 150+30*i);
		}
	}
	
	private void dessinerCurseur(Graphics g)
	{
		g.drawImage(fleche,210-fleche.getWidth()-5,153+30*cursor);
	}
	
	//#endregion
	
	@Override
	public void onDown(){
		cursor = (cursor+1)%menu.size();
	}
	
	@Override
	public void onUp(){
		cursor = (cursor-1+menu.size())%menu.size(); 
	}
	
	@Override
	public void onStart(){
		game.enterState(Config.EXPLORATION);
	}
}
