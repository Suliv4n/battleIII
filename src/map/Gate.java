package map;

public class Gate 
{
	private int x;
	private int y;
	private String idMapCible;
	private int xCible;
	private int yCible;
	
	public Gate(int x, int y, String idMapCible, int xCible, int yCible)
	{
		this.idMapCible = idMapCible;
		this.xCible = xCible;
		this.yCible = yCible;
		this.x = x;
		this.y = y;
	}
	
	public String getIdMapCible()
	{
		return idMapCible;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getXT()
	{
		return xCible;
	}
	
	public int getYT()
	{
		return yCible;
	}
	
	public String getTarget()
	{
		return idMapCible;
	}
}
