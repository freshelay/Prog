package base;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import playground.LevelMovingHitObjects;
import playground.Playground;

/**
 * main class to start a game with only one level {@link playground.LevelMovingHitObjects}.
 *
 */
public class MovingObjectsGame extends GameLoop {

  private static Logger logger = LogManager.getLogger(MovingObjectsGame.class);

  /**
   * constructor defines {@link GameLoop#levels} with one instance of
   * {@link playground.LevelMovingHitObjects}.
   */
  MovingObjectsGame() {
    super();
    this.levels = new ArrayList<Playground>(1);
    this.levels.add(new LevelMovingHitObjects());
  }
  /**
   * starts this game.
   * 
   * @param args command line parameters (forwarded to {@link GameLoop#runGame(String[])}).
   * @throws IOException if highscore.txt file cannot be written or accessed, the exception is
   *         thrown (and game ends).
   */
  public static void main(String[] args) throws IOException {
    logger.info("Starting Game Program...let's play and don't get hit!");
    GameLoop myGame = new MovingObjectsGame();
    myGame.runGame(args);
  }


}
