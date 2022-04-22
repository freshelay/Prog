package gameobjects;

import java.awt.Color;
import java.io.File;
import collider.RectCollider;
import playground.Playground;
import playground.SpaceInvadersLevel;
import rendering.RectArtist;

/**
 * A rectangle object. <br>
 * If {@link #generateColliders()} is called, it generates a RectCollider with id-prefix
 * "shotcollider_" and registers it for this RectObject.
 *
 */
public class RectObject extends GameObject {

  /** width in pixels of the RectObject (&gt; 0) */
  protected double width;
  /** height in pixels of the RectObject (&gt; 0) */
  protected double height;

  /**
   * Initializes the RectObject with a suitable RectArtist for drawing the RectObject.
   * 
   * @param id String unique name to be used.
   * @param pg {@link Playground} instance this RectObject belongs to (the level it belongs to).
   * @param x position in horizontal direction in pixels (zero or positive number).
   * @param y position in vertical direction in pixels (zero or positive number).
   * @param vx speed/velocity in horizontal direction in pixels (negative, zero or positive number).
   * @param vy speed/velocity in vertical direction in pixels (negative, zero or positive number).
   * @param width in pixels
   * @param height in pixels
   * @param color solid color for the whole object, used to initialize an instance of
   *        {@link rendering.RectArtist} used for this RectObject.
   */
  public RectObject(String id, Playground pg, double x, double y, double vx, double vy,
      double width, double height, Color color) {
    super(id, pg, x, y, vx, vy);

    this.width = width;
    this.height = height;

    this.artist = new RectArtist(this, width, height, color);

  }


  /**
   * generates a new {@link RectCollider} with id-prefix "shotcollider_" and registers it for 'this'
   * [@link RectObject}. The {@link RectCollider} uses the same dimensions ({@link #width} and {@link #height}) as this RectObject.
   * 
   * @return this RectObject itself
   */
  public RectObject generateColliders() {
    this.scol.add(new RectCollider("shotcollider_" + id, this, this.width, this.height));
    return this;
  }
  
  /** Getter for the width
   * 
   * @return double width value as set by constructor
   */
  public double getWidth() {
    return this.width;
  }
  
  /** Getter for the height
   * 
   * @return double height value as set by constructor
   */
  public double getHeight() {
    return this.height;
  }

}
