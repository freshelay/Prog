package controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import playground.*;
import gameobjects.*;
import java.util.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Controller using key events for up, down, left, right and space (shooting) to control the ego
 * object behavior.
 */
public class EgoController extends ObjectController {
  // either rad is zero or width/height is zero (guaranteed by constructors)
  private double rad = 0;
  private double width = 0;
  private double height = 0;

  private Integer pressedKey = null;
  private Integer lastPressedKey = null;
  private static Logger logger = LogManager.getLogger(EgoController.class);


  /**
   *  constructor that gives the ego controller a radius to stop the ego object when it reaches the level boundaries.
   * @param egoRad radius to use as a boundary stop for level borders (usually use the same dimensions as your ego object)
   */
  public EgoController(double egoRad) {
    this.rad = egoRad;
  }
  
  /**
   *  constructor that gives the ego controller a width and height to stop the ego object when it reaches the level boundaries.
   * @param width width to use as a boundary stop for level borders (usually use the same dimensions as your ego object)
   * @param height height to use as a boundary stop for level borders (usually use the same dimensions as your ego object)
   */
  public EgoController(double width, double height) {
    this.width = width;
    this.height = height;
  }


  public void onUp(KeyEvent kc, GameObject ego) {
    ego.setVX(0.0);
    ego.setVY(-SpaceInvadersLevel.EGOSPEED);
  }

  public void onDown(KeyEvent kc, GameObject ego) {
    ego.setVX(0.0);
    ego.setVY(SpaceInvadersLevel.EGOSPEED);
  }

  public void onLeft(KeyEvent kc, GameObject ego) {
    ego.setVY(0.0);
    ego.setVX(-SpaceInvadersLevel.EGOSPEED);
  }

  public void onRight(KeyEvent kc, GameObject ego) {
    ego.setVY(0.0);
    ego.setVX(SpaceInvadersLevel.EGOSPEED);
  }

  public void onStop(KeyEvent kc, GameObject ego) {
    ego.setVY(0.0);
    ego.setVX(0.0);
    ego.setComponentProperty("controller", "setDummy", "NEW");
    ego.setComponentProperty("controller", "setDummy2", "XXX");
  }


  /** checks the position and respects level boundaries and own radius or width/height set on constructor. 
   * 
   * @return true if the object reached the boundaries of the level, false otherwise
   */
  public boolean stopObject() {
    // check whether ego object is at level boundaries
    // can use radius (rad) and width or height in one check as either rad or width/height is zero.
    int pgSizeX = this.getPlayground().getSizeX();
    int pgSizeY = this.getPlayground().getSizeY();
    double ts = this.getTimestep();
    if (this.getX() + rad + (width/2d) + this.getVX() * ts >= pgSizeX
        || this.getX() - rad - (width/2d) + this.getVX() * ts < 0) {
      return true;
    }
    if (this.getY() + rad + (height/2d) + this.getVY() * ts >= pgSizeY
        || this.getY() - rad - (height/2d) + this.getVY() * ts < 0) {
      return true;
    }
    return false;

  }


  /** behavior for shooting on key space
   * 
   * @param e KeyEvent of the space key
   * @param ego EgoObject instance (used to determine position of shot object's start)
   */
  public void onSpace(KeyEvent e, GameObject ego) {
    pressedKey = lastPressedKey;
    lastPressedKey = null;

    // create unique name for object
    // read Flag nextShot read (if not existing already it will be set)
    // it will be updated by 1 and saved
    Integer nextShot =
        (Integer) this.getPlayground().getOrCreateLevelFlag("nextShot", Integer.valueOf(0));
    String shotName = "simpleShot" + nextShot++;
    this.getPlayground().setLevelFlag("nextShot", nextShot);

    SimpleShotController simpleshot = new SimpleShotController();
    GameObject ss = new RectObject(shotName, this.getPlayground(), ego.getX(), ego.getY(), 0,
        -1. * SpaceInvadersLevel.SHOTSPEED, 4, 12, Color.CYAN).addController(simpleshot);
    ss.generateColliders();
    this.getPlayground().addObject(ss);
  }


  /**
   * updates position based on key events (mouse currently ignored)
   */
  public void updateObject() {

    logger.trace("Playground inst is"+this.getPlayground()) ;
    Stack<KeyEvent> keyEvents = this.getPlayground().getKeyEvents();

    GameObject ego = this.gameObject;

    while (!keyEvents.isEmpty()) {

      KeyEvent e = keyEvents.pop();
      boolean pressed = false;
      boolean released = true;
      int kc = e.getKeyCode();

      if (e.paramString().indexOf("PRESSED") >= 0) {
        pressed = true;
        released = false;
      }

      /**
       * Generelle Idee: Wenn eine Taste gedrückt wird wird sie gespeichert. wenn die zuvor
       * gespeicherte Taste wieder losgelassen wird stoppt das Ego-Objekt. Falls vor dem Loslassen
       * eine andere Taste gedrückt wird, wird diese gespeichert und die alte vergessen. Dh das
       * loslassen der alten Taste stoppt das Objekt nicht. Spezialfall: space, das loslassen von
       * space stoppt das Objekt nicht!
       */

      if (pressed == true) {
        lastPressedKey = pressedKey;
        pressedKey = kc;
      }

      /**
       * Nur eine losgelassene Taste die auch vorher gedrückt wurde stoppt das Objekt. Eine
       * losgelassene Taste die nicht vorher gedrückt wurde bzw vergessen wurde stoppt das Objekt
       * nicht
       */
      if (released == true) {
        if (pressedKey != null) {
          if (pressedKey.equals(kc)) {
            ego.setVX(0);
            ego.setVY(0);
            pressedKey = null;
          }
        }
        continue;
      }

      if (kc == KeyEvent.VK_LEFT) {
        this.onLeft(e, ego);
      }

      if (kc == KeyEvent.VK_RIGHT) {
        this.onRight(e, ego);
      }

      if (kc == KeyEvent.VK_UP) {
        this.onUp(e, ego);
      }

      if (kc == KeyEvent.VK_DOWN) {
        this.onDown(e, ego);
      }

      // stop
      if (kc == KeyEvent.VK_Z) {
        this.onStop(e, ego);

      }


      // shot
      if (kc == KeyEvent.VK_SPACE) {
        // space is not registered! Releasing space does not stop the egoobject
        this.onSpace(e, ego);
      }
    }


    boolean stop = this.stopObject();

    if (stop) {
      this.setVX(0);
      this.setVY(0);
    }

    // updateSpeed and position
    applySpeedVector();

  }


}
