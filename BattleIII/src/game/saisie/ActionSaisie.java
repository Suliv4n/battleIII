package game.saisie;

import java.util.HashMap;

/**
 * Permet de lancer des opérations après une saisies.
 * 
 * 
 * @author Darklev
 *
 */
public interface ActionSaisie 
{
	/**
	 * Opération(s) après une saisie, comme affecter la saisie
	 * à une variable.
	 * 
	 * La saisie est obtenue avec Saisie.getSaisie();
	 * 
	 * @param param
	 * 		Dictionnaire de paramètres.
	 */
	public void action(HashMap<String, Object> param);

	/**
	 * Contrôle(s) à effectuer sur la saisie avant de lancer les opérations.
	 * 
	 * @return Vrai si tous les contrôles sont validés, faux sinon.
	 */
	public boolean controle();
	

}
