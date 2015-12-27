package map;

import java.util.ArrayList;

import org.newdawn.slick.tiled.TiledMap;

/**
 * Fabrique d'AnimatedTileManager.
 * @author Sulivan
 *
 */
public class FactoryAnimatedTileManager 
{
	/**
	 * Fabrique une AnimatedTileManager à partir d'une tile d'une map.
	 * 
	 * @param map
	 * 		Map dont le tile animé appartient.
	 * @param idTile
	 * 		Id de la tile animée.
	 * @return
	 * 		L'AnimatedTileManager du tile passé en paramètre ou retourne null 
	 * 		si le tile n'est pas un tile animé.
	 */
	public static AnimatedTileManager make(TiledMap map, int idTile)
	{
		String animRequest = map.getTileProperty(idTile, "animation", "false");
		String type = map.getTileProperty(idTile, "animationtype", "loop");
		int codeType = 0;
		if(type.equalsIgnoreCase("loop"))
		{
			codeType = AnimatedTileManager.LOOP;
		}
		else if(type.equalsIgnoreCase("reverse"))
		{
			codeType = AnimatedTileManager.REVERSE;
		}
		
		
		if(!animRequest.equals("false"))
		{
			//récupération de toutes les tiles nécessaires à l'animation
			ArrayList<Integer> timekeys = new ArrayList<Integer>();
			int i = idTile;
			int time = 0;
			int timekey = 0;
			do
			{
				timekey = parseTile(i, map);
				if(timekey != -1)
				{
					time += timekey;
					timekeys.add(time);
				}
				i++;
			}
			while(timekey != -1);
			return new AnimatedTileManager(codeType, timekeys , idTile);
		}
		else
		{
			return null;
		}
		
	}
	
	/**
	 * Analyse une tile pour retourner la tile suivante de son animation.
	 * Retourne -1 si c'est la dernière tile.
	 * @param idTile
	 * 		Tile qui doit être analysé.
	 * @param map
	 * 		Map où se trouve la tile à analyser.
	 * @return
	 * 		L'id du tile suivant dans l'animation ou -1 si c'est le dernier tile de l'animation.
	 */
	private static int parseTile(int idTile, TiledMap map)
	{
		String tid = map.getTileProperty(idTile, "animation", "false");
		
		try{
			int next = Integer.parseInt(tid);
			return next;
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
		
	}
	
	

}
