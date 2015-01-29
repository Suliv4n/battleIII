package game.battle;

import java.util.ArrayList;
import java.util.function.Function;

import skill.Skill;
import util.Random;
import characters.Ennemy;
import game.BattleWithATB;
import game.battle.actions.Action;
import game.battle.actions.AttackAction;
import game.battle.actions.SkillAction;
import game.system.application.Application;

public class Artificialntelligences {
	
	public static final int DEFAULT_AI = 0;
	
	public static Function<BattleWithATB, Action> createArtificialIntelligence(Ennemy ennemy, int typeAI){
		
		switch(typeAI){
		case(DEFAULT_AI):
		default: 
			return (BattleWithATB battle) -> {
				ArrayList<IBattle> targets = new ArrayList<IBattle>();
				if(ennemy.getSkills().size() > 0){
					Skill s = ennemy.getSkills().get(Random.randInt(0, ennemy.getSkills().size() - 1));
					if(s.getTargets() == Skill.ENNEMY){
						targets.add(Application.application().getGame().getParty().getRandomValidTarget());
					}
					else if(s.getTargets() == Skill.ALL_ENNEMIS){
						targets = Application.application().getGame().getParty().getValidTargets();
					}
					else if(s.getTargets() == Skill.ALLY){
						targets.add(battle.getEnnemis().getRandomTarget());
					} 
					else if(s.getTargets() == Skill.ALL_ALLIES){
						targets = battle.getEnnemis().getValidTargets();
					}
					return new SkillAction(ennemy, targets, s);
				}
				else{
					targets.add(Application.application().getGame().getParty().getRandomValidTarget());
					return new AttackAction(ennemy, targets);
				}
			};
		}
	}
}
