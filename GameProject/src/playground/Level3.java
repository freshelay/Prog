package playground;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import controller.ObjectController;
import controller.ZickZackController;
import gameobjects.AnimatedGameobject;
import gameobjects.GameObject;


/**
 * extends {@link SpaceInvadersLevel} with a ZigZack move of the enemies in
 * {@link #createSingleEnemy(String, double, double, double, double, ObjectController, double)} and
 * sets a different {@link #getStartupMessage()}.
 */
public class Level3 extends SpaceInvadersLevel {

  private static Logger logger = LogManager.getLogger(Level3.class);

  @Override
  protected String getStartupMessage() {
    return "Get ready for level 3!!!";
  }


  @Override
  protected GameObject createSingleEnemy(String name, double x_enemy, double y_enemy,
      double vx_enemy, double vy_enemy, ObjectController enemyController, double gameTime) {
    logger.trace("creating enemy [" + name + "] with ZickZackController ");
    ObjectController zzController = new ZickZackController(gameTime, 0.5);
    GameObject go = new AnimatedGameobject(name, this, x_enemy, y_enemy, vx_enemy, vy_enemy,
        ENEMYSCALE, this.enemyAnim, this.getGameTime(), "loop").addController(zzController)
            .generateColliders();

    return go.generateColliders();
  }



}
