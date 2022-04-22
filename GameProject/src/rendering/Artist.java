package rendering;

import gameobjects.GameObject;
import java.awt.Graphics2D;

public abstract class Artist {
  protected GameObject gameObject;

  Artist(GameObject go) {
    this.gameObject = go;
  }

  public double getX() {
    return this.gameObject.getX();
  }


  public double getY() {
    return this.gameObject.getY();
  }


  public double getVX() {
    return this.gameObject.getX();
  }

  public double getVY() {
    return this.gameObject.getX();
  }


  public double getGameTime() {
    return this.gameObject.getGameTime();
  }



  public abstract void draw(Graphics2D g);

}
