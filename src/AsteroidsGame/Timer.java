package AsteroidsGame;

public class Timer {

	private float millisPerCycle;
	private long lastUpdate;
	private int elapsedCycles;
	private float excessCycles;
	private boolean isPaused;

	public Timer(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}

	public void setCyclesPerSecond(float cyclesPerSecond) {
		millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}

	public void reset() {
		elapsedCycles = 0;
		excessCycles = 0.0f;
		lastUpdate = getCurrentTime();
		isPaused = false;
	}

	public void update() {
		long currUpdate = getCurrentTime();
		float delta = (float) (currUpdate - lastUpdate) + excessCycles;

		if (!isPaused) {
			elapsedCycles += (int) Math.floor(delta / millisPerCycle);
			excessCycles = delta % millisPerCycle;
		}

		this.lastUpdate = currUpdate;
	}

	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public boolean hasElapsedCycle() {
		if (elapsedCycles > 0) {
			this.elapsedCycles--;
			return true;
		}
		return false;
	}

	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0);
	}

	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}

}
