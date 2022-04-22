package gameobjects;


import java.awt.Color;
import collider.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import collider.Collider;
import controller.ObjectController;
import playground.Playground;
import rendering.*;

public class EgoObject extends GameObject {

  double egoRad = 0;

  public EgoObject(String id, Playground pg, double x, double y, double vx, double vy,
      double egoRad) {
    super(id, pg, x, y, vx, vy);
    this.egoRad = egoRad;
    this.artist = new CircleArtist(this, egoRad, Color.WHITE);
  }

  public GameObject generateColliders() {
    CircleCollider coll = new CircleCollider("coll", this, this.egoRad);
    this.addCollider(coll);
    return this;
  }

}
