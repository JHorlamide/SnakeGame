import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;


public class SnakeGame extends JPanel implements ActionListener, KeyListener {
   private final int TILE_SIZE = 25;
   private final int BOARD_WIDTH;
   private final int BOARD_HEIGHT;

   private final Tile snakeFood;
   private final Tile snakeHead;
   private final ArrayList<Tile> snakeBody;

   private final Random random;

   Timer gameLoop;
   Boolean gameOver = false;
   private int velocityX;
   private int velocityY;

   SnakeGame(int boardWidth, int boardHeight) {
      this.BOARD_HEIGHT = boardHeight;
      this.BOARD_WIDTH = boardWidth;
      setPreferredSize(new Dimension(this.BOARD_WIDTH, this.BOARD_HEIGHT));
      setBackground(Color.black);
      addKeyListener(this);
      setFocusable(true);

      velocityX = 0;
      velocityY = 0;

      snakeHead = new Tile(5, 5);
      snakeBody = new ArrayList<Tile>();
      snakeFood = new Tile(10, 10);
      random = new Random();
      placeFoodRandomly();

      gameLoop = new Timer(100, this);
      gameLoop.start();
   }

   public void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      drawGameElements(graphics);
   }

   public void drawGameElements(Graphics graphics) {
      /* Snake Food */
      var snakeFoodX = snakeFood.x * TILE_SIZE;
      var snakeFoodY = snakeFood.x * TILE_SIZE;
      graphics.setColor(Color.red);
      graphics.fill3DRect(snakeFoodX, snakeFoodY, TILE_SIZE, TILE_SIZE, true);

      /* Snake Head */
      var snakeHeadX = snakeHead.x * TILE_SIZE;
      var snakeHeadY = snakeHead.y * TILE_SIZE;
      graphics.setColor(Color.green);
      graphics.fill3DRect(snakeHeadX, snakeHeadY, TILE_SIZE, TILE_SIZE, true);

      /* Snake Body */
      for (Tile snakePart : snakeBody) {
         var snakePartX = snakePart.x * TILE_SIZE;
         var snakePartY = snakePart.y * TILE_SIZE;
         graphics.fill3DRect(snakePartX, snakePartY, TILE_SIZE, TILE_SIZE, true);
      }

      graphics.setFont(new Font("Arial", Font.PLAIN, 16));
      if (gameOver) {
         graphics.setColor(Color.red);
         graphics.drawString("Game Over: " + String.valueOf(snakeBody.size()), TILE_SIZE - 16, TILE_SIZE);
      } else {
         graphics.drawString("Score: " + String.valueOf(snakeBody.size()), TILE_SIZE - 16, TILE_SIZE);
      }
   }

   private void placeFoodRandomly() {
      snakeFood.x = random.nextInt(BOARD_WIDTH / TILE_SIZE);
      snakeFood.y = random.nextInt(BOARD_HEIGHT / TILE_SIZE);
   }

   private boolean collision(Tile snakeHead, Tile snakeFood) {
      return snakeHead.x == snakeFood.x && snakeHead.y == snakeFood.y;
   }

   private void move() {
      /* Eat food */
      if (collision(snakeHead, snakeFood)) {
         snakeBody.add(new Tile(snakeFood.x, snakeFood.y));
         placeFoodRandomly();
      }

      for (int i = snakeBody.size() - 1; i >= 0; i--) {
         var snakePart = snakeBody.get(i);

         if (i == 0) {
            snakePart.x = snakeHead.x;
            snakePart.y = snakeHead.y;
         } else {
            var prevSnakePart = snakeBody.get(i - 1);
            snakePart.x = prevSnakePart.x;
            snakePart.y = prevSnakePart.y;
         }
      }

      snakeHead.x += velocityX;
      snakeHead.y += velocityY;

      /* Game over */
      for (Tile snakePart : snakeBody) {
          if (collision(snakeHead, snakePart)) {
            gameOver = true;
            break;
         }
      }

      if(snakeHead.x * TILE_SIZE < 0 || snakeHead.x * TILE_SIZE > BOARD_WIDTH ||
         snakeHead.y * TILE_SIZE < 0 || snakeHead.y * TILE_SIZE > BOARD_HEIGHT) gameOver = true;
   }

   @Override
   public void actionPerformed(ActionEvent event) {
      move();
      repaint();
      if(gameOver) gameLoop.stop();
   }

   @Override
   public void keyPressed(KeyEvent event) {
      var keyCode = event.getKeyCode();

      if (keyCode == KeyEvent.VK_UP && velocityY != 1)
         setVelocity(0, -1);

      else if (keyCode == KeyEvent.VK_DOWN && velocityY != -1)
         setVelocity(0, 1);

      else if (keyCode == KeyEvent.VK_LEFT && velocityX != 1)
         setVelocity(-1, 0);

      else if (keyCode == KeyEvent.VK_RIGHT && velocityX != -1)
         setVelocity(1, 0);

   }

   private void setVelocity(int x, int y) {
      velocityX = x;
      velocityY = y;
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyReleased(KeyEvent e) {

   }

   private static class Tile {
      int x;
      int y;

      public Tile(int x, int y) {
         this.x = x;
         this.y = y;
      }
   }
}
