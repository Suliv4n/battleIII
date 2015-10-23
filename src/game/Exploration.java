package game;

import game.dialogue.Dialogue;
import game.dialogue.Line;
import game.dialogue.Select;
import game.settings.Settings;
import game.system.Configurations;
import game.system.application.Application;

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
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;

import characters.EnnemisParty;
import characters.NonPlayerCharacter;
import characters.Party;
import audio.MusicManager;
import data.DataManager;


/**
 * 
 * _-_-_-_-[State Exploration]-_-_-_-_-_
 * 
 * @author Darklev
 *
 */
public class Exploration extends Top 
{

	//#region -----------PROPRIETES----------------
	
	private StateBasedGame game;
	
	
	//Vrai si l'"quipe est en mouvement.
	//Faux sinon.
	private boolean playerMoving;
	
	
	//dialogue en cours
	private Dialogue dialogue;
	private Select selectionneur;
	private int curseurSelect = 0;
	
	private ParticleSystem particles;

	
	//#endregion

	//#region ------OVERRIDE BASICGAMESTATE--------
	@Override
	public void init(GameContainer container, StateBasedGame sbg)
			throws SlickException 
	{
		this.game = sbg;
	}

	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException 
	{
		
		Map map = Application.application().getGame().getParty().getMap();
		
		map.drawLayer(0);
		map.drawLayer(1);

		map.renderNPC();
		//afficher Jeu.getEquipe()
		Application.application().getGame().getParty().draw(1,false);
		if(Settings.MULTIPLAYER){
			map.renderOtherPlayers();
		}
		
		map.drawLayer(2);
		map.drawLayer(3);
		map.drawLayer(4);
		
		if(map.getExterieur())
		{
			afficherFiltreTemporel(g);
		}
		
		if(particles != null){
			particles.render();
		}
		
		//AFFICHAGE DU DIALOGUE
		if(dialogue != null)
		{
			dialogue.render();
		}
		//!AFFICHAGE DU DIALOGUE
		
		//test.render(50, 50);
		
		
		super.render(container, sbg, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException 
	{	
		super.update(container, sbg, delta);
		
		if(particles != null){
			particles.update(delta);
		}
		
		Input in = container.getInput();
		
		in.disableKeyRepeat();
		
		if(dialogue == null)
		{	

			EnnemisParty ennemis = null; //Prend une valeur en cas de rencontre si mouvement
		
			
			//GESTION DES MOUVEMENTS + INTERACTION________________________________________________

			if(playerMoving)
			{
				//Activer portail -- Detecter portail au pied du personnage (en bas du sprite)
				Gate portail = Application.application().getGame().getParty().getMap().getGate((int) ((Application.application().getGame().getParty().getAbsoluteX())/32), (int) ((Application.application().getGame().getParty().getAbsoluteY()+16)/32));
				if(portail != null)
				{
					Application.application().getGame().getParty().setMap(DataManager.loadMap(portail.getTarget(), (int) (- portail.getXT()*32 - 16 + Config.LONGUEUR/2), ((int) - portail.getYT()*32 - 16 + Config.LARGEUR/2), true));
					Application.application().getGame().getParty().setValAbsoluteX(portail.getXT() * 32 +16);
					Application.application().getGame().getParty().setValAbsoluteY(portail.getYT() * 32 +16);
					
				}
				else
				{
					//_-_-_-GENERER COMBAT-_-_-_
					ennemis = Application.application().getGame().getParty().getMap().generateEnnemis();
					if(ennemis != null)
					{
						in.clearKeyPressedRecord();
						playerMoving = false;
						//Jeu.entrerCombat(Jeu.getEquipe(), ennemis, sbg);
						Application.application().getGame().launchBattle(ennemis);
					}
				}
			}		
			//FIN GESTION MOUVEMENT___________________________________________________		
			
			Map map = Application.application().getGame().getParty().getMap();
			
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
		}
		
		//DIALOGUE_____________________________________________________________________
		else
		{
			dialogue.update(delta);
			/*
			if(selectionneur == null && dialogue.getSelect() != null)
			{
				selectionneur = dialogue.getSelect();
			}
			
			if(selectionneur == null)
			{
				if(in.isKeyPressed(Input.KEY_RETURN))
				{
					 if(!dialogue.next())
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
			*/
		}
		
		//INTERACTION_____________________________________________
		if(in.isKeyPressed(Input.KEY_RETURN))
		{
			onValidate();
		}
		
		Application.application().getGame().getParty().getMap().updateAnimatedTile();
		
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

	/*
	private void afficherDialogue(Graphics g)
	{
		g.setColor(Config.couleur2);
		g.drawRect(0, 400, 640, 80);
		g.setColor(Config.couleur1);
		g.fillRect(1, 401, 639, 79);
		g.setColor(Color.white);
		//g.setFont(Application.application().getGame().getFont());
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
			g.drawImage(Application.application().getGame().getArrow(0), 2, (406 - h) + 20 * curseurSelect);
			
			int i = 0;
			for(String c : selectionneur.getChoix())
			{
				g.drawString(c, 13, (400 - h) + i * 20);
				i++;
			}
		}

	}
	*/
	
	public void controllerButtonPressed(int controller, int button){

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

	
	public void onValidate(){
		if(dialogue == null){ //Pas de dialogue => interaction avec les éléments du jeu (PNJ, coffre, commande)
			int intX=0;
			int intY=0;
			Application.application().getGame().getParty();
			if(Application.application().getGame().getParty().getDirection() == Party.NORTH)
			{
				intX = (int) ((Application.application().getGame().getParty().getAbsoluteX())/32);
				intY = (int) ((Application.application().getGame().getParty().getAbsoluteY()-16)/32);
			} else {
				Application.application().getGame().getParty();
				if(Application.application().getGame().getParty().getDirection() == Party.SOUTH)
				{
					intX = (int) ((Application.application().getGame().getParty().getAbsoluteX())/32);
					intY = (int) ((Application.application().getGame().getParty().getAbsoluteY()+16)/32);
				} else {
					Application.application().getGame().getParty();
					if(Application.application().getGame().getParty().getDirection() == Party.WEST)
					{
						intX = (int) ((Application.application().getGame().getParty().getAbsoluteX()-16)/32);
						intY = (int) ((Application.application().getGame().getParty().getAbsoluteY())/32);
					} else {
						Application.application().getGame().getParty();
						if(Application.application().getGame().getParty().getDirection() == Party.EAST)
						{
							intX = (int) ((Application.application().getGame().getParty().getAbsoluteX()+16)/32);
							intY = (int) ((Application.application().getGame().getParty().getAbsoluteY())/32);
						}
					}
				}
			}			
			
			Application.application().getGame().getParty();
			if(Application.application().getGame().getParty().getDirection() == Party.NORTH)
			{
				
				Chest coffre = Application.application().getGame().getParty().getMap().getChest(intX, intY);
				if(coffre != null)
				{
					if(!Application.application().getGame().isChestOpened(coffre))
					{
						ArrayList<Line> rep = new ArrayList<Line>();
						if(coffre.getContenu() != null)
						{
							rep.add(new Line("Vous trouvez : " + coffre.getContenu() + " x " + coffre.getQuantity())); 
						}
						else if(coffre.getMoney() > 0)
						{
							Application.application().getGame().getParty().updateMoney(coffre.getMoney());
							rep.add(new Line("Vous trouvez : " + coffre.getMoney() + " pièces d'or.")); 
						}
						else
						{
							rep.add(new Line("Le coffre est vide."));
						}
						coffre.gainContent(Application.application().getGame().getParty());
						Application.application().getGame().getParty().getMap().updateChestSprite(coffre.getX(), coffre.getY());
						dialogue = new Dialogue(rep);
						dialogue.next();
					}
				}
			}
			
			Command commande = Application.application().getGame().getParty().getMap().getCommande(intX,intY);
			if(commande != null){
				commande.run(Application.application().getGame().getParty().getMap());
			}
			
			//-------------------PNJ-----------------------------
			NonPlayerCharacter pnj = Application.application().getGame().getParty().getMap().getNPC(intX, intY);
			if(pnj != null)
			{
				dialogue = pnj.getDialogue();
				if (!dialogue.next()) {dialogue = null;}
			}
		}
		else{ //Gestion dialogue
			if(selectionneur == null && dialogue.getSelect() != null) //selectionneur
			{
				selectionneur = dialogue.getSelect();
			}
			
			if(selectionneur == null){ //S'il n'y a pas de sélectionneur
				 if(!dialogue.next()){
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
	
	/**
	 * Change le dialogue.
	 * 
	 * @param dialogue
	 * 		Nouveu dialogue.
	 */
	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
		this.dialogue.next();
	}
	
	//----------------------------------------------------------------------------
	//---------------------------EVENT--------------------------------------------
	//----------------------------------------------------------------------------

	@Override
	public void onHoldUp(){
		if(dialogue == null){
			
			if(Application.application().getGame().getParty().getDirection() != Party.NORTH)
			{
				Application.application().getGame().getParty().setDirection(Party.NORTH);
			}
			
			double speed = Application.application().getGame().getParty().speed();
			Application.application().getGame().getParty().setAbsoluteY(-speed);
			
			Application.application().getGame().getParty().setMoving(true);
		}
	}
	
	@Override
	public void onHoldDown(){
		if(dialogue == null){
			
			if(Application.application().getGame().getParty().getDirection() != Party.SOUTH)
			{
				Application.application().getGame().getParty().setDirection(Party.SOUTH);
			}
			
			double speed = Application.application().getGame().getParty().speed();
			Application.application().getGame().getParty().setAbsoluteY(speed);
			
			Application.application().getGame().getParty().setMoving(true);
		}
	}
	
	@Override
	public void onHoldRight(){
		if(dialogue == null){
			
			if(Application.application().getGame().getParty().getDirection() != Party.EAST)
			{
				Application.application().getGame().getParty().setDirection(Party.EAST);
			}
			
			double speed = Application.application().getGame().getParty().speed();
			Application.application().getGame().getParty().setAbsoluteX(speed);
			
			Application.application().getGame().getParty().setMoving(true);
		}
	}
	
	@Override
	public void onHoldLeft(){
		if(dialogue == null){
			
			if(Application.application().getGame().getParty().getDirection() != Party.WEST)
			{
				Application.application().getGame().getParty().setDirection(Party.WEST);
			}
			
			double speed = Application.application().getGame().getParty().speed();
			Application.application().getGame().getParty().setAbsoluteX(-speed);
			
			Application.application().getGame().getParty().setMoving(true);
		}
	}
	
	@Override
	public void onNoDirectionKeyPressedOrDown(){
		Application.application().getGame().getParty().setMoving(false);
	}
	
	
	@Override
	public void onStart(){
		if(dialogue == null){
			game.enterState(Config.MENU);
		}
	}


}
