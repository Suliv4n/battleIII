package game.battle.effect;

import game.battle.IBattle;

import java.util.function.BiConsumer;
import java.util.function.Function;

import characters.Ennemy;
import game.system.application.Application;

public enum EffectType {
	PHYSIQUE_DAMAGE((IBattle target , Integer value) -> {
		target.updateHealthPoints(value);
	}),
	
	MAGIC_DAMAGE((IBattle target , Integer value) -> {
		target.updateHealthPoints(value);
	}),
	
	HEAL((IBattle target , Integer value) -> {
		target.updateHealthPoints(-value);
	}),
	
	SEARCH((IBattle target , Integer value) -> {
		Application.application().getGame().getParty().analyse(((Ennemy)target).getID());
	});
	
	private EffectType(BiConsumer<IBattle, Integer> apply){
		
	}
}
