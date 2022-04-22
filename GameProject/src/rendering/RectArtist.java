package rendering;

import gameobjects.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;

public class RectArtist extends Artist {

  protected Color color;
  protected double width, height;

  public RectArtist(GameObject go, double width, double height, Color color) {
    super(go);
    this.color = color;
    this.width = width;
    this.height = height;
  }

  @Override
  public void draw(Graphics2D g) {
    g.setColor(this.color);

    // g.drawLine((int) (Math.round(this.getX())), (int) (Math.round(this.getY()-this.height/2.)),
    // (int) Math.round(this.getX()),
    // (int) Math.round(this.getY() + this.height/2.));
    int x = (int) this.getX();
    int y = (int) this.getY();
    g.fillRect((int) (this.getX() - this.width / 2.), (int) (this.getY() - this.height / 2.),
        (int) this.width, (int) this.height);

  }

}
