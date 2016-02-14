package game.system.command.commands.multiplayer;

import game.system.application.Application;
import game.system.command.Executor;
import game.system.command.rule.ParameterRule;

public class SetKeyCommand extends Executor{
	

	public SetKeyCommand() {
		super();
		
		addParameterRule(new ParameterRule("key",ParameterRule.STRING, null ,true));
		
		setScript(command -> {
			String key = command.getParameter(0);
			Application.application().getConnector().setKey(key);
		});
	}
}
