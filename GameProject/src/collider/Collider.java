package collider;

import java.awt.Graphics2D;
import java.util.LinkedList;
import gameobjects.GameObject;
import playground.Playground;
import controller.ObjectController;

/**
 * abstract base class for all Colliders to detect collisions between GameObjects
 * 
 *
 */
public abstract class Collider {

  /** unique internal name for Collider */
  public String id = null;
  /** GameObject it belongs to */
  protected GameObject gameobject = null;
  /** PlayGround instance it belongs to */
  protected Playground playground = null;
  /** the ObjectController to the corresponding GameObject (can be null) */
  protected ObjectController controller = null;

  protected double dx = 0.;
  double dy = 0.;

  /**
   * 
   * @param id unique name for Collider (internally)
   * @param o GameObject instance it belongs to
   */
  public Collider(String id, GameObject o) {
    this.gameobject = o;

    this.id = id;
    this.controller = o.getObjectController();
    this.playground = o.getPlayground();

  }


  /**
   * setter for offset values to be used relative to GameObject center. default is zero.
   * 
   * @param dx offset in X direction (default 0)
   * @param dy offset in Y direction (default 0)
   * @return this instance of Collider
   */
  public Collider setOffsets(double dx, double dy) {
    this.dx = dx;
    this.dy = dy;
    return this;
  }

  public String toString() {
    return "baseColl";
  }

  /**
   * returns the corresponding game objects X coordinate (center) plus this colliders offset in X
   * (probably zero).
   * 
   * @return X value
   */
  public double getX() {
    return this.gameobject.getX() + this.dx;
  }


  /**
   * returns the corresponding game objects Y coordinate (center) plus this colliders offset in Y
   * (probably zero).
   * 
   * @return Y value
   */
  public double getY() {
    return this.gameobject.getY() + this.dy;
  }

  /**
   * returns the internal unique name
   * 
   * @return the String with the name
   */
  public String getId() {
    return id;
  }


  /**
   * setter for corresponding GameObject
   * 
   * @param gameObject to be saved in attribute
   */
  public void setObject(GameObject gameObject) {
    this.gameobject = gameObject;
  }

  /**
   * setter for GameController
   * 
   * @param controller to be saved in attribute
   */
  public void setController(ObjectController controller) {
    this.controller = controller;
  }

  /**
   * setter for Playground instance this collider belongs to
   * 
   * @param playground instance to be stored in attribute
   */
  public void setPlayground(Playground playground) {
    this.playground = playground;
  }

  /**
   * checks the collission with another collider instance.
   * 
   * @param other the instance to compare to
   * @return true if the colliders collide (touch or overlap)
   */
  abstract public boolean collidesWith(Collider other);


}
