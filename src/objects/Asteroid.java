package objects;

import java.awt.Graphics2D;
import java.util.Random;

import AsteroidsGame.AsteroidSize;
import AsteroidsGame.Entity;
import AsteroidsGame.Game;
import AsteroidsGame.Vector;

public class Asteroid extends Entity {

	private static final double MIN_ROTATION = 0.0075;
	private static final double MAX_ROTATION = 0.0175;
	private static final double ROTATION_VARIANCE = MAX_ROTATION - MIN_ROTATION;
	private static final double MIN_VELOCITY = 0.75;
	private static final double MAX_VELOCITY = 1.65;
	private static final double VELOCITY_VARIANCE = MAX_VELOCITY - MIN_VELOCITY;
	private static final double MIN_DISTANCE = 200.0;
	private static final double MAX_DISTANCE = 500 / 2.0;
	private static final double DISTANCE_VARIANCE = MAX_DISTANCE - MIN_DISTANCE;
	private static final float SPAWN_UPDATES = 10;
	private AsteroidSize size;
	private double rotationSpeed;
	
	public Asteroid(Random random) {
		super(calculatePosition(random), calculateVelocity(random), AsteroidSize.Large.radius, AsteroidSize.Large.killValue);
		this.rotationSpeed = -MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = AsteroidSize.Large;
	}
	
	public Asteroid(Asteroid parent, AsteroidSize size, Random random) {
		super(new Vector(parent.position), calculateVelocity(random), size.radius, size.killValue);
		this.rotationSpeed = MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = size;
		
		for(int i = 0; i < SPAWN_UPDATES; i++) {
			update(null);
		}
	}
	
	private static Vector calculatePosition(Random random) {
		Vector vec = new Vector(500 / 2.0, 500 / 2.0);
		return vec.add(new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_DISTANCE + random.nextDouble() * DISTANCE_VARIANCE));
	}
	
	private static Vector calculateVelocity(Random random) {
		return new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_VELOCITY + random.nextDouble() * VELOCITY_VARIANCE);
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		rotate(rotationSpeed);
	}

	@Override
	public void draw(Graphics2D g, Game game) {
		g.drawPolygon(size.polygon);
	}
		
	@Override
	public void handleCollision(Game game, Entity other) {
		if(other.getClass() != Asteroid.class) {
			if(size != AsteroidSize.Small) {
				AsteroidSize spawnSize = AsteroidSize.values()[size.ordinal() - 1];
				
				for(int i = 0; i < 2; i++) {
					game.registerEntity(new Asteroid(this, spawnSize, game.getRandom()));
				}
			}
			
			flagForRemoval();
			game.addScore(getKillScore());		
		}
	}
	
}
