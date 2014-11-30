package data;



import game.dialogue.Dialogue;
import game.dialogue.Line;

import java.util.ArrayList;
import java.util.HashMap;


import map.Chest;
import map.Command;
import map.Interact;
import map.Map;
import map.Gate;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.xml.*;

import bag.IItems;
import bag.item.Item;
import bag.item.stuff.Weapon;

import audio.MusicManager;

import personnage.Ennemy;
import personnage.EnnemisParty;
import personnage.NonPlayerCharacter;
import skill.Skill;



/**
 * La classe GenerateurDonnees permet de récupérer des 
 * objets (Skill, Map...) à partir des fichiers XML dans le dossier
 * ressources/donnees 
 * 
 */
public class DataManager 
{

	//Classe non instanciable
	private DataManager(){}

	/**
	 * Retourne le skill dont l'id est passé en paramètre à partir
	 * du fichier xml ressources/donnees/skills.xml
	 * 
	 * Retourne null si le fichier ne peut 5 chargé, ou si l'id n'existe pas.
	 * 
	 * @param idSkill
	 * 		L'id du skill à retourner
	 * @return
	 * 		Le skill dont l'id est passé en paramètre
	 */	
	public static Skill loadSkill(String idSkill)
	{
		XMLParser parser = new XMLParser();
		try 
		{
			XMLElement file = parser.parse("ressources/donnees/skills.xml");
			XMLElementList listSkills = file.getChildrenByName("skill");
			ArrayList<XMLElement> collectionSkills = new ArrayList<XMLElement>();
			listSkills.addAllTo(collectionSkills);

			for(XMLElement e : collectionSkills)
			{

				if(e.getAttribute("id").equals(idSkill))
				{
					String name = e.getAttribute("nom","#ERR NO NAME");
					String description = e.getAttribute("description","#ERR NO DESCRIPTION");
					String target = e.getAttribute("cible","ennemi");
					int power = e.getIntAttribute("puissance",0);
					int precision = e.getIntAttribute("precision",0);
					int consommation = e.getIntAttribute("consommation",0);
					int levelMax = e.getIntAttribute("max",10);
					int up = e.getIntAttribute("up",0);
					String effects = e.getAttribute("effets","");
					int type = e.getIntAttribute("type",0);

					Skill skill = new Skill(idSkill, name, description, power, precision, consommation, up, levelMax, effects, Skill.getCodeCible(target),type);
					return skill;

				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Génère la carte dont l'id est passé en paramètre à partie des infos des fichiers
	 * donnees/maps.xml
	 * 
	 * @param idMap
	 * 		Id de la map
	 * @param x
	 * 		Abscisse d'affichage de la map.		
	 * @param y
	 * 		Ordonnée d'affichage de la map.
	 * @param load
	 * 		Vrai si la tiledMap doit être chargé, faux sinon.
	 * @return
	 * 		La map dont l'id est passé en paramètre.
	 */
	public static Map loadMap(String idMap, double x, double y, boolean load)
	{
		XMLParser parser = new XMLParser();
		try 
		{
			XMLElement file = parser.parse("ressources/donnees/maps.xml");
			XMLElementList listMaps = file.getChildrenByName("map");
			ArrayList<XMLElement> collectionMaps = new ArrayList<XMLElement>();
			listMaps.addAllTo(collectionMaps);
			
			for(XMLElement e : collectionMaps)
			{
				if(e.getAttribute("id").equals(idMap))
				{					
					String battleBackground = e.getAttribute("terrain","default");
					String music = e.getAttribute("musique",null);
					String map = e.getAttribute("map");
					String name = e.getAttribute("nom", "Inconnu");
					boolean outDoor = e.getBooleanAttribute("exterieur", true);
					ArrayList<String> ennemis = loadEnnemisParty(e.getChildrenByName("ennemis"));
					ArrayList<Gate> gate = loadMapGates(e.getChildrenByName("portails"));
					ArrayList<Chest> chests = loadChestsOfMap(e.getChildrenByName("coffres"), idMap);
					ArrayList<Command> commands = loadMapCommands(e.getChildrenByName("commandes"));
					
					ArrayList<NonPlayerCharacter> NPCList = new ArrayList<NonPlayerCharacter>();
					
					if(e.getChildrenByName("pnjs").size()>0)
					{
						XMLElementList xmlNPC = e.getChildrenByName("pnjs").get(0).getChildrenByName("pnj");
						
						for(int i=0; i<xmlNPC.size(); i++)
						{
							NonPlayerCharacter npc = loadNPC(xmlNPC.get(i).getAttribute("id"));
							
							int xNPC = xmlNPC.get(i).getIntAttribute("x",0);
							int yNPC = xmlNPC.get(i).getIntAttribute("x",0);
							int position = xmlNPC.get(i).getIntAttribute("position",0);
							
							
							Dialogue dialogue = loadDialogue(xmlNPC.get(i).getAttribute("dialogue"));
							
							NPCList.add(npc.setX(xNPC).setY(yNPC).setPosition(position).setDialogue(dialogue));
							
						}
					}
					
					return new Map(idMap, name, map, battleBackground, x, y, music, outDoor, ennemis, gate, chests, NPCList, commands, load); 
				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Charge les portails(gate) pour la map.
	 * 
	 * @param gates
	 * 		Les noeuds XML gate d'une map à charger.
	 * @return
	 * 		Une ArrayList de portails.
	 * @throws SlickXMLException
	 */
	private static ArrayList<Gate> loadMapGates(
			XMLElementList gates) throws SlickXMLException 
	{
		ArrayList<Gate> res = new ArrayList<Gate>();
		
		if(gates == null)
		{
			return res;
		}
		if(gates.size()==0)
		{
			return res;
		}
		
		ArrayList<XMLElement> collectionGates = new ArrayList<XMLElement>();		
		XMLElementList listGates = gates.get(0).getChildrenByName("portail");
		listGates.addAllTo(collectionGates);
		
		for(XMLElement e : collectionGates)
		{
			int x = e.getIntAttribute("x");
			int y = e.getIntAttribute("y");
			
			int xt = e.getIntAttribute("xt");
			int yt = e.getIntAttribute("yt");
			
			String target = e.getAttribute("target");
			
			res.add(new Gate(x, y, target, xt, yt));
		}
		
		return res;
	}
	
	/**
	 * Charge les coffres présents dans le noeud XML passé en paramètre.
	 * 
	 * @param chests
	 * 		Le noeud XML qui contient les informations sur le coffre.
	 * @param idMap
	 * 		ID de la map dont les croffres appartiennent.
	 * @return
	 * 		Les coffres de la map.
	 * @throws SlickXMLException
	 */
	private static ArrayList<Chest> loadChestsOfMap(
			XMLElementList chests, String idMap) throws SlickXMLException 
	{
		ArrayList<Chest> res = new ArrayList<Chest>();
		
		if(chests == null || chests.size() == 0)
		{
			return res;
		}
		
		ArrayList<XMLElement> collectionChests = new ArrayList<XMLElement>();		
		XMLElementList listChests = chests.get(0).getChildrenByName("coffre");
		listChests.addAllTo(collectionChests);
		
		for(XMLElement e : collectionChests)
		{
			int x = e.getIntAttribute("x");
			int y = e.getIntAttribute("y");
			
			int money = e.getIntAttribute("po",0);
			int quantity = e.getIntAttribute("qte",1);
			
			String construct = e.getAttribute("contenu",null);
			IItems content = null;
			
			if(construct != null)
			{
				String[]tab = construct.split(":");
				if(tab[0].equals("objet"))
				{
					content = loadItem(tab[1]);
				}
			}
			
			
			res.add(new Chest(idMap, x, y, content, quantity, money));
		}
		
		return res;
	}
	
	/**
	 * Génére les commandes du XML passé en paramètre.
	 * 
	 * @param XMLCommands
	 * 		Noeud XML qui contient les commandes.
	 * @return
	 * 		UNe ArrayList de Commande.
	 * @throws SlickXMLException
	 */
	private static ArrayList<Command> loadMapCommands(XMLElementList XMLCommands) throws SlickXMLException{
		ArrayList<Command> res = new ArrayList<Command>();
		
		if(XMLCommands == null || XMLCommands.size() == 0){
			return res;
		}
		
		ArrayList<XMLElement> collectionCommands = new ArrayList<XMLElement>();		
		XMLElementList listCommands = XMLCommands.get(0).getChildrenByName("commande");
		listCommands.addAllTo(collectionCommands);
		
		for(XMLElement e : collectionCommands){
			int x = e.getIntAttribute("x",0);
			int y = e.getIntAttribute("y",0);
			int z = e.getIntAttribute("z",0);
			
			ArrayList<Interact> targets = new ArrayList<Interact>();
			
			targets = loadInteractsCommands(e.getChildrenByName("interaction"));
			
			res.add(new Command(x,y,z,targets));
		}
		
		return res;
	}
	
	/**
	 * Charge les interactions d'une commande depuis le noeud XML passé en paramètre.
	 *  
	 * @param XMLInteracts
	 * 		Noeud XML qui contient les informations sur les intéractions.
	 * @return
	 * 		Une ArrayList d'interracts.
	 * @throws SlickXMLException
	 */
	private static ArrayList<Interact> loadInteractsCommands(XMLElementList XMLInteracts) throws SlickXMLException{
		ArrayList<Interact> res = new ArrayList<Interact>();
		
		if(XMLInteracts == null || XMLInteracts.size() == 0){
			return res;
		}
		
		ArrayList<XMLElement> collectionInteracts = new ArrayList<XMLElement>();		
		XMLInteracts.addAllTo(collectionInteracts);
		
		for(XMLElement e : collectionInteracts){
			int x = e.getIntAttribute("x",0);
			int y = e.getIntAttribute("y",0);
			int z = e.getIntAttribute("z",0);
			
			int change = e.getIntAttribute("change",0);
	
			res.add(new Interact(x, y, z, change));
		}
		
		return res;
	}

	/**
	 * Récupère un groupe d'ennemis à partir d'une noeud xml groupe.
	 * 
	 * @param ennemisParty
	 * 		Le noeud XML qui contient le groupe d'ennemis.
	 * @return
	 * 		le groupe d'ennemis.
	 * @throws SlickException
	 */
	private static ArrayList<String> loadEnnemisParty(XMLElementList  ennemisParty) throws SlickException 
	{
		ArrayList<String> res = new ArrayList<String>();

		if(ennemisParty.size() == 0)
		{		
			return res;
		}
		XMLElementList ennemis = ennemisParty.get(0).getChildrenByName("groupe");
		for(int i = 0 ; i<ennemis.size();i++)
		{	
			res.add(ennemis.get(i).getAttribute("id"));
		}
		
		return res;
	}
	
	/**
	 * Charge une ennmi dont l'id est passé en paramètre.
	 * 
	 * @param id
	 * 		Id de l'ennemi à charger.
	 * @return
	 */
	private static Ennemy loadEnnemy(String id)
	{
		XMLParser parser = new XMLParser();
		XMLElement file;
		try 
		{
			file = parser.parse("ressources/donnees/ennemis.xml");		
			XMLElementList listeEnnemis = file.getChildrenByName("ennemi");
			ArrayList<XMLElement> collecEnnemis = new ArrayList<XMLElement>();
			listeEnnemis.addAllTo(collecEnnemis);
			
			for(XMLElement element : collecEnnemis)
			{

				if(element.getAttribute("id").equals(id))
				{
					String name = element.getAttribute("nom","noname");
					String description = element.getAttribute("description","[no description]");
					
					int physicAttaque = element.getIntAttribute("atqphy",1);
					int magicAttaque = element.getIntAttribute("atqmag",1);
					int physicDefense = element.getIntAttribute("defphy",1);
					int magicDefense = element.getIntAttribute("defmag",1);
					int agility = element.getIntAttribute("dext",1);
					int heal = element.getIntAttribute("soin",1);
					int health = element.getIntAttribute("pv",1);
					int experience = element.getIntAttribute("xp",1);
					int money = element.getIntAttribute("argent",0);
					
					String imageValue = element.getAttribute("image");
					
					String[] dataImage = imageValue.split(";");
					
					System.out.println(dataImage[0]);
					
					Image image = new Image(dataImage[0], new Color(255,0,255)).getSubImage(Integer.parseInt(dataImage[1]), Integer.parseInt(dataImage[2]), Integer.parseInt(dataImage[3]), Integer.parseInt(dataImage[4]));
					
					Ennemy ennemy = new Ennemy(id, name,description,physicAttaque,magicAttaque,physicDefense,magicDefense,agility,heal,health,experience,money,image);
					return ennemy;
				}
			}
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Charge l'item dont l'id est passé en paramètre.
	 * 
	 * @param id
	 * 		Id de l'item à charger.
	 * @return
	 * 		L'item
	 */
	public static Item loadItem(String id)
	{
		XMLParser parser = new XMLParser();
		XMLElement file;
		
		try 
		{
			file = parser.parse("ressources/donnees/objets.xml");		
			XMLElementList listItems = file.getChildrenByName("objet");
			ArrayList<XMLElement> collectionItems = new ArrayList<XMLElement>();
			listItems.addAllTo(collectionItems);
			
			Item item = null;
			
			for(XMLElement element : collectionItems)
			{

				if(element.getAttribute("id").equals(id))
				{
					String name = element.getAttribute("nom","noname");
					String description = element.getAttribute("description","[no description]");
					int idImage = element.getIntAttribute("icone",0);
					boolean rarity = element.getBooleanAttribute("rare", false);
					boolean battle = element.getBooleanAttribute("combat", false);
					
					ArrayList<String> effects = new ArrayList<String>();
					
					if(element.getChildrenByName("effets").size() > 0)
					{
						XMLElementList effectLists = element.getChildrenByName("effets").get(0).getChildrenByName("effet");
						ArrayList<XMLElement> collectionEffects = new ArrayList<XMLElement>();
						effectLists.addAllTo(collectionEffects);
						
						for(XMLElement e : collectionEffects)
						{
							effects.add(e.getAttribute("item"));
						}
					}
					
					
					Image icon = getIcone( idImage, "ressources/images/objets.png");
					item = new Item(id, icon, description, name, battle, rarity, effects);
				}
			}
			
			return item;
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Charge l'équipe d'ennemis dont l'id est passé en paramètre.
	 * 
	 * @param id
	 * 		Id de l'équipe à charger.
	 * @return
	 */
	public static EnnemisParty loadEnnemisParty(String id)
	{
		XMLParser parser = new XMLParser();
		try 
		{
			XMLElement file = parser.parse("ressources/donnees/groupes.xml");
			XMLElementList listParties = file.getChildrenByName("groupe");
			ArrayList<XMLElement> collectionParties = new ArrayList<XMLElement>();
			listParties.addAllTo(collectionParties);
			
			for(XMLElement e : collectionParties)
			{
				if(e.getAttribute("id").equals(id))
				{					
					int rarity = e.getIntAttribute("rarete",1);
					String music = e.getAttribute("musique",null);

					HashMap<Integer, Ennemy> ennemis = loadEnnemisFromParty(e.getChildrenByName("ennemi"));
					
					return new EnnemisParty(rarity, music, ennemis); 
				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Charge les ennemis du noeud XML passé en paramètre.
	 * @param ennemis
	 * 		Noeud XML qui comporte les infos sur les ennemis.
	 * @return
	 * 		Une ArrayList d'ennemis.
	 * @throws SlickXMLException
	 */
	private static HashMap<Integer, Ennemy> loadEnnemisFromParty(
			XMLElementList ennemis) throws SlickXMLException 
	{
		HashMap<Integer, Ennemy> res = new HashMap<Integer, Ennemy>();
		
		if(ennemis == null)
		{
			return res;
		}
		
		if(ennemis.size() == 0)
		{
			return res;
		}
		
		for(int i = 0; i<ennemis.size() ; i++)
		{
			res.put(ennemis.get(i).getIntAttribute("place"), loadEnnemy(ennemis.get(0).getAttribute("id")));
		}
		
		return res;
	}
	
	/**
	 * Charge les non player character depuis un id.
	 * 
	 * @param id
	 * 		L'id du NPC à charger.
	 * @return
	 * 		Le NPC.
	 */
	public static NonPlayerCharacter loadNPC(String id)
	{
		XMLParser parser = new XMLParser();
		try 
		{
			XMLElement file = parser.parse("ressources/donnees/pnj.xml");
			XMLElementList listNPC = file.getChildrenByName("pnj");
			ArrayList<XMLElement> collectionNPC = new ArrayList<XMLElement>();
			listNPC.addAllTo(collectionNPC);
			
			for(XMLElement e : collectionNPC)
			{
				if(e.getAttribute("id").equals(id))
				{					
					String name = e.getAttribute("nom", "???");
					String pathSpriteSheet = "ressources/spritesheet/"+e.getAttribute("spritesheet");
					
					SpriteSheet sprites = new SpriteSheet(pathSpriteSheet, 32,32,new Color(255,0,255));
					Animation[] animations = new Animation[4];
					for(int i = 0; i<4; i++)
					{
						animations[i] = new Animation(sprites, 0, i, 2,i ,true, 100, true);
					}
					
					return new NonPlayerCharacter(id, name, animations); 
				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Charge le dialogue dont l'id est passé en paramètre.
	 * 	
	 * @param id
	 * 		L'id du dialogue à charger.
	 * @return
	 * 		Le dialogue.
	 */
	public static Dialogue loadDialogue(String id)
	{
		XMLParser parser = new XMLParser();
		try 
		{
			XMLElement file = parser.parse("ressources/donnees/dialogues.xml");
			XMLElementList listDialogues = file.getChildrenByName("dialogue");
			ArrayList<XMLElement> collectionDialogues = new ArrayList<XMLElement>();
			listDialogues.addAllTo(collectionDialogues);
			
			
			for(XMLElement e : collectionDialogues)
			{
				if(e.getAttribute("id").equals(id))
				{					
					ArrayList<Line> dialogueLines = new ArrayList<Line>();
					XMLElementList xmlRep = e.getChildrenByName("replique");
					for(int i=0; i<xmlRep.size(); i++)
					{
						String speaker = xmlRep.get(i).getAttribute("enonciateur", "???");
						String line = xmlRep.get(i).getAttribute("replique");
						dialogueLines.add(new Line(line, speaker));
					}
					return new Dialogue(dialogueLines); 
				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Charge l'arme dont l'id est passé en paramètre.
	 * 
	 * @param id
	 * 		Id de l'arme à charger.
	 * @return
	 * 	L'arme
	 */
	public static Weapon loadWeapon(String id)
	{
		XMLParser parser = new XMLParser();
		try 
		{
			XMLElement file = parser.parse("ressources/donnees/equipables.xml");
			XMLElementList listWeapons = file.getChildrenByName("armes").get(0).getChildrenByName("arme");
			ArrayList<XMLElement> collectionWeapons = new ArrayList<XMLElement>();
			listWeapons.addAllTo(collectionWeapons);
			
			
			for(XMLElement e : collectionWeapons)
			{
				if(e.getAttribute("id").equals(id))
				{	
					String name = e.getAttribute("nom", "no name");
					int physicDamage = e.getIntAttribute("atqphy",0);
					int magicDamage = e.getIntAttribute("atqmag",0);
					int physicDefense = e.getIntAttribute("defphy",0);
					int magicDefense = e.getIntAttribute("defmag",0);
					int type = Weapon.codeWeapon(e.getAttribute("type","epee"));
					
					String pathImage = e.getAttribute("image", "ressources/images/equipables.png");
					int idImage = e.getIntAttribute("idimg",0);
				    
					int x = idImage%15;
				    int y = (int) (idImage/15);
				    
					
					Image image = getIcone(idImage, pathImage);
					return new Weapon(id, name, type, physicDamage, magicDamage, physicDefense, magicDefense, image);
				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Charge l'image dont l'id est passé en paramètre.
	 * 
	 * @param id
	 * 		L'id de l'image à charger.
	 * @return
	 * 		L'image
	 */
	public static Image loadImage(String id)
	{
		XMLParser parser = new XMLParser();
		try 
		{
			XMLElement file = parser.parse("ressources/donnees/images.xml");
			XMLElementList listImages = file.getChildrenByName("image");
			ArrayList<XMLElement> collectionImages = new ArrayList<XMLElement>();
			listImages.addAllTo(collectionImages);
			
			for(XMLElement e : collectionImages)
			{
				if(e.getAttribute("id").equals(id))
				{	
					int x = e.getIntAttribute("x",0);
					int y = e.getIntAttribute("y",0);
					String path = e.getAttribute("file", null);
					if(path == null){
						return null;
					}
					Image image = new Image(path, new Color(255,0,255));
					int width = e.getIntAttribute("width", image.getWidth());
					int height = e.getIntAttribute("height", image.getHeight());
					
					return image.getSubImage(x, y, width, height);
					
				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Retourne une subimage servant d'icône pour les IObjets.
	 * 
	 * @param id
	 * 		Id de l'icone à charger.
	 * @param src
	 * 		Fichier image qui contient l'icone à charger. 
	 * @return
	 * 		L'icone (Image)
	 * @throws SlickException
	 */
	private static Image getIcone(int id, String src) throws SlickException
	{
		Image icones = new Image(src, new Color(255,0,255));
		return icones.getSubImage(1+(id%15)*21, 1 + ((int) id/15), 20, 20);
	}
	

}
