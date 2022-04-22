package base;

import java.io.IOException;
import java.util.ArrayList;
import playground.Level1;
import playground.Level2;
import playground.Level3;
import playground.Level4;
import playground.Playground;

/**
 * main class to start a game with four levels {@link playground.Level1} {@link playground.Level2}
 * {@link playground.Level3} and {@link playground.Level4}.
 *
 */
public class BuggyGame extends GameLoop {
  
  /** constructor that defines {@link GameLoop#levels} with 4 levels (instances of {@link playground.Level1}
   * {@link playground.Level2} {@link playground.Level3} and {@link playground.Level4}.
   */
  public BuggyGame() {
    super();    
    this.levels = new ArrayList<Playground>(4);
    this.levels.add(new Level1());
    this.levels.add(new Level2());
    this.levels.add(new Level3());
    this.levels.add(new Level4());
  }

  /**
   * starts this game.
   * 
   * @param args command line parameters (forwarded to {@link GameLoop#runGame(String[])}).
   * @throws IOException if highscore.txt file cannot be written or accessed, the exception is
   *         thrown (and game ends).
   */
  public static void main(String[] args) throws IOException {
    GameLoop myGame = new BuggyGame();
    myGame.runGame(args);
  }

}
