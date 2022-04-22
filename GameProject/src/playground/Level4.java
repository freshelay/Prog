package playground;

import gameobjects.GameObject;
import controller.LimitedTimeController;
import gameobjects.TextObject;
import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * extends extends {@link SpaceInvadersLevel}
 * <ul>
 * <li>Hit aliens twice to kill them
 * <li>they say AUA when not destroyed
 * </ul>
 */
public class Level4 extends SpaceInvadersLevel {

  /** constant defining the number of shots needed to destroy an enemy */
  public static final int MAX_HITS = 2;

  private static Logger logger = LogManager.getLogger(Level4.class);

  @Override
  protected String getStartupMessage() {
    return "Jetzt gibts Saures!";
  }

  @Override
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {
    double gameTime = this.getGameTime();

    Object counterFlag = e.getOrCreateObjectFlag("counter", Integer.valueOf(1));

    int counter = (Integer) counterFlag;

    if (counter >= MAX_HITS) {
      logger.trace("enemy was hit before for " + counter + " times, which is above "
          + HitTwiceLevel.MAX_HITS);
      super.actionIfEnemyIsHit(e, shot);
    } else {
      logger.trace("enemy was hit before for "+counter+" times, which is below "+HitTwiceLevel.MAX_HITS);
      e.setObjectFlag("counter", Integer.valueOf(counter + 1));
      // spawn a bonus points object
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVY();
      logger.trace("creating new TextObject bonus" + e.getId());
      LimitedTimeController bonusTextController =
          new LimitedTimeController(gameTime, SpaceInvadersLevel.EXPL_DURATION);
      GameObject bonusText = new TextObject("bonus" + e.getId(), this, e.getX(), e.getY(), vx, vy,
          "Aua", 20, Color.YELLOW).addController(bonusTextController);

      this.addObject(bonusText);
    }
    deleteObject(shot.getId());

  }



}
