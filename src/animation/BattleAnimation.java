package animation;

import game.Combat;
import game.battle.IBattle;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Animation pour les combats.
 * 
 * @author Darklev
 *
 */
public class BattleAnimation 
{
	private int frame;
	private IAnimation animation;
	
	/**
	 * Les instances d'Animation sont créées à partir de 
	 * la classe AnimationFactory.
	 */
	protected BattleAnimation(IAnimation animation)
	{
		this.animation = animation;
		frame = 0;
	}
	
	/**
	 * Joue la frame (affichage et son) en cours de l'Animation 
	 * 
	 * @param caster
	 * 		Le lanceur
	 * @param targets
	 * 		Les cibles

	 * @return
	 * 		Vrai si l'animation est terminée, faux sinon.
	 * 
	 * @throws SlickException
	 */
	public boolean render(IBattle caster, ArrayList<IBattle> targets ) throws SlickException
	{
		boolean res =  animation.render(caster, targets, frame);
		frame ++;
		if(res)
			frame = 0;
		return res;
	}
}
