package AsteroidsGame;

public class Vector {

	public double x;
	public double y;

	public Vector(double angle) {
		x = Math.cos(angle);
		y = Math.sin(angle);
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector(Vector vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	public Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector add(Vector vec) {
		x += vec.x;
		y += vec.y;
		return this;
	}

	public Vector scale(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	public Vector normalize() {
		double length = getLengthSquared();
		if (length != 0.0f && length != 1.0f) {
			length = Math.sqrt(length);
			this.x /= length;
			this.y /= length;
		}
		return this;
	}

	public double getLengthSquared() {
		return (x * x + y * y);
	}

	public double getDistanceToSquared(Vector vec) {
		double dx = x - vec.x;
		double dy = y - vec.y;
		return (dx * dx + dy * dy);
	}

}
