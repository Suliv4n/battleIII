package game.system.command.commands.multiplayer;

import java.io.IOException;

import org.newdawn.slick.SlickException;

import characters.Mage;
import characters.Party;
import characters.Ranger;
import characters.Warrior;
import data.DataManager;
import game.system.application.Application;
import game.system.command.Command;
import game.system.command.Executor;
import game.system.command.Handler;
import game.system.command.rule.ParameterRule;

public class SetKeyCommand extends Executor{
	

	public SetKeyCommand() {
		super();
		
		addParameterRule(new ParameterRule("key",ParameterRule.STRING, null ,true));
		
		setScript(new Handler() {
			@Override
			public void go(Command command) {
				String key = command.getParameter(0);
				Application.application().getConnector().setKey(key);
			}
		});
	}
}
