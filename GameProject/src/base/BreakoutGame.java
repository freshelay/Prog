package base;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * main class to start a game with only one level.
 *
 */
public class BreakoutGame extends GameLoop {
  
  private static Logger logger = LogManager.getLogger(BreakoutGame.class);

  public BreakoutGame() {     
    this.levels.clear(); // removes Level1 added by superclass constructor
    
    // this.levels.add(new LevelBreakout1());  // FIXME add this when your level exists 
    
  }
  /**
   * starts this game.
   * 
   * @param args command line parameters (forwarded to {@link GameLoop#runGame(String[])}).
   * @throws IOException if highscore.txt file cannot be written or accessed, the exception is
   *         thrown (and game ends).
   */
  public static void main(String[] args) throws IOException {
    GameLoop myGame = new BreakoutGame();
    logger.info("BreakoutGame program started.");
    myGame.runGame(args);
  }

}
