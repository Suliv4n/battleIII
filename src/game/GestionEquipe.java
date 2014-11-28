package game;


import game.system.application.Application;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


import personnage.Party;
import personnage.Character;
import skill.Skill;
import ui.GUIList;
import ui.ListRenderer.ElementRenderer;

import data.Format;



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
	
	private int curseur = 0;
	private int curseur_action = 0;
	private int curseur_ordre = 0;
	
	//curseur absolue
	private int curseur_competences = 0;
	//curseur relatif
	private int premier_competences = 0;
	private Image fleche;
	
	private Character selection;
	
	
	
	private static final int ORDRE = 1;
	private static final int COMPETENCES = 2;
	
	private int action = -1;
	
	//Liste des skills;
	private HashMap<Character,GUIList<Skill>> listesSkill;
	
	//#endregion
	
	//#region ------OVERRIDE BASICGAMESTATE---
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
	{
		fleche = Application.application().getGame().getArrow(0);
		listesSkill = new HashMap<Character,GUIList<Skill>>();
		ElementRenderer renderer = new ElementRenderer() {
			
			@Override
			public void render(int x, int y, Object element, int index) {
				Graphics g = Application.application().getGraphics();
				g.setColor(Color.white);
				Skill skill = (Skill) element;
				g.drawString(skill.getName(), x+20, y);
			}
		};
		
		for(Character p : Application.application().getGame().getParty()){
			GUIList<Skill> liste = new GUIList<>(204, 440, 20, Config.couleur1, Config.couleur2, true);
			liste.setData(p.getSkills());
			liste.setElementRenderer(renderer);
			listesSkill.put(p, liste);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
	{
		Application.application().getGame().getParty().renderTeamList(g,curseur);
		Application.application().getGame().getParty().get(curseur).drawStatistiques(g);
		Application.application().getGame().getParty().get(curseur).drawStuff(g);
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
				curseur = (curseur + 1)%Application.application().getGame().getParty().numberOfCharacters();
			}
			else if(in.isKeyPressed(Input.KEY_UP))
			{
				curseur = (curseur + Application.application().getGame().getParty().numberOfCharacters() - 1)%Application.application().getGame().getParty().numberOfCharacters();
			}
			
			if (in.isKeyPressed(Input.KEY_ESCAPE))
			{
				in.clearKeyPressedRecord();
				game.enterState(Config.MENU);
			}
			
			if(in.isKeyPressed(Input.KEY_RETURN))
			{
				selection = Application.application().getGame().getParty().get(curseur);
			}
		}
		else
		{

			switch(action)
			{
			case(ORDRE):
				if(curseur == curseur_ordre)
				{
					curseur_ordre = (curseur_ordre + 1)%Application.application().getGame().getParty().numberOfCharacters();
				}
				
				if(in.isKeyPressed(Input.KEY_DOWN))
				{
					if((curseur_ordre + 1)%Application.application().getGame().getParty().numberOfCharacters() != curseur)
					{
						curseur_ordre = (curseur_ordre + 1)%Application.application().getGame().getParty().numberOfCharacters();
					}
					else
					{
						curseur_ordre = (curseur_ordre + 2)%Application.application().getGame().getParty().numberOfCharacters();
					}
				}
				else if(in.isKeyPressed(Input.KEY_UP))
				{
					if((curseur_ordre - 1)%Application.application().getGame().getParty().numberOfCharacters() != curseur)
					{
						curseur_ordre = (curseur_ordre - 1 + Application.application().getGame().getParty().numberOfCharacters())%Application.application().getGame().getParty().numberOfCharacters();
					}
					else
					{
						curseur_ordre = (curseur_ordre - 2 + Application.application().getGame().getParty().numberOfCharacters())%Application.application().getGame().getParty().numberOfCharacters();
					}
					
				}
				
				if (in.isKeyPressed(Input.KEY_ESCAPE))
				{
					in.clearKeyPressedRecord();
					action = -1;
				}
				
				if(in.isKeyPressed(Input.KEY_RETURN))
				{
					Application.application().getGame().getParty().swap(curseur, curseur_ordre);
					curseur = curseur_ordre;
					action = -1;
				}
				break;
			case(COMPETENCES):
				listesSkill.get(selection).update(in);
			
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
		g.drawImage(Application.application().getGame().getArrow(1),5,75+curseur_ordre*160);
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
	
	private void afficherCompetences(Character personnage, Graphics g) 
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
		//g.drawLine(200, 42, 200, 638);
		//g.drawLine(201, 42, 201, 638);
		
		
		g.setColor(Color.white);
		g.drawString("Compétences", 250, 10);
		g.drawImage(personnage.getAnimation(Party.SOUTH).getImage(2), 5,5);
		
		
		listesSkill.get(selection).render(0, 40);

		
		//Eviter le nullPointerException
		if(listesSkill.get(selection).size() !=0)
		{
			Skill skill = listesSkill.get(selection).getObject();
			g.drawString("Nom : "+skill.getName(),205,50);
			g.drawString("Description :\n"+Format.multiLines(skill.getDescription(),46),205,100);
			g.drawString("Puissance : "+skill.getPower(), 205, 200);
			g.drawString("Coût : "+skill.getConsommation(), 205, 220);
			g.drawString("Niveau : "+skill.getLevel()+"/"+skill.getNiveauMax(), 205, 240);
		}
	}
	//#endregion
}
