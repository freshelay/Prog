package gameobjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.lang.reflect.*;
import rendering.*;
import collider.Collider;
import controller.ObjectController;
import playground.Playground;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * The class {@link GameObject} represents a (possibly animated) object appearing in a level of the
 * game. It is therefore attached to an instance of the class {@link Playground}. A GameObject has
 * at least the following properties:
 * <ul>
 * <li>2D screen position
 * <li>2D speed
 * <li>a name that is unique within a certain {@link Playground}<br>
 * <li>a reference to the {@link Playground} object it belongs to<br>
 * <li>a reference to an instance of {@link controller.ObjectController} that handles the movement
 * logic of the object<br>
 * <li>a (circular) radius for simple collision checking. This may be handled differently in
 * subclasses<br>
 * </ul>
 * The main task of GameObject, or its subclasses, is to draw the object on the screen, which is
 * handles by the {@link #draw(Graphics2D)} method. It is this method that must be redefined if a
 * new appearance should be realized. For introducing new behavior, it is sufficient to supply a
 * different {@link controller.ObjectController} instance when constructing a GameObject.
 */
public abstract class GameObject {

  public static final int RADIUS = 0;
  public static final int RECTANGLE = 1;
  public static final int MASK = 2;

  protected Artist artist = null;

  public String id = null;
  protected double x = 0;
  protected double vx = 0;
  protected double y = 0;
  protected double vy = 0;
  protected BufferedImage mask = null; // UNDONE implement usage of mask
  protected boolean active = true;
  // public int collisionMode = GameObject.RADIUS;
  protected Playground playground = null;

  private ObjectController controller = null;
  public LinkedList<Collider> scol;

  private static Logger logger = LogManager.getLogger(GameObject.class);

  /**
   * Constructor to initialize a GameObject, respectively set the current {@link Playground}
   * instance this GameObject belongs to.
   * 
   * @param id unique ID for this GameObject (should not be null or empty String)
   * @param playground the Playground the GameObject belongs to (should not be null)
   * @param x initial screen position in direction horizontal (positive value including zero)
   * @param y initial screen position in direction vertical (positive value including zero)
   * @param vx initial speed (velocity) in direction horizontal (can be negative, zero, positive)
   * @param vy initial speed (velocity) in direction horizontal (can be negative, zero, positive)
   */
  public GameObject(String id, Playground playground, double x, double y, double vx, double vy) {
    setX(x);
    setY(y);
    setVX(vx);
    setVY(vy);
    this.id = id;
    this.controller = null;
    this.scol = new LinkedList<Collider>();
    this.setPlayground(playground);
  }

  /**
   * Constructor to initialize a GameObject, respectively set the current Playground instance this
   * GameObject belongs to.
   * 
   * @param id unique ID for this GameObject (should not be null or empty String)
   * @param playground the Playground the GameObject belongs to (should not be null)
   * @param controller controller instance to be used for this GameObject (can be null)
   * @param x initial screen position in direction horizontal (positive value including zero)
   * @param y initial screen position in direction vertical (positive value including zero)
   * @param vx initial speed (velocity) in direction horizontal (can be negative, zero, positive)
   * @param vy initial speed (velocity) in direction horizontal (can be negative, zero, positive)
   */
  public GameObject(String id, Playground playground, ObjectController controller, double x,
      double y, double vx, double vy) {
    this(id, playground, x, y, vx, vy);
    this.controller = controller;
    if (this.controller != null) {
      this.controller.setObject(this);
      this.controller.setPlayground(playground);
    }
  }

  /**
   * sets colliders.
   * 
   * @param l LinkedList of Colliders.
   */
  public void setColliders(LinkedList<Collider> l) {
    this.scol = l;
  }

  /**
   * generates and sets collider(s) for this GameObject. This implementation does nothing. Intended
   * to be overridden by subclasses.
   * 
   * @return instance of this GameObject (this).
   */
  public GameObject generateColliders() {
    return this;
  }

  /**
   * Sets the controller to use for this GameObject's logical behavior.
   * 
   * @param c instance to be used.
   * @return the current instance (this).
   */
  public GameObject addController(ObjectController c) {
    this.controller = c;
    this.controller.setObject(this);
    this.controller.setPlayground(playground);
    return this;
  }

  /**
   * Sets the artist to be used for drawing the object onto visible canvas area.
   * 
   * @param a instance to be used for calling {@link rendering.Artist#draw(Graphics2D)}.
   * @return the current instance (this).
   */
  public GameObject addArtist(Artist a) {
    this.artist = a;
    return this;
  }

  /**
   * saves the collider in the internal list of Colliders to be used for this GameObject.
   * 
   * @param c instance to be added to internal list
   */
  public void addCollider(Collider c) {
    if (this.scol == null) {
      this.scol = new LinkedList<Collider>();
    }
    this.scol.add(c);
  }

  public Playground getPlayground() {
    return playground;
  }

  public void setPlayground(Playground playground) {
    this.playground = playground;
  }

  /**
   * calls via reflection a method of a component if this GameObjects instance and provides the
   * given value as String parameter.
   * 
   * @param comp class name of GameObject component. Currently only "controller" is supported,
   *        otherwise nothing happens.
   * @param property method name of the component to call.
   * @param value argument to pass to the method as String parameter.
   */
  public void setComponentProperty(String comp, String property, Object value) {
    if (comp.equals("controller")) {
      Class<? extends Object> clO = this.controller.getClass();
      for (Method m : clO.getMethods()) {
        if (m.getName().indexOf(property) != -1) {
          logger.debug("Method " + property + " found!!");
          try {
            m.invoke(this.getObjectController(), value);
          } catch (Exception e) {
          }
        }
      }

    }
  }

  public void setObjectFlag(String flag, Object value) {
    this.playground.setLevelFlag(this.id + "/" + flag, value);
  }


  public Object getObjectFlag(String flag) {
    return this.playground.getLevelFlag(this.id + "/" + flag);
  }


  public Object getOrCreateObjectFlag(String flag, Object createValue) {
    return this.playground.getOrCreateLevelFlag(this.id + "/" + flag, createValue);
  }



  public boolean isActive() {
    return active;
  }


  public GameObject setActive(boolean flag) {
    this.active = flag;
    return this;
  }

  /**
   * return the unique object ID.
   * 
   * @return unique object ID
   */
  public String getId() {
    return id;
  }

  /**
   * gets the screen X position.
   * 
   * @return screen x position
   */
  public double getX() {
    return x;
  }

  /**
   * gets the screen Y position.
   * 
   * @return screen Y position
   */
  public double getY() {
    return y;
  }

  /**
   * gets the screen X speed in pixels per frame.
   * 
   * @return screen x speed
   */
  public double getVX() {
    return vx;
  }

  /**
   * gets the screen Y speed in pixels per frame.
   * 
   * @return screen y speed
   */
  public double getVY() {
    return vy;
  }

  /**
   * set screen x position.
   * 
   * @param x new position
   */
  public void setX(double x) {
    if (this.active == true) {
      this.x = x;
    }
  }

  /**
   * set screen y position.
   * 
   * @param y new position
   */
  public void setY(double y) {
    if (this.active == true) {
      this.y = y;
    }
  }

  /**
   * set screen x speed in pixel per frame
   * 
   * @param vx new x speed
   */
  public void setVX(double vx) {
    if (this.active == true) {
      this.vx = vx;
    }
  }

  /**
   * set screen y speed in pixel per frame.
   * 
   * @param vy new y speed.
   */
  public void setVY(double vy) {
    if (this.active == true) {
      this.vy = vy;
    }
  }

  /**
   * Sets a new object controller (replaces any former one).
   * 
   * @param controller An instance of {@link controller.ObjectController} or one of its subclasses.
   */
  public void setObjectController(ObjectController controller) {
    this.controller = controller;
  }


  /**
   * Access to object controller.
   * 
   * @return the controller for this object.
   */
  public ObjectController getObjectController() {
    return this.controller;
  }


  public double getGameTime() {
    return this.playground.getGameTime();
  }



  /**
   * Collision detection implemented by iteration through the own list of {@link collider.Collider}
   * and calling their {@link collider.Collider#collidesWith(Collider)} method to check collision
   * with the given parameter instance of other {@link GameObject}.
   * 
   * @param other instance of the other GameObject to check collision with
   * @return true if collision is detected, false otherwise
   */
  public boolean collisionDetection(GameObject other) {
    if (this.scol == null) {
      return false;
    }
    for (Collider c : this.scol) {
      logger.trace(other.id);
      for (Collider o : other.scol) {
        if (c.collidesWith(o)) {
          logger.trace(c.id + " " + o.id);
          return true;
        }
      }
    }
    return false;

  }

  /**
   * triggers this GameObjects own controller (if set) to update the object.
   * 
   * @see GameObject#controller
   */
  public void updateObject() {
    if (this.controller != null) {
      controller.updateObject();
    }
  }

  /**
   * Draws the object in its current state. Is called by the game engine, should NOT be called
   * otherwise.
   * 
   * @param g object that has all the necessary drawing functionalities
   */
  public void draw(Graphics2D g) {
    if (this.artist != null) {
      this.artist.draw(g);
    }
  }

}
