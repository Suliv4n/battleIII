package game.battle;

import game.Combat;
import game.battle.atb.ActiveTimeBattleManager;

import java.util.ArrayList;

import org.newdawn.slick.Image;

public interface IBattle 
{
	public int getPhysicAttack();
	public int getMagicAttack();
	public int getPhysicDefense();
	public int getMagicDefense();
	public int getAgility();
	public int getHealth();
	public Image getImageForBattle();
	public ActiveTimeBattleManager getActiveTimeBattleManager();
	
	public int getHealtPoints();
	public int getMaximumHealthPoints();
	public void updateHealthPoints(int dPV);
	public boolean isAlive();	
	
	public String getName();
	
	public void setCibles(ArrayList<IBattle> cibles);
	public ArrayList<IBattle> getTargets();
	public void chooseNextTarget(Combat combat);
	
	public Object getAction();
	public void setAction(Object action);
	public void cancelAction();
}
