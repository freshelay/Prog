package rendering;

import gameobjects.GameObject;
import playground.Animation;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AnimationArtist extends Artist {

  protected LinkedList<BufferedImage> imageArray;
  protected LinkedList<Double> showtime;
  protected double t0;

  protected int loopFrame = 0;

  protected double w = 0, h = 0, scale = 0;
  protected String playmode = ""; // can be loop, forward, backward
  private static Logger logger = LogManager.getLogger(AnimationArtist.class);


  public AnimationArtist(GameObject go, Animation anim, double t0, String playmode, double scale) {
    super(go);

    this.imageArray = anim.getImageList();
    this.showtime = anim.getShowtimeList();
    this.t0 = t0;
    logger.debug("AnimationArtist start = ") ;
    this.loopFrame = 0;

    for (int i = 0; i < imageArray.size(); i++) {
      logger.trace("ANIMRAWWH="+imageArray.get(i).getWidth()) ;
      this.w = Math.max(this.w, imageArray.get(i).getWidth());
      this.h = Math.max(imageArray.get(i).getHeight(), this.h);
    }
    
    logger.debug("AnimationArtist RAW WH = "+this.w+"/"+this.h) ;
    
    this.playmode = playmode;
    this.w *= scale;
    this.h *= scale;
  }

  public double getW() {
    return w;
  }

  public double getH() {
    return h;
  }


  @Override
  public void draw(Graphics2D g) {
    double i2 = this.getGameTime();

    double elapsedTime = i2 - t0;

    // Diff chance from sec to millis
    double mseconds = (double) elapsedTime;
     logger.debug("showtime= "+showtime.get(loopFrame)+" elapsed= "+elapsedTime+" i2= "+i2);

    if (playmode.equals("loop")) {
      if (mseconds >= showtime.get(loopFrame)) {
        loopFrame++;
        if (loopFrame >= imageArray.size())
          loopFrame = 0;
        t0 = i2;
      }
    }

    if (playmode.equals("forward")) {
      if (mseconds >= showtime.get(loopFrame) && loopFrame < imageArray.size() - 1) {
        loopFrame++;
        t0 = i2;
      }
    }

    if (playmode.equals("backward")) {
      if (mseconds >= showtime.get(loopFrame) && loopFrame > 1) {
        loopFrame--;
        t0 = i2;
      }
    }

    g.drawImage(imageArray.get(loopFrame), (int) Math.round(this.getX() - w / 2.),
        (int) Math.round(this.getY() - h / 2.), (int) w, (int) h, null);

  }

}
