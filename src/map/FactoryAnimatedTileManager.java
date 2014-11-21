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
		String type = map.getTileProperty(idTile, "type", "loop");
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
			//récupération de toutes les tiles nécessaire à l'animation
			ArrayList<Integer> framekey = new ArrayList<Integer>();
			int i = idTile;
			int fk;
			do
			{
				fk = parseTile(i, map);
				if(fk != -1)
				{
					framekey.add(fk);
				}
				i++;
			}
			while(fk != -1);
			return new AnimatedTileManager(codeType, framekey , idTile);
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
