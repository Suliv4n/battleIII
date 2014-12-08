package game.system;

import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;

/**
 * Class permettant d'avoir des infos sur le système
 * (mémoire, fps ...).
 * 
 * @author Darklev
 *
 */
public class SystemInformations {
	
	private static SystemInformations system;
	private ArrayList<Long> memoryUsage;

	
	private static final Color AXE_X_COLOR = new Color(255,255,255);
	private static final Color AXE_Y_COLOR = new Color(255,255,255);
	private static final int AXE_X_WIDTH = 200;
	private static final int AXE_Y_WIDTH = 100;
	private static final Color LINE_COLOR = new Color(150,50,50);
	private static final float AXE_Y_SCALE_PX_PER_MB = 0.5f;
	private static final float AXE_X_SCALE_PX_PER_LOG = 5.0f;
	
	private static final int MAX_LOG = (int) (AXE_X_WIDTH / AXE_X_SCALE_PX_PER_LOG);
	
	private static final int LOG_EVERY = 2000;
	
	private int lastLog = 0;
	
	private SystemInformations(){
		memoryUsage = new ArrayList<Long>();
	}
	
	/**
	 * Met à jour les informations du sysème.
	 * 
	 * @param delta
	 * 		Temps entre deux appels de log().
	 */
	public void log(int delta){
		lastLog += delta;
		if(lastLog >= LOG_EVERY)
		{
			memoryUsage.add(getMemoryUsedInMB());
			if(memoryUsage.size() == MAX_LOG){
				memoryUsage.remove(0);
			}
			lastLog = 0;
		}
		
	}
	
	/**
	 * Retourne la mémoire totale en Méga Bytes.
	 * 
	 * @return la mémoire totale en MégaBytes.
	 */
	public long getMemoryInMB(){
		return (Runtime.getRuntime().totalMemory()/1000000);
	}
	
	/**
	 * Retourne la mémoire utilisée en MégaBytes
	 * 
	 * @return la mémoire utilisée en MégaBytes.
	 */
	public long getMemoryUsedInMB(){
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000;
	}

	/**
	 * Retourne le nombre de FPS.
	 * 
	 * @return
	 */
	public int getFPS(){
		return Application.application().getContainer().getFPS();
	}
	
	/**
	 * Retourne l'instance.
	 * 
	 * @return l'instance.
	 */
	public static SystemInformations getInstance(){
		if(system == null){
			system = new SystemInformations();
		}
		return system;
	}
	
	public void render(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("FPS : ")
		.append(getFPS())
		.append("\nMemory total(used) : ")
		.append(getMemoryInMB())
		.append(" MB")
		.append("(")
		.append(getMemoryUsedInMB())
		.append(" MB)");
		String res = buffer.toString();
		
		Application.application().drawString(res, 10, 10);
		renderMemoryUsageGraph(10, 50);
	}
	
	public void renderMemoryUsageGraph(int x, int y){
		Graphics g = Application.application().getGraphics();
		g.setColor(AXE_Y_COLOR);
		g.drawLine(x, y, x, y + AXE_Y_WIDTH);
		g.setColor(AXE_X_COLOR);
		g.drawLine(x, y + AXE_Y_WIDTH, x + AXE_X_WIDTH, y + AXE_Y_WIDTH);
		g.setColor(LINE_COLOR);
		if(memoryUsage.size() >= 2){
			int preventMemX = x;
			int preventMemY = (int) (y + AXE_Y_WIDTH + (-memoryUsage.get(0).intValue()) * AXE_Y_SCALE_PX_PER_MB);
			for(int i=1; i<memoryUsage.size(); i++){
				g.setColor(LINE_COLOR);
				int memX = (int) ((x+i * AXE_X_SCALE_PX_PER_LOG ));
				int memY = (int) (y + AXE_Y_WIDTH + (-memoryUsage.get(i).intValue()) * AXE_Y_SCALE_PX_PER_MB);
				g.drawLine(preventMemX, preventMemY, memX, memY);
				
				preventMemX = memX;
				preventMemY = memY;
			}
		}

	}
}
