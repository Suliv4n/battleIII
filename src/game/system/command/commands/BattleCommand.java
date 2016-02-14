package game.system.command.commands;

import org.newdawn.slick.SlickException;

import characters.EnnemisParty;
import data.DataManager;
import game.Config;
import game.system.Configurations;
import game.system.Console;
import game.system.ConsoleLine;
import game.system.application.Application;
import game.system.command.Command;
import game.system.command.Executor;
import game.system.command.rule.ParameterRule;

public class BattleCommand extends Executor{

	public BattleCommand() {
		super();
		
		addParameterRule(new ParameterRule("id",ParameterRule.STRING, null ,true));
		addAllowedState(Config.EXPLORATION);
		
		setScript( command -> {
				String id = command.getParameter(0);
				try {
					EnnemisParty ennemis = DataManager.loadEnnemisParty(id);
					if(ennemis == null){
						Application.application().console().print(new ConsoleLine("Ennmies party not found (bad id).", Console.ERROR_COLOR_LINE));
					}
					else{
						Application.application().getGame().launchBattle(ennemis);
						Application.application().console().print(new ConsoleLine("Battle launched.", Configurations.DEFAULT_FONT_COLOR));
					}	
				} catch (SlickException e) {
					Application.application().console().print(new ConsoleLine(e.getMessage(), Console.ERROR_COLOR_LINE));
				}
		});
	}	
}
