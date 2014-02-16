package map;

import java.util.ArrayList;

public class AnimatedTileManager 
{
	public static final int REVERSE = 0; //animation de type : 0 - 1 - 2 - 3 - 2 - 1 - 0 - 1 - 2 ...
	public static final int LOOP = 1; //anitmation de type : 0 - 1 - 2 - 0 - 1 - 2 - 0 ...

	
	private int type;
	private ArrayList<Integer> frames;
	
	private int begin; //premier tile
	private int currentFrame = 0;
	private int index = 0; //le tile à fichier à pour id : begin + index 
	private int max;
	
	
	private int id;
	
	public AnimatedTileManager(int type, ArrayList<Integer> frames, int begin)
	{
		this.type = type;
		this.frames = frames;
		this.begin = begin;
		
		id = begin;
		max = frames.get(frames.size()-1);
	}
	
	public int next()
	{
		currentFrame += 1;
		if(currentFrame >= frames.get(index))
		{
			index = (index + 1) % frames.size(); 
		}
		if(currentFrame == max)
		{
			currentFrame = 0;
		}
		return begin + index;
	}
	
	public int getID()
	{
		return id;
	}
	
	public boolean containsTile(int tile)
	{
		//System.out.println(begin >= tile && tile >= begin+frames.size());
		return tile >= begin && tile <= begin+frames.size() - 1; 
	}
}
