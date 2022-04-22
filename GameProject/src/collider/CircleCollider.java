package collider;

import java.awt.Color;
import gameobjects.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/** Collider for round objects */
public class CircleCollider extends Collider {

  double x;
  double y;
  double vx;
  double vy;

  double r;

  private static Logger logger = LogManager.getLogger(Collider.class);
  
  /**
   * Constructor which sets the radius to be respected for collisions.
   * 
   * @param id String unique name for the collider instance
   * @param o GameObject it belongs to
   * @param radius radius in pixels to use as a size
   */
  public CircleCollider(String id, GameObject o, double radius) {

    super(id, o);
    this.r = radius;
  }

  /** simple concatenation of all attributes (x,y,r) */
  public String toString() {
    return "circ:" + x + " " + y + "/" + r + " ";
  }


  /**
   * calculates the collission of this with other collider
   * 
   * @param _c2 the other collider
   * @return true if a collision was detected
   * @throws Exception in case the math operations are invalid (due to illegal values of x y or
   *         radius)
   */
  public boolean checkCollisionCircCirc(Collider _c2) throws Exception {
    CircleCollider c2 = (CircleCollider) _c2;
    CircleCollider c1 = this;
    logger.trace(c1.x + " " + c1.y + " " + c1.r + " " + c2.x + " " + c2.y+ " " + c2.r);
    int kathete1 = (int) (Math.abs(c2.gameobject.getX() - c1.gameobject.getX()));
    int kathete2 = (int) (Math.abs(c2.gameobject.getX() - c1.gameobject.getY()));
    int hypothenuse = (int) (c1.r + c2.r);

    logger.trace(kathete1 + " " + kathete2 + " " + hypothenuse + " ");

    if (((kathete1 ^ 2) + (kathete2 ^ 2)) <= (hypothenuse ^ 2)) {
      logger.trace("Collision");
      return true;
    }
    return false;
  }



  @Override
  public boolean collidesWith(Collider other) {

    // circ circ
    try {
      return checkCollisionCircCirc(other);
    } catch (Exception e) {
    }

    try {
      return other.collidesWith(this);
    } catch (Exception e) {
    }

    throw new RuntimeException("Collider type not implemented!");
  }

  private Color color = Color.WHITE;



}
