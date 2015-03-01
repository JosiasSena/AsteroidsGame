package AsteroidsGame;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import objects.Asteroid;
import objects.Player;


@SuppressWarnings("serial")
public class Game extends JFrame {
	
	private static final int RespCDL = 100;
	private Main world;
	private Timer logicTimer;
	private static final int ICDL = 0;
	private static final long FRAME_TIME = (long)(1000000000.0 / 60);

	private Random random;
	private List<Entity> entities, pendingEntities;
	private Player player;
	private int deathCooldown, lives, level, showLevelCooldown, restartCooldown, score;
	private boolean isGameOver, restartGame;
	
	private Game() {
		super("Asteroids Game");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		add(this.world = new Main(this), BorderLayout.CENTER);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				
				case KeyEvent.VK_UP:
					if(!checkForRestart()) {
						player.setThrusting(true);
					}
					break;
					
				case KeyEvent.VK_LEFT:
					if(!checkForRestart()) {
						player.setRotateLeft(true);
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(!checkForRestart()) {
						player.setRotateRight(true);
					}
					break;
				case KeyEvent.VK_SPACE:
					if(!checkForRestart()) {
						player.setFiring(true);
					}
					break;
				case KeyEvent.VK_P:
					if(!checkForRestart()) {
						logicTimer.setPaused(!logicTimer.isPaused());
					}
					break;
				default:
					checkForRestart();
					break;
					
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {

				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					player.setThrusting(false);
					break;
				
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					player.setRotateLeft(false);
					break;

				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					player.setRotateRight(false);
					break;
					
				case KeyEvent.VK_SPACE:
					player.setFiring(false);
					break;	
				}
			}
		});
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private boolean checkForRestart() {
		boolean restart = (isGameOver && restartCooldown <= 0);
		if(restart) {
			restartGame = true;
		}
		return restart;
	}
	
	private void startGame() {
		this.random = new Random();
		this.entities = new LinkedList<Entity>();
		this.pendingEntities = new ArrayList<>();
		this.player = new Player();
		
		resetGame();
		
		this.logicTimer = new Timer(60);
		while(true) {
			long start = System.nanoTime();
			
			logicTimer.update();
			for(int i = 0; i < 5 && logicTimer.hasElapsedCycle(); i++) {
				updateGame();
			}
			
			world.repaint();
			
			long delta = FRAME_TIME - (System.nanoTime() - start);
			if(delta > 0) {
				try {
					Thread.sleep(delta / 1000000L, (int) delta % 1000000);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void updateGame() {
		entities.addAll(pendingEntities);
		pendingEntities.clear();
		
		if(restartCooldown > 0) {
			this.restartCooldown--;
		}
		
		if(showLevelCooldown > 0) {
			this.showLevelCooldown--;
		}

		if(isGameOver && restartGame) {
			resetGame();
		}
		
		if(!isGameOver && areEnemiesDead()) {
			this.level++;
			this.showLevelCooldown = 60;
			resetEntityLists();
			
			player.reset();
			player.setFiringEnabled(true);
			
			for(int i = 0; i < level + 2; i++) {
				registerEntity(new Asteroid(random));
			}
		}
		
		if(deathCooldown > 0) {
			this.deathCooldown--;
			switch(deathCooldown) {
			
			case RespCDL:
				player.reset();
				player.setFiringEnabled(false);
				break;
			
			case ICDL:
				player.setFiringEnabled(true);
				break;
			
			}
		}
		
		if(showLevelCooldown == 0) {
			
			for(Entity entity : entities) {
				entity.update(this);
			}
			
			for(int i = 0; i < entities.size(); i++) {
				Entity a = entities.get(i);
				for(int j = i + 1; j < entities.size(); j++) {
					Entity b = entities.get(j);
					if(i != j && a.checkCollision(b) && ((a != player && b != player) || deathCooldown <= ICDL)) {
						a.handleCollision(this, b);
						b.handleCollision(this, a);
					}
				}
			}
			
			Iterator<Entity> iter = entities.iterator();
			while(iter.hasNext()) {
				if(iter.next().needsRemoval()) {
					iter.remove();
				}
			}
		}
	}
	
	private void resetGame() {
		this.score = 0;
		this.level = 0;
		this.lives = 3;
		this.deathCooldown = 0;
		this.isGameOver = false;
		this.restartGame = false;
		resetEntityLists();
	}
	
	private void resetEntityLists() {
		pendingEntities.clear();
		entities.clear();
		entities.add(player);
	}
	
	private boolean areEnemiesDead() {
		for(Entity e : entities) {
			if(e.getClass() == Asteroid.class) {
				return false;
			}
		}
		return true;
	}
	
	public void killPlayer() {
		this.lives--;
	
		if(lives == 0) {
			this.isGameOver = true;
			this.restartCooldown = RespCDL;
			this.deathCooldown = Integer.MAX_VALUE;
		} else {
			this.deathCooldown = 200;
		}
		
		player.setFiringEnabled(false);
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public void registerEntity(Entity entity) {
		pendingEntities.add(entity);
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public boolean isPlayerInvulnerable() {
		return (deathCooldown > ICDL);
	}
	
	public boolean canDrawPlayer() {
		return (deathCooldown <= RespCDL);
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int getLevel() {
		return level;
	}
	
	public boolean isPaused() {
		return logicTimer.isPaused();
	}
	
	public boolean isShowingLevel() {
		return (showLevelCooldown > 0);
	}

	public Random getRandom() {
		return random;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.startGame();
	}

}
