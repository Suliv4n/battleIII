package game.system.command.commands.multiplayer;

import game.system.application.Application;
import game.system.command.Command;
import game.system.command.Executor;
import game.system.command.rule.ParameterRule;

import org.newdawn.slick.SlickException;

import characters.Mage;
import characters.Party;
import characters.Ranger;
import characters.Warrior;
import data.DataManager;

public class AddPlayerCommand extends Executor{
	public AddPlayerCommand() {
		super();
		
		addParameterRule(new ParameterRule("key",ParameterRule.STRING, null ,true));
		addParameterRule(new ParameterRule("map",ParameterRule.STRING, null ,true));
		addParameterRule(new ParameterRule("x",ParameterRule.INTEGER, null ,true));
		addParameterRule(new ParameterRule("y",ParameterRule.INTEGER, null ,true));
		
		addParameterRule(new ParameterRule("type1",ParameterRule.STRING, null ,true));
		addParameterRule(new ParameterRule("name1",ParameterRule.STRING, null ,true));
		addParameterRule(new ParameterRule("type2",ParameterRule.STRING, null ,false));
		addParameterRule(new ParameterRule("name2",ParameterRule.STRING, null ,false));
		addParameterRule(new ParameterRule("type3",ParameterRule.STRING, null ,false));
		addParameterRule(new ParameterRule("name3",ParameterRule.STRING, null ,false));
		
		
		
		setScript(command -> {

				String key = command.getParameter(0);
				String map = command.getParameter(1);
				int x = command.getIntegerParameter(2);
				int y = command.getIntegerParameter(3);
				
				String[] types = new String[]{
						command.getParameter(4),
						command.getParameter(6),
						command.getParameter(8)
						};
				String[] names = new String[]{
						command.getParameter(5),
						command.getParameter(7),
						command.getParameter(9)
						};
				
				try {
					//Party party = Application.application().getConnector().getPlayer(key);
					//if(party == null){
					Party party = new Party(3);
					//}
					
					for(int i=0; i<types.length; i++){
						String type = types[i];
						String name = names[i];
						if(type.equalsIgnoreCase("mage")){
							party.add(new Mage(name));
						}
						else if(type.equalsIgnoreCase("warrior")){
							party.add(new Warrior(name));
						}
						else if(type.equalsIgnoreCase("ranger")){
							party.add(new Ranger(name));
						}
					}
					party.setValAbsoluteX(x);
					party.setValAbsoluteY(y);
					party.setMap(DataManager.loadMap(map, 0, 0, false));
					Application.application().getConnector().addPlayer(key, party);
				} catch (SlickException e) {
					e.printStackTrace();
				}
				
		});
	}	
}
