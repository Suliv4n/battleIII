package game;

import game.settings.Settings;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import ui.GUIList;
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
	private GUIList<String> menu;
	
	private StateBasedGame game;
	//#endregion
	
	//#region ----OVERRIDE BASICGAMESTATE-----
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		this.game = game;
		
		menu = new GUIList<String>(6, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR, true);
		
		menu.setWidth(280);
		menu.setHeight(300);
		
		
		menu.setData(new String[]{
			"Continuer",
			"Équipe",
			"Configurations",
			"Sac",
			"Sauvegarder",
			"Quitter",
		});

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{

		menu.render(170, 90);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		menu.update(container.getInput());
		super.update(container, game, delta);
	}
	

	@Override
	public void onValidate(){
		switch(menu.getSelectedIndex())
		{
		case(0):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(StatesId.EXPLORATION);
			break;
		case(1):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(StatesId.GESTIONNAIRE_EQUIPE);
			
			break;
		case(2):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(StatesId.CONFIGURATION);
			
			break;
		case(3):
			game.getContainer().getInput().clearKeyPressedRecord();
			game.enterState(StatesId.SAC);
			
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
		return StatesId.MENU;
	}

	//#endregion
	
	//#region -------AFFICHAGE----------------
	
	
	
	@Override
	public void onStart(){
		game.enterState(StatesId.EXPLORATION);
	}
}
