package animation;

import game.battle.IBattle;
import game.battle.effect.Effect;
import game.system.application.Application;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;

/**
 * Permet d'afficher les effets d'une action d'une attaque 
 * (dégât, analyse, soin...)
 * 
 * @author Darklev
 *
 */
public class BattleEffectsDisplayer 
{
	
	private ArrayList<Effect> effects;
	private int index = 0;
	private int count;
	
	private HashMap<IBattle, ArrayList<Effect>> targetsSet;
	
	public BattleEffectsDisplayer(ArrayList<Effect> effects){
		this.effects = effects;
		
		targetsSet = new HashMap<IBattle, ArrayList<Effect>>();
		
		for(Effect e : effects){
			
			if(!targetsSet.containsKey(e.getTarget())){
				targetsSet.put(e.getTarget(), new ArrayList<Effect>());
			}
			targetsSet.get(e.getTarget()).add(e);
			
			if(count < targetsSet.get(e.getTarget()).size() ){
				count = targetsSet.get(e.getTarget()).size();
			}
			
		}
		Application.application().debug("Count : "+count);
	}
	
	public boolean isFinished(){
		return index >= count;
	}
	
	public void render() throws SlickException{
		for(IBattle entity : targetsSet.keySet()){
			if(index < targetsSet.get(entity).size()){
				BattleEffectRenderer renderer = targetsSet.get(entity).get(index).getRenderer();
				renderer.render();
			}
		}
		Application.application().debug("Rendering effects");
	}

	public void update(int delta) throws SlickException {
		
		boolean finished = true;
		for(IBattle entity : targetsSet.keySet()){
			if(index < targetsSet.get(entity).size()){
				BattleEffectRenderer renderer = targetsSet.get(entity).get(index).getRenderer();
				renderer.update(delta);
				
				finished &= renderer.isFinished();
			}
		}
		if(finished){
			index++;
		}
		
		Application.application().debug("Updating effects");
	}
	
}
