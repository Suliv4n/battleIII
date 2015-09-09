package data;

import game.Config;
import game.system.application.Application;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import map.Map;
import skill.Skill;

import java.io.*;
import java.util.ArrayList;

import org.jdom2.Element;
import org.jdom2.output.*;

import characters.Character;
import characters.Mage;
import characters.Party;
import characters.Ranger;
import characters.Warrior;



public class Save 
{
	/**
	 * Sauvegarde les informations sur la session en cours.
	 * @param save
	 * 		Fichier qui contient la sauvegarde.
	 * @return
	 * 		Vrai si la sauvegarde s'est bien déroulé.
	 */
	public static boolean sauvegarder(String save)
	{	
		Party party = Application.application().getGame().getParty();
		
		Element root = new Element("equipe");
		org.jdom2.Document document = new org.jdom2.Document(root); 
		Element position = new Element("position");
		
		//position
		position.setAttribute("x", String.valueOf((int) party.getAbsoluteX()));
		position.setAttribute("y", String.valueOf((int) party.getAbsoluteY()));
		position.setAttribute("map", party.getMap().getId());
		position.setAttribute("direction", String.valueOf(party.getDirection()));
		root.addContent(position);
		
		//equipes
		Element characters = new Element("personnages");
		for(Character c : party)
		{
			Element character = null;
			if(c instanceof Mage)
			{
				character = new Element("mage");
			}
			else if(c instanceof Warrior)
			{
				character = new Element("guerrier");
			}
			else if(c instanceof Ranger)
			{
				character = new Element("rodeur");
			}
			
			for(Skill s : c.getSkills()){
				Element skill = new Element("skill");
				skill.setAttribute("id", s.getId());
				skill.setAttribute("level", String.valueOf(s.getLevel()));
				character.addContent(skill);
			}
			
			character.setAttribute("nom", c.getName());
			character.setAttribute("xp", String.valueOf(c.getExperience()));
			characters.addContent(character);
		}
		root.addContent(characters);
		
		XMLOutputter out = new XMLOutputter(org.jdom2.output.Format.getPrettyFormat());
		try 
		{
			out.output(document, new FileOutputStream("resources/donnees/saves/"+save+".xml"));
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * Applique au jeu la sauvegarde dont le nom est passé en paramètre.
	 * 
	 * @param save
	 * 	Nom de la sauvegarde
	 * @throws SlickException
	 */
	public static void loadSave(String save) throws SlickException
	{
		XMLParser parser = new XMLParser();
		XMLElement root = parser.parse("resources/donnees/saves/"+save+".xml");
		
		//récupération de l'équipe de personnages
		Party party = new Party(3);
		XMLElementList characters = root.getChildrenByName("personnages").get(0).getChildren();
		for(int i=0; i<characters.size(); i++)
		{
			XMLElement e = characters.get(i);
			Character c = null;
			if(e.getName() == "mage")
			{
				String name = e.getAttribute("nom");
				c = new Mage(name);
			}
			else if(e.getName() == "rodeur")
			{
				String nom = e.getAttribute("nom");
				c = new Ranger(nom);
			}
			else if(e.getName() == "guerrier")
			{
				String nom = e.getAttribute("nom");
				c = new Warrior(nom);
			}
			
			XMLElementList skills= e.getChildrenByName("skill");
			
			//skills du personnage
			for(int j=0; j<skills.size();j++){
				String idSkill = skills.get(j).getAttribute("id","");
				if(!idSkill.equals("")){
					Skill skill = DataManager.loadSkill(idSkill);
					c.learnSkill(skill);
					skill.setLevel(skills.get(j).getIntAttribute("level",1));
				}
			}
			
			int experience = e.getIntAttribute("xp",0);
			c.gainExperience(experience);
			party.add(c);
		}
		
		
		//position de l'équipe
		XMLElement e = root.getChildrenByName("position").get(0);
		int x = e.getIntAttribute("x");
		int y = e.getIntAttribute("y");
		int direction = e.getIntAttribute("direction");
		String idMap = e.getAttribute("map");
		Map map = DataManager.loadMap(idMap, (int) (- x + Config.LONGUEUR/2), ((int) - y + Config.LARGEUR/2), true);
		
		party.setValAbsoluteX(x);
		party.setValAbsoluteY(y);
		party.setMap(map);
		party.setDirection(direction);
		Application.application().getGame().setParty(party);
		Application.application().getGame().setCurrentSave(save);
	}
	
	/**
	 * Retourne l'équipe de la sauvegarde passé en paramètre.
	 * @param save
	 * @return
	 * @throws FileNotFoundException 
	 * @throws SlickException 
	 */
	private static Party gerPartyFromSave(File save) throws FileNotFoundException, SlickException{
		
		XMLParser parser = new XMLParser();
		XMLElement root = parser.parse("equipe", new FileInputStream(save));
		
		Party party = new Party(3);
		
		XMLElementList characters = root.getChildrenByName("personnages").get(0).getChildren();
		for(int i=0; i<characters.size(); i++)
		{
			XMLElement e = characters.get(i);
			Character p = null;
			if(e.getName() == "mage")
			{
				String name = e.getAttribute("nom");
				p = new Mage(name);
			}
			else if(e.getName() == "rodeur")
			{
				String name = e.getAttribute("nom");
				p = new Ranger(name);
			}
			else if(e.getName() == "guerrier")
			{
				String name = e.getAttribute("nom");
				p = new Warrior(name);
			}
			
			
			int experience = e.getIntAttribute("xp",0);
			p.gainExperience(experience);
		
			party.add(p);
		}
		
		//position de l'équipe
		XMLElement e = root.getChildrenByName("position").get(0);
		int x = e.getIntAttribute("x");
		int y = e.getIntAttribute("y");
		int direction = e.getIntAttribute("direction");
		String idMap = e.getAttribute("map");
		Map map = DataManager.loadMap(idMap, (int) (- x + Config.LONGUEUR/2), ((int) - y + Config.LARGEUR/2), false);
		
		party.setValAbsoluteX(x);
		party.setValAbsoluteY(y);
		party.setMap(map);
		party.setDirection(direction);
		System.out.println(party.get(0));
		Application.application().getGame().setParty(party);
			
		return party;
	}
	
	/**
	 * Retourne toutes les Equipes sauvegardées.
	 * 
	 * @return toutes les équipes sauvegardées.
	 * @throws FileNotFoundException 
	 * @throws SlickException 
	 */
	public static ArrayList<Party> getAllSauvegardes() throws FileNotFoundException, SlickException{
		ArrayList<Party> res = new ArrayList<Party>();
		
		for(int i = 1; i<100; i++){
			File file = new File("resources/donnees/saves/save"+i+".xml");
			Party party = null;
			if(file.exists())
			{
				party = gerPartyFromSave(file);
				if(party.numberOfCharacters() == 0)
				{
					party = null;
				}
			}
			res.add(party);
		}
		
		return res;
	}
}
