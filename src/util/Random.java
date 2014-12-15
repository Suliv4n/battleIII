package util;

public class Random {
	/**
	 * Génère un entier aléatoire entre les bornes incluses
	 * passées en paramètre.
	 * 
	 * @param min
	 * 		Borne minimale.
	 * @param max
	 * 		Borne maximale.
	 */
	public static int randInt(int min, int max){
		java.util.Random rand = new java.util.Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    
	    return randomNum;
	}
}
