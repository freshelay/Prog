package controller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ZickZackController extends EnemyController {
  protected double g0;
  protected double dt;
  protected double lastMod = -1;

  private static Logger logger = LogManager.getLogger(ZickZackController.class);
  
  public ZickZackController(double gameTime, double dt) {
    super();
    this.dt = dt;
    this.g0 = gameTime;
  }

  public void updateObject() {
    double gameTime = this.getPlayground().getGameTime();
    logger.trace("current Object x: "+gameObject.getX());
    double mod = (gameTime - this.g0) % this.dt;
    if (mod < lastMod) {
      gameObject.setVX(-1.0 * gameObject.getVX());
      logger.trace("inverting VX");
    }
    lastMod = mod;

    super.updateObject();

  }
}
