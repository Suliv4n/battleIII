package game;

import game.battle.IBattle;
import game.settings.Settings;
import game.system.Configurations;
import game.system.application.Application;

import java.util.ArrayList;
import java.util.HashMap;





import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;





import characters.Character;
import characters.EnnemisParty;
import characters.Party;
import skill.Skill;
import ui.BarUI;
import ui.GUIList;
import ui.Panel;
import ui.TypeBarre;
import ui.listRenderer.CursorRenderer;
import ui.listRenderer.ElementRenderer;
import util.Random;

public class BattleWithATB extends Top
{

	private Image background;
	private EnnemisParty ennemis;
	private ArrayList<Character> queueATB;
	private boolean showSkill = false;
	
	private GUIList<Character> equipeList;
	private Panel topPanel;
	private Panel bottomPanel;
	
	private HashMap<IBattle, Point> coords;
	
	/**
	 * Liste des actions
	 */
	private HashMap<Character,GUIList<String>> actions;
	
	private HashMap<Character,GUIList<Skill>> skillsLists;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		equipeList = new GUIList<Character>(3, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR, false);
		equipeList.setWidth(640);
		equipeList.setHeight(100);
		equipeList.setAutomaticWidth(false);
		equipeList.setElementRenderer(new ElementRenderer() {
			@Override
			public void render(int x, int y, Object element, int index) {
				Character p = (Character) element;
				Graphics g = Application.application().getGraphics();
				BarUI hvBar = new BarUI(Configurations.HEALTH_BAR_COLOR, Color.black, 150, 5, p.getHealtPoints(), p.getMaximumHealthPoints(), TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				BarUI energyBar = new BarUI(p.energyColor(), Color.black, 150, 5, p.getEnergy(), p.getMaximumEnergy(), TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				BarUI atb = new BarUI(p.getActiveTimeBattleManager().getCurrent() == 100 ? Color.yellow : Color.cyan, Color.black, 50, 5, p.getActiveTimeBattleManager().getCurrent() ,100, TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				hvBar.render(g, x, y+13);
				energyBar.render(g, x+170, y+13);
				atb.render(g,x+500,y+3);
				
				Application.application().drawString("PV:"+p.getHealtPoints() + "/" + p.getMaximumHealthPoints(), x, y-9, Configurations.DEFAULT_FONT_COLOR);
				Application.application().drawString(p.getEnergyLabel()+":"+p.getEnergy() + "/" + p.getMaximumEnergy(), x+170, y-9, Configurations.DEFAULT_FONT_COLOR);
				Application.application().drawString(p.getName(), x + 330, y-5, Configurations.DEFAULT_FONT_COLOR);
			}
		});
		equipeList.setStep(30);
		equipeList.setRenderCursor(false); 
		equipeList.setCursorRenderer( new CursorRenderer() {
			@Override
			public void render(int x, int y) {
				Graphics g = Application.application().getGraphics();
				g.setColor(Config.couleur2);
				g.drawRect(x, y, 580, 25);
			}
		} );
		equipeList.setCursorMarges(-1, 0);
		topPanel = new Panel(640, 100, 2, Config.couleur1, Config.couleur2);
		bottomPanel = new Panel(640, 100, 2, Config.couleur1, Config.couleur2);
		queueATB = new ArrayList<Character>();
	}
	
	/**
	 * Initilalise le combat
	 * @param ennemis
	 * 		Equipe d'ennemis du Battle en cours
	 */
	public void launch(EnnemisParty ennemis){
		this.background = Application.application().getGame().getParty().getMap().getBackgroundBattle();
		this.ennemis = ennemis;
		
		equipeList.setData(Application.application().getGame().getParty().getCharacters());
		
		coords = new HashMap<IBattle, Point>();
		if(Application.application().getGame().getParty().getIfExists(0) != null){coords.put(Application.application().getGame().getParty().get(0), new Point(450,150));}
		if(Application.application().getGame().getParty().getIfExists(1) != null){coords.put(Application.application().getGame().getParty().get(1), new Point(470,220));}
		if(Application.application().getGame().getParty().getIfExists(2) != null){coords.put(Application.application().getGame().getParty().get(2), new Point(490,290));}
	
		//Positionne les ennemis en fonction de la taille de leur image.
		for(int i : this.ennemis.getEnnemis().keySet())
		{
			Image img = ennemis.getEnnemis().get(i).getImageForBattle();
			Point p = null;
			if(i <= 1)
			{
				p = new Point(100+100*(i % 2)  - img.getWidth()/2, 200 - img.getHeight()/2);
			}
			else if(i <= 3)
			{
				p = new Point(115+100*(i%2) - img.getWidth()/2, 250 - img.getHeight()/2);
			}
			else if(i <= 5)
			{
				p = new Point(130+100*(i%2) - img.getWidth()/2, 300 - img.getHeight()/2);
			}
			coords.put(ennemis.getEnnemis().get(i), p);
		}
		
		
		actions = new HashMap<Character,GUIList<String>>();
		skillsLists = new HashMap<Character,GUIList<Skill>>();
		for(Character p : Application.application().getGame().getParty()){
			p.getActiveTimeBattleManager().launch(Random.randInt(20, 60));
			GUIList<String> a = new GUIList<String>(4, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR, true);
			a.setHeight(99);
			a.setData(new String[]{"Attaquer","Compétences"});
			GUIList<Skill> s = new GUIList<Skill>(4, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR, true);
			s.setHeight(99);
			s.setData(p.getSkills());
			actions.put(p, a);
			skillsLists.put(p, s);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		topPanel.render(0, 0);
		background.drawCentered(Configurations.SCREEN_WIDTH/2, Configurations.SCREEN_HEIGHT/2);
		for(IBattle entity : coords.keySet()){
			entity.getImageForBattle().drawCentered(coords.get(entity).getX(), coords.get(entity).getY());
		}
		bottomPanel.render(0, 380);
		equipeList.render(5, 385);
		if(queueATB.size() != 0 && !showSkill){
			actions.get(queueATB.get(0)).render(0, 0);
		}
		else if(showSkill){
			skillsLists.get(queueATB.get(0)).render(0, 0);
		}
		super.render(container, game, g);
	}
	

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		Input in = container.getInput();
		for(Character p : Application.application().getGame().getParty()){
			//ATB à 100% le personnage est ajouté à la queue
			if(p.getActiveTimeBattleManager().getCurrent() == 100 && !queueATB.contains(p) && p instanceof Character){
				queueATB.add(p);
			}
		}
		//File d'attente ATB
		if(queueATB.size() != 0 && !showSkill){
			actions.get(queueATB.get(0)).update(in);
			equipeList.select(queueATB.get(0));
			equipeList.setRenderCursor(true);
		}
		else if(showSkill){
			skillsLists.get(queueATB.get(0)).update(in);
		}
		
		super.update(container, game, delta);
	}

	@Override
	public int getID() 
	{
		return Config.BATTLE_ATB;
	}
	
	@Override
	public void onR() {
		if(queueATB.size() >= 2){
			Character first = queueATB.get(0);
			queueATB.remove(0);
			queueATB.add(queueATB.size(),first);
		}
	}
	
	@Override
	public void onL() {
		if(queueATB.size() >= 2){
			Character last = queueATB.get(queueATB.size() - 1);
			queueATB.remove(queueATB.size() - 1);
			queueATB.add(0,last);
		}
	}
	
	@Override
	public void onValidate(){
		if(queueATB.size() > 0){
			if(actions.get(queueATB.get(0)).getSelectedIndex() == 1){
				showSkill = true;
			}
		}
	}
	
	/**
	 * A la sortie du combat.
	 */
	public void onExit(){
		for(Character c : Application.application().getGame().getParty()){
			c.resetActiveTimeBattleManager();
		}
	}
}
