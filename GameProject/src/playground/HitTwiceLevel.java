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


public class HitTwiceLevel extends SpaceInvadersLevel {

  /** constant defining the number of shots needed to destroy an enemy */
  public static final int MAX_HITS = 2;
  
  private static Logger logger = LogManager.getLogger(HitTwiceLevel.class);

  /** constructor setting internal name to 'hitTwice' */
  public HitTwiceLevel() {
    super();
    this.level = "hitTwice";
  }


  @Override
  protected String getStartupMessage() {
    return "2 shots at alien required!!!";
  }

  protected int calcNrEnemies() {
    return 10;
  }


  public void setupInitialState() {
    super.setupInitialState();
    GameObject ro = new RectObject("obstacleRect", this, 600, 300, 0, 0, 20, 100, Color.RED) // Remove?
        .generateColliders();
    this.addObject(ro);
    logger.debug("added red box on top");

  }


  @Override
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {    
    Object counterFlag = e.getOrCreateObjectFlag("counter", Integer.valueOf(1));

    int counter = (Integer) counterFlag;

    if (counter >= MAX_HITS) {
      logger.trace("enemy was hit before for " + counter + " times, which is above "
          + HitTwiceLevel.MAX_HITS);
      super.actionIfEnemyIsHit(e, shot);
    } else {
      logger.trace("enemy was hit before for "+counter+" times, which is below "+HitTwiceLevel.MAX_HITS);
      e.setObjectFlag("counter", Integer.valueOf(counter + 1));
    }
    deleteObject(shot.getId());
  }



}
