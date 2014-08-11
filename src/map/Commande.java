package map;

import java.util.ArrayList;


public class Commande {
	
	private int x;
	private int y;
	private int z;
	
	private ArrayList<Interact> targets;

	public Commande(int x,int y, int z, ArrayList<Interact> targets){
		this.targets = targets;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void run(Map map)
	{
		
		System.out.println("Exécution de la commande");
		for(Interact i : targets){
			i.toggleActive(map);
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public ArrayList<Interact> getInteracts(){
		return targets;
	}
	
	
}
