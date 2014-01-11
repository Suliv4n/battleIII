package personnage;

import game.Combat;

import java.util.ArrayList;

public interface IBattle 
{
	public int getAttaquePhysique();
	public int getAttaqueMagique();
	public int getDefensePhysique();
	public int getDefenseMagique();
	public int getDexterite();
	public int getSoin();
	
	public int getPV();
	public int getPVMaximum();
	public void updatePV(int dPV);
	public boolean estVivant();	
	
	public String getNom();
	
	public void setCibles(ArrayList<IBattle> cibles);
	public ArrayList<IBattle> getCibles();
	public void selectionnerCibleSuivante(Combat combat);
	
	public Object getAction();
	public void setAction(Object action);
	public void annulerAction();
}
