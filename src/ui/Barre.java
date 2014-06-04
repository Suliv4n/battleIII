package ui;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;



/**
 * Affichage d'une barre avec un remplissage.
 * 
 * @author Sulivan
 *
 */
public class Barre 
{
	Color bg; //background
	Color fg; //foreground
	Color borderColor;
	
	float current;
	float max;
	
	boolean border = false;
	int width;
	int height;
	
	int type = 1;
	
	TypeBarre typeBarre;
	
	
	
	//#region----CONSTRUCTEURS------------------
	
	public Barre(Color foreground, Color background, int width, int height, float current, float max)
	{
		bg = background;
		fg = foreground;
		
		this.current = current;
		this.max = max;
		
		borderColor = Color.black;
		this.width = width;
		this.height = height;
		
		typeBarre = TypeBarre.LEFT_TO_RIGHT;
	}
	
	public Barre(Color foreground, Color background, int width, int height, float current, float max, TypeBarre typeBarre)
	{
		bg = background;
		fg = foreground;
		
		this.current = current;
		this.max = max;
		
		borderColor = Color.black;
		this.width = width;
		this.height = height;
		
		this.typeBarre = typeBarre;
		
	}
	
	public Barre(Color foreground, Color background, int width, int height, float current, float max, TypeBarre typeBarre, boolean border)
	{
		bg = background;
		fg = foreground;
		
		this.current = current;
		this.max = max;
		
		borderColor = Color.black;
		this.border = border;
		this.width = width;
		this.height = height;
		
		this.typeBarre = typeBarre;
		
	}
	
	public Barre(Color foreground, Color background, int width, int height, float current, float max, TypeBarre typeBarre, boolean border, Color borderColor)
	{
		bg = background;
		fg = foreground;
		
		this.current = current;
		this.max = max;
		
		this.borderColor = borderColor;
		this.border = border;
		this.width = width;
		this.height = height;
		
		this.typeBarre = typeBarre;
		
	}
	//#endregion------------------------------------
	
	/**
	 * Affiche la barre remplie au taux current/max
	 * 
	 * @param g
	 * 	Graphics sur lequel la barre doit s'afficher.
	 * 
	 * @param x
	 * 	Absisce d'affichage.
	 * 	
	 * @param y
	 * 	Ordonnée d'affichage.
	 */
	public void afficher(Graphics g, int x, int y)
	{
		//background
		g.setColor(bg);
		g.fillRect(x, y, width, height);
		
		//foreground
		g.setColor(fg);
		if(typeBarre == TypeBarre.LEFT_TO_RIGHT)
		{
			g.fillRect(x+1, y+1, current/max * width - 1 , height-1);
		}
		
		//border
		if(border)
		{
			g.setColor(borderColor);
			g.drawRect(x, y, width, height);
		}
	}
}
