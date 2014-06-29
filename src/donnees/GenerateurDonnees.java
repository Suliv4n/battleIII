package donnees;



import game.dialogue.Dialogue;
import game.dialogue.Replique;

import java.util.ArrayList;
import java.util.HashMap;


import map.Coffre;
import map.Map;
import map.Portail;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.xml.*;

import audio.GestionnaireMusique;

import personnage.Ennemi;
import personnage.EquipeEnnemis;
import personnage.PNJ;
import sac.IObjet;
import sac.objet.Objet;
import sac.objet.stuff.Arme;
import skill.Skill;



/**
 * La classe GenerateurDonnees permet de récupérer des 
 * objets (Skill, Map...) à partir des fichiers XML dans le dossier
 * ressources/donnees 
 * 
 */
public class GenerateurDonnees 
{

	//Classe non instanciable
	private GenerateurDonnees(){}

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
	public static Skill genererSkill(String idSkill)
	{
		XMLParser analyseur = new XMLParser();
		try 
		{
			XMLElement file = analyseur.parse("ressources/donnees/skills.xml");
			XMLElementList listeSkills = file.getChildrenByName("skill");
			ArrayList<XMLElement> collecSkills = new ArrayList<XMLElement>();
			listeSkills.addAllTo(collecSkills);

			for(XMLElement e : collecSkills)
			{

				if(e.getAttribute("id").equals(idSkill))
				{
					String nom = e.getAttribute("nom","#ERR NO NAME");
					String description = e.getAttribute("description","#ERR NO DESCRIPTION");
					String cible = e.getAttribute("cible","ennemi");
					int puissance = e.getIntAttribute("puissance",0);
					int precision = e.getIntAttribute("precision",0);
					int consommation = e.getIntAttribute("consommation",0);
					int lvlMax = e.getIntAttribute("max",10);
					int up = e.getIntAttribute("up",0);
					String effets = e.getAttribute("effets","");
					int type = e.getIntAttribute("type",0);

					Skill skill = new Skill(idSkill, nom, description, puissance, precision, consommation, up, lvlMax, effets, Skill.getCodeCible(cible),type);
					return skill;

				}
			}
			return null;
		}
		catch (SlickException e)
		{
			System.out.println("Impossibke de charger le fichier xml");
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
	 * @return
	 * 		La map dont l'id est passé en paramètre.
	 */
	public static Map genererMap(String idMap, double x, double y)
	{
		XMLParser analyseur = new XMLParser();
		try 
		{
			XMLElement file = analyseur.parse("ressources/donnees/maps.xml");
			XMLElementList listeMaps = file.getChildrenByName("map");
			ArrayList<XMLElement> collecMaps = new ArrayList<XMLElement>();
			listeMaps.addAllTo(collecMaps);
			
			
			for(XMLElement e : collecMaps)
			{
				if(e.getAttribute("id").equals(idMap))
				{					
					String terrain = e.getAttribute("terrain","default");
					String musique = e.getAttribute("musique",null);
					String map = e.getAttribute("map");
					boolean exterieur = e.getBooleanAttribute("exterieur", true);
					ArrayList<String> ennemis = GenererEquipesEnnemisId(e.getChildrenByName("ennemis"));
					ArrayList<Portail> portails = GenererPortailsMap(e.getChildrenByName("portails"));
					ArrayList<Coffre> coffres = GenererCoffresMap(e.getChildrenByName("coffres"), idMap);
					
					ArrayList<PNJ> pnjListe = new ArrayList<PNJ>();
					
					if(e.getChildrenByName("pnjs").size()>0)
					{
						XMLElementList xmlPNJ = e.getChildrenByName("pnjs").get(0).getChildrenByName("pnj");
						
						for(int i=0; i<xmlPNJ.size(); i++)
						{
							PNJ pnj = genererPNJ(xmlPNJ.get(i).getAttribute("id"));
							
							int xPNJ = xmlPNJ.get(i).getIntAttribute("x",0);
							int yPNJ = xmlPNJ.get(i).getIntAttribute("x",0);
							int position = xmlPNJ.get(i).getIntAttribute("position",0);
							
							
							Dialogue dialogue = genererDialogue(xmlPNJ.get(i).getAttribute("dialogue"));
							
							pnjListe.add(pnj.setX(xPNJ).setY(yPNJ).setPosition(position).setDialogue(dialogue));
							
						}
					}
					
					return new Map(idMap, map, terrain, x, y, musique, exterieur, ennemis, portails, coffres, pnjListe); 
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

	
	private static ArrayList<Portail> GenererPortailsMap(
			XMLElementList portails) throws SlickXMLException 
	{
		ArrayList<Portail> res = new ArrayList<Portail>();
		
		if(portails == null)
		{
			return res;
		}
		if(portails.size()==0)
		{
			return res;
		}
		
		ArrayList<XMLElement> collecPortails = new ArrayList<XMLElement>();		
		XMLElementList listePortails = portails.get(0).getChildrenByName("portail");
		listePortails.addAllTo(collecPortails);
		
		for(XMLElement e : collecPortails)
		{
			int x = e.getIntAttribute("x");
			int y = e.getIntAttribute("y");
			
			int xt = e.getIntAttribute("xt");
			int yt = e.getIntAttribute("yt");
			
			String target = e.getAttribute("target");
			
			res.add(new Portail(x, y, target, xt, yt));
		}
		
		return res;
	}
	
	
	private static ArrayList<Coffre> GenererCoffresMap(
			XMLElementList coffres, String idMap) throws SlickXMLException 
	{
		ArrayList<Coffre> res = new ArrayList<Coffre>();
		
		if(coffres == null)
		{
			return res;
		}
		if(coffres.size()==0)
		{
			return res;
		}
		
		ArrayList<XMLElement> collecCoffres = new ArrayList<XMLElement>();		
		XMLElementList listePortails = coffres.get(0).getChildrenByName("coffre");
		listePortails.addAllTo(collecCoffres);
		
		for(XMLElement e : collecCoffres)
		{
			int x = e.getIntAttribute("x");
			int y = e.getIntAttribute("y");
			
			int po = e.getIntAttribute("po",0);
			int qte = e.getIntAttribute("qte",1);
			
			String construct = e.getAttribute("contenu",null);
			IObjet contenu = null;
			
			if(construct != null)
			{
				String[]tab = construct.split(":");
				if(tab[0].equals("objet"))
				{
					contenu = genererObjet(tab[1]);
				}
			}
			
			
			res.add(new Coffre(idMap, x, y, contenu, qte, po));
		}
		
		return res;
	}

	/**
	 * Récupère un groupe d'ennemis à partir d'une noeud xml groupe.
	 * @throws SlickException 
	 */
	private static ArrayList<String> GenererEquipesEnnemisId(XMLElementList  groupesEnnemis) throws SlickException 
	{
		ArrayList<String> res = new ArrayList<String>();

		if(groupesEnnemis.size() == 0)
		{		
			return res;
		}
		XMLElementList ennemis = groupesEnnemis.get(0).getChildrenByName("groupe");
		for(int i = 0 ; i<ennemis.size();i++)
		{
			/*
			XMLElementList ennemisListe = ennemis.get(i).getChildrenByName("ennemi"); 
			int rarete = ennemis.get(i).getIntAttribute("rarete",1);
			String musique = ennemis.get(i).getAttribute("musique","battle");
			
			GestionnaireMusique.chargerMusique(musique);
			
			EquipeEnnemis equipeEnnemis = new EquipeEnnemis(rarete, musique);
			for(int j = 0;j<ennemisListe.size();j++)
			{
				System.out.println(ennemis.size());
				String idEnnemi = ennemisListe.get(j).getAttribute("id");		
				int place = ennemisListe.get(j).getIntAttribute("place");		
				
				equipeEnnemis.ajouter(place,genererEnnemi(idEnnemi));
			}
			
			res.add(equipeEnnemis);
			*/
			
			res.add(ennemis.get(i).getAttribute("id"));
		}
		
		return res;
	}
	
	private static Ennemi genererEnnemi(String id)
	{
		XMLParser analyseur = new XMLParser();
		XMLElement file;
		try 
		{
			file = analyseur.parse("ressources/donnees/ennemis.xml");		
			XMLElementList listeEnnemis = file.getChildrenByName("ennemi");
			ArrayList<XMLElement> collecEnnemis = new ArrayList<XMLElement>();
			listeEnnemis.addAllTo(collecEnnemis);
			
			for(XMLElement element : collecEnnemis)
			{

				if(element.getAttribute("id").equals(id))
				{
					String nom = element.getAttribute("nom","noname");
					String description = element.getAttribute("description","[no description]");
					
					int atqphy = element.getIntAttribute("atqphy",1);
					int atqmag = element.getIntAttribute("atqmag",1);
					int defphy = element.getIntAttribute("defphy",1);
					int defmag = element.getIntAttribute("defmag",1);
					int dext = element.getIntAttribute("dext",1);
					int soin = element.getIntAttribute("soin",1);
					int pv = element.getIntAttribute("pv",1);
					int xp = element.getIntAttribute("xp",1);
					int argent = element.getIntAttribute("argent",0);
					
					String img = element.getAttribute("image");
					
					String[] matImage = img.split(";");
					
					System.out.println(matImage[0]);
					
					Image image = new Image(matImage[0], new Color(255,0,255)).getSubImage(Integer.parseInt(matImage[1]), Integer.parseInt(matImage[2]), Integer.parseInt(matImage[3]), Integer.parseInt(matImage[4]));
					
					Ennemi ennemi = new Ennemi(id, nom,description,atqphy,atqmag,defphy,defmag,dext,soin,pv,xp,argent,image);
					return ennemi;
				}
			}
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Objet genererObjet(String id)
	{
		XMLParser analyseur = new XMLParser();
		XMLElement file;
		
		try 
		{
			//fichier parsé :
			file = analyseur.parse("ressources/donnees/objets.xml");		
			//récupération de la liste des objets :
			XMLElementList listeObjets = file.getChildrenByName("objet");
			//ajout des éléments dans une collection :
			ArrayList<XMLElement> collecObjets = new ArrayList<XMLElement>();
			listeObjets.addAllTo(collecObjets);
			
			Objet objet = null;
			
			for(XMLElement element : collecObjets)
			{

				if(element.getAttribute("id").equals(id))
				{
					String nom = element.getAttribute("nom","noname");
					String description = element.getAttribute("description","[no description]");
					int idimg = element.getIntAttribute("icone",0);
					boolean rare = element.getBooleanAttribute("rare", false);
					boolean combat = element.getBooleanAttribute("combat", false);
					
					ArrayList<String> effets = new ArrayList<String>();
					
					if(element.getChildrenByName("effets").size() > 0)
					{
						XMLElementList effetsListe = element.getChildrenByName("effets").get(0).getChildrenByName("effet");
						ArrayList<XMLElement> collecEffets = new ArrayList<XMLElement>();
						effetsListe.addAllTo(collecEffets);
						
						for(XMLElement e : collecEffets)
						{
							effets.add(e.getAttribute("item"));
						}
					}
					
					
					Image icone = getIcone( idimg, "ressources/images/objets.png");
					objet = new Objet(id, icone, description, nom, combat, rare, effets);
				}
			}
			
			return objet;
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static EquipeEnnemis genererGroupeEnnemis(String id)
	{
		XMLParser analyseur = new XMLParser();
		try 
		{
			XMLElement file = analyseur.parse("ressources/donnees/groupes.xml");
			XMLElementList listeGroupes = file.getChildrenByName("groupe");
			ArrayList<XMLElement> collecMaps = new ArrayList<XMLElement>();
			listeGroupes.addAllTo(collecMaps);
			
			
			for(XMLElement e : collecMaps)
			{
				if(e.getAttribute("id").equals(id))
				{					
					int rarete = e.getIntAttribute("rarete",1);
					String musique = e.getAttribute("musique",null);

					HashMap<Integer, Ennemi> ennemis = genererEnnemisPourGroupe(e.getChildrenByName("ennemi"));
					
					return new EquipeEnnemis(rarete, musique, ennemis); 
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

	private static HashMap<Integer, Ennemi> genererEnnemisPourGroupe(
			XMLElementList ennemis) throws SlickXMLException 
	{
		HashMap<Integer, Ennemi> res = new HashMap<Integer, Ennemi>();
		
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
			res.put(ennemis.get(i).getIntAttribute("place"), genererEnnemi(ennemis.get(0).getAttribute("id")));
		}
		
		return res;
	}
	
	public static PNJ genererPNJ(String id)
	{
		XMLParser analyseur = new XMLParser();
		try 
		{
			XMLElement file = analyseur.parse("ressources/donnees/pnj.xml");
			XMLElementList listePNJ = file.getChildrenByName("pnj");
			ArrayList<XMLElement> collecPNJ = new ArrayList<XMLElement>();
			listePNJ.addAllTo(collecPNJ);
			
			
			for(XMLElement e : collecPNJ)
			{
				if(e.getAttribute("id").equals(id))
				{					
					String nom = e.getAttribute("nom", "???");
					String pathSS = "ressources/spritesheet/"+e.getAttribute("spritesheet");
					
					SpriteSheet sprites = new SpriteSheet(pathSS, 32,32,new Color(255,0,255));
					Animation[] animations = new Animation[4];
					for(int i = 0; i<4; i++)
					{
						animations[i] = new Animation(sprites, 0, i, 2,i ,true, 100, true);
					}
					
					return new PNJ(id, nom, animations); 
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
	
	public static Dialogue genererDialogue(String id)
	{
		XMLParser analyseur = new XMLParser();
		try 
		{
			XMLElement file = analyseur.parse("ressources/donnees/dialogues.xml");
			XMLElementList listeDialogues = file.getChildrenByName("dialogue");
			ArrayList<XMLElement> collecDialogues = new ArrayList<XMLElement>();
			listeDialogues.addAllTo(collecDialogues);
			
			
			for(XMLElement e : collecDialogues)
			{
				if(e.getAttribute("id").equals(id))
				{					
					ArrayList<Replique> repliques = new ArrayList<Replique>();
					XMLElementList xmlRep = e.getChildrenByName("replique");
					for(int i=0; i<xmlRep.size(); i++)
					{
						String enonciateur = xmlRep.get(i).getAttribute("enonciateur", "???");
						String replique = xmlRep.get(i).getAttribute("replique");
						repliques.add(new Replique(replique, enonciateur));
					}
					return new Dialogue(repliques); 
				}
			}
			return new Dialogue(new ArrayList<Replique>());
		}
		catch (SlickException e)
		{
			System.out.println("Impossible de charger le fichier xml");
			e.printStackTrace();
			return null;
		}
	}
	
	public static Arme genererArme(String id)
	{
		XMLParser analyseur = new XMLParser();
		try 
		{
			XMLElement file = analyseur.parse("ressources/donnees/equipables.xml");
			XMLElementList listeArmes = file.getChildrenByName("armes").get(0).getChildrenByName("arme");
			ArrayList<XMLElement> collecArmes = new ArrayList<XMLElement>();
			listeArmes.addAllTo(collecArmes);
			
			
			for(XMLElement e : collecArmes)
			{
				if(e.getAttribute("id").equals(id))
				{	
					String nom = e.getAttribute("nom", "no name");
					int atqphy = e.getIntAttribute("atqphy",0);
					int atqmag = e.getIntAttribute("atqmag",0);
					int defphy = e.getIntAttribute("defphy",0);
					int defmag = e.getIntAttribute("defmag",0);
					int type = Arme.codeArme(e.getAttribute("type","epee"));
					
					String pathImg = e.getAttribute("image", "ressources/images/equipables.png");
					int idimg = e.getIntAttribute("idimg",0);
				    
					int x = idimg%15;
				    int y = (int) (idimg/15);
				    
					
					Image img = getIcone(idimg, pathImg);
					return new Arme(id, nom, type, atqphy, atqmag, defphy, defmag, img);
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
	
	public static Image genererImage(String id)
	{
		XMLParser analyseur = new XMLParser();
		try 
		{
			XMLElement file = analyseur.parse("ressources/donnees/images.xml");
			XMLElementList listeImages = file.getChildrenByName("image");
			ArrayList<XMLElement> collecImages = new ArrayList<XMLElement>();
			listeImages.addAllTo(collecImages);
			
			
			for(XMLElement e : collecImages)
			{
				if(e.getAttribute("id").equals(id))
				{	
					int x = e.getIntAttribute("x",0);
					int y = e.getIntAttribute("y",0);
					String path = e.getAttribute("file", null);
					if(path == null){
						return null;
					}
					Image img = new Image(path, new Color(255,0,255));
					int width = e.getIntAttribute("width", img.getWidth());
					int height = e.getIntAttribute("height", img.getHeight());
					
					return img.getSubImage(x, y, width, height);
					
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
	 */
	private static Image getIcone(int id, String src) throws SlickException
	{
		Image icones = new Image(src, new Color(255,0,255));
		return icones.getSubImage(1+(id%15)*21, 1 + ((int) id/15), 20, 20);
	}
	

}
