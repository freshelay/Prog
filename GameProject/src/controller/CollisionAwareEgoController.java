package controller;

import playground.*;
import gameobjects.*;
import java.util.*;
import java.awt.event.*;
import java.io.File;


/**
 * An EgoController which cannot move through obstacle objects (is collission aware). Only respects
 * GameObjects that have the String 'obstacle' in their name.
 *
 */
public class CollisionAwareEgoController extends EgoController {

  double savex, savey, savevx, savevy;
  double lastSpaceAt = -1;
  private File shot = null;


  /**
   * 
   * @param egoRad radius of ego object to be used.
   */
  public CollisionAwareEgoController(double egoRad) {
    super(egoRad);
  }

  /**
   * 
   * @param egoRad radius of ego object to be used.
   * @param soundOnShot WAV file to be played on shot
   */
  public CollisionAwareEgoController(double egoRad, File soundOnShot) {
    super(egoRad);
    this.shot = soundOnShot;
  }

  public void saveDynamicState() {
    this.savex = this.getX();
    this.savey = this.getY();
    this.savevx = this.getVX();
    this.savevy = this.getVY();
  }


  public void restoreDynamicState() {
    this.setX(savex);
    this.setY(savey);
    this.setVX(savevx);
    this.setVY(savevy);
  }


  public boolean stopObject() {
    boolean s = super.stopObject();

    Playground pg = this.getPlayground();

    LinkedList<GameObject> obstacles = pg.collectObjects("obstacle", false);

    this.saveDynamicState();
    this.applySpeedVector();

    for (GameObject ob : obstacles) {
      if (ob.collisionDetection(this.gameObject)) {
        this.restoreDynamicState();
        return true;
      }
    }
    this.restoreDynamicState();
    return s;
  }

  public void onSpace(KeyEvent e, GameObject ego) {
    double cgt = ego.getGameTime();
    if ((cgt - this.lastSpaceAt) > 0.1) {
      super.onSpace(e, ego);
      Music.music(this.shot);
    }

  }
}
