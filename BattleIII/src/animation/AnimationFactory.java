package animation;

import game.Combat;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Triangulator;

import personnage.IBattle;
import personnage.Personnage;

import sac.objet.stuff.Arme;


/**
 * Classe permettant d'instancier les Animations pour le combat.
 * 
 * @author Darklev
 *
 */
public class AnimationFactory 
{
	
	/**
	 * Retourne une Animation pour le combat en fonction de l'id passé en paramètre (id d'un skill/objet, "attaquer", "défense" ...).
	 * 
	 * @param id
	 * 		L'id de l'animation à retourner.
	 * @return
	 * 		L'animation correspondant à l'id.
	 */
	public static CombatAnimation creerAnimation(String id)
	{
		CombatAnimation animation = null;		
		IAnimation renderer = null;
		
		//-----------------------------$id:attaquer-----------------------------------------------------------------------------------------------------
		if(id.equals("attaquer"))
		{
			renderer = new IAnimation() 
			{
				
				@Override
				public boolean playFrame(Graphics g, Point coordonneesLanceur,
						ArrayList<Point> coordonneesCibles, IBattle lanceur,
						ArrayList<IBattle> cibles, Combat combat, int frame) throws SlickException 
				{
					if(frame == 0)
					{
						RessourceAnimationLoader.ajouterImage("poing", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(18, 0, 23, 23));
						RessourceAnimationLoader.ajouterImage("etoile", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(0, 0, 17, 17));
					}
					if(frame >= 15)
					{
						Point p = coordonneesCibles.get(0);
						RessourceAnimationLoader.getImage("poing").drawCentered(p.x, p.y);
					}
					if(frame <= 30)
					{
						Arme arme = null;
						if(lanceur instanceof Personnage)
						{
							arme = ((Personnage)lanceur).getArmePrincipale();
						}
						
						if(arme != null)
						{
							/*
							Image img = arme.getImage();
							img.rotate(frame*3);
							
							Point p = coordonneesCibles.get(0);
							img.drawCentered(p.x, p.y);*/
						}
						else
						{
							Point p = coordonneesCibles.get(0);
							RessourceAnimationLoader.getImage("etoile").drawCentered(p.x, p.y);
						}
					}
					
					return frame == 30;
				}

			};
		}
		//----------------------------------------$id:m1--------------------------------------------------------------------------------------------------
		/*
		 * Affiche une boule de feu s'orientant et avançant vers la cible.
		 * (Compétence : Boule de feu)
		 */
		else if(id.equals("m1") || id.equals("debug"))
		{
			renderer = new IAnimation() {
				
				@Override
				public boolean playFrame(Graphics g, Point coordonneesLanceur,
						ArrayList<Point> coordonneesCibles, IBattle lanceur,
						ArrayList<IBattle> cibles, Combat combat, int frame)
						throws SlickException 
						
				{
					//déplacements totaux.
					double dx = coordonneesCibles.get(0).x - coordonneesLanceur.x;
					double dy = coordonneesCibles.get(0).y - coordonneesLanceur.y;
					double angle = Math.toDegrees(Math.atan(dy/dx));
					if(frame == 0)
					{
						RessourceAnimationLoader.ajouterImage("bouleFeu1", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(0, 55, 22, 17));
						RessourceAnimationLoader.getImage("bouleFeu1").rotate((float)angle);
						RessourceAnimationLoader.ajouterImage("bouleFeu2", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(23, 55, 22, 17));
						RessourceAnimationLoader.getImage("bouleFeu2").rotate((float)angle);
						RessourceAnimationLoader.ajouterImage("bouleFeu3", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(46, 55, 22, 17));
						RessourceAnimationLoader.getImage("bouleFeu3").rotate((float)angle);
						RessourceAnimationLoader.ajouterImage("explosion", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(74, 54, 23, 22));
					}
					if(frame < 10)
					{
						RessourceAnimationLoader.getImage("bouleFeu1").draw((float)(dx/30) * frame + coordonneesLanceur.x,(float)(dy/30) * frame + coordonneesLanceur.y , 2f);
					}
					else if (frame < 20)
					{
						RessourceAnimationLoader.getImage("bouleFeu1").draw((float)(dx/30) * frame + coordonneesLanceur.x,(float)(dy/30) * frame + coordonneesLanceur.y, 2f);
					}
					else if(frame < 30)
					{
						RessourceAnimationLoader.getImage("bouleFeu1").draw((float)(dx/30) * frame + coordonneesLanceur.x,(float)(dy/30) * frame + coordonneesLanceur.y, 2f);
					}
					else
					{
						RessourceAnimationLoader.getImage("explosion").draw(coordonneesCibles.get(0).x, coordonneesCibles.get(0).y);
					}
					
					return frame == 40;
				}
			};
		}
		//----------------------------------------$id:m2--------------------------------------------------------------------------------------------------
		/*
		 * Affiche un livre dont les pages se tournent sur le lanceur.
		 * (Compétence : Recherche)
		 */
		else if(id.equals("m2"))
		{
			renderer = new IAnimation() {
				
				@Override
				public boolean playFrame(Graphics g, Point coordonneesLanceur,
						ArrayList<Point> coordonneesCibles, IBattle lanceur,
						ArrayList<IBattle> cibles, Combat combat, int frame)
						throws SlickException {
					if(frame == 0)
					{
						RessourceAnimationLoader.ajouterImage("livre1", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(0, 25, 27, 25));
						RessourceAnimationLoader.ajouterImage("livre2", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(28, 25, 27, 25));
						RessourceAnimationLoader.ajouterImage("livre3", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(56, 25, 27, 25));
					}
					else if(frame < 90)
					{
						if(frame % 30 < 10)
						{
							g.drawImage(RessourceAnimationLoader.getImage("livre1"), (int)coordonneesLanceur.getX(), (int)coordonneesLanceur.getY());
						}
						else if(frame%30 < 20)
						{
							g.drawImage(RessourceAnimationLoader.getImage("livre2"), (int)coordonneesLanceur.getX(), (int)coordonneesLanceur.getY());
						}
						else
						{
							g.drawImage(RessourceAnimationLoader.getImage("livre3"), (int)coordonneesLanceur.getX(), (int)coordonneesLanceur.getY());
						}
					}
					return frame == 90;
				}
			};
		}
		//---------------------------------------$id:m4-----------------------------------------------------------------------------------------------------
		else if(id.equals("m4"))
		{
			renderer = new IAnimation() {

				@Override
				public boolean playFrame(Graphics g, Point coordonneesLanceur,
						ArrayList<Point> coordonneesCibles, IBattle lanceur,
						ArrayList<IBattle> cibles, Combat combat, int frame)
						throws SlickException {
					
					Point cible = coordonneesCibles.get(0);
					
					if(frame == 0)
					{
						RessourceAnimationLoader.ajouterImage("etoile1", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(0, 81, 13, 13));
						RessourceAnimationLoader.ajouterImage("etoile2", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(14, 81, 13, 13));
						RessourceAnimationLoader.ajouterImage("etoile3", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(28, 83, 9, 9));
						
						RessourceAnimationLoader.getImage("etoile1").setAlpha(0.75f);
						RessourceAnimationLoader.getImage("etoile2").setAlpha(0.75f);
						RessourceAnimationLoader.getImage("etoile3").setAlpha(0.75f);
					}
					else if(frame < 10)
					{
						g.drawImage(RessourceAnimationLoader.getImage("etoile1"), cible.x - 16, cible.y - 16);
						g.drawImage(RessourceAnimationLoader.getImage("etoile2"), cible.x - 10, cible.y + 8);
						g.drawImage(RessourceAnimationLoader.getImage("etoile3"), cible.x + 5, cible.y - 5);
						g.drawImage(RessourceAnimationLoader.getImage("etoile3"), cible.x + 16, cible.y +16);	
					}
					else if(frame < 20)
					{
						g.drawImage(RessourceAnimationLoader.getImage("etoile1"), cible.x + 15, cible.y - 4);
						g.drawImage(RessourceAnimationLoader.getImage("etoile2"), cible.x - 6, cible.y - 9);
						g.drawImage(RessourceAnimationLoader.getImage("etoile3"), cible.x + 5, cible.y + 15);
						g.drawImage(RessourceAnimationLoader.getImage("etoile3"), cible.x + 16, cible.y - 8);
					}
					else if(frame < 30)
					{
						g.drawImage(RessourceAnimationLoader.getImage("etoile1"), cible.x  - 10, cible.y + 14);
						g.drawImage(RessourceAnimationLoader.getImage("etoile2"), cible.x - 6, cible.y + 9);
						g.drawImage(RessourceAnimationLoader.getImage("etoile3"), cible.x - 5, cible.y - 15);
						g.drawImage(RessourceAnimationLoader.getImage("etoile3"), cible.x - 16, cible.y + 8);
					}
					
					return frame == 30;
				}
				
			};
		}
		
		
		animation = new CombatAnimation(renderer); 
		return animation;
	}
}
