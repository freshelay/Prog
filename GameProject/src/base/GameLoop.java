package base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gameobjects.GameObject;
import playground.Level1;
import playground.Playground;
import ui.GameUI;


/**
 * Main class starting any game, contains main(). Apart from that, this class manages all
 * non-logical functionalities which should be hidden from a game designer like:
 * <ul>
 * <li>Setting up windows, panels, buttons, action callbacks, ...
 * <li>Reading keyboard inputs
 * <li>Redrawing game window if necessary
 * <li>managing the game time and calling the appropriate {@link GameObject} or {@link Playground}
 * methods periodically, at every time step of the game.
 * </ul>
 * There will normally never be a need to modify this file, a designer/game programmer should always
 * redefine the {@link GameObject} and {@link Playground} classes and implement new functionality
 * there. To make a long story short<br>
 */
public class GameLoop {

  /** Pixel width of Game GUI ( above 0) */
  public static int SIZEX = 300;
  /** Pixel height of Game GUI (above 0) */
  public static int SIZEY = 200;

   /**
    *  before call to {@link #runGame(String[])} this List should be initialized (in constructor).
   */
  protected List<Playground> levels = null;

  private static Logger logger = LogManager.getLogger(GameLoop.class);

  /** constructor which initializes the {@link #levels} ArrayList of Playground instances (levels) to be played. */
  public GameLoop() {
    this.levels = new ArrayList<Playground>(1);
    this.levels.add( new Level1());
  }


  /**
   * loops over all {@link #levels} and implements the game loop to update continuously the level
   * during play time
   *
   * @param args command line arguments forwarded (currently ignored)
   * @throws IOException if hitghscore.txt cannot be written.
   */
  public void runGame(String[] args) throws IOException {

    logger.info("GUI starts");
    GameUI gameUI = new GameUI(SIZEX, SIZEY);   // probably change to your new GUI class

    double gameTime = -1;
    Playground currentPlayground = null;

    // loop over different levels
    ListIterator<Playground> levelIterator = levels.listIterator();
    while (true) {
      logger.debug("LevelIndex is " + (levelIterator.nextIndex()) + " (of " + levels.size() + " levels)");
      gameTime = 0;
      long start = System.nanoTime();
      
      // loop over single level
      while (true) {

        int act = GameUI.getNewAction();

        // Query GameUI for high-level user commands; new game/reset/etc...
        if (act == GameUI.ACTION_RESET) {
          // ReStart Game in same Level
          logger.info("GUI RESET");          
          currentPlayground.prepareLevel("level" + (levelIterator.nextIndex()-1));
          GameUI.resetAction();
        }

        if (act == GameUI.ACTION_NEW) {
          // new game
          logger.info("GUI NEW");
          start = System.nanoTime();
          levelIterator = levels.listIterator(); // reset
          currentPlayground = levelIterator.next(); // again level
          currentPlayground.prepareLevel("level" + (levelIterator.nextIndex()-1));
          gameUI.setPlayground(currentPlayground);          
          GameUI.resetAction();
          break;
        }

        if (act == GameUI.ACTION_BUTTON) {
          // Event of Button pressed --> PAUSE!
          logger.info("GUI PAUSE");
          if (currentPlayground != null) {
            boolean p = currentPlayground.isPaused();
            p = !p;
            currentPlayground.setPaused(p);
          }
          GameUI.resetAction();
        }

        if (act == GameUI.ACTION_SAVE) {
          logger.info("GUI SAVE");
          // UNDONE save current state (not yet working/implemented)
          GameUI.resetAction();
        }

        if (act == GameUI.ACTION_LOAD) {
          logger.info("GUI LOAD");
          // load game state (currently simply resets)
          GameUI.resetAction();
        }

        // if game has been created: execute a single iteration of the game loop
        if (currentPlayground != null) {
          // calc time that was used for painting the game, in seconds since last loop
          long end = System.nanoTime();
          double realTS = ((double) (end - start) / 1000000000.);
          
          // time calc for one loop of the while
          start = System.nanoTime();
          
          
          
          if (currentPlayground.levelFinished() || currentPlayground.gameOver() == true) {
            break; // leave level; breaks WHILE
          }

          // paint current state of level and start time measurement          
          gameUI.waitWhilePainting();


          gameUI.grabFocus(); // needed to grab input events in next step

          // communicate inputs to level
          currentPlayground.processKeyEvents(gameUI.getKeyEvents());
          currentPlayground.processMouseEvents(gameUI.getMouseEvents());

          if (currentPlayground.isPaused() == false) {
            
            // update objects and level
            currentPlayground.updateObjects();
            currentPlayground.applyGameLogic();
            

            // update game time
            gameTime += realTS;             
            // communicate gameTime and timestep to level
            currentPlayground.setTimestep(realTS);
            currentPlayground.setGameTime(gameTime);
            Playground.setGlobalFlag("gameTime", Double.valueOf(realTS));
            logger.trace("gameTime is now "+gameTime);

          } // if
        } // if

      } // inner while loop within level

      // after level is done: leave outer loop if game over
      if (currentPlayground.gameOver() == true) {
        break; // outer while ends game
      }

      // after level is done: reset level and go to next, if there is one
      if (currentPlayground.levelFinished() == true) {
        currentPlayground.reset();

        // increase level counter, go on to next one        
        logger.debug("level finished. now new LevelIndex is " + levelIterator.nextIndex());
        if (levelIterator.nextIndex() >= levels.size()) {
          logger.info("reached end of levels");
          break; // outer while ends game;
        }
        currentPlayground = levelIterator.next();
        currentPlayground.prepareLevel("level" + (levelIterator.nextIndex()-1));
      }

    } // outer loop over levels
    logger.info("Game ends. Bye.");
    System.exit(0);
  } // main()


  /**
   * main to start the whole application
   * 
   * @param args Java default command line args, forwarded to {@link #runGame(String[])}
   * @throws IOException in case highscore.txt cannot be written.
   */
  public static void main(String[] args) throws IOException {
    GameLoop gl = new GameLoop();
    gl.runGame(args);
  }

}
