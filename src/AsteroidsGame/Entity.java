package AsteroidsGame;

import java.awt.Graphics2D;

public abstract class Entity {
	
	public Vector position;
	protected Vector velocity;
	protected double rotation, radius;
	private boolean needsRemoval;
	private int killScore;
	
	public Entity(Vector position, Vector velocity, double radius, int killScore) {
		this.position = position;
		this.velocity = velocity;
		this.radius = radius;
		this.rotation = 0.0f;
		this.killScore = killScore;
		this.needsRemoval = false;
	}
	
	public void rotate(double amount) {
		this.rotation += amount;
		this.rotation %= Math.PI * 2;
	}
	
	public int getKillScore() {
		return killScore;
	}
	
	public void flagForRemoval() {
		this.needsRemoval = true;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public double getCollisionRadius() {
		return radius;
	}
	
	public boolean needsRemoval() {
		return needsRemoval;
	}
	
	public void update(Game game) {
		position.add(velocity);
		if(position.x < 0.0f) {
			position.x += 500;
		}
		if(position.y < 0.0f) {
			position.y += 500;
		}
		position.x %= 500;
		position.y %= 500;
	}
	
	public boolean checkCollision(Entity entity) {
		double radius = entity.getCollisionRadius() + getCollisionRadius();
		return (position.getDistanceToSquared(entity.position) < radius * radius);
	}
	
	public abstract void handleCollision(Game game, Entity other);
	public abstract void draw(Graphics2D g, Game game);
}
