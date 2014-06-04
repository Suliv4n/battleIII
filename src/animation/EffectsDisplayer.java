package animation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Permet d'afficher les effets d'une action d'une attaque 
 * (dégât, analyse, soin...)
 * 
 * @author Darklev
 *
 */
public class EffectsDisplayer 
{
	private Point lanceur;
	private HashMap<Point, String> cibles; // exemple d'effets : 152;poison;attphy:152
	 
	private int index; // index parcourant les effets
	
	private int frame;
	
	public EffectsDisplayer(Point lanceur, HashMap<Point,String> cibles)
	{
		this.lanceur = lanceur;
		this.cibles = cibles;
		index = 0;
		frame = 0;
	}
	
	public EffectsDisplayer(HashMap<Point,String> cibles)
	{
		this.cibles = cibles;
		index = 0;
		frame = 0;
	}
	
	/**
	 * Affiche les effets de combat avec une animation sur 30 frames ( = 1 seconde)
	 * 
	 * @return
	 * 	Vrai si l'animation est terminée, faux sinon.
	 */
	public boolean afficherEffets(Graphics g)
	{
		//afficher les effets sur 30 frames (1s)
		g.setColor(Color.white);
		
		
		boolean b = true;
		if(frame <= 7)
		{
			//int coeff = frame < 15 ? -1 : 1;
			g.setColor(Color.white);

			
			for(Point p : cibles.keySet())
			{
				
			
				//FORMATAGE ET PARAMETRAGE DE L'AFFICHAGE DE L'EFFET EN COURS
				String effet = formatageEtParametrage(p, g);
				
				
				if(frame < 15)
				{
					g.drawString(effet, p.x, p.y - frame * 2);
				}
				else
				{
					g.drawString(effet, p.x, p.y + frame * 2 - 30);
				}
				
			}
			b = false;
		}
		else if(frame <= 30)
		{
			
			ArrayList<Point> aSupprimer = new ArrayList<Point>();
			
			for(Point p : cibles.keySet())
			{
				String effet = formatageEtParametrage(p, g);
				
				g.drawString(effet, p.x, p.y);
				
				//fin d'une ligne
				if(frame == 30 && index == cibles.get(p).split(";").length - 1)
				{
					aSupprimer.add(p);
				}
			}
			
			for(Point p : aSupprimer)
			{
				cibles.remove(p);
			}
			
			b = false;
			
		}
		
		frame ++;
		
		
		if(cibles.size() != 0 && b)
		{
			index ++;
			return false;
		}
		else
		{
			return b;
		}
	}
	
	private String formatageEtParametrage(Point p, Graphics g)
	{
		String[] lesEffets = cibles.get(p).split(";");
		String effet = lesEffets[index];
		//Formatage de l'effet à afficher
		if(effet.matches("-?[0-9]+"))
		{
			if(effet.contains("-"))
			{
				g.setColor(new Color(0,200,75));
			}
			effet = String.valueOf(Math.abs(Integer.parseInt(effet)));
		}
		else if(effet.equalsIgnoreCase("Analyse"))
		{
			effet = "Analysé";
		}
		else if(effet.equalsIgnoreCase("miss"))
		{
			effet = "Raté";
		}
		
		return effet;
	}
	
	
}
