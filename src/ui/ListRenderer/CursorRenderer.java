package ui.ListRenderer;


public interface CursorRenderer {
	/**
	 * Affiche le curseur aux coordonnées spécifiées.
	 * @param x
	 * 		Abscisse du curseur à afficher.
	 * @param y
	 * 		Ordonnées du curseur à afficher.
	 */
	public void render(int x, int y);
}
