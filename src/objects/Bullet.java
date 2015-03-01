package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import AsteroidsGame.Entity;
import AsteroidsGame.Game;
import AsteroidsGame.Vector;

public class Bullet extends Entity {
	
	private int lifespan;

	public Bullet(Entity owner, double angle) {
		super(new Vector(owner.position), new Vector(angle).scale(6.75), 2.0, 0);
		this.lifespan = 60;
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		this.lifespan--;
		if(lifespan <= 0) {
			flagForRemoval();
		}
	}

	@Override
	public void handleCollision(Game game, Entity other) {
		if(other.getClass() != Player.class) {
			flagForRemoval();
		}
	}
	
	@Override
	public void draw(Graphics2D g, Game game) {
		g.setColor(Color.PINK);
		g.drawOval(-1, -1, 2, 2);
		g.setColor(Color.YELLOW);
		
	}

}
