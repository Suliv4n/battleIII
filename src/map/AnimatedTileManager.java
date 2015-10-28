package map;

import java.util.ArrayList;

/**
 * Cette classe permet de gérer les tiles animées d'une map.
 * 
 * @author Darklev
 *
 */
public class AnimatedTileManager 
{
	public static final int REVERSE = 0; //animation de type : 0 - 1 - 2 - 3 - 2 - 1 - 0 - 1 - 2 ...
	public static final int LOOP = 1; //anitmation de type : 0 - 1 - 2 - 0 - 1 - 2 - 0 ...

	
	private int type;
	private ArrayList<Integer> frames;
	
	private int begin; //premier tile
	private int index = 0; //le tile à fichier à pour id : begin + index 
	private int max;
	
	private int time = 0;
	
	
	private int id;
	
	/**
	 * Constructeur
	 * 
	 * @param type
	 * 		Type de de tile animée. (REVERSE ou LOOP)
	 * @param frames
	 * 		Nombres de frame entre 2 tiles.
	 * @param begin
	 * 		Id de la première tile
	 */
	public AnimatedTileManager(int type, ArrayList<Integer> frames, int begin)
	{
		this.type = type;
		this.frames = frames;
		this.begin = begin;
		
		id = begin;
		max = frames.get(frames.size()-1);
	}
	
	/**
	 * Met à jour l'animatedTileManager et retourne l'id du tile courant.
	 * @return
	 * 		l'id du tile courant.
	 */
	public void update(int delta)
	{
		time += delta;
		
		if(time >= frames.get(index))
		{
			index = (index + 1) % frames.size(); 
		}
		if(time >= max)
		{
			time = 0;
		}

		
		id = begin + index;
	}
	
	/**
	 * Retourne l'id du tile courant.
	 * @return
	 */
	public int getCurrentID()
	{
		return id;
	}
	
	/**
	 * Retourne vrai si le tile passé en paramètre est inclu dans l'animation, sinon retourne faux.
	 * @param tile
	 * 	Id du tile à tester.
	 * @return
	 * 		Vrai si le tile est inclu dans l'animation, faux sinon.
	 */
	public boolean containsTile(int tile)
	{
		//System.out.println(begin >= tile && tile >= begin+frames.size());
		return tile >= begin && tile <= begin+frames.size() - 1; 
	}
}
