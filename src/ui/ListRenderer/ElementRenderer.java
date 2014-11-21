package ui.ListRenderer;

public interface ElementRenderer {

	/**
	 * Afficher un élément d'une List aux coordonnées spécifiées.
	 * 
	 * @param x
	 * 		Abscisse du curseur à afficher.
	 * @param y
	 * 		Ordonnées du curseur à afficher.
	 * @param element
	 * 		Element courant à afficher
	 * @param index
	 * 		index de l'élément courant à afficher.
	 */
	public void render(int x, int y, Object element, int index);
}
