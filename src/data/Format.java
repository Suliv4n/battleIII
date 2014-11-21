package data;


/**
 * Méthodes permettant de formater des chaînes de caracères en fonction des besoin du jeu.
 * 
 * @author Darklev
 */
public class Format 
{
	/**
	 * Formate une chaine de caractère pour la séparer en plusieurs lignes
	 * (avec \n). Chaque ligne contiendra au plus le nombre passé en paramètre de 
	 * caractères.
	 * 
	 * @param string
	 * 		La chaine à formater.
	 * @param car
	 * 		Le nombre de caractères maximale de caracères par ligne.
	 * @return
	 * 		La chaine de caractères sur plusieurs lignes.
	 */
	public static String multiLines(String string, int car)
	{
		String res = "";

		String[] container = string.split(" +");
		int car_ligne = 0;
		for(String mot : container)
		{
			car_ligne += mot.length()+1;
			if(car_ligne <= car)
			{
				res += " "+mot;
			}
			else
			{
				res += "\n"+mot;
				car_ligne = mot.length();
			}
		}
		return res.substring(1);
	}
}
