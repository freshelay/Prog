package playground;

import controller.FallingStarController;
import gameobjects.EgoObject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.RectObject;
import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * extends {@link SpaceInvadersLevel} with 10 enemies that need two shots each to be destroyed.
 * 
 */


public class LevelHitTwice extends SpaceInvadersLevel {

  /** constant defining the number of shots needed to destroy an enemy */
  public static final int MAX_HITS = 2;
  
  private static Logger logger = LogManager.getLogger(LevelHitTwice.class);

  /** constructor setting internal name to 'hitTwice' */
  public LevelHitTwice() {
    super();
    this.level = "hitTwice";
  }


  @Override
  protected String getStartupMessage() {
    return "2 shots at alien required!!!";
  }

  @Override
  protected int calcNrEnemies() {
    return 10;
  }



  @Override
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {    
    Object counterFlag = e.getOrCreateObjectFlag("counter", Integer.valueOf(1));

    int counter = (Integer) counterFlag;

    if (counter >= MAX_HITS) {
      logger.trace("enemy was hit before for " + counter + " times, which is above "
          + LevelHitTwice.MAX_HITS);
      super.actionIfEnemyIsHit(e, shot);
    } else {
      logger.trace("enemy was hit before for "+counter+" times, which is below "+LevelHitTwice.MAX_HITS);
      e.setObjectFlag("counter", Integer.valueOf(counter + 1));
    }
    deleteObject(shot.getId());
  }



}
