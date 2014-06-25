package game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import donnees.Sauvegarde;

/**
 * 
 * _-_-_-_-[State Menu]-_-_-_-_-_
 * 
 * @author Darklev
 *
 */
public class Menu extends BasicGameState 
{

	//#region --------PROPRIETES--------------
	private ArrayList<String> leMenu;
	private int curseur;
	private Image fleche;
	//#endregion
	
	//#region ----OVERRIDE BASICGAMESTATE-----
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		curseur = 0;
		leMenu = new ArrayList<String>();
		leMenu.add("Continuer");
		leMenu.add("Equipe");
		leMenu.add("Configuration");
		leMenu.add("Sac");
		leMenu.add("Sauvegarder");
		leMenu.add("Quitter");
		
		fleche = Jeu.getFleche(0);
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
		Input in = container.getInput();
		in.disableKeyRepeat();
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			curseur = (curseur+1)%leMenu.size(); 
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			curseur = (curseur-1+leMenu.size())%leMenu.size(); 
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			switch(curseur)
			{
			case(0):
				container.getInput().clearKeyPressedRecord();
				game.enterState(Config.EXPLORATION);
				break;
			case(1):
				container.getInput().clearKeyPressedRecord();
				game.enterState(Config.GESTIONNAIRE_EQUIPE);
				
				break;
			case(2):
				container.getInput().clearKeyPressedRecord();
				game.enterState(Config.CONFIGURATION);
				
				break;
			case(3):
				container.getInput().clearKeyPressedRecord();
				game.enterState(Config.SAC);
				
				break;
			case(4):
				Sauvegarde.sauvegarder("save1");
				break;
			case(5):
				//TODO confirmation
				System.exit(0);
			}
		}
		
		
		if(in.isKeyPressed(Input.KEY_TAB) || in.isKeyPressed(Input.KEY_ESCAPE))
		{
			container.getInput().clearKeyPressedRecord();
			game.enterState(Config.EXPLORATION);
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
		for(int i=0; i<leMenu.size();i++)
		{
			g.drawString(leMenu.get(i), 210, 150+30*i);
		}
	}
	
	private void dessinerCurseur(Graphics g)
	{
		g.drawImage(fleche,210-fleche.getWidth()-5,153+30*curseur);
	}
	
	//#endregion
}
