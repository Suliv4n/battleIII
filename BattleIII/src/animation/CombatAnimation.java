package animation;

import game.Combat;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import personnage.IBattle;

/**
 * Animation pour les combats.
 * 
 * @author Darklev
 *
 */
public class CombatAnimation 
{
	private int frame;
	private IAnimation animation;
	
	/**
	 * Les instances d'Animation sont créées à partir de 
	 * la classe AnimationFactory.
	 */
	protected CombatAnimation(IAnimation anim)
	{
		animation = anim;
		frame = 0;
	}
	
	/**
	 * Joue la frame (affichage et son) en cours de l'Animation 
	 * 
	 * @param g
	 * 		Grahics à utiliser
	 * @param coordonneesLanceur
	 * 		Coordonnées du lanceur
	 * @param coordonneesCibles
	 * 		Coordonnées des cibles
	 * @param lanceur
	 * 		Le lanceur
	 * @param cibles
	 * 		Les cibles
	 * @param combat
	 * 		Combat en cours.
	 * @return
	 * 		Vrai si l'animation est terminée, faux sinon.
	 * 
	 * @throws SlickException
	 */
	public boolean playFrame(Graphics g, Point coordonneesLanceur, ArrayList<Point> coordonneesCibles, 
			IBattle lanceur, ArrayList<IBattle> cibles , Combat combat) throws SlickException
	{
		boolean res =  animation.playFrame(g, coordonneesLanceur, coordonneesCibles, lanceur, cibles, combat, frame);
		frame ++;
		if(res)
			frame = 0;
		return res;
	}
}
