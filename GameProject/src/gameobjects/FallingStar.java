package gameobjects;

import java.awt.Color;
import java.util.LinkedList;
import collider.*;
import playground.Playground;
import rendering.*;

public class FallingStar extends GameObject {

  private Color color = Color.WHITE;

  protected double rad = -1;


  public FallingStar(String id, Playground playground, double x, double y, double vx, double vy,
      Color color, double rad) {
    super(id, playground, x, y, vx, vy);
    this.rad = rad;
    this.color = color;
    LinkedList<Collider> cols = new LinkedList<Collider>();
    CircleCollider cc = new CircleCollider("cc", this, rad);
    cols.add(cc);
    setColliders(cols);
    this.artist = new CircleArtist(this, rad, color);
  }

}
