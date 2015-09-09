package game.battle;

import game.Combat;
import game.battle.actions.Action;
import game.battle.atb.ActiveTimeBattleManager;

import java.util.ArrayList;

import org.newdawn.slick.Image;

public abstract class IBattle 
{
	public abstract int getPhysicAttack();
	public abstract int getMagicAttack();
	public abstract int getPhysicDefense();
	public abstract int getMagicDefense();
	public abstract int getAgility();
	public abstract int getHealth();
	public abstract Image getImageForBattle();
	public abstract ActiveTimeBattleManager getActiveTimeBattleManager();
	public abstract void resetActiveTimeBattleManager();
	public abstract void launchActiveTime();
	
	public abstract int getHealtPoints();
	public abstract int getMaximumHealthPoints();
	public abstract void updateHealthPoints(int dPV);
	public abstract boolean isAlive();	
	
	public abstract String getName();
	
	public abstract void setTargets(ArrayList<IBattle> cibles);
	public abstract ArrayList<IBattle> getTargets();
	public abstract void chooseNextTarget(Combat combat);
	
	public abstract Action getAction();
	public abstract void setAction(Action action);
	public abstract void cancelAction();
	
}
