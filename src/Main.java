import javax.swing.*;

public class Main {
   public static void main(String[] args) {
      int BOARD_WIDTH = 600;
      int BOARD_HEIGHT = 600;

      var frame = new JFrame("Snake");
      frame.setVisible(true);
      frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      var snakeGame = new SnakeGame(BOARD_WIDTH, BOARD_HEIGHT);
      frame.add(snakeGame);
      frame.pack();
      snakeGame.requestFocus();
   }
}