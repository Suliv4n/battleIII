package map;

import java.util.ArrayList;

import org.newdawn.slick.tiled.TiledMap;

public class FactoryAnimatedTileManager 
{
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
			System.out.println("animtation tile detectée pour : " + idTile);
			ArrayList<Integer> framekey = new ArrayList<Integer>();
			int i = idTile;
			int fk;
			do
			{
				fk = analyserTile(i, map);
				if(fk != -1)
				{
					framekey.add(fk);
					System.out.println("	-next : "+fk);
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
	
	private static int analyserTile(int idTile, TiledMap map)
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
