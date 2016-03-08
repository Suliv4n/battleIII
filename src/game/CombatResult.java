package game;

import game.settings.Settings;
import game.system.Configurations;
import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ui.BarUI;
import ui.GUIList;
import ui.Panel;
import ui.TypeBarre;
import characters.Character;
import characters.Party;
import bag.IItems;
import audio.MusicManager;


public class CombatResult extends BasicGameState
{
	
	private int po;
	private int exp;
	private ArrayList<IItems> drops;
	
	private Panel mainPanel;
	private GUIList<Character> characters;
	

	public void init(int po, int exp, ArrayList<IItems> drops)
	{
		if(drops == null){
			drops = new ArrayList<IItems>();
		}
		this.po = po;
		this.exp = exp;
		this.drops = drops;
	}
	
	@Override
	public void init(GameContainer conatiner, StateBasedGame game)
			throws SlickException
	{
		mainPanel = Panel.createFullScreen(2, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR);
		characters = new GUIList<Character>(3, null, null, false);
		
		characters.setRenderCursor(false);
		characters.setElementRenderer((int x , int y,  Object object, int i) -> {
			Character character = (Character) object;
			BarUI experienceBar = new BarUI(Configurations.EXPERIENCE_BAR_COLOR , Color.black, 200, 10, character.getExperience(), character.experienceRequiredForNextLevel(), TypeBarre.LEFT_TO_RIGHT, true, Settings.BORDER_COLOR);
			
			experienceBar.render(Application.application().getGraphics(), x, y);
		});
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException 
	{
		mainPanel.render(0, 0);
		dessinerEquipe(g);
	}



	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException 
	{
		Party equipe = Application.application().getGame().getParty();
		
		equipe.updateMoney(po);
		po=0;
		
		if(exp>0)
		{
			for(Character p : equipe)
			{
				if(p.isAlive())
				{
					p.gainExperience(1);
				}
			}
		}
		exp=Math.max(0, exp-1);
		
		
		gererInput(container.getInput(), sbg);
		container.getInput().clearKeyPressedRecord();
	}

	private void gererInput(Input in, StateBasedGame sbg) 
	{
		if(in.isKeyPressed(Input.KEY_RETURN))
		{
			if(exp > 0)
			{
				Application.application().getGame().getParty().updateMoney(po);
				exp = 0;
			}
			else
			{	
				sbg.enterState(StatesId.EXPLORATION);
			}
		}
		
	}

	@Override
	public int getID()
	{
		return StatesId.COMBATRESULT;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame sbg) throws SlickException{

		MusicManager.playLoop("victory");
		
	}
	
	private void dessinerEquipe(Graphics g) 
	{
		Party equipe = Application.application().getGame().getParty();
		int delta=100; // pixel entre 2 persos
		for(int i=0;i<equipe.numberOfCharacters();i++)
		{
			//dessiner barre xp :
			g.setColor(Settings.BORDER_COLOR);
			g.drawRect(70,70+delta*i,200,20);
			g.setColor(Color.black);
			g.fillRect(71, 71+delta*i, 199, 19);
			g.setColor(Configurations.EXPERIENCE_BAR_COLOR);
			g.fillRect(71, 71+delta*i, (float) (equipe.get(i).getExperience()/(double) (equipe.get(i).experienceRequiredForNextLevel())*199) , 19);
			
			g.drawImage(equipe.get(i).getAnimation(Party.SOUTH).getImage(2), 10, 60+i*delta);
			g.setColor(Color.white);
			g.drawString(equipe.get(i).getName(), 70, 45+delta*i);
			g.drawString("Exp. : " + equipe.get(i).getExperience(), 500, 70+delta*i);
		}
	}

}
