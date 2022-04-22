package playground;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.awt.event.*;
import gameobjects.GameObject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Playground represents a level of the game, focusing on the game LOGIC, i.e., not so much on the
 * graphical representation. In particular, an instance of Playground
 * <ul>
 * <li>manages the different moving or static objects in a level (e.g., collisions, adding objects,
 * removing objects). This is mainly done by the methods {@link #addObject}, {@link #deleteObject}.
 * <li>processes keyboard inputs provided by GameLoop in {@link #processKeyEvents(Stack)} and
 * {@link #processMouseEvents(Stack)}
 * <li>represents the state of a level represented by <b>flags</b>. Each flag has a name (a String)
 * and an arbitrary value of any type. Methods: {@link #setLevelFlag(String, Object)},
 * {@link #getLevelFlag(String)}. As an example, the current score is a flag usually named "points",
 * with an Integer as a value. This value can be retrieved and manipulated using the above mentioned
 * methods.
 * </ul>
 */
public abstract class Playground {

  public static final int FLAGS_GLOBAL = 1;
  public static final int FLAGS_LEVEL = 2;
  public static final int FLAGS_ALL = 3;
  protected int canvasX = -1;
  protected int canvasY = -1;

  /** only one set of objects exists concurrently so this can be static */
  protected static HashMap<String, GameObject> gameObjects = new HashMap<String, GameObject>();

  /** only one set of objects exists concurrently so this can be static */
  protected static HashMap<String, Object> flags = new HashMap<String, Object>();

  protected String level = "";
  protected double timeStep = 0;
  protected double gameTime = 0;
  LinkedList<GameObject> addables = new LinkedList<GameObject>();
  LinkedList<String> removables = new LinkedList<String>();
  // HashMap<Integer,Integer> keys ;
  Stack<KeyEvent> keyEvents;
  Stack<MouseEvent> mouseEvents;

  protected boolean pausedFlag = false;

  private static Logger logger = LogManager.getLogger(Playground.class);

  public Playground() {
    this.canvasX = -1;
    this.canvasY = -1;
  }

  // here, the level communicates its size preference to the GameUI
  // called automatically
  public abstract int preferredSizeX();

  public abstract int preferredSizeY();

  /**
   * Adds a graphics object to a level.
   * 
   * @param o GameObject The object to be added
   */
  public void addObject(GameObject o) {
    // gameObjects.put(o.getId(), o);
    addables.addLast(o);
  }

  /**
   * Adds a graphics object to a level.
   * 
   * @param o GameObject The object to be added
   */
  public void addObjectNow(GameObject o) {
    gameObjects.put(o.getId(), o);
  }


  /**
   * Puts objects with a certain substring in their name into a LinkedLisrt and returns them.
   * 
   * @param substr The string that must be part of the object name if object is to be considered
   *        found.
   * @param filterInactive if set true only active objects are considered.
   * @return a reference to a LinkedList filled with all objects that have <b>substr</b> in their
   *         name
   */
  public LinkedList<GameObject> collectObjects(String substr, boolean filterInactive) {
    LinkedList<GameObject> l = new LinkedList<GameObject>();
    for (Map.Entry<String, GameObject> entry : Playground.gameObjects.entrySet()) { // Iterator
                                                                                    // usage
      GameObject obj = entry.getValue();
      if (obj.getId().contains(substr)) {
        if (filterInactive == true) {
          if (obj.isActive()) {
            l.add(obj);
          }
        } else {
          l.add(obj);
        }
      }
    }
    return l;
  };


  /**
   * Removes a graphics object from a level.
   * 
   * @param id String The unique identifier of the object
   */
  public void deleteObject(String id) {
    // gameObjects.remove(id);
    removables.addLast(id);
  }

  /**
   * Removes a graphics object from a level immediately, CAUTION.
   * 
   * @param id String The unique identifier of the object
   */
  public void deleteObjectNow(String id) {
    gameObjects.remove(id);
  }


  /**
   * Retrieves a graphics object by name.
   * 
   * @param id String Unique id of the object
   * @return reference to the requested game object, or null if not found
   */
  public GameObject getObject(String id) {
    return gameObjects.get(id);
  }

  /**
   * Sets a level-wide permanent flag.
   * 
   * @param flag String Q unique name in this level. If it exists value is overwritten.
   * @param value Object Any Object can be the value of a flag!
   */

  public static void setGlobalFlag(String flag, Object value) {
    flags.put("/global/" + flag, value);
  }

  public Object setLevelFlag(String flag, Object value) {
    flags.put("/" + this.level + "/" + flag, value);
    return value;
  }

  /**
   * mode can be: FLAGS_ALL (all), FLAGS_GLOBAL(global), FLAGs_LEVEL(level)
   * 
   * @param mode can be only one of {@link #FLAGS_GLOBAL} {@link #FLAGS_ALL} or
   *        {@link #FLAGS_LEVEL }
   */
  public void resetFlags(int mode) {
    LinkedList<String> delKeys = new LinkedList<String>();
    for (Map.Entry<String, Object> entry : Playground.flags.entrySet()) {
      logger.trace(entry.getKey() + " IndexofGlobal = " + entry.getKey().indexOf("/global/"));
      if ((mode == FLAGS_GLOBAL) && (entry.getKey().indexOf("/global/") != -1)) {
        logger.debug("GLOBAL: scheduling for removal: " + entry.getKey());
        delKeys.add(entry.getKey());
      } else if ((mode == FLAGS_LEVEL) && (entry.getKey().indexOf("/global/") == -1)) {
        logger.debug("LEVEL: scheduling for removal: " + entry.getKey());
        delKeys.add(entry.getKey());
      } else if (mode == FLAGS_ALL) {
        logger.debug("ALL: scheduling for removal: " + entry.getKey());
        delKeys.add(entry.getKey());
      }
    }

    for (String str : delKeys) {
      logger.trace("removing key " + str);
      flags.remove(str);
    }
  }


  /**
   * Retrieves a level-wide flag by name.
   * 
   * @param flag String Unique flag id
   * @return the value associated with <b>flag</b>, or <b>null</b> if the flag does not exist.
   */
  public static Object getGlobalFlag(String flag) {
    return flags.get("/global/" + flag);
  }

  /** checks for existence and if not creates the new global flag with the given initial value. Returns the value.
   *  afterwards it is guaranteed that no priorly existing value is overridden and that it definitely exists (created if not present before). 
  * 
   * @param flag  String name for the global flag  (created if not present)
   * @param value Object value to be stored (used only if flag was not present)
   * @return the current value of the flag (maybe the initial one in case flag was not there before)
   */
  public static Object getOrCreateGlobalFlag(String flag, Object value) {
    Object tmp = getGlobalFlag(flag);
    if (tmp == null) {
      setGlobalFlag(flag, value);
      return value;
    } else {
      return tmp;
    }
  }


  public Object getLevelFlag(String flag) {
    return flags.get("/" + this.level + "/" + flag);
  }

  public Object getOrCreateLevelFlag(String flag, Object createValue) {
    Object tmp = getLevelFlag(flag);
    if (tmp == null) {
      return setLevelFlag(flag, createValue);
    } else {
      return tmp;
    }
  }


  /**
   * Reinitializes the level.
   */
  public void reset() {
    gameObjects.clear();
  }

  public boolean isPaused() {
    return this.pausedFlag;
  }

  public void setPaused(boolean p) {
    this.pausedFlag = p;
  }

  public void togglePause() {
    pausedFlag = !pausedFlag;

  }


  /**
   * Method meant to be filled with own code, processes Keyboard inputs.
   * 
   * @param keyEvents all collected {@link KeyEvent}s collected since last game loop.
   */

  public void processKeyEvents(Stack<KeyEvent> keyEvents) {
    this.keyEvents = keyEvents;
    Playground.setGlobalFlag("inputs", keyEvents);
  }


  public void processMouseEvents(Stack<MouseEvent> mouseEvents) {
    this.mouseEvents = mouseEvents;
    Playground.setGlobalFlag("inputs", mouseEvents);
  }


  public Stack<KeyEvent> getKeyEvents() {
    return this.keyEvents;
  }

  public Stack<MouseEvent> getMouseEvents() {
    return this.mouseEvents;
  }


  /**
   * Method meant to be filled with own code, handles the entore game logic (collision checks, timed
   * events, ...).
   * 
   */
  public abstract void applyGameLogic();

  /**
   * Sets up a single level. Prepares all objects etc.
   * 
   * @param level String a string identifying the level number etc
   */
  public abstract void prepareLevel(String level);

  public abstract boolean gameOver();

  public abstract boolean levelFinished();

  public int getSizeX() {
    return canvasX;
  }

  public int getSizeY() {
    return canvasY;
  }

  /**
   * Calls all object update methods in level. Internal, never call directly.
   * 
   */
  public void updateObjects() {
    for (GameObject gameObject : gameObjects.values()) { // Iterator usage
      if (gameObject.isActive() == true) {
        gameObject.updateObject();
        logger.trace("updated object " + gameObject.scol);
      }
    }

    for (GameObject o : addables) { // Iterator usage
      addObjectNow(o);
    }

    for (String s : removables) { // Iterator usage
      deleteObjectNow(s);
    }
    removables.clear();
    addables.clear();
  }

  public void setTimestep(double s) {
    timeStep = s;
  }

  public double getTimestep() {
    return timeStep;
  }

  /** set the game time value (in seconds)
   *
   * @param s  seconds the game is running
   */
  public void setGameTime(double s) {
    this.gameTime = s;
  }

  /** returns time in seconds since level start */
  public double getGameTime() {
    return this.gameTime;
  }


  /**
   * To be redefined!! Draws mainly h level background and global information like points etc.
   * 
   * @param g2 Graphics2D abstract drawing object of java Swing, used to carry out all drawing
   *        operations.
   */
  public abstract void redrawLevel(Graphics2D g2);

  /**
   * Internal, do not call directly.
   * 
   * @param g2 Graphics2D abstract drawing object of java Swing, used to carry out all drawing
   *        operations.
   */
  public void redraw(Graphics2D g2) {
    redrawLevel(g2);
    for (GameObject gameObject : gameObjects.values()) {
      if (gameObject.isActive()) {
        gameObject.draw(g2);
      }
    }
  }


}
