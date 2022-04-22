package controller;

import gameobjects.GameObject;
import playground.Playground;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Class that controls the LOGICAL behavior of an object independently of how it is displayed or
 * drawn. The most important method here is {@link #updateObject}: this method is, by various
 * indirections, called exactly once per game time step for every object that is on the playground.
 * It has, by virtue of the member variables {@link #gameObject} full access to
 * <ul>
 * <li>the object it is controlling
 * <li>the playground this object belongs to
 * </ul>
 * Typically, updateObject would check whether an object leaves the screen to react appropriately.
 * In that case the object can be marked for deletion (by adding it to the flag "deleted" that is
 * always defined for any playground), but of course other reactions are possible like rebounding,
 * emerging on the other side, ...
 */

public abstract class ObjectController {
  protected GameObject gameObject = null;
  protected String dummy = "";
  
  private static Logger logger = LogManager.getLogger(ObjectController.class); 

  public void setObject(GameObject gameObject) {
    this.gameObject = gameObject;
  }


  public void setDummy(String x) {
    logger.debug("DUMMY called!!");
    this.dummy = x;
    logger.debug("DUMMY is now:" + dummy);
  }

  /**
   * Is called once every game time step by the game itself. NEVER call this directly, not
   * necessary!<br>
   * The method can do whatever it likes, including nothing. The attribute {@link #gameObject}
   * contains a reference to the controlled object, which allows access to the Playground the object
   * belongs to (useful for getting the pixel size in x and y of the playing field.<br>
   * <strong>Recommended:</strong> when implementing this method, call at the end
   * {@link #applySpeedVector() } method. This is a helper method that sets the new x,y coordinates
   * for the {@link #gameObject} correctly.
   */
  public abstract void updateObject();

  /**
   * Convenience method: simply moves the object forward one step from its present position, using
   * its present speed.
   */
  public void applySpeedVector() {
    double ts = this.getPlayground().getTimestep();
    this.setX(this.getX() + this.getVX() * ts);
    gameObject.setY(this.getY() + this.getVY() * ts);
  }


  public double getTimestep() {
    return this.gameObject.getPlayground().getTimestep();
  }

  public double getX() {
    return this.gameObject.getX();
  }

  public double getY() {
    return this.gameObject.getY();
  }

  public double getVX() {
    return this.gameObject.getVX();
  }

  public double getVY() {
    return this.gameObject.getVY();
  }

  public void setX(double x) {
    this.gameObject.setX(x);
  }


  public void setY(double y) {
    this.gameObject.setY(y);
  }

  public void setVX(double vx) {
    this.gameObject.setVX(vx);
  }

  public void setVY(double vy) {
    this.gameObject.setVY(vy);
  }

  public Playground getPlayground() {
    return this.gameObject.getPlayground();
  }

  public void setPlayground(Playground playground) {
    this.gameObject.setPlayground(playground);
  }



}
