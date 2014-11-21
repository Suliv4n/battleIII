package ui;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;



/**
 * Affichage d'une barre avec un remplissage.
 * 
 * @author Darklev
 *
 */
public class BarUI 
{
	private Color backgroundColor; //background
	private Color foregroundColor; //foreground
	private Color borderColor;
	
	float current;
	float max;
	
	boolean border = false;
	int width;
	int height;
	
	int type = 1;
	
	TypeBarre typeBarre;
	
	
	
	//#region----CONSTRUCTEURS------------------
	/**
	 * Créé une barre ui.
	 * 
	 * @param foreground
	 * 		Couleur de la barre.
	 * @param background
	 * 		Couleur de fond la barre.
	 * @param width
	 * 		Longueur de la barre.
	 * @param height
	 * 		Hauteur de la barre.
	 * @param current
	 * 		Valeur courante.
	 * @param max
	 * 		Valeur maximale.
	 */
	public BarUI(Color foreground, Color background, int width, int height, float current, float max)
	{
		backgroundColor = background;
		foregroundColor = foreground;
		
		this.current = current;
		this.max = max;
		
		borderColor = Color.black;
		this.width = width;
		this.height = height;
		
		typeBarre = TypeBarre.LEFT_TO_RIGHT;
	}
	
	/**
	 * Créé une barre ui.
	 * 
	 * @param foreground
	 * 		Couleur de la barre.
	 * @param background
	 * 		Couleur de fond la barre.
	 * @param width
	 * 		Longueur de la barre.
	 * @param height
	 * 		Hauteur de la barre.
	 * @param current
	 * 		Valeur courante.
	 * @param max
	 * 		Valeur maximale.
	 * @param typeBarre
	 * 		Type de barre (RITGH_TO_LEFT, LEFT_TO_RIGHT, UP_TO_DOWN, DOWN_TO_UP)
	 */
	public BarUI(Color foreground, Color background, int width, int height, float current, float max, TypeBarre typeBarre)
	{
		backgroundColor = background;
		foregroundColor = foreground;
		
		this.current = current;
		this.max = max;
		
		borderColor = Color.black;
		this.width = width;
		this.height = height;
		
		this.typeBarre = typeBarre;
		
	}
	
	/**
	 * Créé une barre ui.
	 * 
	 * @param foreground
	 * 		Couleur de la barre.
	 * @param background
	 * 		Couleur de fond la barre.
	 * @param width
	 * 		Longueur de la barre.
	 * @param height
	 * 		Hauteur de la barre.
	 * @param current
	 * 		Valeur courante.
	 * @param max
	 * 		Valeur maximale.
	 * @param typeBarre
	 * 		Type de barre (RITGH_TO_LEFT, LEFT_TO_RIGHT, UP_TO_DOWN, DOWN_TO_UP)
	 * @param border
	 * 		Vrai si la bordure doit être affichée, faux sinon.
	 */
	public BarUI(Color foreground, Color background, int width, int height, float current, float max, TypeBarre typeBarre, boolean border)
	{
		backgroundColor = background;
		foregroundColor = foreground;
		
		this.current = current;
		this.max = max;
		
		borderColor = Color.black;
		this.border = border;
		this.width = width;
		this.height = height;
		
		this.typeBarre = typeBarre;
		
	}
	
	/**
	 * Créé une barre ui.
	 * 
	 * @param foreground
	 * 		Couleur de la barre.
	 * @param background
	 * 		Couleur de fond la barre.
	 * @param width
	 * 		Longueur de la barre.
	 * @param height
	 * 		Hauteur de la barre.
	 * @param current
	 * 		Valeur courante.
	 * @param max
	 * 		Valeur maximale.
	 * @param typeBarre
	 * 		Type de barre (RITGH_TO_LEFT, LEFT_TO_RIGHT, UP_TO_DOWN, DOWN_TO_UP)
	 * @param border
	 * 		Vrai si la bordure doit être affichée, faux sinon.
	 * @param borderColor
	 * 		Couleur de la bordure.
	 * 		
	 */
	public BarUI(Color foreground, Color background, int width, int height, float current, float max, TypeBarre typeBarre, boolean border, Color borderColor)
	{
		backgroundColor = background;
		foregroundColor = foreground;
		
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
	public void render(Graphics g, int x, int y)
	{
		//background
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
		
		//foreground
		g.setColor(foregroundColor);
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
