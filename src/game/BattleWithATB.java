package game;

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


import personnage.Party;
import personnage.EnnemisParty;
import personnage.IBattle;
import personnage.Character;
import ui.BarUI;
import ui.GUIList;
import ui.Panel;
import ui.TypeBarre;
import ui.ListRenderer.CursorRenderer;
import ui.ListRenderer.ElementRenderer;



public class BattleWithATB extends BasicGameState
{

	private Image background;
	private EnnemisParty ennemis;
	private ArrayList<Character> queueATB;
	
	private GUIList<Character> equipeList;
	private Panel barreHaut;
	private Panel barreBas;
	
	private HashMap<IBattle, Point> coords;
	
	/**
	 * Liste des actions
	 */
	private HashMap<Character,GUIList<String>>actions;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		equipeList = new GUIList<Character>(640, 100, 3, Config.couleur1, Config.couleur2, false);
		equipeList.setElementRenderer(new ElementRenderer() {
			@Override
			public void render(int x, int y, Object element, int index) {
				Character p = (Character) element;
				Graphics g = Application.application().getGraphics();
				BarUI pv = new BarUI(Config.couleurPV, Color.black, 150, 5, p.getHealtPoints(), p.getMaximumHealthPoints(), TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				BarUI energie = new BarUI(p.energyColor(), Color.black, 150, 5, p.getEnergy(), p.getMaximumEnergy(), TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				BarUI atb = new BarUI(p.getActiveTimeBattleManager().getCurrent() == 100 ? Color.yellow : Color.cyan, Color.black, 50, 5, p.getActiveTimeBattleManager().getCurrent() ,100, TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				pv.render(g, x, y+10);
				energie.render(g, x+170, y+10);
				atb.render(g,x+500,y+5);
				g.setColor(Color.white);
				g.drawString("PV:"+p.getHealtPoints() + "/" + p.getMaximumHealthPoints(), x, y-5);
				g.drawString(p.getEnergyLabel()+":"+p.getEnergy() + "/" + p.getMaximumEnergy(), x+170, y-5);
				g.drawString(p.getName(), x + 330, y+2);
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
		barreHaut = new Panel(640, 100, 2, Config.couleur1, Config.couleur2);
		barreBas = new Panel(640, 100, 2, Config.couleur1, Config.couleur2);
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
		for(Character p : Application.application().getGame().getParty()){
			p.getActiveTimeBattleManager().launch();
			GUIList<String> a = new GUIList<String>(100, 98, 3, Config.couleur1, Config.couleur2, true);
			a.setData(new String[]{"Attaquer","Compétences"});
			actions.put(p,a);
		}
		
		

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		barreHaut.render(0, 0);
		background.drawCentered(Config.LONGUEUR/2, Config.LARGEUR/2);
		for(IBattle entity : coords.keySet()){
			entity.getImageForBattle().drawCentered(coords.get(entity).getX(), coords.get(entity).getY());
		}
		barreBas.render(0, 380);
		equipeList.render(5, 385);
		if(queueATB.size() != 0){
			actions.get(queueATB.get(0)).render(0, 0);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		Input in = container.getInput();
		for(Character p : Application.application().getGame().getParty()){
			//ATB à 100% le personnage est ajouté à la queue
			if(p.getActiveTimeBattleManager().getCurrent() == 100 && !queueATB.contains(p)){
				queueATB.add(p);
			}
		}
		//File d'attente ATB
		if(queueATB.size() != 0){
			actions.get(queueATB.get(0)).update(in);
			equipeList.select(queueATB.get(0));
			equipeList.setRenderCursor(true);
		}
		//equipeList.update(in);
		if(in.isKeyPressed(Input.KEY_A)){
			onPressL();
		}
		else if(in.isKeyPressed(Input.KEY_Z)){
			onPressR();
		}
	}

	private void onPressR() {
		if(queueATB.size() >= 2){
			Character first = queueATB.get(0);
			queueATB.remove(0);
			queueATB.add(queueATB.size(),first);
		}
	}

	private void onPressL() {
		if(queueATB.size() >= 2){
			Character last = queueATB.get(queueATB.size() - 1);
			queueATB.remove(queueATB.size() - 1);
			queueATB.add(0,last);
		}
		
	}

	@Override
	public int getID() 
	{
		return Config.BATTLE_ATB;
	}
	
	
	
}
