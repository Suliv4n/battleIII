package audio;


import java.util.HashMap;

import game.StatesId;
import game.settings.Settings;
import game.system.application.Application;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;



/**
 * M�thodes statiques permettant la gestion de la musique.
 * 
 * @author Darklev
 */
public class MusicManager 
{
	/**
	 * Musique en cours.
	 */
	private static Music currentMusic;
	
	/**
	 * Chemin vers le fichier .ogg
	 */
	private static String path = "#?NO MUSIC?#";
	

	/**
	 * Dictionnaire des musiques charg�es
	 */
	private static HashMap<String, Music> musics;
	
	private MusicManager(){}
	
	/**
	 * Initialise la classe.
	 */
	public static void init()
	{
		musics = new HashMap<String,Music>();
	}
	
	/**
	 * Joue en boucle la musique dont le nom est pass� en param�tre.
	 * S'il s'agit de la m�me musique que celle en cours, la nouvelle musique n'est pas charg�e
	 * et la musique en cours n'est donc pas interrompue.
	 * 
	 * @param musique
	 * 		Nom de la musique � jouer.
	 */
	public static void playLoop(String musique)
	{
		if(musics.get(musique) != null)
		{
			currentMusic = musics.get(musique);
			path = "resources/musiques/"+musique+".ogg";
			if(!musics.get(musique).playing())
			{
				currentMusic.loop();
				setVolume();
			}
		}
		else
		{
			try{
				musique = "resources/musiques/"+musique+".ogg";
				if(!path.equals(musique))
				{
					path = musique;
					currentMusic = new Music(musique);
					currentMusic.loop();
					setVolume();
				}
				
			}
			catch (SlickException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stoppe la musique en cours.
	 */
	public static void stop()
	{
				
		if(currentMusic != null)
		{
			if(currentMusic.playing())
			{
				currentMusic.stop();
			}
		}		
	}
	
	/**
	 * Met � jour le volume de la musique, en fonction du volume
	 * param�tr�e dans la Classe Jeu.Config.
	 */
	public static void setVolume()
	{
		currentMusic.setVolume(Settings.MUSIC_VOLUME/100f);
	}
	
	/**
	 * Charge une musique et la garde en m�moire dans le dictionnaire de musiques.
	 * Permet de jouer une musique plus rapidement avec la m�thode JouerEnBoucle().
	 * @throws SlickException 
	 */
	public static void loadMusic(String musique) throws SlickException
	{
		try{
		musics.put(musique, new Music("resources/musiques/"+musique+".ogg"));
		}
		catch(SlickException e){
			Application.application().debug("Music not found = " + musique);
		}
	}
}
