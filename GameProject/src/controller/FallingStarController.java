package controller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * Controls background stars. When they touch the bottom of the display they reappear on top.
 */
public class FallingStarController extends ObjectController {
  int rad = 3;
  private static Logger logger = LogManager.getLogger(FallingStarController.class);


  @Override
  public void updateObject() {
    logger.trace(
        "+" + this.gameObject.getId() + " HO " + this.gameObject + "/" + this.getPlayground());
    if (this.getY() + rad >= this.getPlayground().getSizeY()) {
      this.setY(10);
    }
    applySpeedVector();
  }
}
