package rendering;

import gameobjects.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;

public class CircleArtist extends Artist {

  protected double egoRad;
  protected Color color;

  public CircleArtist(GameObject go) {
    super(go);
    this.egoRad = 15.0;
  }

  public CircleArtist(GameObject go, double egoRad, Color color) {
    super(go);
    this.egoRad = egoRad;
    this.color = color;
  }

  @Override
  public void draw(Graphics2D g) {
    g.setColor(this.color);
    double x = this.getX();
    double y = this.getY();
    int posX = (int) (x - egoRad);
    int posY = (int) (y - egoRad);
    int rad = (int) (2 * egoRad);
    g.fillOval(posX, posY, rad, rad);
  }

}
