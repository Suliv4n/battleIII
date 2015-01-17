package game.system.command.rule;

import game.system.command.CommandParseException;

import java.util.Arrays;



public class ParameterRule{
	public static final int INTEGER = 0;
	public static final int POSITIF_INTEGER = 1;
	public static final int FLOAT = 2;
	public static final int STRING = 3;
	
	private String[] allowedValue;
	private int type;
	private String libelle;
	private boolean required;
	
	public ParameterRule(String libelle, int type, String[] allowedValues, boolean required ){
		this.libelle = libelle;
		this.type = type;
		this.allowedValue = allowedValues;
		this.required = required;
	}
	
	public void check(String value) throws CommandParseException{
		if(required && value == null){
			throw new CommandParseException("Parameter " + libelle + " is required.");
		}
		switch(type){
			case(INTEGER):
				if(!value.matches("^-?[0-9]*$"))
				{
					throw new CommandParseException("Parameter " + libelle + " should be integer." + value);
				}
			break;
			case(POSITIF_INTEGER):
				if(!value.matches("^-?[0-9]*$"))
				{
					throw new CommandParseException("Parameter " + libelle + " should be positif integer " + value);
				}
			break;
			case(FLOAT):
				if(!value.matches("^-?[0-9].[0-9]*$"))
				{
					throw new CommandParseException("Parameter " + libelle + " should be float integer " + value);
				}
			break;
		}
		if(allowedValue != null && allowedValue.length > 0 && !Arrays.asList(allowedValue).contains(value)){
			throw new CommandParseException("Parameter " + libelle + " : not allowed value. " + value);
		}
	}
	
	public boolean isRequired(){
		return required;
	}
}
