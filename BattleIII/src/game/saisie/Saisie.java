package game.saisie;

import java.util.ArrayList;
import java.util.HashMap;

import game.Config;
import game.Jeu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import donnees.Formatage;


/**
 * GameState permettant au joueur de faire une saisie
 * en affichant un clavier.
 * 
 * 
 * @author Darklev
 *
 */
public class Saisie extends BasicGameState
{
	
	private static ArrayList<String> defaut;
	private static ArrayList<String> saisie;
	private static ArrayList<Image> icone;
	private static ArrayList<String> message;
	private static int stateSortie;
	
	//--------------OPERATIONS----------------
	private static ArrayList<ActionSaisie> operations; //file d'ActionSaisie (FIFO)
	private static ArrayList<HashMap<String, Object>> param;
	//----------------------------------------
	private static boolean termine;	
	
	private int curseurX = 0;
	private int curseurY = 0;
	
	private static char[][] mat;
	
	
	
	public static void initSaisie()
	{
		mat = new char[8][21];
		
		char[][] caracteres = 
		{
				{'0','1','2','3','4','5','6','7','8','9','+','-','*','/',' ',' ',' ',' ',' ',' ',' '},
				{'A','B','C','D','E','F','G','H','I','J','K','L','M',' ',' ',' ',' ',' ',' ',' ',' '},
				{'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',' ',' ',' ',' ',' ',' ',' ',' '},
				{'a','b','c','d','e','f','g','h','i','j','k','l','m',' ',' ',' ',':','!','_',' ',' '},
				{'n','o','p','q','r','s','t','u','v','w','x','y','z',' ',' ',' ',',','.','?',' ',' '},
				{'é','è','ê','ë','î','ï','à','ç','ü','ö','ñ',' ',' ',' ',' ','{','}','(',')','[',']'},
				{'$','£','€',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','@','{','}','(',')','[',']'},
				{'_',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','_',' ',' ',' ',' ',' ',' ',' ','_'},		
		};
		mat = caracteres;	
	}
	
	public static void init(Image icone,String message, String defaut, int sortie, ActionSaisie operations, HashMap<String, Object> param)
	{
		//-----instanciation-----\\
		if(Saisie.defaut == null)
		{
			Saisie.defaut = new ArrayList<String>();
		}
		if(Saisie.saisie == null)
		{
			Saisie.saisie = new ArrayList<String>();
		}
		if(Saisie.icone == null)
		{
			Saisie.icone = new ArrayList<Image>();
		}
		if(Saisie.message == null)
		{
			Saisie.message = new ArrayList<String>();
		}
		if(Saisie.param == null)
		{
			Saisie.param = new ArrayList<HashMap<String, Object>>();
		}
		if(Saisie.operations == null)
		{
			Saisie.operations = new ArrayList<ActionSaisie>();
		}

		//Saisie.operations = operations;
		Saisie.message.add(message);
		if(defaut == null)
			defaut = "";
		Saisie.defaut.add(defaut);
		Saisie.saisie.add(defaut);
		Saisie.icone.add(icone);
		Saisie.param.add(param);
		
		Saisie.stateSortie = sortie;
		
		Saisie.operations.add(operations);
		termine = false;
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException 
	{

		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		if(saisie.size()>0)
		{
			g.setColor(Config.couleur1);
			g.fillRect(0, 0, 640,480);
			
			g.setColor(Config.couleur2);
			g.drawRect(0, 0, 639,479);
			g.drawRect(1, 1, 637,477);
			
			g.drawLine(0, 100, 640, 100);
			g.drawLine(0, 101, 640, 101);
			
			g.drawLine(0, 150, 640, 150);
			g.drawLine(0, 151, 640, 151);
			
			g.setColor(Color.white);
			g.drawString(Formatage.multiLignes(message.get(0),70),10,10);
			g.drawString(saisie.get(0)+"_", 200, 110);
			
			afficherCurseur(g);
			
			g.setColor(Color.white);
			
			//affiche le "clavier"
			for(int i=0; i<mat[0].length;i++)
			{
				for(int j=0; j<mat.length;j++)
				{
					g.drawString(String.valueOf(mat[j][i]),15+i*30,160+j*30);
				}
			}
			
			g.setColor(Config.couleur2);
			g.drawLine(0, 399, 640, 399);
			g.drawLine(0, 400, 640, 400);
			
			//options
			g.setColor(Color.white);
			g.drawString("Supprimer", 30, 430);
			g.drawString("Défaut", 165, 430);
			g.drawString("Valider", 300, 430);
		}
	}

	/**
	 * Affiche le curseur.
	 * 
	 * @param g
	 */
	private void afficherCurseur(Graphics g) 
	{
		if(curseurY>=8)
		{
			g.drawImage(Jeu.getFleche(0), 10 + curseurX*135, 435);
		}
		else
		{
			g.setColor(Config.couleur2);
			g.drawRect(10 + curseurX*30, 160+curseurY*30,20,20);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		Input in = container.getInput();
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			curseurY = (curseurY + 1) % 9;
			if(curseurY >= 8)
			{
				curseurX = Math.min(curseurX, 2);
			}
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			curseurY = (curseurY + 8) % 9;
		}
		else if(in.isKeyPressed(Input.KEY_LEFT))
		{
			
			if(curseurY >= 8)
			{
				curseurX = (curseurX + 2) % 3;
			}
			else
			{
				curseurX = (curseurX + 20) % 21;
			}
		}
		else if(in.isKeyPressed(Input.KEY_RIGHT))
		{
			if(curseurY >= 8)
			{
				curseurX = (curseurX + 1) % 3;
			}
			else
			{
				curseurX = (curseurX + 1) % 21;
			}
		}
		else if(in.isKeyPressed(Input.KEY_BACK))
		{
			supprimer();
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			if(curseurY >=8)
			{
				if(curseurX == 0)
				{
					supprimer();
				}
				else if(curseurX == 1)
				{
					defaut();
				}
				else if(curseurX == 2)
				{
					if(operations.get(0).controle())
					{
						termine = true;
						operations.get(0).action(param.get(0));
						
						saisie.remove(0);
						operations.remove(0);
						param.remove(0);
						defaut.remove(0);
						message.remove(0);
						
						
						if(operations.size() == 0)
						{
							game.enterState(stateSortie);
						}		
						else
						{
							game.enterState(getID());
						}
						
					}
				}
			}
			else
			{
				saisie.set(0, saisie.get(0) + mat[curseurY][curseurX]);
			}			
		}
	}

	private void supprimer()
	{
		if(saisie.get(0).length()>0)
			saisie.set(0,saisie.get(0).substring(0,saisie.get(0).length()-1));
	}
	
	private void defaut()
	{
		saisie = defaut;
	}
	
	@Override
	public int getID() 
	{
		return Config.SAISIE;
	}

	public static String getSaisie() 
	{
		return saisie.get(0);
	}

	public static boolean estTermine() 
	{
		return termine;
	}
}
