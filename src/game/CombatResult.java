package game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import audio.GestionnaireMusique;

import personnage.Equipe;
import personnage.Personnage;

import sac.IObjet;

public class CombatResult extends BasicGameState
{
	
	private static int po;
	private static int exp;
	private static ArrayList<IObjet> drops;
	

	public static void init(int po, int exp, ArrayList<IObjet> drops)
	{
		CombatResult.po = po;
		CombatResult.exp = exp;
		CombatResult.drops = drops;
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException 
	{
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException 
	{
		dessinerCadrePrincipal(g);
		dessinerEquipe(g);
	}



	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException 
	{
		Equipe equipe = Jeu.getEquipe();
		
		equipe.updatePO(po);
		po=0;
		
		if(exp>0)
		{
			for(Personnage p : equipe)
			{
				if(p.estVivant())
				{
					p.gagnerXP(1);
				}
			}
		}
		exp=Math.max(0, exp-1);
		
		
		gererInput(container.getInput(), sbg);
		container.getInput().clearKeyPressedRecord();
	}

	private void gererInput(Input in, StateBasedGame sbg) 
	{
		if(in.isKeyPressed(Input.KEY_RETURN))
		{
			if(exp > 0)
			{
				Jeu.getEquipe().updatePO(po);
				exp = 0;
			}
			else
			{	
				sbg.enterState(Config.EXPLORATION);
			}
		}
		
	}

	@Override
	public int getID()
	{
		return Config.COMBATRESULT;
	}
	
	private void dessinerCadrePrincipal(Graphics g) 
	{
		g.setColor(Config.couleur1);
		g.fillRect(2, 2, 638, 478);
		
		g.setColor(Config.couleur2);
		g.drawRect(0, 0, 639, 479);
		g.drawRect(1, 1, 637, 477);
		g.drawLine(0, 40, 640, 40);
		g.drawLine(0, 41, 640, 41);
	}
	
	private void dessinerEquipe(Graphics g) 
	{
		Equipe equipe = Jeu.getEquipe();
		int delta=100; // pixel entre 2 persos
		for(int i=0;i<equipe.nbPersonnages();i++)
		{
			//dessiner barre xp :
			g.setColor(Config.couleur2);
			g.drawRect(70,70+delta*i,200,20);
			g.setColor(Color.black);
			g.fillRect(71, 71+delta*i, 199, 19);
			g.setColor(Config.couleurXP);
			g.fillRect(71, 71+delta*i, (float) (equipe.get(i).getXP()/(double) (equipe.get(i).prochainNiveau())*199) , 19);
			System.out.println((double) (equipe.get(i).getXP()/equipe.get(i).prochainNiveau())*198);
			
			g.drawImage(equipe.get(i).getAnimation(Equipe.BAS).getImage(2), 10, 60+i*delta);
			g.setColor(Color.white);
			g.drawString(equipe.get(i).getNom(), 70, 45+delta*i);
			g.drawString("Exp. : " + equipe.get(i).getXP(), 500, 70+delta*i);
		}
	}

}
