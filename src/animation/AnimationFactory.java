package animation;

import game.Combat;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Triangulator;

import bag.item.stuff.Weapon;

import data.DataManager;

import personnage.IBattle;
import personnage.Character;



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
	public static BattleAnimation createAnimation(String id)
	{
		BattleAnimation animation = null;		
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
						ResourcesAnimitationManager.addImage("poing", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(18, 0, 23, 23));
						ResourcesAnimitationManager.addImage("etoile", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(0, 0, 17, 17));
					}
					if(frame >= 15)
					{
						Point p = coordonneesCibles.get(0);
						ResourcesAnimitationManager.getImage("poing").drawCentered(p.x, p.y);
					}
					if(frame <= 30)
					{
						Weapon arme = null;
						if(lanceur instanceof Character)
						{
							arme = ((Character)lanceur).getMainWeapon();
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
							ResourcesAnimitationManager.getImage("etoile").drawCentered(p.x, p.y);
						}
					}
					
					return frame == 30;
				}

			};
		}
		//----------------------------------------$id:bouleDeFeu--------------------------------------------------------------------------------------------------
		/*
		 * Affiche une boule de feu s'orientant et avançant vers la cible.
		 * (Compétence : Boule de feu)
		 */
		else if(id.equals("bouleDeFeu") || id.equals("debug"))
		{
			renderer = new IAnimation() {
				
				@Override
				public boolean playFrame(Graphics g, Point coordonneesLanceur,
						ArrayList<Point> coordonneesCibles, IBattle lanceur,
						ArrayList<IBattle> cibles, Combat combat, int frame)
						throws SlickException 
						
				{
					// déplacements totaux.
					double dx = coordonneesCibles.get(0).x - coordonneesLanceur.x;
					double dy = coordonneesCibles.get(0).y - coordonneesLanceur.y;
					double angle = Math.toDegrees(Math.atan(dy/dx));
					if(frame == 0)
					{
						ResourcesAnimitationManager.addImage("bouleDeFeu1", DataManager.loadImage("bouleDeFeu1"));
						ResourcesAnimitationManager.getImage("bouleDeFeu1").rotate((float)angle);
						ResourcesAnimitationManager.addImage("bouleDeFeu2", DataManager.loadImage("bouleDeFeu2"));
						ResourcesAnimitationManager.getImage("bouleDeFeu2").rotate((float)angle);
						ResourcesAnimitationManager.addImage("bouleDeFeu3", DataManager.loadImage("bouleDeFeu3"));
						ResourcesAnimitationManager.getImage("bouleDeFeu3").rotate((float)angle);
						ResourcesAnimitationManager.addImage("explosion", DataManager.loadImage("explosion"));
					}
					if(frame < 10)
					{
						ResourcesAnimitationManager.getImage("bouleDeFeu1").draw((float)(dx/30) * frame + coordonneesLanceur.x,(float)(dy/30) * frame + coordonneesLanceur.y , 2f);
					}
					else if (frame < 20)
					{
						ResourcesAnimitationManager.getImage("bouleDeFeu2").draw((float)(dx/30) * frame + coordonneesLanceur.x,(float)(dy/30) * frame + coordonneesLanceur.y, 2f);
					}
					else if(frame < 30)
					{
						ResourcesAnimitationManager.getImage("bouleDeFeu3").draw((float)(dx/30) * frame + coordonneesLanceur.x,(float)(dy/30) * frame + coordonneesLanceur.y, 2f);
					}
					else
					{
						ResourcesAnimitationManager.getImage("explosion").draw(coordonneesCibles.get(0).x, coordonneesCibles.get(0).y);
					}
					
					return frame == 40;
				}
			};
		}
		//----------------------------------------$id:recherche--------------------------------------------------------------------------------------------------
		/*
		 * Affiche un livre dont les pages se tournent sur le lanceur.
		 * (Compétence : Recherche)
		 */
		else if(id.equals("recherche"))
		{
			renderer = new IAnimation() {
				
				@Override
				public boolean playFrame(Graphics g, Point coordonneesLanceur,
						ArrayList<Point> coordonneesCibles, IBattle lanceur,
						ArrayList<IBattle> cibles, Combat combat, int frame)
						throws SlickException {
					if(frame == 0)
					{
						ResourcesAnimitationManager.addImage("livre1", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(0, 25, 27, 25));
						ResourcesAnimitationManager.addImage("livre2", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(28, 25, 27, 25));
						ResourcesAnimitationManager.addImage("livre3", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(56, 25, 27, 25));
					}
					else if(frame < 90)
					{
						if(frame % 30 < 10)
						{
							g.drawImage(ResourcesAnimitationManager.getImage("livre1"), (int)coordonneesLanceur.getX(), (int)coordonneesLanceur.getY());
						}
						else if(frame%30 < 20)
						{
							g.drawImage(ResourcesAnimitationManager.getImage("livre2"), (int)coordonneesLanceur.getX(), (int)coordonneesLanceur.getY());
						}
						else
						{
							g.drawImage(ResourcesAnimitationManager.getImage("livre3"), (int)coordonneesLanceur.getX(), (int)coordonneesLanceur.getY());
						}
					}
					return frame == 90;
				}
			};
		}
		//---------------------------------------$id:soin-----------------------------------------------------------------------------------------------------
		else if(id.equals("soin"))
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
						ResourcesAnimitationManager.addImage("etoile1", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(0, 81, 13, 13));
						ResourcesAnimitationManager.addImage("etoile2", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(14, 81, 13, 13));
						ResourcesAnimitationManager.addImage("etoile3", new Image("ressources/images/skill/skill1.png", new Color(255,0,255)).getSubImage(28, 83, 9, 9));
						
						ResourcesAnimitationManager.getImage("etoile1").setAlpha(0.75f);
						ResourcesAnimitationManager.getImage("etoile2").setAlpha(0.75f);
						ResourcesAnimitationManager.getImage("etoile3").setAlpha(0.75f);
					}
					else if(frame < 10)
					{
						g.drawImage(ResourcesAnimitationManager.getImage("etoile1"), cible.x - 16, cible.y - 16);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile2"), cible.x - 10, cible.y + 8);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile3"), cible.x + 5, cible.y - 5);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile3"), cible.x + 16, cible.y +16);	
					}
					else if(frame < 20)
					{
						g.drawImage(ResourcesAnimitationManager.getImage("etoile1"), cible.x + 15, cible.y - 4);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile2"), cible.x - 6, cible.y - 9);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile3"), cible.x + 5, cible.y + 15);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile3"), cible.x + 16, cible.y - 8);
					}
					else if(frame < 30)
					{
						g.drawImage(ResourcesAnimitationManager.getImage("etoile1"), cible.x  - 10, cible.y + 14);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile2"), cible.x - 6, cible.y + 9);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile3"), cible.x - 5, cible.y - 15);
						g.drawImage(ResourcesAnimitationManager.getImage("etoile3"), cible.x - 16, cible.y + 8);
					}
					
					return frame == 30;
				}
				
			};
		}
		
		
		animation = new BattleAnimation(renderer); 
		return animation;
	}
}
