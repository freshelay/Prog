package collider;

import java.awt.Color;

import gameobjects.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/** a {@link Collider} for Rectangles, usually used for {@link RectObject} instances.
 * @see gameobjects.RectObject#generateColliders() 
 */
public class RectCollider extends Collider {

  //double x;
  //double y;
  //double vx;
  //double vy;
  double w, h;

  private Color color = Color.WHITE;
  
  private static Logger logger = LogManager.getLogger(RectCollider.class);

  /**
   * initializes this RectCollider.
   * calls superclass constructor of {@link Collider#Collider(String, GameObject)} with params String id and GameObject o.
   * 
   * @param id String unique name for this RectCollider
   * @param o GameObject instance this RectCollider belongs to (cannot be null)
   * @param w width in pixels for the collider dimensions (> 0)
   * @param h height in pixels for the collider dimensions (>0)
   */
  public RectCollider(String id, GameObject o, double w, double h) {
    super(id, o);
    this.w = w;
    this.h = h;
  }

  public String toString() {
    return " " + w + " " + h + " ";
  }

  /**
   * checks collision with other Collider, which needs to be a RectCollider, too.
   * @param other RectCollider (is casted) to calculate collision with
   * @return true if collission is detected
   */
  public boolean checkCollisionRectRect(Collider other) {
    RectCollider r1 = this;
    RectCollider r2 = (RectCollider) other;

    if ((((r1.getX() + r1.w / 2.) >= (r2.getX() - r2.w / 2.)) && ((r1.getX() + r1.w / 2.) <= (r2
        .getX() + r2.w / 2.)))
        || (((r2.getX() + r2.w / 2.) >= (r1.getX() - r1.w / 2.)) && ((r2.getX() + r2.w / 2.) <= (r1
            .getX() + r1.w / 2.)))) {
      if ((((r1.getY() + r1.h / 2.) >= (r2.getY() - r2.h / 2.)) && ((r1.getY() + r1.h / 2.) <= (r2
          .getY() + r2.h / 2.)))
          || (((r2.getY() + r2.h / 2.) >= (r1.getY() - r1.h / 2.)) && ((r2.getY() + r2.h / 2.) <= (r1
              .getY() + r1.h / 2.)))) {
        return true;
      }
    }
    return false;
  }

 /**
  * checks collision with other Collider, which needs to be a CircleCollider
  * @param other CircleCollider (is casted) to calculate collision with
  * @return true if collission is detected
  */
  public boolean checkCollisionRectCirc(Collider other) {
    RectCollider r = this;
    CircleCollider c = (CircleCollider) (other);
    double circleDistX = Math.abs(c.getX()  - (r.getX()  ));
    double circleDistY = Math.abs(c.getY()  - (r.getY() ) );

    logger.trace("c.x:"+c.x+" "+"c.y:"+c.y+" "+"c.r:"+c.r+" "+"r.x:"+r.getX()+" "+"r.y:"+r.getY()+" "+"r.w:"+r.w+" "+"r.h:"+r.h+" "+"circleDistX:"+circleDistX+" "+"circleDistY:"+circleDistY);

    if (circleDistX > (r.w / 2 + c.r))
      return false;
    if (circleDistY > (r.h / 2 + c.r))
      return false;

    if (circleDistX <= (r.w / 2)) {
        logger.trace("Collision Rect with circle");
      return true;
    }
    if (circleDistY <= (r.h / 2)) {
      logger.trace("Collision Rect with circle (second)");
      return true;
    }

    double cornerDistSqr = Math.pow(circleDistX - r.w / 2, 2) + Math.pow(circleDistY - r.h / 2, 2); // Satz
                                                                                                    // des
                                                                                                    // Pythagoras
    return (cornerDistSqr <= c.r * c.r); // falls true zurueckgegeben: Kollision
  }



  @Override
  public boolean collidesWith(Collider other) {

    // rect circ
    try {
      return checkCollisionRectCirc(other);
    } catch (Exception e) {
      // do nothing
    }

    // rect rect
    try {
      return checkCollisionRectRect(other);
    } catch (Exception e) {
      // do nothing
    }

    try {
      return other.collidesWith(this);
    } catch (Exception e) {
      // do nothing
    }

    throw new RuntimeException("Collider type not implemented!");
  }

}
