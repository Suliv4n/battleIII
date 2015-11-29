package map;

/**
 * Cette classe permet d'éxécution des actions sur un tile
 * comme le supprimer ou le réafficher par exemple.
 * 
 * @author Darklev
 *
 */
public class Interact {
	
	//id du tile
	private int idTile;
	
	//valeur à ajouter à l'id du tile lors de l'interact.
	private int change;
	
	//coordonnées du tile cible
	private int x;
	private int y;
	private int z;
	
	private boolean active = false;
	
	/**
	 * 
	 * @param x
	 * 		Abscisse du tile cible.
	 * @param y
	 * 		Ordonnée du tile cible.
	 * @param z
	 * 		Layer du tile cible.
	 * @param change
	 * 		Valeur à ajouter à l'id du tile cible.
	 */
	public Interact(int x, int y, int z, int change){
		this.x = x;
		this.y = y;
		this.z = z;
		this.change = change;
	}
	
	/**
	 * Supprime ou réaffiche le tile cible de l'interact courant.
	 * @param map
	 */
	public void toggleActive(Map map)
	{
		active = !active;
		
		if(active){
			map.getTiledMap().setTileId(x, y, z, 0); //suppression
		}
		else{
			map.getTiledMap().setTileId(x, y, z, idTile); //réaffichage
		}
		

	}
	
	/**
	 * Retourne l'abscisse du tile cible.
	 * 
	 * @return l'abscisse du tile cible.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Retourne l'ordonnée du tile cible.
	 * 
	 * @return l'ordonnée du tile cible.
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Retourne le layer du tile cible.
	 * 
	 * @return le layer du tile cible.
	 */
	public int getZ(){
		return z;
	}
	
	/**
	 * Parmètre le tile cible.
	 * @param idTile
	 * 		L'id du tile cible.
	 */
	public void setTileId(int idTile){
		this.idTile = idTile;
	}
	
}
