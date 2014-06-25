package game;


import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import personnage.Equipe;
import personnage.Personnage;
import skill.Skill;

import donnees.Formatage;



/**
 * 
 * _-_-_-_-[State Gestion d'Equipe]-_-_-_-_-_
 * 
 * @author Darklev
 *
 */
public class GestionEquipe extends BasicGameState 
{
	
	//#region ----------PROPRIETES------------
	
	private Equipe equipe;
	private int curseur = 0;
	private int curseur_action = 0;
	private int curseur_ordre = 0;
	
	//curseur absolue
	private int curseur_competences = 0;
	//curseur relatif
	private int premier_competences = 0;
	private Image fleche;
	
	private Personnage selection;
	
	
	
	private static final int ORDRE = 1;
	private static final int COMPETENCES = 2;
	
	private int action = -1;
	
	//#endregion
	
	//#region ------OVERRIDE BASICGAMESTATE---
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		equipe = Jeu.getEquipe();
		fleche = Jeu.getFleche(0);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		equipe.afficherListeEquipe(g,curseur);
		equipe.get(curseur).afficherStats(g);
		equipe.get(curseur).afficherEquipements(g);
		afficherCurseur(g);
		if(selection != null)
		{
			genererMenu(g);
			afficherCurseurAction(g);
			
			switch(action)
			{
			case(ORDRE):
				afficherCurseurOrdre(g);
				break;
			case(COMPETENCES):
				afficherCompetences(selection,g);
			}
		}
	}




	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
	{
		
		Input in = container.getInput();
		in.disableKeyRepeat();
		
		if(selection == null)
		{		
			if(in.isKeyPressed(Input.KEY_DOWN))
			{
				curseur = (curseur + 1)%equipe.nbPersonnages();
			}
			else if(in.isKeyPressed(Input.KEY_UP))
			{
				curseur = (curseur + equipe.nbPersonnages() - 1)%equipe.nbPersonnages();
			}
			
			if (in.isKeyPressed(Input.KEY_ESCAPE))
			{
				in.clearKeyPressedRecord();
				game.enterState(Config.MENU);
			}
			
			if(in.isKeyPressed(Input.KEY_RETURN))
			{
				selection = equipe.get(curseur);
			}
		}
		else
		{

			switch(action)
			{
			case(ORDRE):
				if(curseur == curseur_ordre)
				{
					curseur_ordre = (curseur_ordre + 1)%equipe.nbPersonnages();
				}
				
				if(in.isKeyPressed(Input.KEY_DOWN))
				{
					if((curseur_ordre + 1)%equipe.nbPersonnages() != curseur)
					{
						curseur_ordre = (curseur_ordre + 1)%equipe.nbPersonnages();
					}
					else
					{
						curseur_ordre = (curseur_ordre + 2)%equipe.nbPersonnages();
					}
				}
				else if(in.isKeyPressed(Input.KEY_UP))
				{
					if((curseur_ordre - 1)%equipe.nbPersonnages() != curseur)
					{
						curseur_ordre = (curseur_ordre - 1 + equipe.nbPersonnages())%equipe.nbPersonnages();
					}
					else
					{
						curseur_ordre = (curseur_ordre - 2 + equipe.nbPersonnages())%equipe.nbPersonnages();
					}
					
				}
				
				if (in.isKeyPressed(Input.KEY_ESCAPE))
				{
					in.clearKeyPressedRecord();
					action = -1;
				}
				
				if(in.isKeyPressed(Input.KEY_RETURN))
				{
					equipe.swap(curseur, curseur_ordre);
					curseur = curseur_ordre;
					action = -1;
				}
				break;
			case(COMPETENCES):
				if(in.isKeyPressed(Input.KEY_DOWN) && selection.getSkills().size()!=0)
				{
					curseur_competences = (curseur_competences + 1)%selection.getSkills().size();
					if(curseur_competences>=21)
					{
						premier_competences++;
					}
					else if(curseur_competences == 0)
					{
						premier_competences = 0;
					}
				}
				else if(in.isKeyPressed(Input.KEY_UP) && selection.getSkills().size()!=0)
				{
					curseur_competences = (curseur_competences - 1 + selection.getSkills().size())%selection.getSkills().size();
					if(curseur_competences-premier_competences < 0)
					{
						premier_competences--;
					}
					else if(curseur_competences == selection.getSkills().size()-1)
					{
						premier_competences = Math.max(0,selection.getSkills().size()-21);
					}
				}
				if (in.isKeyPressed(Input.KEY_ESCAPE))
				{
					in.clearKeyPressedRecord();
					curseur_competences = 0;
					action = -1;
				}				
			default:
				if(in.isKeyPressed(Input.KEY_DOWN))
				{
					curseur_action = (curseur_action + 2)%4;
				}
				else if(in.isKeyPressed(Input.KEY_UP))
				{
					curseur_action = (curseur_action + 2 )%4;
				}
				
				if(in.isKeyPressed(Input.KEY_RIGHT))
				{
					curseur_action = (curseur_action + 1)%4;
				}
				else if(in.isKeyPressed(Input.KEY_LEFT))
				{
					curseur_action = (curseur_action + 3 )%4;
				}
				
				if (in.isKeyPressed(Input.KEY_ESCAPE))
				{
					selection = null;
				}
				
				if(in.isKeyPressed(Input.KEY_RETURN))
				{
					action = curseur_action;
				}
				
			}
		}
		
		
		
	}


	@Override
	public int getID() 
	{
		return Config.GESTIONNAIRE_EQUIPE;
	}
	
	private void afficherCurseur(Graphics g)
	{
		g.drawImage(fleche,5,75+curseur*160);
	}

	private void afficherCurseurAction(Graphics g) 
	{
		if(curseur_action <= 1)
		{
			g.drawImage(fleche, 270+(curseur_action%2)*166, 392);
		}
			
		else if(curseur_action <= 3)
		{
			g.drawImage(fleche, 270+(curseur_action%2)*166, 432);
		}
		
	}
	
	//#endregion

	//#region ------AFFICHAGE-----------------
	private void afficherCurseurOrdre(Graphics g) 
	{
		g.drawImage(Jeu.getFleche(1),5,75+curseur_ordre*160);
	}
	
	private void afficherCurseurCompetences(Graphics g) 
	{

		g.drawImage(fleche,5,55+20*(curseur_competences-premier_competences));
	}
	
	private void genererMenu(Graphics g) 
	{
		g.setColor(Config.couleur2);
		g.drawRect(257, 361, 382, 118);
		
		g.setColor(Config.couleur1);
		g.fillRect(258, 362, 381, 117);
		
		
		g.setColor(Color.white);
		g.drawString("Equipements",283,387);
		g.drawString("Compétences",283,427);
		g.drawString("Ordre",450,387);
		g.drawString("Stats",450,427);

	}
	
	private void afficherCompetences(Personnage personnage, Graphics g) 
	{
		/*
		g.setColor(Color.white);
		g.drawString("COMPETENCES", 250, 15);
		*/
		
		g.setColor(Config.couleur1);
		g.fillRect(2, 2, 638, 478);
		
		g.setColor(Config.couleur2);
		g.drawRect(0, 0, 639, 479);
		g.drawRect(1, 1, 637, 477);
		g.drawLine(0, 40, 640, 40);
		g.drawLine(0, 41, 640, 41);
		g.drawLine(200, 42, 200, 638);
		g.drawLine(201, 42, 201, 638);
		
		
		g.setColor(Color.white);
		g.drawString("Compétences", 250, 10);
		g.drawImage(personnage.getAnimation(Equipe.BAS).getImage(2), 5,5);
		
		ArrayList<Skill> lesSkills = selection.getSkills();
		
		
		g.setColor(Color.white);
		for(int i=premier_competences; i<lesSkills.size() && i<premier_competences+21; i++)
		{
			g.drawString(lesSkills.get(i).getNom(),20,50+20*(i-premier_competences));
		}

		afficherCurseurCompetences(g);
		
		//Eviter le nullPOinterException
		if(lesSkills.size()!=0)
		{
			g.drawString("Nom : "+lesSkills.get(curseur_competences).getNom(),205,50);
			g.drawString("Description :\n"+Formatage.multiLignes(lesSkills.get(curseur_competences).getDescription(),46),205,100);
			g.drawString("Puissance : "+lesSkills.get(curseur_competences).getPuissance(), 205, 200);
			g.drawString("Coût : "+lesSkills.get(curseur_competences).getConsommation(), 205, 220);
			
			g.drawString("Niveau : "+lesSkills.get(curseur_competences).getLevel()+"/"+lesSkills.get(curseur_competences).getNiveauMax(), 205, 240);
		}
	}
	//#endregion
}
