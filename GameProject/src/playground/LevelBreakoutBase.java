package playground;

import gameobjects.*;
import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import controller.*;

public abstract class LevelBreakoutBase extends Playground {

  /**
   * instance of the ball, needs to be set by {@link #prepareLevel(String) }
   * 
   */
  protected GameObject ball = null,
      /**
       * instance of the ego objects, needs to be set by {@link #prepareLevel(String) }
       * 
       */
      ego = null;

  private static Logger logger = LogManager.getLogger(LevelBreakoutBase.class);

  public LevelBreakoutBase() {
    super();
    this.canvasX = this.preferredSizeX();
    this.canvasY = this.preferredSizeY();
  }

  /**
   * signals to game engine that the game has finished by game over. called every game loop. default
   * implementation is always false.
   * 
   * @return false
   */
  public boolean gameOver() {
    return false;
  }


  /**
   * signals to game engine that the game has finished by success. called every game loop. default
   * implementation is always false.
   * 
   * @return false
   */
  public boolean levelFinished() {
    return false;
  }


  /**
   * signals to game engine that the game has been requested to be reseted (restart). called every
   * game loop. default implementation is always false.
   * 
   * @return false
   */
  public boolean resetRequested() {
    return false;
  }

  /**
   * unimplemented empty method called by game engine every loop.
   * 
   */
  public void redrawLevel(Graphics2D g) {

    // fill background with light magenta
    int[] x = {0, canvasX, canvasX, 0};
    int[] y = {0, 0, canvasY, canvasY};
    Polygon bg = new Polygon(x, y, 4);
    g.setColor(new Color(140,140,200));
    g.fill(bg);
    
  }



  /**
   * Signal that the level has a size of 700x700 pixels.
   * 
   * @return x size of level 700
   */
  @Override
  public int preferredSizeX() {
    return 700;
  }

  /**
   * Signal that the level has a size of 700x700 pixels.
   * 
   * @return y size of level 700
   */
  @Override
  public int preferredSizeY() {
    return 700;
  }


  /**
   * Method that gets called by applyGameLogic() whenever the ball collides with a brick.
   * 
   * 
   * @param ball A reference to the current ball object
   * @param brick A reference to the ego object
   */
  protected abstract void actionIfBallHitsBrick(GameObject ball, GameObject brick);

  /**
   * Method that gets called by applyGameLogic() whenever the ball collides with the ego object.
   * 
   * @param ball A reference to the current ball object
   * @param ego A reference to the ego object
   */
  protected abstract void actionIfBallHitsEgo(GameObject ball, GameObject ego);

  /**
   * Models interactions between GameObjects. notably ball/ego and ball/brick. called every game
   * loop.
   */
  @Override
  public void applyGameLogic() {
    LinkedList<GameObject> bricks = collectObjects("brick", false);

    for (GameObject brick : bricks) {
      if (this.ball.collisionDetection(brick)) {
        logger.trace("Collision detected of ball and brick " + brick.getId());
        this.actionIfBallHitsBrick(this.ball, brick);
      }
    }

    if (ego.collisionDetection(ball)) {
      logger.trace("Collision detected of ball and ego");
      actionIfBallHitsEgo(this.ball, this.ego);
    }
  }

  /**
   * Creates the ego object and returns it, called by {@link #prepareLevel}. Does NOT add the ego
   * object to the playground!
   * 
   * @return The created ego object instance (of class {@link RectObject} with
   *         {@link EgoController}.
   */
  protected abstract GameObject createEgoObject();

  /**
   * Creates the ball object and returns it, called by #prepareLevel. Does NOT add the ball object
   * to the playground!
   * 
   * @return The created ball object instance (of class {@link FallingStar})
   */
  protected abstract GameObject createBall();

  /**
   * Creates the GameObject (RectObject) instance representing a single brick at a certain grid
   * position. The brick is NOT added here!
   * 
   * @param row row position in the grid, ranges from 0 to calcNrBricksY()-1
   * @param column column position in the grid of bricks, ranges from 0 to calcNrBricksX()-1
   * @return The GameObject instance (really a RectObject) representing the created brick.
   */
  protected abstract GameObject createBrick(int row, int column);

  /**
   * Prepares a generic Breakout-Type level. This method relies on the methods {@link #createEgoObject()},
   * {@link #createBall} and {@link #createBrick}, among others, which are meant to be overwritten
   * in subclasses. <br>
   * Attention: the attributes {@link #ball} and {@link #ego} need to be set properly to GameObject
   * instances when implementing this method {@link #prepareLevel(String)}.
   * 
   * @param level String passes by the game engine (not used currently and can be ignored).
   */
  @Override
  abstract public void prepareLevel(String level);

}
