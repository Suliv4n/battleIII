package donnees;

import game.Config;
import game.Jeu;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import map.Map;
import personnage.Equipe;
import personnage.Guerrier;
import personnage.Mage;
import personnage.Personnage;
import personnage.Rodeur;
import skill.Skill;

import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.SortedMap;

import org.jdom2.Element;
import org.jdom2.output.*;


public class Sauvegarde 
{
	/**
	 * Sauvegarde
	 * @param sauvegarde
	 * @return
	 */
	public static boolean sauvegarder(String sauvegarde)
	{	
		Equipe equipe = Jeu.getEquipe();
		
		Element root = new Element("equipe");
		org.jdom2.Document document = new org.jdom2.Document(root); 
		Element position = new Element("position");
		
		//position
		position.setAttribute("x", String.valueOf((int) equipe.getAbsolueX()));
		position.setAttribute("y", String.valueOf((int) equipe.getAbsolueY()));
		position.setAttribute("map", equipe.getMap().getId());
		position.setAttribute("direction", String.valueOf(equipe.getDirection()));
		root.addContent(position);
		
		//equipes
		Element personnages = new Element("personnages");
		for(Personnage p : equipe)
		{
			Element personnage = null;
			if(p instanceof Mage)
			{
				personnage = new Element("mage");
			}
			else if(p instanceof Guerrier)
			{
				personnage = new Element("guerrier");
			}
			else if(p instanceof Rodeur)
			{
				personnage = new Element("rodeur");
			}
			
			for(Skill s : p.getSkills()){
				Element skill = new Element("skill");
				skill.setAttribute("id", s.getId());
				skill.setAttribute("level", String.valueOf(s.getLevel()));
				personnage.addContent(skill);
			}
			
			personnage.setAttribute("nom", p.getNom());
			personnage.setAttribute("xp", String.valueOf(p.getXP()));
			personnages.addContent(personnage);
		}
		root.addContent(personnages);
		
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		try 
		{
			sortie.output(document, new FileOutputStream("ressources/donnees/saves/"+sauvegarde+".xml"));
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
	 * @param sauvegarde
	 * 	Nom de la sauvegarde
	 * @throws SlickException
	 */
	public static void charger(String sauvegarde) throws SlickException
	{
		XMLParser parser = new XMLParser();
		XMLElement root = parser.parse("ressources/donnees/saves/"+sauvegarde+".xml");
		
		//récupération de l'équipe de personnages
		Equipe equipe = new Equipe(3);
		XMLElementList personnages = root.getChildrenByName("personnages").get(0).getChildren();
		for(int i=0; i<personnages.size(); i++)
		{
			XMLElement e = personnages.get(i);
			Personnage p = null;
			if(e.getName() == "mage")
			{
				String nom = e.getAttribute("nom");
				p = new Mage(nom);
			}
			else if(e.getName() == "rodeur")
			{
				String nom = e.getAttribute("nom");
				p = new Rodeur(nom);
			}
			else if(e.getName() == "guerrier")
			{
				String nom = e.getAttribute("nom");
				p = new Guerrier(nom);
			}
			
			XMLElementList skills= e.getChildrenByName("skill");
			
			//skills du personnage
			for(int j=0; j<skills.size();j++){
				String idSkill = skills.get(j).getAttribute("id","");
				if(!idSkill.equals("")){
					Skill skill = GenerateurDonnees.genererSkill(idSkill);
					p.apprendreCompetence(skill);
					skill.setLevel(skills.get(j).getIntAttribute("level",1));
				}
			}
			
			int xp = e.getIntAttribute("xp",0);
			p.gagnerXP(xp);
			equipe.ajouter(p);
		}
		
		
		//position de l'équipe
		XMLElement e = root.getChildrenByName("position").get(0);
		int x = e.getIntAttribute("x");
		int y = e.getIntAttribute("y");
		int direction = e.getIntAttribute("direction");
		String idMap = e.getAttribute("map");
		Map map = GenerateurDonnees.genererMap(idMap, (int) (- x + Config.LONGUEUR/2), ((int) - y + Config.LARGEUR/2), true);
		
		equipe.setValAbsolueX(x);
		equipe.setValAbsolueY(y);
		equipe.setMap(map);
		equipe.setDirection(direction);
		System.out.println(equipe.get(0));
		Jeu.setEquipe(equipe);
		
	}
	
	/**
	 * Retourne l'équipe de la sauvegarde passé en paramètre.
	 * @param save
	 * @return
	 * @throws FileNotFoundException 
	 * @throws SlickXMLException 
	 */
	private static Equipe getEquipeFromSave(File save) throws SlickXMLException, FileNotFoundException{
		
		XMLParser parser = new XMLParser();
		XMLElement root = parser.parse("equipe", new FileInputStream(save));
		
		Equipe equipe = new Equipe(3);
		
		XMLElementList personnages = root.getChildrenByName("personnages").get(0).getChildren();
		for(int i=0; i<personnages.size(); i++)
		{
			XMLElement e = personnages.get(i);
			Personnage p = null;
			if(e.getName() == "mage")
			{
				String nom = e.getAttribute("nom");
				p = new Mage(nom);
			}
			else if(e.getName() == "rodeur")
			{
				String nom = e.getAttribute("nom");
				p = new Rodeur(nom);
			}
			else if(e.getName() == "guerrier")
			{
				String nom = e.getAttribute("nom");
				p = new Guerrier(nom);
			}
			
			
			int xp = e.getIntAttribute("xp",0);
			p.gagnerXP(xp);
			

			
			equipe.ajouter(p);
		}
		
		//position de l'équipe
		XMLElement e = root.getChildrenByName("position").get(0);
		int x = e.getIntAttribute("x");
		int y = e.getIntAttribute("y");
		int direction = e.getIntAttribute("direction");
		String idMap = e.getAttribute("map");
		Map map = GenerateurDonnees.genererMap(idMap, (int) (- x + Config.LONGUEUR/2), ((int) - y + Config.LARGEUR/2), false);
		
		equipe.setValAbsolueX(x);
		equipe.setValAbsolueY(y);
		equipe.setMap(map);
		equipe.setDirection(direction);
		System.out.println(equipe.get(0));
		Jeu.setEquipe(equipe);
			
		return equipe;
	}
	
	/**
	 * Retourne toutes les Equipes sauvegardées.
	 * 
	 * @return toutes les équipes sauvegardées.
	 * @throws FileNotFoundException 
	 * @throws SlickXMLException 
	 */
	public static ArrayList<Equipe> getAllSauvegardes() throws SlickXMLException, FileNotFoundException{
		ArrayList<Equipe> res = new ArrayList<Equipe>();
		
		for(int i = 1; i<100; i++){
			File file = new File("ressources/donnees/saves/save"+i+".xml");
			Equipe equipe = null;
			if(file.exists())
			{
				equipe = getEquipeFromSave(file);
				if(equipe.nbPersonnages() == 0)
				{
					equipe = null;
				}
			}
			res.add(equipe);
		}
		
		return res;
	}
}
