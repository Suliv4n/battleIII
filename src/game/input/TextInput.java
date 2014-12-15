package game.input;

import java.util.ArrayList;
import java.util.HashMap;

import game.Config;
import game.launcher.Launcher;
import game.system.application.Application;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Format;


/**
 * GameState permettant au joueur de faire une saisie
 * en affichant un clavier.
 * 
 * 
 * @author Darklev
 *
 */
public class TextInput extends BasicGameState
{
	
	private static ArrayList<String> defaultValue;
	private static ArrayList<String> input;
	private static ArrayList<Image> icon;
	private static ArrayList<String> message;
	private static int stateOut; //L'id du après avoir validé la saisie.
	
	//--------------OPERATIONS----------------
	private static ArrayList<InputAction> operations; //file d'ActionSaisie (FIFO)
	private static ArrayList<HashMap<String, Object>> param;
	//----------------------------------------
	private static boolean end;	
	
	private int cursorX = 0;
	private int cursorY = 0;
	
	private static char[][] mat;
	
	
	/**
	 * Initialise les saisies.
	 */
	public static void initSaisie()
	{
		mat = new char[8][21];
		
		char[][] characters = 
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
		mat = characters;	
	}
	
	/**
	 * Initialise une saisie
	 * 
	 * @param icon
	 * 		Icone à afficher.
	 * @param message
	 * 		Texte à afficher.
	 * @param defaultValue
	 * 		Valeur par défaut.
	 * @param stateOut
	 * 		Id de la GameState après la saisie.
	 * @param operations
	 * 		Operations à effectuer après la saisie.
	 * @param param
	 * 		Paramètres supplémentaires pour les opéartions à effectuer après la saisie.
	 */
	public static void init(Image icon,String message, String defaultValue, int stateOut, InputAction operations, HashMap<String, Object> param)
	{
		if(TextInput.defaultValue == null)
		{
			TextInput.defaultValue = new ArrayList<String>();
		}
		if(TextInput.input == null)
		{
			TextInput.input = new ArrayList<String>();
		}
		if(TextInput.icon == null)
		{
			TextInput.icon = new ArrayList<Image>();
		}
		if(TextInput.message == null)
		{
			TextInput.message = new ArrayList<String>();
		}
		if(TextInput.param == null)
		{
			TextInput.param = new ArrayList<HashMap<String, Object>>();
		}
		if(TextInput.operations == null)
		{
			TextInput.operations = new ArrayList<InputAction>();
		}

		//Saisie.operations = operations;
		TextInput.message.add(message);
		if(defaultValue == null){
			defaultValue = "";
		}
		TextInput.defaultValue.add(defaultValue);
		TextInput.input.add(defaultValue);
		TextInput.icon.add(icon);
		TextInput.param.add(param);
		
		TextInput.stateOut = stateOut;
		
		TextInput.operations.add(operations);
		end = false;
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
		if(input.size()>0)
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
			g.drawString(Format.multiLines(message.get(0),70),10,10);
			g.drawString(input.get(0)+"_", 200, 110);
			
			drawCursor(g);
			
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
	private void drawCursor(Graphics g) 
	{
		if(cursorY>=8)
		{
			g.drawImage(Application.application().getGame().getArrow(0), 10 + cursorX*135, 435);
		}
		else
		{
			g.setColor(Config.couleur2);
			g.drawRect(10 + cursorX*30, 160+cursorY*30,20,20);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		Input in = container.getInput();
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			cursorY = (cursorY + 1) % 9;
			if(cursorY >= 8)
			{
				cursorX = Math.min(cursorX, 2);
			}
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			cursorY = (cursorY + 8) % 9;
		}
		else if(in.isKeyPressed(Input.KEY_LEFT))
		{
			
			if(cursorY >= 8)
			{
				cursorX = (cursorX + 2) % 3;
			}
			else
			{
				cursorX = (cursorX + 20) % 21;
			}
		}
		else if(in.isKeyPressed(Input.KEY_RIGHT))
		{
			if(cursorY >= 8)
			{
				cursorX = (cursorX + 1) % 3;
			}
			else
			{
				cursorX = (cursorX + 1) % 21;
			}
		}
		else if(in.isKeyPressed(Input.KEY_BACK))
		{
			delete();
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			if(cursorY >=8)
			{
				if(cursorX == 0)
				{
					delete();
				}
				else if(cursorX == 1)
				{
					setDefaultValue();
				}
				else if(cursorX == 2)
				{
					if(operations.get(0).controle())
					{
						end = true;
						operations.get(0).action(param.get(0));
						
						input.remove(0);
						operations.remove(0);
						param.remove(0);
						defaultValue.remove(0);
						message.remove(0);
						
						
						if(operations.size() == 0)
						{
							game.enterState(stateOut);
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
				input.set(0, input.get(0) + mat[cursorY][cursorX]);
			}			
		}
	}

	/**
	 * Supprime le dernier caractère de la saisie en cours.
	 */
	private void delete()
	{
		if(input.get(0).length()>0)
			input.set(0,input.get(0).substring(0,input.get(0).length()-1));
	}
	
	/**
	 * Retourne à la valeur par défaut.
	 */
	private void setDefaultValue()
	{
		input = defaultValue;
	}
	
	@Override
	public int getID() 
	{
		return Config.SAISIE;
	}
	/**
	 * Retourne le texte saisi
	 * @return
	 * 	Le texte saisi.
	 */
	public static String getSaisie() 
	{
		return input.get(0);
	}

	/**
	 * Retourne vrai si la saisie est terminée.
	 * 
	 * @return
	 * 	Vrai si la saisie est terminée, faux sinon.
	 */
	public static boolean isEnd() 
	{
		return end;
	}
}
