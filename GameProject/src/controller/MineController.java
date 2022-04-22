package controller;

import controller.ObjectController;
import gameobjects.GameObject;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MineController extends ObjectController {
  int rad = 3;

  double xSpeed = 0.;
  double lineSpeed = 0;
  private static Logger logger = LogManager.getLogger(MineController.class);


  public MineController(double lineSpeed) {
    this.lineSpeed = lineSpeed;
  }

  @Override
  public void updateObject() {

    if (gameObject.getY() >= this.getPlayground().getSizeY() - 10) {
      this.gameObject.setVY(0);
      if (xSpeed == 0.) {
        GameObject ego = getPlayground().getObject("ego");
        double egoXPos = ego.getX();
        if (egoXPos > this.gameObject.getX()) {
          xSpeed = 50;
        } else {
          xSpeed = -50;
        }
        this.gameObject.setVX(xSpeed);

      }
      this.gameObject.setVX(xSpeed);

    }
    if (this.gameObject.getX() < 0 || (this.gameObject.getX() > this.getPlayground().getSizeX())) {
      logger.debug("deleting" + this.gameObject.getId());
      getPlayground().deleteObject(this.gameObject.getId());
    }

    applySpeedVector();
  }
}
