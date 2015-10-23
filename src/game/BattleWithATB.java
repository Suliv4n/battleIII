package game;

import game.battle.IBattle;
import game.battle.actions.Action;
import game.battle.actions.AttackAction;
import game.battle.actions.SkillAction;
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
import org.newdawn.slick.state.StateBasedGame;

import characters.Character;
import characters.EnnemisParty;
import characters.Ennemy;
import skill.Skill;
import ui.BarUI;
import ui.GUIList;
import ui.Panel;
import ui.TypeBarre;
import ui.listRenderer.CursorRenderer;
import ui.listRenderer.ElementRenderer;

public class BattleWithATB extends Top
{

	private Image background;
	private EnnemisParty ennemis;
	private ArrayList<Character> queueATB;
	
	private boolean showSkills = false;
	private boolean attacking = false;
	
	private ArrayList<Action> actionsQueue;
	private int typeSelectTargets = -1; //Skill.ALL_ENNEMIES, ...
	private ArrayList<IBattle> cursorsTargets;
	
	private GUIList<Character> equipeList;
	private Panel topPanel;
	private Panel bottomPanel;
	
	private HashMap<IBattle, Point> coords;
	
	/**
	 * Liste des actions
	 */
	private HashMap<Character,GUIList<String>> actionsLists;
	
	private HashMap<Character,GUIList<Skill>> skillsLists;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		cursorsTargets = new ArrayList<IBattle>();
		
		equipeList = new GUIList<Character>(3, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR, false);
		equipeList.setWidth(640);
		equipeList.setHeight(100);
		equipeList.setAutomaticWidth(false);
		equipeList.setElementRenderer(new ElementRenderer() {
			@Override
			public void render(int x, int y, Object element, int index) {
				Character p = (Character) element;
				Graphics g = Application.application().getGraphics();
				BarUI hpBar = new BarUI(Configurations.HEALTH_BAR_COLOR, Color.black, 150, 5, p.getHealtPoints(), p.getMaximumHealthPoints(), TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				BarUI energyBar = new BarUI(p.energyColor(), Color.black, 150, 5, p.getEnergy(), p.getMaximumEnergy(), TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				BarUI atb = new BarUI(p.getActiveTimeBattleManager().getCurrent() == 100 ? Color.yellow : Color.cyan, Color.black, 50, 5, p.getActiveTimeBattleManager().getCurrent() ,100, TypeBarre.LEFT_TO_RIGHT, true, Config.couleur2);
				hpBar.render(g, x, y+13);
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
				g.setColor(Settings.BORDER_COLOR);
				g.drawRect(x, y, 580, 25);
			}
		} );
		equipeList.setCursorMarges(-1, 0);
		topPanel = new Panel(640, 100, 2, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR);
		bottomPanel = new Panel(640, 100, 2, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR);
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
		
		actionsQueue = new ArrayList<Action>(); 
		
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
				p = new Point(115+100*(i % 2) - img.getWidth()/2, 250 - img.getHeight()/2);
			}
			else if(i <= 5)
			{
				p = new Point(130+100*(i % 2) - img.getWidth()/2, 300 - img.getHeight()/2);
			}
			coords.put(ennemis.getEnnemis().get(i), p);
			ennemis.getEnnemis().get(i).launchActiveTime();
		}
		
		
		actionsLists = new HashMap<Character,GUIList<String>>();
		skillsLists = new HashMap<Character,GUIList<Skill>>();
		for(Character p : Application.application().getGame().getParty()){
			p.launchActiveTime();
			GUIList<String> a = new GUIList<String>(4, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR, true);
			a.setHeight(99);
			a.setData(new String[]{"Attaquer","Compétences"});
			GUIList<Skill> s = new GUIList<Skill>(4, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR, true);
			s.setHeight(99);
			s.setData(p.getSkills());
			s.setElementRenderer(
				(int x, int y, Object element, int index) -> {
					Skill skill = (Skill) element;
					Application.application().drawString(skill.getName(), x, y);
			});
			
			actionsLists.put(p, a);
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
			if(entity.isAlive()){
				entity.getImageForBattle().drawCentered(coords.get(entity).getX(), coords.get(entity).getY());
			}
		}
		bottomPanel.render(0, 380);
		equipeList.render(5, 385);
		if(queueATB.size() != 0 && !showSkills && typeSelectTargets == -1){
			actionsLists.get(queueATB.get(0)).render(0, 0);
		}
		else if(showSkills){
			skillsLists.get(queueATB.get(0)).render(0, 0);
		}
		else if(typeSelectTargets != -1){
			drawTargetsCursors();
		}
		if(actionsQueue.size() > 0){
			Action currentAction = actionsQueue.get(0);
			if(!currentAction.isRenderFisnished()){
				currentAction.render();
			}
			else if(!currentAction.getEffectsAnimation().isFinished()){
				currentAction.getEffectsAnimation().render();
			}
		}

		super.render(container, game, g);
	}
	

	private void drawTargetsCursors() {
		for(IBattle t : cursorsTargets){
			if(t.isAlive()){
				Point p = coords.get(t);
				Graphics g = Application.application().getGraphics();
				g.drawImage(Application.application().getGame().getArrow(0), 
					p.getX() - t.getImageForBattle().getWidth(), 
					p.getY());
			}
		}
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
		for(Ennemy e : ennemis.getEnnemis().values()){
			if(e.getActiveTimeBattleManager().getCurrent() == 100 && e.isAlive()){
				actionsQueue.add(actionsQueue.size(), e.doIA(this));
				e.getActiveTimeBattleManager().resetToZero();
			}
		}
		//File d' ATB
		if(queueATB.size() != 0 && !showSkills && typeSelectTargets == -1){
			actionsLists.get(queueATB.get(0)).update(in);
			equipeList.select(queueATB.get(0));
			equipeList.setRenderCursor(true);
		}
		else if(showSkills){
			skillsLists.get(queueATB.get(0)).update(in);
		}

		if(actionsQueue.size() > 0){
			actionsQueue.get(0).update(delta);
			if(actionsQueue.get(0).isRenderFisnished()){
				
				actionsQueue.get(0).calculateEffectsOnce();
				actionsQueue.get(0).getEffectsAnimation().update(delta);
				if(actionsQueue.get(0).getEffectsAnimation().isFinished()){
					
					actionsQueue.get(0).applyEffects();
					
					IBattle caster = actionsQueue.get(0).getCaster();
					caster.resetActiveTimeBattleManager();
					caster.launchActiveTime();
					
					if(caster instanceof Character){
						queueATB.remove(caster);
					}
					
					actionsQueue.remove(0);
					
					typeSelectTargets = -1;
					showSkills = false;
					
					onActionFinished();
				}
			}
			pauseAllATB();
		}
		else{
			resumeAllATB();
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
		if(queueATB.size() > 0 && !showSkills && typeSelectTargets == -1){ //selection d'une action
			if(actionsLists.get(queueATB.get(0)).getSelectedIndex() == 0){
				typeSelectTargets = Skill.ENNEMY;
				cursorsTargets.clear();
				cursorsTargets.add(ennemis.getFirstValidTarget());
				attacking = true;
			}
			else if(actionsLists.get(queueATB.get(0)).getSelectedIndex() == 1){
				showSkills = true;
			}
		}
		else if(showSkills && queueATB.get(0).getSkills().size() > 0){ //selection d'un skill
			showSkills = false;
			Skill skill = skillsLists.get(queueATB.get(0)).getObject();
			typeSelectTargets = skill.getTargets();
			cursorsTargets.clear();
			if(typeSelectTargets == Skill.ENNEMY){
				cursorsTargets.add(ennemis.getFirstValidTarget());
			}
			queueATB.get(0).setAction(new SkillAction(queueATB.get(0), cursorsTargets, skill));
		}
		else if(queueATB.size() > 0){ //ajout de l'action dans la queue actions
			if(attacking){
				attacking = false;
				queueATB.get(0).setAction(new AttackAction(queueATB.get(0), cursorsTargets));
			}
			typeSelectTargets = -1;
			actionsQueue.add(queueATB.get(0).getAction());
		}
	}
	
	@Override
	public void onBack(){
		if(typeSelectTargets != -1){
			typeSelectTargets = -1;
		}
		else if(showSkills){
			showSkills = false;
		}
	}
	
	@Override
	public void onLeft(){
		if(typeSelectTargets == Skill.ENNEMY){
			int cursor = ennemis.indexOf((Ennemy) cursorsTargets.get(0));
			if(cursor % 2 == 1 && ennemis.isValidTarget(cursor - 1)){
				cursorsTargets.set(0, ennemis.getEnnemis().get(cursor - 1));
			}
			else if(cursor % 2 == 0 && ennemis.isValidTarget(cursor + 1)){
				cursorsTargets.set(0, ennemis.getEnnemis().get(cursor + 1));
			}
			if(cursor % 2 == 0){
				for(int i : new int[]{1,3,5}){
					if(ennemis.isValidTarget(i)){
						cursorsTargets.set(0, ennemis.getEnnemis().get(i));
					}
				}
			}
			else{
				for(int i : new int[]{0,2,4}){
					if(ennemis.isValidTarget(i)){
						cursorsTargets.set(0, ennemis.getEnnemis().get(i));
					}
				}
			}
		}
	}
	
	@Override
	public void onRight(){
		if(typeSelectTargets == Skill.ENNEMY){
			int cursor = ennemis.indexOf((Ennemy) cursorsTargets.get(0));
			
			if(cursor % 2 == 0 && ennemis.isValidTarget(cursor + 1)){
				cursorsTargets.set(0, ennemis.getEnnemis().get(cursor + 1));
			}
			else if(cursor % 2 == 1 && ennemis.isValidTarget(cursor - 1)){
				cursorsTargets.set(0, ennemis.getEnnemis().get(cursor - 1));
			}
			else{
				if(cursor % 2 == 0){
					for(int i : new int[]{1,3,5}){
						if(ennemis.isValidTarget(i)){
							cursorsTargets.set(0, ennemis.getEnnemis().get(i));
						}
					}
				}
				else{
					for(int i : new int[]{0,2,4}){
						if(ennemis.isValidTarget(i)){
							cursorsTargets.set(0, ennemis.getEnnemis().get(i));
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onUp(){
		if(typeSelectTargets == Skill.ENNEMY){
			int cursor = ennemis.indexOf((Ennemy) cursorsTargets.get(0));
			do{
				cursor = (cursor + 4) % 6;
			}while(!ennemis.isValidTarget(cursor));
			cursorsTargets.set(0, ennemis.getEnnemis().get(cursor));
		}
	}
	
	@Override
	public void onDown(){
		if(typeSelectTargets == Skill.ENNEMY){
			int cursor = ennemis.indexOf((Ennemy) cursorsTargets.get(0));
			do{
				cursor = (cursor + 2) % 6;
			}while(!ennemis.isValidTarget(cursor));
			cursorsTargets.set(0, ennemis.getEnnemis().get(cursor));
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

	/**
	 * Retourne les coordonnées des protagonistes (ennemis ou alliés) du combat
	 * qui sont passés en paramètre.
	 * 
	 * @param characters
	 * 		Membre du combat dont les coordonées sont passées en paramètre.
	 * @return liste des coordonnées des membres du combat passés en paramètre.
	 */
	public ArrayList<Point> getCoords(ArrayList<IBattle> characters) {
		ArrayList<Point> res = new ArrayList<Point>();
		
		for(IBattle ib : coords.keySet()){
			if(characters.contains(ib)){
				res.add(getCoords(ib));
			}
		}
		
		return res;
	}
	
	/**
	 * Retourne les coordonnées d'un protagoniste (ennemis ou alliés) du combat
	 * passé en paramètre.
	 * 
	 * @param character
	 * 		Membre du combat dont les coordonées sont passées en paramètre.
	 * @return liste des coordonnées du membre du combat passés en paramètre.
	 */
	public Point getCoords(IBattle character) {
		return coords.get(character);
	}

	/**
	 * Retourne les ennemis du combat.
	 * 
	 * @return les ennemis du combat.
	 */
	public EnnemisParty getEnnemis() {
		return ennemis;
	}

	
	
	public void pauseAllATB(){
		for(IBattle i : coords.keySet()){
			i.getActiveTimeBattleManager().pause();
		}
	}
	
	public void resumeAllATB(){
		for(IBattle i : coords.keySet()){
			i.getActiveTimeBattleManager().resume();
		}
	}

	private void onActionFinished(){
		
	}
	
}