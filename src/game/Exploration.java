package game;

import game.dialogue.Dialogue;
import game.dialogue.Line;
import game.dialogue.Select;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import map.Chest;
import map.Command;
import map.Map;
import map.Gate;

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

import audio.MusicManager;

import data.Format;
import data.DataManager;


import personnage.Party;
import personnage.EnnemisParty;
import personnage.NonPlayerCharacter;


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
	private Select selectionneur;
	private int curseurSelect = 0;
	
	private ParticleSystem particles;
	
	
	
	//#endregion

	//#region ------OVERRIDE BASICGAMESTATE--------
	@Override
	public void init(GameContainer arg0, StateBasedGame sbg)
			throws SlickException 
	{
		this.game = sbg;

		
		ArrayList<Line> repliques = new ArrayList<Line>();
		repliques.add(new Line("Hello world!", "Sul"));
		repliques.add(new Line("Ceci est un dialogue", "Sul"));
		repliques.add(new Line("Doit je répéter? $select[Oui{0};Non;Quitter{end}]", "Sul"));
		repliques.add(new Line("Au revoir"));
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
		if(animation == null){animation = Launcher.getParty().getAnimation();}
		
		Map map = Launcher.getParty().getMap();
		
		map.afficherLayer(0);
		map.afficherLayer(1);
		
		double X = Launcher.getParty().getRelativeX();
		double Y = Launcher.getParty().getRelativeY();
		

		
		if(Launcher.getParty().getRelativeX()<Map.WIDTH/2)
		{
			X = Launcher.getParty().getAbsoluteX();
		}
		else if(Launcher.getParty().getRelativeX()>Map.WIDTH/2)
		{
			X = Map.WIDTH-Launcher.getParty().getMap().getTiledMap().getWidth()*32+Launcher.getParty().getAbsoluteX();
		}
		if(Launcher.getParty().getRelativeY()<Map.HEIGHT/2)
		{
			Y = Launcher.getParty().getAbsoluteY();
		}
		else if(Launcher.getParty().getRelativeY()>Map.HEIGHT/2)
		{
			Y = Map.HEIGHT - Launcher.getParty().getMap().getTiledMap().getHeight()*32+ Launcher.getParty().getAbsoluteY();
		}
			
		X -= animation.getWidth()/2;
		Y -= animation.getHeight()/2;
				
		
		map.renderNPC();
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
			int X = (int) (Launcher.getParty().getAbsoluteX());
			int Y = (int) (Launcher.getParty().getAbsoluteY());
			EnnemisParty ennemis = null; //Prend une valeur en cas de rencontre si mouvement
			
			if(!animation.equals(Launcher.getParty().getAnimation())){
				animation = Launcher.getParty().getAnimation();
			}
			
			//GESTION DES MOUVEMENTS + INTERACTION________________________________________________
			int d = 0;
			
			
			if(in.isKeyDown(Input.KEY_LEFT) || in.isControllerLeft(0))
			{
				d = Launcher.getParty().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(), (int) (-Launcher.getParty().speed()), 0);
				double marge = Launcher.getParty().getMap().scrollX(d);
				if(marge == 0)
				{
					Launcher.getParty().setValRelativeX(Map.WIDTH/2);
				}
				else
				{
					Launcher.getParty().setRelativeX(marge);
				}
				
				Launcher.getParty().setAbsoluteX(d);
				Launcher.getParty();
				if(Launcher.getParty().getDirection() != Party.WEST)
				{
					Launcher.getParty();
					Launcher.getParty().setDirection(Party.WEST);
					animation = Launcher.getParty().getAnimation();
				}
				
				enMouvement = true;			
			}		
			else if(in.isKeyDown(Input.KEY_RIGHT) || in.isControllerRight(0))
			{
				d = Launcher.getParty().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(), (int) (Launcher.getParty().speed()), 0);
				double marge = Launcher.getParty().getMap().scrollX(d);
				if(marge == 0)
				{
					Launcher.getParty().setValRelativeX(Map.WIDTH/2);
				}
				else
				{
					Launcher.getParty().setRelativeX(marge);
				}
				Launcher.getParty().setAbsoluteX(d);
				
				Launcher.getParty();
				if(Launcher.getParty().getDirection() != Party.EAST)
				{
					Launcher.getParty();
					Launcher.getParty().setDirection(Party.EAST);
					animation = Launcher.getParty().getAnimation();
				}
				enMouvement = true;
			}
			
			else if(in.isKeyDown(Input.KEY_UP) || in.isControllerUp(0))
			{
				d = Launcher.getParty().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(),0 , (int) (-Launcher.getParty().speed()));
				double marge = Launcher.getParty().getMap().scrollY(d);
				if(marge == 0)
				{
					Launcher.getParty().setValRelativeY(Map.HEIGHT/2);
				}
				else
				{
					Launcher.getParty().setRelativeY(marge);
				}
				Launcher.getParty().setAbsoluteY(d);
				
				Launcher.getParty();
				if(Launcher.getParty().getDirection() != Party.NORTH)
				{
					Launcher.getParty();
					Launcher.getParty().setDirection(Party.NORTH);
					animation = Launcher.getParty().getAnimation();
				}
				enMouvement = true;
			}
			
			else if(in.isKeyDown(Input.KEY_DOWN) || in.isControllerDown(0))
			{
				d = Launcher.getParty().getMap().distance(X, Y, animation.getWidth(), animation.getHeight(),0 , (int) (Launcher.getParty().speed()));
				double marge = Launcher.getParty().getMap().scrollY(d);
	
				if(marge == 0)
				{
					Launcher.getParty().setValRelativeY(Map.HEIGHT/2);
				}
				else
				{
					Launcher.getParty().setRelativeY(marge);
				}
				Launcher.getParty().setAbsoluteY(d);
		
				
				Launcher.getParty();
				if(Launcher.getParty().getDirection() != Party.SOUTH)
				{
					Launcher.getParty();
					Launcher.getParty().setDirection(Party.SOUTH);
					animation = Launcher.getParty().getAnimation();
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
				Gate portail = Launcher.getParty().getMap().getGate((int) ((Launcher.getParty().getAbsoluteX())/32), (int) ((Launcher.getParty().getAbsoluteY()+16)/32));
				if(portail != null)
				{
					Launcher.getParty().setMap(DataManager.loadMap(portail.getTarget(), (int) (- portail.getXT()*32 - 16 + Config.LONGUEUR/2), ((int) - portail.getYT()*32 - 16 + Config.LARGEUR/2), true));
					System.out.println("new coords : " + (int) (portail.getXT()*32 - Config.LONGUEUR/2)+ "  -  " + ((int) portail.getYT()*32 - Config.LARGEUR/2));
					Launcher.getParty().setValAbsoluteX(portail.getXT() * 32 +16);
					Launcher.getParty().setValAbsoluteY(portail.getYT() * 32 +16);
					
				}
				else
				{
					//_-_-_-GENERER COMBAT-_-_-_
					ennemis = Launcher.getParty().getMap().generateEnnemis();
					if(ennemis != null)
					{
						in.clearKeyPressedRecord();
						enMouvement = false;
						//Jeu.entrerCombat(Jeu.getEquipe(), ennemis, sbg);
						Launcher.launchBattle(ennemis, sbg);
					}
				}
			}		
			//FIN GESTION MOUVEMENT___________________________________________________		
			
			Map map = Launcher.getParty().getMap();
			
			//GESTION MUSIQUE---NE PAS JOUER MUSIQUE SI Jeu.getEquipe()ENNEMIS
			//JouerEnBoucle() ne fait rien si la musique est déjà en train de jouer.
			if( map.getMusic() == null)
			{
				MusicManager.stop();
			}
			else
			{
				if(ennemis == null)
				{
					MusicManager.playLoop(map.getMusic());
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
			if(selectionneur == null && dialogue.getSelect() != null)
			{
				selectionneur = dialogue.getSelect();
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
		
		Launcher.getParty().getMap().updateAnimatedTile();
		
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
		g.fillRect(0, 0, Map.WIDTH, Map.HEIGHT);
	}

	private void afficherDialogue(Graphics g)
	{
		g.setColor(Config.couleur2);
		g.drawRect(0, 400, 640, 80);
		g.setColor(Config.couleur1);
		g.fillRect(1, 401, 639, 79);
		g.setColor(Color.white);
		g.setFont(Launcher.getFont());
		//Jeu.getFont().drawString(10,405, Formatage.multiLignes(dialogue.get().getFormatStandard(),70));
		g.drawString(Format.multiLines(dialogue.get().getFormatStandard(),70),10,405);
		if(selectionneur != null)
		{
			int l = selectionneur.getDimensionBoite().width;
			int h = selectionneur.getDimensionBoite().height;
			g.setColor(Config.couleur2);
			g.drawRect(0, 400 - h, l, h);
			g.setColor(Config.couleur1);
			g.fillRect(1, 401 - h, l-1 , h - 1);
			g.setColor(Color.white);
			g.drawImage(Launcher.getArrow(0), 2, (406 - h) + 20 * curseurSelect);
			
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
			Launcher.getParty();
			if(Launcher.getParty().getDirection() == Party.NORTH)
			{
				intX = (int) ((Launcher.getParty().getAbsoluteX())/32);
				intY = (int) ((Launcher.getParty().getAbsoluteY()-16)/32);
			} else {
				Launcher.getParty();
				if(Launcher.getParty().getDirection() == Party.SOUTH)
				{
					intX = (int) ((Launcher.getParty().getAbsoluteX())/32);
					intY = (int) ((Launcher.getParty().getAbsoluteY()+16)/32);
				} else {
					Launcher.getParty();
					if(Launcher.getParty().getDirection() == Party.WEST)
					{
						intX = (int) ((Launcher.getParty().getAbsoluteX()-16)/32);
						intY = (int) ((Launcher.getParty().getAbsoluteY())/32);
					} else {
						Launcher.getParty();
						if(Launcher.getParty().getDirection() == Party.EAST)
						{
							intX = (int) ((Launcher.getParty().getAbsoluteX()+16)/32);
							intY = (int) ((Launcher.getParty().getAbsoluteY())/32);
						}
					}
				}
			}			
			
			Launcher.getParty();
			if(Launcher.getParty().getDirection() == Party.NORTH)
			{
				
				Chest coffre = Launcher.getParty().getMap().getChest(intX, intY);
				if(coffre != null)
				{
					if(!Launcher.estCoffreOuvert(coffre))
					{
						ArrayList<Line> rep = new ArrayList<Line>();
						if(coffre.getContenu() != null)
						{
							rep.add(new Line("Vous trouvez : " + coffre.getContenu() + " x " + coffre.getQuantity())); 
						}
						else if(coffre.getMoney() > 0)
						{
							Launcher.getParty().updateMoney(coffre.getMoney());
							rep.add(new Line("Vous trouvez : " + coffre.getMoney() + " pièces d'or.")); 
						}
						else
						{
							rep.add(new Line("Le coffre est vide."));
						}
						coffre.gainContent(Launcher.getParty());
						Launcher.getParty().getMap().updateChestSprite(coffre.getX(), coffre.getY());
						dialogue = new Dialogue(rep);
						dialogue.suivant();
					}
				}
			}
			
			Command commande = Launcher.getParty().getMap().getCommande(intX,intY);
			if(commande != null){
				commande.run(Launcher.getParty().getMap());
			}
			
			//-------------------PNJ-----------------------------
			NonPlayerCharacter pnj = Launcher.getParty().getMap().getNPC(intX, intY);
			if(pnj != null)
			{
				dialogue = pnj.getDialogue();
				if (!dialogue.suivant()) {dialogue = null;}
			}
		}
		else{ //Gestion dialogue
			if(selectionneur == null && dialogue.getSelect() != null) //selectionneur
			{
				selectionneur = dialogue.getSelect();
			}
			
			if(selectionneur == null){ //S'il n'y a pas de sélectionneur
				 if(!dialogue.suivant()){
					 dialogue = null;
					 game.getContainer().getInput().clearKeyPressedRecord();
				 }
			}
			else{ //s'il y a un selectionneur
				dialogue.validateSelect(curseurSelect);
				if(dialogue.isEnd()){
					dialogue = null;
				}
				curseurSelect = 0;
				selectionneur = null;
			}
		}
	}

}
