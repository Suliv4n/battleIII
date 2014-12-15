package game.system.application;

import game.BattleWithATB;
import game.Combat;
import game.CombatResult;
import game.Config;
import game.Configuration;
import game.Exploration;
import game.GameOver;
import game.GestionEquipe;
import game.InterfaceSac;
import game.Menu;
import game.TitleScreen;
import game.input.InputAction;
import game.input.TextInput;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import map.Chest;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import characters.EnnemisParty;
import characters.Party;
import audio.MusicManager;



public class Game extends StateBasedGame{
	
	//-------STATES--------------------------
	private Exploration exploration;
	private GestionEquipe gestionEquipe;
	private Configuration configuration;
	private Combat combat;
	private Menu menu;
	private TextInput saisie;
	private GameOver gameOver;
	private InterfaceSac interfaceSac;
	private TitleScreen titleScreen;
	private CombatResult combatResult;
	private BattleWithATB battleWithATB;
	//---------STATES-------------------------
	
	
	private ArrayList<Chest> openedChests;
	
	private Party party;
	private int currentSave = -1;
	private Image arrows;
	private UnicodeFont font;
	
	public Game(String name) throws SlickException {
		super(name);
	}

	public void init() throws SlickException {
		party = new Party(0);
		arrows = new Image("ressources/images/fleches.png",new Color(255,0,255));
		openedChests = new ArrayList<Chest>();
		
		InputStream inputStream	= ResourceLoader.getResourceAsStream("ressources/fonts/8bitoperator.ttf");
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			font.deriveFont(24);
			this.font = new UnicodeFont(font, 15, false, false);
			this.font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			this.font.addAsciiGlyphs();
			this.font.loadGlyphs();
		} catch (FontFormatException | IOException e){
			throw new SlickException(e.getMessage());
		}
		
	}
	
	@Override
	public void initStatesList(GameContainer container)
			throws SlickException {
		
	    init();
	    
		exploration = new Exploration();
		menu = new Menu();
		gestionEquipe = new GestionEquipe();
		configuration = new Configuration();
		combat = new Combat();
		saisie = new TextInput();
		gameOver = new GameOver();
		interfaceSac = new InterfaceSac();
		combatResult = new CombatResult();
		titleScreen = new TitleScreen();
		battleWithATB = new BattleWithATB();
		
		
		addState(titleScreen);
		addState(exploration);
		addState(saisie);
		addState(menu);
		addState(gestionEquipe);
		addState(configuration);
		addState(interfaceSac);
		addState(combat);
		addState(battleWithATB);
		addState(gameOver);
		addState(combatResult);
	}
	
	/**
	 * Retourne la police du jeu.
	 * 
	 * @return
	 * 		La police du jeu.
	 */
	public UnicodeFont getGameFont()
	{
		return font;
	}
	
	/**
	 * Retourne les coffres ouverts.
	 * 
	 * @return les coffres ouverts.
	 */
	public ArrayList<Chest> getOpenedChests()
	{
		return openedChests;
	}
	
	/**
	 * Ajoute un coffre dans les coffres ouverts.
	 * 
	 * @param chest
	 * 		Le coffre ouvert à ajouter.s
	 */
	public void addOpenedChest(Chest chest)
	{
		openedChests.add(chest);
	}
	
	/**
	 * Teste si un coffre est ouvert.
	 * 
	 * @param chest
	 * 		COffre à tester.
	 * 
	 * @return vrai si le coffre est ouvert, sinon retourne faux.
	 */
	public boolean isChestOpened(Chest chest)
	{
		return openedChests.contains(chest);
	}
	
	/**
	 * Entre dans un combat.
	 * 
	 * @param ennemis
	 * 		Les ennemis du combat.
	 */
	public void launchBattle(EnnemisParty ennemis) {
		battleWithATB.launch(ennemis);
		MusicManager.playLoop(ennemis.getMusic());
		Application.application().getGame().enterState(Config.BATTLE_ATB);
	}
	
	public void exitBattle(){
		if(getCurrentState() == combat || getCurrentState() == battleWithATB){
			battleWithATB.onExit();
			enterState(exploration.getID());
		}
	}
	
	/**
	 * Retourne l'équipe de personnages du jeu en cours.
	 * 
	 * @return l'équipe de personnage courante.
	 */
	public Party getParty(){
		return party;
	}
	
	/**
	 * Modifie l'équipe de joueurs.
	 * 
	 * @param party
	 * 		Nouvelle équipe de joueur.
	 * @throws SlickException 
	 */
	public void setParty(Party party) throws SlickException {
		this.party = party;
		gestionEquipe.init(Application.application().getContainer(), this);
	}
	
	/**
	 * Modifie le numéro de la sauvegarde courante.
	 * 
	 * @param save
	 * 		Nouveau numéro de la sauvegarde courante.
	 */
	public void setCurrentSave(int save){
		currentSave = save;
	}
	
	/**
	 * Retourne le numéro de la sauvegarde courante.
	 * 
	 * @return le numéro de la sauvegarde courante.
	 */
	public int getCurrentSave(){
		return currentSave;
	}
	
	/**
	 * Retourne le curseur dont l'id est passé en paramètre.
	 * @param id
	 * 		Id de l'image du curseur à retourner.
	 * @return l'image du curseur.
	 */
	public Image getArrow(int id)
	{
		return arrows.getSubImage(id*11, 0, 11, 9);
	}
	
	/**
	 * Entre dans la state Input, pour la saisie par 
	 * le joueur.
	 * 
	 * @param icon
	 * 		Icone de la saisie.
	 * @param message
	 * 		Message de la saisie.
	 * @param default
	 * 		Saisie par défaut.
	 * @param out
	 * 		Id de la state de sortie.	
	 * @param operations
	 * 		Operations après la saisie.
	 * @param parameters
	 * 		Paramètres pour les opérations après la saisie.
	 */
	public static void input(Image icon,String message, String defaut, int out, InputAction operations, HashMap<String, Object> parameters)
	{
		TextInput.init(icon, message, defaut, out, operations, parameters);
		Application.application().getGame().enterState(Config.SAISIE);
	}
}