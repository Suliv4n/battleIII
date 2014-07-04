package game;

import game.dialogue.Dialogue;
import game.dialogue.Replique;
import game.dialogue.Selectionneur;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import map.Coffre;
import map.Map;
import map.Portail;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import audio.GestionnaireMusique;

import donnees.Formatage;
import donnees.GenerateurDonnees;

import personnage.Equipe;
import personnage.EquipeEnnemis;
import personnage.PNJ;

import static org.lwjgl.opengl.GL11.*;

/**
 * 
 * _-_-_-_-[State Exploration]-_-_-_-_-_
 * 
 * @author Darklev
 *
 */
public class Exploration extends BasicGameState 
{

	//#region -----------PROPRIETES----------------
	
	private Equipe equipe;
	
	//Vrai si l'equipe est en mouvement.
	//Faux sinon.
	private boolean enMouvement;
	
	//Animation du personnage qui marche.
	private Animation animation;
	
	//dialogue en cours
	private Dialogue dialogue;
	private Selectionneur selectionneur;
	private int curseurSelect = 0;
	
	private ParticleSystem particles;
	
	//#endregion

	//#region ------OVERRIDE BASICGAMESTATE--------
	@Override
	public void init(GameContainer arg0, StateBasedGame sbg)
			throws SlickException 
	{
		/*
		particles = new ParticleSystem("ressources/images/particles/rain.png", 1500, new Color(255,0,255));
		
		File xmlFile = new File("ressources/particles/rain.xml");
		ConfigurableEmitter ce;
		try {
			ce = ParticleIO.loadEmitter(xmlFile);
			particles.addEmitter(ce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		equipe = Jeu.getEquipe();
		animation = equipe.getAnimation();
		
		//Jeu.saisir(null, "Nom du rôdeur :", "Tell", this.getID(), sbg);
	}

	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
			throws SlickException 
	{
		
		
		Map map = equipe.getMap();
		
		map.afficherLayer(0);
		map.afficherLayer(1);
		
		double X = equipe.getRelativeX();
		double Y = equipe.getRelativeY();
		

		
		if(equipe.getRelativeX()<Map.LONG/2)
		{
			X = equipe.getAbsolueX();
		}
		else if(equipe.getRelativeX()>Map.LONG/2)
		{
			X = Map.LONG-equipe.getMap().getTiledMap().getWidth()*32+equipe.getAbsolueX();
		}
		if(equipe.getRelativeY()<Map.LARG/2)
		{
			Y = equipe.getAbsolueY();
		}
		else if(equipe.getRelativeY()>Map.LARG/2)
		{
			Y = Map.LARG - equipe.getMap().getTiledMap().getHeight()*32+ equipe.getAbsolueY();
		}
			
		X -= animation.getWidth()/2;
		Y -= animation.getHeight()/2;
				
		
		map.afficherPNJ();
		//afficher equipe
		if(enMouvement)
		{		
			g.drawAnimation(animation, (int) X, (int) Y);
		}
		else
		{
			g.drawImage(animation.getImage(2),(int) X, (int) Y);
		}
		
		
		
		map.afficherLayer(2);
		map.afficherLayer(3);
		map.afficherLayer(4);
		
		if(map.getExterieur())
		{
			afficherFiltreTemporel(g);
		}
		
		//AFFICHAGE DU DIALOGUE
		if(dialogue != null)
		{
			afficherDialogue(g);
		}
		//AFFICHAGE DU DIALOGUE
		
		//particles.render();
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException 
	{	
		//particles.update(delta);
		
		Input in = container.getInput();
		in.disableKeyRepeat();
		
		if(dialogue == null)
		{		
			int X = (int) (equipe.getAbsolueX());
			int Y = (int) (equipe.getAbsolueY());
			EquipeEnnemis ennemis = null; //Prend une valeur en cas de rencontre si mouvement
			
			if(!animation.equals(equipe.getAnimation()))
			{
				animation = equipe.getAnimation();
			}
			
			//GESTION DES MOUVEMENTS + INTERACTION________________________________________________
			int d = 0;
			if(in.isKeyDown(Input.KEY_LEFT))
			{
				d = equipe.getMap().distance(X, Y, animation.getWidth(), animation.getHeight(), (int) (-equipe.vitesse()), 0);
				double marge = equipe.getMap().scrollX(d);
				if(marge == 0)
				{
					equipe.setValRelativeX(Map.LONG/2);
				}
				else
				{
					equipe.setRelativeX(marge);
				}
				
				equipe.setAbsolueX(d);
				if(equipe.getDirection() != Equipe.GAUCHE)
				{
					equipe.setDirection(Equipe.GAUCHE);
					animation = equipe.getAnimation();
				}
				
				enMouvement = true;			
			}		
			else if(in.isKeyDown(Input.KEY_RIGHT))
			{
				d = equipe.getMap().distance(X, Y, animation.getWidth(), animation.getHeight(), (int) (equipe.vitesse()), 0);
				double marge = equipe.getMap().scrollX(d);
				if(marge == 0)
				{
					equipe.setValRelativeX(Map.LONG/2);
				}
				else
				{
					equipe.setRelativeX(marge);
				}
				equipe.setAbsolueX(d);
				
				if(equipe.getDirection() != Equipe.DROITE)
				{
					equipe.setDirection(Equipe.DROITE);
					animation = equipe.getAnimation();
				}
				enMouvement = true;
			}
			
			else if(in.isKeyDown(Input.KEY_UP))
			{
				d = equipe.getMap().distance(X, Y, animation.getWidth(), animation.getHeight(),0 , (int) (-equipe.vitesse()));
				double marge = equipe.getMap().scrollY(d);
				if(marge == 0)
				{
					equipe.setValRelativeY(Map.LARG/2);
				}
				else
				{
					equipe.setRelativeY(marge);
				}
				equipe.setAbsolueY(d);
				
				if(equipe.getDirection() != Equipe.HAUT)
				{
					equipe.setDirection(Equipe.HAUT);
					animation = equipe.getAnimation();
				}
				enMouvement = true;
			}
			
			else if(in.isKeyDown(Input.KEY_DOWN))
			{
				d = equipe.getMap().distance(X, Y, animation.getWidth(), animation.getHeight(),0 , (int) (equipe.vitesse()));
				double marge = equipe.getMap().scrollY(d);
	
				if(marge == 0)
				{
					equipe.setValRelativeY(Map.LARG/2);
				}
				else
				{
					equipe.setRelativeY(marge);
				}
				equipe.setAbsolueY(d);
		
				
				if(equipe.getDirection() != Equipe.BAS)
				{
					equipe.setDirection(Equipe.BAS);
					animation = equipe.getAnimation();
				}
				enMouvement = true;
			}
			//INTERACTION_____________________________________________
			else if(in.isKeyPressed(Input.KEY_RETURN))
			{
				int intX=0;
				int intY=0;
				if(equipe.getDirection() == Equipe.HAUT)
				{
					intX = (int) ((equipe.getAbsolueX())/32);
					intY = (int) ((equipe.getAbsolueY()-16)/32);
				}
				else if(equipe.getDirection() == Equipe.BAS)
				{
					intX = (int) ((equipe.getAbsolueX())/32);
					intY = (int) ((equipe.getAbsolueY()+16)/32);
				}
				else if(equipe.getDirection() == Equipe.GAUCHE)
				{
					intX = (int) ((equipe.getAbsolueX()-16)/32);
					intY = (int) ((equipe.getAbsolueY())/32);
				}
				else if(equipe.getDirection() == Equipe.DROITE)
				{
					intX = (int) ((equipe.getAbsolueX()+16)/32);
					intY = (int) ((equipe.getAbsolueY())/32);
				}			
				
				if(equipe.getDirection() == Equipe.HAUT)
				{
					
					Coffre coffre = equipe.getMap().getCoffre(intX, intY);
					if(coffre != null)
					{
						if(!Jeu.estCoffreOuvert(coffre))
						{
							ArrayList<Replique> rep = new ArrayList<Replique>();
							if(coffre.getContenu() != null)
							{
								rep.add(new Replique("Vous trouvez : " + coffre.getContenu() + " x " + coffre.getQuantite())); 
							}
							else if(coffre.getPO() > 0)
							{
								rep.add(new Replique("Vous trouvez : " + coffre.getPO() + " pièces d'or")); 
							}
							else
							{
								rep.add(new Replique("Le coffre est vide."));
							}
							coffre.recupererContenu(equipe);
							equipe.getMap().updateCoffreSprite(coffre.getX(), coffre.getY());
							dialogue = new Dialogue(rep);
							dialogue.suivant();
						}
					}
				}
				
				//-------------------PNJ-----------------------------
				PNJ pnj = equipe.getMap().getPNJ(intX, intY);
				if(pnj != null)
				{
					dialogue = pnj.getDialogue();
					if (!dialogue.suivant()) {dialogue = null;}
				}
			}
			if(d == 0)
			{
				enMouvement = false;
			}
			else
			{
				//Activer portail -- Detecter portail au pied du personnage (en bas du sprite)
				Portail portail = equipe.getMap().getPortail((int) ((equipe.getAbsolueX())/32), (int) ((equipe.getAbsolueY()+16)/32));
				if(portail != null)
				{
					equipe.setMap(GenerateurDonnees.genererMap(portail.getTarget(), (int) (- portail.getXT()*32 - 16 + Config.LONGUEUR/2), ((int) - portail.getYT()*32 - 16 + Config.LARGEUR/2), true));
					System.out.println("new coords : " + (int) (portail.getXT()*32 - Config.LONGUEUR/2)+ "  -  " + ((int) portail.getYT()*32 - Config.LARGEUR/2));
					equipe.setValAbsolueX(portail.getXT() * 32 +16);
					equipe.setValAbsolueY(portail.getYT() * 32 +16);
					
				}
				else
				{
					//_-_-_-GENERER COMBAT-_-_-_
					ennemis = equipe.getMap().genererEnnemis();
					if(ennemis != null)
					{
						in.clearKeyPressedRecord();
						enMouvement = false;
						Jeu.entrerCombat(equipe, ennemis, sbg);
					}
				}
			}		
			//FIN GESTION MOUVEMENT___________________________________________________		
			
			Map map = equipe.getMap();
			
			//GESTION MUSIQUE---NE PAS JOUER MUSIQUE SI EQUIPEENNEMIS
			//JouerEnBoucle() ne fait rien si la musique est déjà en train de jouer.
			if( map.getMusic() == null)
			{
				GestionnaireMusique.stopper();
			}
			else
			{
				if(ennemis == null)
				{
					GestionnaireMusique.jouerEnBoucle(map.getMusic());
				}
			}
			
			if(in.isKeyPressed(Input.KEY_TAB))
			{
				in.clearKeyPressedRecord();
				sbg.enterState(Config.MENU);
			}
		}
		
		//DIALOGUE_____________________________________________________________________
		else
		{
			if(selectionneur == null && dialogue.getSelectionneur() != null)
			{
				selectionneur = dialogue.getSelectionneur();
			}
			
			if(selectionneur == null)
			{
				if(in.isKeyPressed(Input.KEY_RETURN))
				{
					 if(!dialogue.suivant())
					 {
						 dialogue = null;
						 in.clearKeyPressedRecord();
					 }
				}
			}
			else
			{
				gererSelectionneur(container.getInput());
			}
		}
		
		equipe.getMap().updateAnimatedTile();
		
		in.clearKeyPressedRecord();
	}

	@Override
	public int getID() 
	{		
		return Config.EXPLORATION;
	}
	
	//#endregion
	
	//#region --------AFFICHAGE--------------------
	
	/**
	 * Affiche un filtre selon l'heure à laquelle joue le joueur.
	 * 
	 * @param g
	 * 		Grpahics pour l'affichage.
	 */
	private void afficherFiltreTemporel(Graphics g) 
	{
	    Date dateDepart = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH");
	    int heure = Integer.parseInt(sdf.format(dateDepart));
	    
	    if(heure>=21 || heure<=6)
	    {
	    	//nuit
	    	g.setColor(new Color(000,000,100,50));	    	
	    }
	    else if(heure>=7 && heure<=9)
	    {
	    	//matin
	    	g.setColor(new Color(200,200,255,40));
	    }
	    else if(heure>=10 && heure<=17)
	    {
	    	//journée
	    	g.setColor(new Color(000,000,000,000));
	    }
	    else
	    {
	    	//soir
	    	g.setColor(new Color(150,50,000,50));	
	    }
		g.fillRect(0, 0, Map.LONG, Map.LARG);
	}

	private void afficherDialogue(Graphics g)
	{
		g.setColor(Config.couleur2);
		g.drawRect(0, 400, 640, 80);
		g.setColor(Config.couleur1);
		g.fillRect(1, 401, 639, 79);
		g.setColor(Color.white);
		g.setFont(Jeu.getFont());
		//Jeu.getFont().drawString(10,405, Formatage.multiLignes(dialogue.get().getFormatStandard(),70));
		g.drawString(Formatage.multiLignes(dialogue.get().getFormatStandard(),70),10,405);
		if(selectionneur != null)
		{
			int l = selectionneur.getDimensionBoite().width;
			int h = selectionneur.getDimensionBoite().height;
			g.setColor(Config.couleur2);
			g.drawRect(0, 400 - h, l, h);
			g.setColor(Config.couleur1);
			g.fillRect(1, 401 - h, l-1 , h - 1);
			g.setColor(Color.white);
			g.drawImage(Jeu.getFleche(0), 2, (406 - h) + 20 * curseurSelect);
			
			int i = 0;
			for(String c : selectionneur.getChoix())
			{
				g.drawString(c, 13, (400 - h) + i * 20);
				i++;
			}
		}
	}
	
	
	//#endregion
	
	//#region -----------GESTION INPUT-------------
	/**
	 * Gère le sélectionneur en cours.
	 * 
	 * @param in
	 * 		Entrée d'événements
	 */
	private void gererSelectionneur(Input in)
	{
		if(in.isKeyPressed(Input.KEY_DOWN))
		{
			curseurSelect = (curseurSelect + 1) % selectionneur.nbChoix();
		}
		else if(in.isKeyPressed(Input.KEY_UP))
		{
			curseurSelect = (curseurSelect + selectionneur.nbChoix() - 1) % selectionneur.nbChoix();
		}
		else if(in.isKeyPressed(Input.KEY_RETURN))
		{
			
			dialogue.validerSelectionneur(curseurSelect);
			if(dialogue.estTermine())
			{
				dialogue = null;
			}
			curseurSelect = 0;
			selectionneur = null;
		}
	}
	//#endregion

}
