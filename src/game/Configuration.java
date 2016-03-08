package game;

import game.launcher.Launcher;
import game.settings.Settings;
import game.system.application.Application;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import audio.MusicManager;


/**
 * 
 * _-_-_-_-[State Configuration]-_-_-_-_-_
 * 
 * @author Darklev
 *
 */
public class Configuration extends BasicGameState 
{

	//#region ----------PROPRIETES---------------
	
	private int curseur = 0;
	
	private Image fleche;	
	
	//#endregion

	//#region ------OVERRIDE BASICGAMESTATE--------
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		fleche = Application.application().getGame().getArrow(0);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{

		
		g.setColor(Settings.BACKGROUND_COLOR);
		g.fillRect(2, 2, 638, 478);
		
		g.setColor(Color.white);
		g.drawString("CONFIGURATIONS", 250, 15);
		
		g.setColor(Settings.BORDER_COLOR);
		g.drawRect(0, 0, 639, 479);
		g.drawRect(1, 1, 637, 477);
		g.drawLine(0, 40, 640, 40);
		g.drawLine(0, 41, 640, 41);
		
		//Interface couleur 1:
		g.drawRect(15,70,610,100);
		g.setColor(Settings.BACKGROUND_COLOR);
		g.fillRect(20, 60, 175, 20);
		g.setColor(Color.white);
		g.drawString("Couleur principale",25,60);
		g.drawString("Rouge", 40, 80);
		g.drawString("Vert", 40, 100);
		g.drawString("Bleu", 40, 120);
		g.drawString("Défaut", 40, 140);
		g.setColor(new Color(100,100,100));
		g.drawLine(120, 90, 376, 90);
		g.drawLine(120, 110, 376, 110);
		g.drawLine(120, 130, 376, 130);
		g.drawLine(120, 91, 376, 91);
		g.drawLine(120, 111, 376, 111);
		g.drawLine(120, 131, 376, 131);
		g.setColor(Color.white);
		g.drawString(String.valueOf(Settings.BACKGROUND_COLOR.getRed()), 390, 80);
		g.drawString(String.valueOf(Settings.BACKGROUND_COLOR.getGreen()), 390, 100);
		g.drawString(String.valueOf(Settings.BACKGROUND_COLOR.getBlue()), 390, 120);
		
		//Interface couleur 2:
		g.setColor(Settings.BORDER_COLOR);
		g.drawRect(15,190,610,100);
		g.setColor(Settings.BACKGROUND_COLOR);
		g.fillRect(20, 180, 130, 20);
		g.setColor(Color.white);
		g.drawString("Couleur cadre",25,180);
		g.drawString("Rouge", 40, 200);
		g.drawString("Vert", 40, 220);
		g.drawString("Bleu", 40, 240);
		g.drawString("Défaut", 40, 260);
		g.setColor(new Color(100,100,100));
		g.drawLine(120, 210, 376, 210);
		g.drawLine(120, 230, 376, 230);
		g.drawLine(120, 250, 376, 250);
		g.drawLine(120, 211, 376, 211);
		g.drawLine(120, 231, 376, 231);
		g.drawLine(120, 251, 376, 251);
		g.setColor(Color.white);
		g.drawString(String.valueOf(Settings.BORDER_COLOR.getRed()), 390, 200);
		g.drawString(String.valueOf(Settings.BORDER_COLOR.getGreen()), 390, 220);
		g.drawString(String.valueOf(Settings.BORDER_COLOR.getBlue()), 390, 240);
		


		
		g.setColor(Color.white);
		if(Application.application().isFullScreen())
		{
			g.drawString("Mode fenêtre", 40, 300);
		}
		else
		{
			g.drawString("Mode plein écran", 40, 300);
		}
		
		g.drawString("Musique", 40, 320);
		g.drawString("Son", 40, 340);
		g.drawString("Retour", 40, 360);
		
		//Volumes
		g.setColor(new Color(100,100,100));
		g.drawLine(240, 330, 340, 330);
		g.drawLine(240, 350, 340, 350);
		
		g.setColor(Color.white);
		g.drawString(String.valueOf(Settings.SOUND_VOLUME),350,340);
		g.drawString(String.valueOf(Settings.MUSIC_VOLUME),350,320);
		
		afficherCurseursCouleurs(g);
		afficherCurseursVolumes(g);
		afficherCurseur(g);
	
		
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		Input in = container.getInput();
		
		if(in.isKeyPressed(Input.KEY_ESCAPE))
		{
			in.clearKeyPressedRecord();
			game.enterState(StatesId.MENU);
		}
		
		if(in.isKeyDown(Input.KEY_LEFT))
		{
			int d = 1;

			switch(curseur)
			{
			case(0):
				Settings.BACKGROUND_COLOR = new Color(Math.max(Settings.BACKGROUND_COLOR.getRed()-d,0),Settings.BACKGROUND_COLOR.getGreen(),Settings.BACKGROUND_COLOR.getBlue());
				break;
			case(1):
				Settings.BACKGROUND_COLOR = new Color(Settings.BACKGROUND_COLOR.getRed(),Math.max(Settings.BACKGROUND_COLOR.getGreen()-d,0),Settings.BACKGROUND_COLOR.getBlue());
				break;
			case(2):
				Settings.BACKGROUND_COLOR = new Color(Settings.BACKGROUND_COLOR.getRed(),Settings.BACKGROUND_COLOR.getGreen(),Math.max(Settings.BACKGROUND_COLOR.getBlue()-d,0));
				break;
			case(4):
				Settings.BORDER_COLOR = new Color(Math.max(Settings.BORDER_COLOR.getRed()-d,0),Settings.BORDER_COLOR.getGreen(),Settings.BORDER_COLOR.getBlue());
				break;
			case(5):
				Settings.BORDER_COLOR = new Color(Settings.BORDER_COLOR.getRed(),Math.max(Settings.BORDER_COLOR.getGreen()-d,0),Settings.BORDER_COLOR.getBlue());
				break;
			case(6):
				Settings.BORDER_COLOR = new Color(Settings.BORDER_COLOR.getRed(),Settings.BORDER_COLOR.getGreen(),Math.max(Settings.BORDER_COLOR.getBlue()-d,0));
				break;
			case(9):
				Settings.MUSIC_VOLUME = Math.max(Settings.MUSIC_VOLUME-1, 0);
				MusicManager.setVolume();
				break;
			case(10):
				Settings.SOUND_VOLUME = Math.max(Settings.SOUND_VOLUME-1, 0);
				break;

			}
		}
		
		else if(in.isKeyDown(Input.KEY_RIGHT))
		{
			int d = 1;

			switch(curseur)
			{
			case(0):
				Settings.BACKGROUND_COLOR = new Color(Math.min(Settings.BACKGROUND_COLOR.getRed()+d,255),Settings.BACKGROUND_COLOR.getGreen(),Settings.BACKGROUND_COLOR.getBlue());
				break;
			case(1):
				Settings.BACKGROUND_COLOR = new Color(Settings.BACKGROUND_COLOR.getRed(),Math.min(Settings.BACKGROUND_COLOR.getGreen()+d,255),Settings.BACKGROUND_COLOR.getBlue());
				break;
			case(2):
				Settings.BACKGROUND_COLOR = new Color(Settings.BACKGROUND_COLOR.getRed(),Settings.BACKGROUND_COLOR.getGreen(),Math.min(Settings.BACKGROUND_COLOR.getBlue()+d,255));
				break;
			case(4):
				Settings.BORDER_COLOR = new Color(Math.min(Settings.BORDER_COLOR.getRed()+d,255),Settings.BORDER_COLOR.getGreen(),Settings.BORDER_COLOR.getBlue());
				break;
			case(5):
				Settings.BORDER_COLOR = new Color(Settings.BORDER_COLOR.getRed(),Math.min(Settings.BORDER_COLOR.getGreen()+d,255),Settings.BORDER_COLOR.getBlue());
				break;
			case(6):
				Settings.BORDER_COLOR = new Color(Settings.BORDER_COLOR.getRed(),Settings.BORDER_COLOR.getGreen(),Math.min(Settings.BORDER_COLOR.getBlue()+d,255));
				break;
			case(9):
				Settings.MUSIC_VOLUME = Math.min(Settings.MUSIC_VOLUME+1, 100);
				MusicManager.setVolume();
				break;
			case(10):
				Settings.SOUND_VOLUME = Math.min(Settings.SOUND_VOLUME+1, 100);
				break;
			}
		}
		
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			if(curseur == 3)
			{
				couleurDefaut(1);
			}
			else if(curseur == 7)
			{
				couleurDefaut(2);
			}
			else if(curseur == 8)
			{
				Application.application().toggleFullScreen();
			}
			else if(curseur == 11)
			{
				in.clearKeyPressedRecord();
				game.enterState(StatesId.MENU);
			}
		}
		
		else if(in.isKeyPressed(Input.KEY_DOWN))
		{
			curseur = (curseur+1)%12;
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			curseur = (curseur+11)%12;
		}
		
	}

	@Override
	public int getID() 
	{
		return StatesId.CONFIGURATION;
	}
	
	//#endregion
	
	//#region -------AFFICHAGE-----------------
	
	private void afficherCurseursCouleurs(Graphics g)
	{
		g.setColor(new Color(200,200,200));
		
		//Couleur 1:
		g.drawLine(120+Settings.BACKGROUND_COLOR.getRed(), 89, 120+Settings.BACKGROUND_COLOR.getRed(), 92);
		g.drawLine(120+Settings.BACKGROUND_COLOR.getGreen(), 109, 120+Settings.BACKGROUND_COLOR.getGreen(), 112);
		g.drawLine(120+Settings.BACKGROUND_COLOR.getBlue(), 129, 120+Settings.BACKGROUND_COLOR.getBlue(), 132);
		
		//Couleur 2:
		g.drawLine(120+Settings.BORDER_COLOR.getRed(), 209, 120+Settings.BORDER_COLOR.getRed(), 212);
		g.drawLine(120+Settings.BORDER_COLOR.getGreen(), 229, 120+Settings.BORDER_COLOR.getGreen(), 232);
		g.drawLine(120+Settings.BORDER_COLOR.getBlue(), 249, 120+Settings.BORDER_COLOR.getBlue(), 252);
	}

	
	private void afficherCurseursVolumes(Graphics g)
	{
		g.setColor(new Color(200,200,200));
		
		//Musique
		g.drawLine(240+Settings.MUSIC_VOLUME,329,240+Settings.MUSIC_VOLUME,331);

		
		//Son
		g.drawLine(240+Settings.SOUND_VOLUME,349,240+Settings.SOUND_VOLUME,351);

		
	}
	
	
	private void afficherCurseur(Graphics g)
	{
		if(curseur<=3)
		{
			g.drawImage(fleche, 20, 85+20*curseur);
		}
		else if(curseur <= 7)
		{
			g.drawImage(fleche, 20, 205+20*(curseur-4));
		}
		else
		{
			g.drawImage(fleche, 20, 225+20*(curseur-4));
		}
	}
	
	//#endregion
	
	//#region ----------AUTRES METHODES-------
	public void couleurDefaut(int numCouleur)
	{
		switch(numCouleur)
		{
		case(1):
			Settings.BACKGROUND_COLOR = new Color(150,20,0);
			break;
		case(2):
			Settings.BORDER_COLOR = new Color(200,150,0);
			break;		
		}		
	}
	
	//#endregion
}
