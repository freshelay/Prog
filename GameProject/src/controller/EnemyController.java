package controller;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This class controls the space invaders.
 */
public class EnemyController extends ObjectController {

  private static Logger logger = LogManager.getLogger(EnemyController.class);

  @Override
  public void updateObject() {
    logger.trace("updatre" + gameObject.getId());
    if ((gameObject.getX() > this.getPlayground().getSizeX() * 0.9) && (gameObject.getVX() > 0)) {
      logger.trace("toleft!" + gameObject.getX());
      gameObject.setVX(-this.getVX());
    }
    if ((gameObject.getX() < this.getPlayground().getSizeX() * 0.1) && (gameObject.getVX() < 0)) {
      logger.trace("toright!" + gameObject.getX());
      gameObject.setVX(-this.getVX());
    }

    // if it reaches the bottom, delete it and deduct points
    if (gameObject.getY() >= this.getPlayground().getSizeY()) {
      this.getPlayground().deleteObject(gameObject.getId());
      // add to points counter
      Integer pts = (Integer) this.getPlayground().getGlobalFlag("points");
      this.getPlayground().setGlobalFlag("points", pts - 200);
    }

    applySpeedVector();
  }
}
