package animation;

import characters.Ennemy;

public abstract class DeathAnimation {
	
	private Ennemy ennemy;
	
	public DeathAnimation(Ennemy ennemy){
		this.ennemy = ennemy;
	}
	
	public abstract void render();
	public abstract void update(int delta);
	public abstract boolean isFinished();

	public Ennemy getEnnemy() {
		return ennemy;
	}

	
	
}
