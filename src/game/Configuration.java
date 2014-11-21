package game;

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
		fleche = Launcher.getArrow(0);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{

		
		g.setColor(Config.couleur1);
		g.fillRect(2, 2, 638, 478);
		
		g.setColor(Color.white);
		g.drawString("CONFIGURATIONS", 250, 15);
		
		g.setColor(Config.couleur2);
		g.drawRect(0, 0, 639, 479);
		g.drawRect(1, 1, 637, 477);
		g.drawLine(0, 40, 640, 40);
		g.drawLine(0, 41, 640, 41);
		
		//Interface couleur 1:
		g.drawRect(15,70,610,100);
		g.setColor(Config.couleur1);
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
		g.drawString(String.valueOf(Config.couleur1.getRed()), 390, 80);
		g.drawString(String.valueOf(Config.couleur1.getGreen()), 390, 100);
		g.drawString(String.valueOf(Config.couleur1.getBlue()), 390, 120);
		
		//Interface couleur 2:
		g.setColor(Config.couleur2);
		g.drawRect(15,190,610,100);
		g.setColor(Config.couleur1);
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
		g.drawString(String.valueOf(Config.couleur2.getRed()), 390, 200);
		g.drawString(String.valueOf(Config.couleur2.getGreen()), 390, 220);
		g.drawString(String.valueOf(Config.couleur2.getBlue()), 390, 240);
		


		
		g.setColor(Color.white);
		if(Launcher.estPleinEcran())
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
		g.drawString(String.valueOf(Config.son),350,340);
		g.drawString(String.valueOf(Config.musique),350,320);
		
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
			game.enterState(Config.MENU);
		}
		
		if(in.isKeyDown(Input.KEY_LEFT))
		{
			int d = 1;

			switch(curseur)
			{
			case(0):
				Config.couleur1 = new Color(Math.max(Config.couleur1.getRed()-d,0),Config.couleur1.getGreen(),Config.couleur1.getBlue());
				break;
			case(1):
				Config.couleur1 = new Color(Config.couleur1.getRed(),Math.max(Config.couleur1.getGreen()-d,0),Config.couleur1.getBlue());
				break;
			case(2):
				Config.couleur1 = new Color(Config.couleur1.getRed(),Config.couleur1.getGreen(),Math.max(Config.couleur1.getBlue()-d,0));
				break;
			case(4):
				Config.couleur2 = new Color(Math.max(Config.couleur2.getRed()-d,0),Config.couleur2.getGreen(),Config.couleur2.getBlue());
				break;
			case(5):
				Config.couleur2 = new Color(Config.couleur2.getRed(),Math.max(Config.couleur2.getGreen()-d,0),Config.couleur2.getBlue());
				break;
			case(6):
				Config.couleur2 = new Color(Config.couleur2.getRed(),Config.couleur2.getGreen(),Math.max(Config.couleur2.getBlue()-d,0));
				break;
			case(9):
				Config.musique = Math.max(Config.musique-1, 0);
				MusicManager.setVolume();
				break;
			case(10):
				Config.son = Math.max(Config.son-1, 0);
				break;

			}
		}
		
		else if(in.isKeyDown(Input.KEY_RIGHT))
		{
			int d = 1;

			switch(curseur)
			{
			case(0):
				Config.couleur1 = new Color(Math.min(Config.couleur1.getRed()+d,255),Config.couleur1.getGreen(),Config.couleur1.getBlue());
				break;
			case(1):
				Config.couleur1 = new Color(Config.couleur1.getRed(),Math.min(Config.couleur1.getGreen()+d,255),Config.couleur1.getBlue());
				break;
			case(2):
				Config.couleur1 = new Color(Config.couleur1.getRed(),Config.couleur1.getGreen(),Math.min(Config.couleur1.getBlue()+d,255));
				break;
			case(4):
				Config.couleur2 = new Color(Math.min(Config.couleur2.getRed()+d,255),Config.couleur2.getGreen(),Config.couleur2.getBlue());
				break;
			case(5):
				Config.couleur2 = new Color(Config.couleur2.getRed(),Math.min(Config.couleur2.getGreen()+d,255),Config.couleur2.getBlue());
				break;
			case(6):
				Config.couleur2 = new Color(Config.couleur2.getRed(),Config.couleur2.getGreen(),Math.min(Config.couleur2.getBlue()+d,255));
				break;
			case(9):
				Config.musique = Math.min(Config.musique+1, 100);
				MusicManager.setVolume();
				break;
			case(10):
				Config.son = Math.min(Config.son+1, 100);
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
				Launcher.switchModeAffichage();
			}
			else if(curseur == 11)
			{
				in.clearKeyPressedRecord();
				game.enterState(Config.MENU);
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
		return Config.CONFIGURATION;
	}
	
	//#endregion
	
	//#region -------AFFICHAGE-----------------
	
	private void afficherCurseursCouleurs(Graphics g)
	{
		g.setColor(new Color(200,200,200));
		
		//Couleur 1:
		g.drawLine(120+Config.couleur1.getRed(), 89, 120+Config.couleur1.getRed(), 92);
		g.drawLine(120+Config.couleur1.getGreen(), 109, 120+Config.couleur1.getGreen(), 112);
		g.drawLine(120+Config.couleur1.getBlue(), 129, 120+Config.couleur1.getBlue(), 132);
		
		//Couleur 2:
		g.drawLine(120+Config.couleur2.getRed(), 209, 120+Config.couleur2.getRed(), 212);
		g.drawLine(120+Config.couleur2.getGreen(), 229, 120+Config.couleur2.getGreen(), 232);
		g.drawLine(120+Config.couleur2.getBlue(), 249, 120+Config.couleur2.getBlue(), 252);
	}

	
	private void afficherCurseursVolumes(Graphics g)
	{
		g.setColor(new Color(200,200,200));
		
		//Musique
		g.drawLine(240+Config.musique,329,240+Config.musique,331);

		
		//Son
		g.drawLine(240+Config.son,349,240+Config.son,351);

		
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
			Config.couleur1 = new Color(150,20,0);
			break;
		case(2):
			Config.couleur2 = new Color(200,150,0);
			break;		
		}		
	}
	
	//#endregion
}
