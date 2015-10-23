package game.battle.effect;

import game.battle.IBattle;

import java.util.function.BiConsumer;
import java.util.function.Function;

import characters.Ennemy;
import game.system.application.Application;

public enum EffectType {
	PHYSIC_DAMAGE((IBattle target , Integer value) -> {
		target.updateHealthPoints(-value);
	}),
	
	MAGIC_DAMAGE((IBattle target , Integer value) -> {
		target.updateHealthPoints(-value);
	}),
	
	HEAL((IBattle target , Integer value) -> {
		target.updateHealthPoints(value);
	}),
	
	SEARCH((IBattle target , Integer value) -> {
		Application.application().getGame().getParty().analyse(((Ennemy)target).getID());
	});
	
	private BiConsumer<IBattle, Integer> applier;
	
	private EffectType(BiConsumer<IBattle, Integer> applier){
		this.applier = applier;
	}
	
	public void apply(IBattle target, int value){
		applier.accept(target, value);
	}
	
	
}
