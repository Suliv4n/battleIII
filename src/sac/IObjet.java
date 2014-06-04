package sac;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public interface IObjet 
{
	public Image getIcone() throws SlickException;
	public String getNom();
	public boolean utilisableCombat();
	public boolean estRare();
	public ArrayList<String> getEffets();
	public String getDescription();
}

