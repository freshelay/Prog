package gameobjects;

import java.util.LinkedList;
import playground.Playground;
import playground.Animation;
import collider.Collider;
import collider.RectCollider;
import rendering.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class AnimatedGameobject extends GameObject {

  protected AnimationArtist animArtist;
  private static Logger logger = LogManager.getLogger(AnimationArtist.class);


  // auto-generates collider according to box width/height
  public GameObject generateColliders() {
    logger.debug("Created animated Obj "+ this.animArtist.getW()+" "+this.animArtist.getH()) ;
    double w = this.animArtist.getW() ;
    double h = this.animArtist.getH() ;
    this.addCollider(new RectCollider("RectColl_" + this.id, this, w,
        h)) ;
        
    logger.info("ANIMGO-COLL ID="+this.getId()+" WH= "+w+"/"+h) ;
    return this ;
  }


  public AnimatedGameobject(String id, Playground pg, double x, double y, double vx, double vy,
      double scale, Animation anim, double t0, String abspielmodus) {
    super(id, pg, x, y, vx, vy); // Konstruktor-Aufruf GameObject

    this.artist = new AnimationArtist(this, anim, t0, abspielmodus, scale);
    this.animArtist = (AnimationArtist) (this.artist);

  }


}
