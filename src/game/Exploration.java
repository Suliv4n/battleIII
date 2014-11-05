package game;

import game.dialogue.Dialogue;
import game.dialogue.Replique;
import game.dialogue.Selectionneur;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import map.Coffre;
import map.Commande;
import map.Map;
import map.Portail;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import audio.GestionnaireMusique;

import donnees.Formatage;
import donnees.GenerateurDonnees;


import personnage.Equipe;
import personnage.EquipeEnnemis;
import personnage.PNJ;


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
	
	private StateBasedGame game;
	
	
	//Vrai si l'Jeu.getEquipe() est en mouvement.
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
		this.game = sbg;

		
		ArrayList<Replique> repliques = new ArrayList<Replique>();
		repliques.add(new Replique("Hello world!", "Sul"));
		repliques.add(new Replique("Ceci est un dialogue", "Sul"));
		repliques.add(new Replique("Doit je répéter? $select[Oui{0};Non;Quitter{end}]", "Sul"));
		repliques.add(new Replique("Au revoir"));
		this.dialogue = new Dialogue(repliques);
		dialogue.suivant();
		
		particles = new ParticleSystem("ressources/images/particles/rain.png", 1500, new Color(255,0,255));
		
		File xmlFile = new File("ressources/particles/rain.xml");
		ConfigurableEmitter ce;
		try {
			ce = ParticleIO.loadEmitter(xmlFile);
			particles.addEmitter(ce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//animation = Jeu.getEquipe().getAnimation();
		
		//Jeu.saisir(null, "Nom du rôdeur :", "Tell", this.getID(), sbg);
	}

	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
			throws SlickException 
	{
		if(animation == null){animation = Jeu.getEquipe().getAnimation();}
		
		Map map = Jeu.getEquipe().getMap();
		
		map.afficherLayer(0);
		map.afficherLayer(1);
		
		double X = Jeu.getEquipe().getRelativeX();
		double Y = Jeu.getEquipe().getRelativeY();
		

		
		if(Jeu.getEquipe().getRelativeX()<Map.LONG/2)
		{
			X = Jeu.getEquipe().getAbsolueX();
		}
		else if(Jeu.getEquipe().getRelativeX()>Map.LONG/2)
		{
			X = Map.LONG-Jeu.getEquipe().getMap().getTiledMap().getWidth()*32+Jeu.getEquipe().getAbsolueX();
		}
		if(Jeu.getEquipe().getRelativeY()<Map.LARG/2)
		{
			Y = Jeu.getEquipe().getAbsolueY();
		}
		else if(Jeu.getEquipe().getRelativeY()>Map.LARG/2)
		{
			Y = Map.LARG - Jeu.getEquipe().getMap().getTiledMap().getHeight()*32+ Jeu.getEquipe().getAbsolueY();
		}
			
		X -= animation.getWidth()/2;
		Y -= animation.getHeight()/2;
				
		
		map.afficherPNJ();
		//afficher Jeu.getEquipe()
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
		
		particles.render();
		
		//AFFICHAGE DU DIALOGUE
		if(dialogue != null)
		{
			afficherDialogue(g);
		}
		//!AFFICHAGE DU DIALOGUE
		
		//test.render(50, 50);
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException 
	{	
		particles.update(delta);
		
		Input in = container.getInput();
		
		in.disableKeyRepeat();
		
		
		if(dialogue == null)
		{		
			int X = (int) (Jeu.getEquipe().getAbsolueX());
			int Y = (int) (Jeu.getEquipe().getAbsolueY());
			EquipeEnnemis ennemis = null; //Prend une valeur en cas de rencontre si mouvement
			
			if(!animation.equals(Jeu.getEquipe().getAnimation())){
				animation = Jeu.getEquipe().getAnimation();
			}
			
			//GESTION DES MOUVEMENTS + INTERACTION________________________________________________
			int d = 0;
			
			
			if(in.isKeyDown(Input.KEY_LEFT) || in.isControllerLeft(0))
			{
				d = Jeu.getEquipe().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(), (int) (-Jeu.getEquipe().vitesse()), 0);
				double marge = Jeu.getEquipe().getMap().scrollX(d);
				if(marge == 0)
				{
					Jeu.getEquipe().setValRelativeX(Map.LONG/2);
				}
				else
				{
					Jeu.getEquipe().setRelativeX(marge);
				}
				
				Jeu.getEquipe().setAbsolueX(d);
				Jeu.getEquipe();
				if(Jeu.getEquipe().getDirection() != Equipe.GAUCHE)
				{
					Jeu.getEquipe();
					Jeu.getEquipe().setDirection(Equipe.GAUCHE);
					animation = Jeu.getEquipe().getAnimation();
				}
				
				enMouvement = true;			
			}		
			else if(in.isKeyDown(Input.KEY_RIGHT) || in.isControllerRight(0))
			{
				d = Jeu.getEquipe().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(), (int) (Jeu.getEquipe().vitesse()), 0);
				double marge = Jeu.getEquipe().getMap().scrollX(d);
				if(marge == 0)
				{
					Jeu.getEquipe().setValRelativeX(Map.LONG/2);
				}
				else
				{
					Jeu.getEquipe().setRelativeX(marge);
				}
				Jeu.getEquipe().setAbsolueX(d);
				
				Jeu.getEquipe();
				if(Jeu.getEquipe().getDirection() != Equipe.DROITE)
				{
					Jeu.getEquipe();
					Jeu.getEquipe().setDirection(Equipe.DROITE);
					animation = Jeu.getEquipe().getAnimation();
				}
				enMouvement = true;
			}
			
			else if(in.isKeyDown(Input.KEY_UP) || in.isControllerUp(0))
			{
				d = Jeu.getEquipe().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(),0 , (int) (-Jeu.getEquipe().vitesse()));
				double marge = Jeu.getEquipe().getMap().scrollY(d);
				if(marge == 0)
				{
					Jeu.getEquipe().setValRelativeY(Map.LARG/2);
				}
				else
				{
					Jeu.getEquipe().setRelativeY(marge);
				}
				Jeu.getEquipe().setAbsolueY(d);
				
				Jeu.getEquipe();
				if(Jeu.getEquipe().getDirection() != Equipe.HAUT)
				{
					Jeu.getEquipe();
					Jeu.getEquipe().setDirection(Equipe.HAUT);
					animation = Jeu.getEquipe().getAnimation();
				}
				enMouvement = true;
			}
			
			else if(in.isKeyDown(Input.KEY_DOWN) || in.isControllerDown(0))
			{
				d = Jeu.getEquipe().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(),0 , (int) (Jeu.getEquipe().vitesse()));
				double marge = Jeu.getEquipe().getMap().scrollY(d);
	
				if(marge == 0)
				{
					Jeu.getEquipe().setValRelativeY(Map.LARG/2);
				}
				else
				{
					Jeu.getEquipe().setRelativeY(marge);
				}
				Jeu.getEquipe().setAbsolueY(d);
		
				
				Jeu.getEquipe();
				if(Jeu.getEquipe().getDirection() != Equipe.BAS)
				{
					Jeu.getEquipe();
					Jeu.getEquipe().setDirection(Equipe.BAS);
					animation = Jeu.getEquipe().getAnimation();
				}
				enMouvement = true;
			}
			if(d == 0)
			{
				enMouvement = false;
			}
			else
			{
				//Activer portail -- Detecter portail au pied du personnage (en bas du sprite)
				Portail portail = Jeu.getEquipe().getMap().getPortail((int) ((Jeu.getEquipe().getAbsolueX())/32), (int) ((Jeu.getEquipe().getAbsolueY()+16)/32));
				if(portail != null)
				{
					Jeu.getEquipe().setMap(GenerateurDonnees.genererMap(portail.getTarget(), (int) (- portail.getXT()*32 - 16 + Config.LONGUEUR/2), ((int) - portail.getYT()*32 - 16 + Config.LARGEUR/2), true));
					System.out.println("new coords : " + (int) (portail.getXT()*32 - Config.LONGUEUR/2)+ "  -  " + ((int) portail.getYT()*32 - Config.LARGEUR/2));
					Jeu.getEquipe().setValAbsolueX(portail.getXT() * 32 +16);
					Jeu.getEquipe().setValAbsolueY(portail.getYT() * 32 +16);
					
				}
				else
				{
					//_-_-_-GENERER COMBAT-_-_-_
					ennemis = Jeu.getEquipe().getMap().genererEnnemis();
					if(ennemis != null)
					{
						in.clearKeyPressedRecord();
						enMouvement = false;
						//Jeu.entrerCombat(Jeu.getEquipe(), ennemis, sbg);
						Jeu.launchBattle(ennemis, sbg);
					}
				}
			}		
			//FIN GESTION MOUVEMENT___________________________________________________		
			
			Map map = Jeu.getEquipe().getMap();
			
			//GESTION MUSIQUE---NE PAS JOUER MUSIQUE SI Jeu.getEquipe()ENNEMIS
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
				onStart();
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
		
		//INTERACTION_____________________________________________
		if(in.isKeyPressed(Input.KEY_RETURN))
		{
			onValidate();
		}
		
		Jeu.getEquipe().getMap().updateAnimatedTile();
		
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
	
	public void controllerButtonPressed(int controller, int button){
		System.out.println(controller + " " + button);
		if(button == ControllerInput.START){
			onStart();
		}
		else if(button == ControllerInput.VALIDATE){
			onValidate();
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
		if(ControllerInput.isControllerDownPressed(0, in) || in.isKeyPressed(Input.KEY_DOWN)){
			curseurSelect = (curseurSelect + 1) % selectionneur.nbChoix();
		}
		else if(ControllerInput.isControllerUpPressed(0, in) || in.isKeyPressed(Input.KEY_UP)){
			curseurSelect = (curseurSelect + selectionneur.nbChoix() - 1) % selectionneur.nbChoix();
		}
	}
	//#endregion
	
	private void onStart(){
		if(dialogue == null){
			game.enterState(Config.MENU);
		}
	}
	
	private void onValidate(){
		if(dialogue == null){ //Pas de dialogue => interaction avec les éléments du jeu (PNJ, coffre, commande)
			int intX=0;
			int intY=0;
			Jeu.getEquipe();
			if(Jeu.getEquipe().getDirection() == Equipe.HAUT)
			{
				intX = (int) ((Jeu.getEquipe().getAbsolueX())/32);
				intY = (int) ((Jeu.getEquipe().getAbsolueY()-16)/32);
			} else {
				Jeu.getEquipe();
				if(Jeu.getEquipe().getDirection() == Equipe.BAS)
				{
					intX = (int) ((Jeu.getEquipe().getAbsolueX())/32);
					intY = (int) ((Jeu.getEquipe().getAbsolueY()+16)/32);
				} else {
					Jeu.getEquipe();
					if(Jeu.getEquipe().getDirection() == Equipe.GAUCHE)
					{
						intX = (int) ((Jeu.getEquipe().getAbsolueX()-16)/32);
						intY = (int) ((Jeu.getEquipe().getAbsolueY())/32);
					} else {
						Jeu.getEquipe();
						if(Jeu.getEquipe().getDirection() == Equipe.DROITE)
						{
							intX = (int) ((Jeu.getEquipe().getAbsolueX()+16)/32);
							intY = (int) ((Jeu.getEquipe().getAbsolueY())/32);
						}
					}
				}
			}			
			
			Jeu.getEquipe();
			if(Jeu.getEquipe().getDirection() == Equipe.HAUT)
			{
				
				Coffre coffre = Jeu.getEquipe().getMap().getCoffre(intX, intY);
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
							Jeu.getEquipe().updatePO(coffre.getPO());
							rep.add(new Replique("Vous trouvez : " + coffre.getPO() + " pièces d'or.")); 
						}
						else
						{
							rep.add(new Replique("Le coffre est vide."));
						}
						coffre.recupererContenu(Jeu.getEquipe());
						Jeu.getEquipe().getMap().updateCoffreSprite(coffre.getX(), coffre.getY());
						dialogue = new Dialogue(rep);
						dialogue.suivant();
					}
				}
			}
			
			Commande commande = Jeu.getEquipe().getMap().getCommande(intX,intY);
			if(commande != null){
				commande.run(Jeu.getEquipe().getMap());
			}
			
			//-------------------PNJ-----------------------------
			PNJ pnj = Jeu.getEquipe().getMap().getPNJ(intX, intY);
			if(pnj != null)
			{
				dialogue = pnj.getDialogue();
				if (!dialogue.suivant()) {dialogue = null;}
			}
		}
		else{ //Gestion dialogue
			if(selectionneur == null && dialogue.getSelectionneur() != null) //selectionneur
			{
				selectionneur = dialogue.getSelectionneur();
			}
			
			if(selectionneur == null){ //S'il n'y a pas de sélectionneur
				 if(!dialogue.suivant()){
					 dialogue = null;
					 game.getContainer().getInput().clearKeyPressedRecord();
				 }
			}
			else{ //s'il y a un selectionneur
				dialogue.validerSelectionneur(curseurSelect);
				if(dialogue.estTermine()){
					dialogue = null;
				}
				curseurSelect = 0;
				selectionneur = null;
			}
		}
	}

}
