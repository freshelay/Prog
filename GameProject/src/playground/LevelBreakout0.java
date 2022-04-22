package playground;

import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import collider.RectCollider;
import controller.EgoController;
import controller.ReboundController;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.RectObject;

/**
 * solution example for Breakout game
 * <ul>
 * <li>colored bricks
 * <li>rebound depends on position where ball hits ego object
 * <li>rebound on brick hit
 * 
 * not implemented: penalty if balls hits ground, no score for removed bricks, no bonus items, no
 * lives.
 * </ul>
 * 
 */


public class LevelBreakout0 extends LevelBreakoutBase {

  private static Logger logger = LogManager.getLogger(LevelBreakoutBase.class);


  protected GameObject createEgoObject() {
    RectObject ro = new RectObject("ego", this, 350, 550, 0, 0, 80, 10, Color.BLUE);
    ro.generateColliders();
    EgoController ec = new EgoController(80,10);
    ro.addController(ec);
    logger.info("ego created.");

    return ro;
  }


  protected GameObject createBall() {
    GameObject co = new FallingStar("ball1", this, 350, 350, 100, 100, Color.RED, 5);
    co.addController(new ReboundController());
    logger.info("ball created.");
    return co;
  }


  /**
   * creates bricks. For collision {@link RectCollider} is used.
   */
  @Override
  protected GameObject createBrick(int row, int column) {
    double xSize = 60;
    double ySize = 30;
    double xStart = 40;
    double yStart = 40;
    double space = 5;
    Color c = Color.BLUE;

    RectObject ro = new RectObject("brick" + row + "/" + column, this, xStart + column * (xSize + space),
        yStart + row * (ySize + space), 0, 0, xSize - 4, ySize - 4, c);
    RectCollider rc = new RectCollider("egal", ro, xSize - 4, ySize - 4);
    ro.addCollider(rc);

    return ro;
  }

  /**
   * lets ball bounce in Y direction, deletes brick and creates a red/blue colored explosion.
   */
  @Override
  protected void actionIfBallHitsBrick(GameObject ball, GameObject brick) {

    ball.setVY(ball.getVY() * -1); // bounce effect for ball
    this.deleteObject(brick.getId()); // remove brick from field
    logger.debug("deleted brick " + brick.getId());

  }


  /**
   * Let the ball bounce off in Y direction.
   * 
   */
  @Override
  protected void actionIfBallHitsEgo(GameObject ball, GameObject ego) {
    double ballY = ball.getY();
    double egoY = ego.getY();
    ball.setY(ballY < egoY ? ballY - 10 : ballY + 10); // radius 5 hard coded.
    ball.setVY(ball.getVY() * -1);
    logger.debug("ball bounces of ego object.");
  }


  /**
   * Prepares a Breakout level with a 3 x 3 matrix of blocks on top. This method relies on the
   * methods {@link #createEgoObject()}, {@link #createBall()} and {@link #createBrick(int, int)}, among others, which
   * are meant to be overwritten in subclasses.
   * 
   * @param level String passes by the game engine (not used currently and can be ignored).
   */
  @Override
  public void prepareLevel(String level) {
    for (int y = 3; y < 6; y++) {
      for (int x = 0; x < 3; x++) {
        logger.trace("trying to create brick X, Y (" + x + "," + y + ")");
        GameObject brick = this.createBrick(x, y);
        this.addObject(brick);
      }
    }
    this.ego = this.createEgoObject();
    this.ball = this.createBall();
    this.addObject(this.ego);
    this.addObject(this.ball);
    logger.info("level preperation succeeded.");

  }


}
