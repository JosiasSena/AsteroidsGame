package AsteroidsGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Main extends JPanel {

	private static final Font TITLE_FONT = new Font("Dialog", Font.PLAIN, 25);
	private static final Font SUBTITLE_FONT = new Font("Dialog", Font.PLAIN, 15);
	private Game game;
	
	int numStars;
	Point[] stars;
	
	public Main(Game game) {
		this.game = game;

		// Generate Stars
		numStars = (500 * 500) / 500;
		stars = new Point[numStars];
		for (int i = 0; i < numStars; i++){
			stars[i] = new Point((int) (Math.random() * 500), (int) (Math.random() * 500));
		}
		
		setPreferredSize(new Dimension(500, 500));
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.setColor(Color.CYAN);
			for (int i = 0; i < numStars; i++){
				graphics.drawLine(stars[i].x, stars[i].y, stars[i].x,
						stars[i].y);
			}
		
		Graphics2D g2d = (Graphics2D) graphics;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.WHITE);
		AffineTransform identity = g2d.getTransform();
		Iterator<Entity> iterator = game.getEntities().iterator();
		
		while(iterator.hasNext()) {
			Entity entity = iterator.next();
			if(entity != game.getPlayer() || game.canDrawPlayer()) {
				Vector pos = entity.getPosition(); //Get the position of the entity.
				
				drawEntity(g2d, entity, pos.x, pos.y);
				g2d.setTransform(identity);

				double radius = entity.getCollisionRadius();
				double x = (pos.x < radius) ? pos.x + 500
						: (pos.x > 500 - radius) ? pos.x - 500 : pos.x;
				double y = (pos.y < radius) ? pos.y + 500
						: (pos.y > 500 - radius) ? pos.y - 500 : pos.y;
				
				if(x != pos.x || y != pos.y) {
					drawEntity(g2d, entity, x, y);
					g2d.setTransform(identity);
				}
			}	
		}
		
		if(!game.isGameOver()) {
			graphics.setColor(Color.MAGENTA);
			graphics.drawString("Score: " + game.getScore(), 420, 15);
			graphics.drawString("Lives: ", 5, 15);
			graphics.drawString("by Josias Sena", 5, 495);
			graphics.setColor(Color.GREEN);
		}
		
		if(game.isGameOver()) {
			graphics.setColor(Color.RED);
			drawTextCentered("Game Over", TITLE_FONT, g2d, -25);
			graphics.setColor(Color.WHITE);
			drawTextCentered("Final Score: " + game.getScore(), SUBTITLE_FONT, g2d, 10);
		} else if(game.isPaused()) {
			drawTextCentered("Paused", TITLE_FONT, g2d, -25);
		} else if(game.isShowingLevel()) {
			drawTextCentered("Level: " + game.getLevel(), TITLE_FONT, g2d, -25);
		}
		
		g2d.translate(15, 30);
		g2d.scale(0.85, 0.85);
		graphics.setColor(Color.red);
		for(int i = 0; i < game.getLives(); i++) {
			graphics.drawLine(-10, -8, 10, 0);
			graphics.drawLine(-10, 8, 10, 0);
			graphics.drawLine(-10, 8, -5, 0);
			graphics.drawLine(-10, -8, -5, 0);
			g2d.translate(30, 0);
		}
	}
	
	private void drawTextCentered(String text, Font font, Graphics2D g, int y) {
		g.setFont(font);
		g.drawString(text, 500 / 2 - g.getFontMetrics().stringWidth(text) / 2, 500 / 2 + y);
	}

	private void drawEntity(Graphics2D g2d, Entity entity, double x, double y) {
		g2d.translate(x, y);
		double rotation = entity.getRotation();
		if(rotation != 0.0f) {
			g2d.rotate(entity.getRotation());
		}
		entity.draw(g2d, game);
	}

}
