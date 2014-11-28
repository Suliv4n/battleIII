/*
				  ___    _  _____  _____  _     ___          ___  ___  ___                                 
				 | _ )  /_\|_   _||_   _|| |   | __|        |_ _||_ _||_ _|                                
				 | _ \ / _ \ | |    | |  | |__ | _|          | |  | |  | |                                 
				 |______/_\__|____  |___ |____|____| _____  ____|___________  _       _    _  _  ___       
				 |_   _|| || || __| | __|/_\  | \| ||_   _|/_\  / __|\ \ / / | |     /_\  | \| ||   \      
				   | |  | __ || _|  | _|/ _ \ | .` |  | | / _ \ \__ \ \ V /  | |__  / _ \ | .` || |) |     
				   |_|  |____||_____|__/__ \____|\____|____/ \_\|___/____|___|____|/_____\|____|_____  ___ 
				  / _ \ | __| |_   _|| || || __|  / __|/ _ \ | \| | / __|| __|| _ \|_   _|| __|| | | || _ \
				 | (_) || _|    | |  | __ || _|  | (__| (_) || .` || (__ | _| |  _/  | |  | _| | |_| ||   /
				  \___/ |_|     |_|  |_||_||___|  \___|\___/ |_|\_| \___||___||_|    |_|  |___| \___/ |_|_\

 */

package game.launcher;

import org.newdawn.slick.SlickException;

import game.system.application.Application;

/**
 * MAIN CLASS
 * 
 * @author Darklev
 *
 */
public class Launcher
{
	public static void main(String[] args) {
		lib.LibUtil.addToJavaLibraryPath("lib");
		try {
			Application.application().launch();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}