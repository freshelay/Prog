package playground;

import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import controller.EgoController;
import controller.FallingStarController;
import controller.ObjectController;
import gameobjects.AnimatedGameobject;
import gameobjects.EgoObject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.TextObject;

/**
 * Class that realizes all the game logic of a very simple game level. The level contains for now
 * only two objects that are {@link GameObject} subclasses: {@link FallingStar} and
 * {@link EgoObject}. Functions performed by this class are:
 * <ul>
 * <li>initially set up the level, spawn all object etc., in method {@link #prepareLevel}
 * <li>React to keyboard commands in method {@link #processKeyEvents(java.util.Stack)}
 * <li>define basic object movement rules for all objects in the level in the various
 * ObjectController subclasses: {@link EgoController} and {@link FallingStarController}.
 * </ul>
 */
public class LevelBoss extends SpaceInvadersLevel {

  private static int MAX_SHOTS = 10;
  private static Logger logger = LogManager.getLogger(LevelBoss.class);

  @Override
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {

    Object counterFlag = e.getOrCreateObjectFlag("counter", Integer.valueOf(1));

    int counter = (Integer) counterFlag;

    if (counter >= LevelBoss.MAX_SHOTS) {
      logger.trace("enemy was hit before for " + counter + " times, which is equal or above "
          + HitTwiceLevel.MAX_HITS);
      super.actionIfEnemyIsHit(e, shot);
    } else {
      logger.trace("enemy was hit before for " + counter + " times, which is below "
          + HitTwiceLevel.MAX_HITS);
      e.setObjectFlag("counter", Integer.valueOf(counter + 1));
    }
    deleteObject(shot.getId());
  }



  @Override
  double calcEnemyShotProb() {
    return 1.5 * this.getTimestep();
  }

  @Override
  protected double calcEnemySpeedX() {
    return ENEMYSPEEDX * 2;
  }

  @Override
  protected double calcEnemySpeedY() {
    return ENEMYSPEEDY * 2;
  }

  @Override
  protected int calcNrEnemies() {
    return (int) 1;
  }

  @Override
  protected GameObject createEnemyShotObject(GameObject parentObject, String name,
      ObjectController limitedTimeController) {
    GameObject ego = this.getObject("ego");

    double deltax = parentObject.getX() - ego.getX();
    double deltay = parentObject.getY() - ego.getY();

    double norm = Math.sqrt(deltax * deltax + deltay * deltay);
    deltax *= -ENEMYSHOTSPEED / norm;
    deltay *= -ENEMYSHOTSPEED / norm;

    logger.trace("Creating EnemyShot as TextObject [" + name + "] in direction " + deltax + "/"
        + deltay + " towards ego");
    GameObject to = new TextObject(name, this, parentObject.getX(), parentObject.getY(), deltax,
        deltay, "*", 20, Color.GREEN).generateColliders().addController(limitedTimeController);
    return to;

  }


  @Override
  protected GameObject createSingleEnemy(String name, double x_enemy, double y_enemy,
      double vx_enemy, double vy_enemy, ObjectController enemyController, double gameTime) {

    GameObject go = new AnimatedGameobject(name, this, this.canvasX / 2, 10, vx_enemy, 50,
        ENEMYSCALE * 3, this.enemyAnim, this.getGameTime(), "loop").generateColliders()
            .addController(enemyController);

    return go;
  }


  @Override
  protected String getStartupMessage() {
    return "BOSS LEVEL!";
  }



}
