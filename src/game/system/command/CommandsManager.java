package game.system.command;

import game.system.Console;
import game.system.ConsoleLine;
import game.system.application.Application;
import game.system.command.commands.BattleCommand;
import game.system.command.commands.multiplayer.AddPlayerCommand;
import game.system.command.commands.multiplayer.SetKeyCommand;

import java.util.HashMap;


public class CommandsManager {
	
	private static HashMap<String, Executor> commands;
	
	public static void init()
	{
		commands = new HashMap<String, Executor>();
		commands.put("battle", new BattleCommand());
		
		//multiplayer
		commands.put("add-player", new AddPlayerCommand());
		commands.put("key", new SetKeyCommand());
	}
	
	public static void execute(Command command){
		if(commands.containsKey(command.getCommand())){
			try {
				commands.get(command.getCommand()).execute(command);
			} catch (CommandParseException e) {
				Application.application().console().print(new ConsoleLine(e.getMessage(), Console.ERROR_COLOR_LINE));
			}
		}
		else{
			Application.application().console().print(new ConsoleLine("Command " + command.getCommand() + " does not exists.", Console.ERROR_COLOR_LINE));
		}
	}
}
