package playground;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Advanced version of abstract {@link LevelBreakoutBase} providing a complete implementation of
 * {@link #prepareLevel(String)}. Additionally abstract methods for number of bricks in X and Y
 * direction are provided as well as abstract methods for brick sizes and start coordinates.
 * 
 * @see #calcNrBricksX()
 * @see #calcNrBricksY()
 * @see #getBrickSizeX()
 * @see #getBrickSizeY()
 * @see #getBrickStartX()
 * @see #getBrickStartY()
 *
 */
public abstract class LevelBreakoutBaseAdvanced extends LevelBreakoutBase {

  private static Logger logger = LogManager.getLogger(LevelBreakoutBaseAdvanced.class);


  /**
   * provides the number of bricks to be set in horizontal direction.
   * 
   * @return positive value of how many bricks are to be next to each other in X direction
   */
  protected abstract int calcNrBricksX();

  /**
   * provides the number of bricks to be set in vertical direction.
   * 
   * @return positive value of how many bricks are to be next to each other in Y direction
   */
  protected abstract int calcNrBricksY();

  /**
   * provides the length of one brick.
   * 
   * @return positive value of how long a brick should be in X direction.
   */
  protected abstract double getBrickSizeX();

  /**
   * provides the height of one brick.
   * 
   * @return positive value of how high a brick should be in Y direction.
   */
  protected abstract double getBrickSizeY();

  /**
   * provides the start coordinate of upper left corner (X value).
   * 
   * @return positive value of the X coordinate to use as the starting point of the upper left
   *         corner of the brick set.
   */
  protected abstract double getBrickStartX();

  /**
   * provides the start coordinate of upper left corner (Y value).
   * 
   * @return positive value of the Y coordinate to use as the starting point of the upper left
   *         corner of the brick set.
   */
  protected abstract double getBrickStartY();



  /**
   * Prepares a complete Breakout type level and uses the values provided by implementations of
   * {@link #calcNrBricksX()} and {@link #calcNrBricksY()} to generate the stone matrix. 
   * Furthermore, it relies on the methods {@link #createEgoObject()}, {@link #createBall} and {@link #createBrick},
   * which are meant to be overwritten in subclasses. <br>
   * Attention: For collission detection bricks created by {@link #createBrick(int, int)} need to have the String 'brick' in ID.
   * 
   * @see LevelBreakoutBase#prepareLevel(String) for further information.
   * 
   * @param level String passes by the game engine (not used currently and can be ignored).
   * 
   */
  @Override
  public void prepareLevel(String level) {

    for (int y = 0; y < this.calcNrBricksY(); y++) {
      for (int x = 0; x < this.calcNrBricksX(); x++) {
        logger.trace("trying to create brick X, Y (" + x + "," + y + ")");
        this.addObject(this.createBrick(x, y));
      }
    }
    this.ego = this.createEgoObject();
    this.ball = this.createBall();
    this.addObject(this.ego);
    this.addObject(this.ball);
    logger.info("level preperation succeeded.");
  }

}
