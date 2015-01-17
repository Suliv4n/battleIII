package game.system.command;

import game.system.application.Application;
import game.system.command.rule.ParameterRule;

import java.util.ArrayList;

public class Executor {
	private ArrayList<ParameterRule> parameterRules;
	private Handler script; 
	private Command command;
	private ArrayList<Integer> allowedStates;
	
	public Executor(){
		parameterRules = new ArrayList<ParameterRule>();
		allowedStates = new ArrayList<Integer>();
	}
	
	public void setScript(Handler script)
	{
		this.script = script;
	}
	
	public void addParameterRule(ParameterRule rule){
		parameterRules.add(rule);
	}
	
	public void addAllowedState(int state){
		allowedStates.add(state);
	}
	
	public final void check() throws CommandParseException{
		int i = 0;
		int currentState = Application.application().getGame().getCurrentState().getID();
		if(allowedStates.size() > 0 && !allowedStates.contains(currentState)){
			throw new CommandParseException("State not allowed. " + "(" + currentState + " - " + allowedStates.get(0) + ")");
		}
		for(ParameterRule r : parameterRules){
			r.check(command.getParameter(i));
			i++;
		}
	}
	
	public void execute(Command command) throws CommandParseException{
		this.command = command;
		check();
		script.go(command);
	}
	
	public Command getCommand(){
		return command;
	}
}
