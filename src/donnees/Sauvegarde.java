package donnees;

import game.Config;
import game.Jeu;


import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import map.Map;

import personnage.Equipe;
import personnage.Guerrier;
import personnage.Mage;
import personnage.Personnage;
import personnage.Rodeur;

import java.io.*;

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
		
		org.jdom2.Element root = new org.jdom2.Element("equipe");
		org.jdom2.Document document = new org.jdom2.Document(root); 
		org.jdom2.Element position = new org.jdom2.Element("position");
		
		//position
		position.setAttribute("x", String.valueOf((int) equipe.getAbsolueX()));
		position.setAttribute("y", String.valueOf((int) equipe.getAbsolueY()));
		position.setAttribute("map", equipe.getMap().getId());
		position.setAttribute("direction", String.valueOf(equipe.getDirection()));
		root.addContent(position);
		
		//equipes
		org.jdom2.Element personnages = new org.jdom2.Element("personnages");
		for(Personnage p : equipe)
		{
			org.jdom2.Element personnage = null;
			if(p instanceof Mage)
			{
				personnage = new org.jdom2.Element("mage");
			}
			else if(p instanceof Guerrier)
			{
				personnage = new org.jdom2.Element("guerrier");
			}
			else if(p instanceof Rodeur)
			{
				personnage = new org.jdom2.Element("rodeur");
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
	public static void getSauvegarde(String sauvegarde) throws SlickException
	{
		XMLParser parser = new XMLParser();
		XMLElement root = parser.parse("ressources/donnees/saves/"+sauvegarde+".xml");
		//XMLElement root = file.getChildrenByName("equipe").get(0);
		
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
		Map map = GenerateurDonnees.genererMap(idMap, (int) (- x + Config.LONGUEUR/2), ((int) - y + Config.LARGEUR/2));
		
		equipe.setValAbsolueX(x);
		equipe.setValAbsolueY(y);
		equipe.setMap(map);
		equipe.setDirection(direction);
		System.out.println(equipe.get(0));
		Jeu.setEquipe(equipe);
		
	}
}
