package playground;

// import utilities.* ;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.util.LinkedList;
import controller.EnemyController;
import controller.FallingStarController;
import controller.LimitedTimeController;
import controller.ObjectController;
import controller.EgoController;
import controller.CollisionAwareEgoController;
import gameobjects.AnimatedGameobject;
import gameobjects.FallingStar;
import gameobjects.GameObject;
import gameobjects.EgoObject;
import gameobjects.TextObject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Class that realizes all the game logic of a very simple game level. The level contains for now
 * only two objects that are {@link GameObject} subclasses: {@link FallingStar} and
 * {@link EgoObject}. Functions performed by this class are:
 * <ul>
 * <li>initially set up the level, spawn all object etc., in method {@link #prepareLevel}
 * <li>React to game events in {@link #actionIfEgoCollidesWithCollect(GameObject, GameObject)} ,
 * {@link #actionIfEgoCollidesWithEnemy(GameObject, GameObject)}, etc.
 * <li>define basic object movement rules for all objects in the level in the various
 * ObjectController subclasses: {@link EgoController} and {@link FallingStarController}.
 * </ul>
 */
public class SpaceInvadersLevel extends Playground {

  public static final double SHOTSPEED = 175;
  public static final double EGOSPEED = 220;
  protected static final int LEVEL2STARS = 80;
  protected static final double BONUS_DURATION = 1.;
  protected static final double ENEMYSPEEDX = 60;
  protected static final double ENEMYSPEEDY = 40;
  protected static final double ENEMYSCALE = 1.0;
  protected static final double ENEMYSHOTSPEED = 75;
  protected static final int NRSHARDS = 50;
  protected static final double EXPL_DURATION = 1.;
  protected static final int NR_ENEMIES = 30;
  protected static final int NR_COLLECT = 5;
  protected static final Color EXPL_COLOR = Color.RED;
  protected static final double SHARDSPEED = 200;
  protected static final double STARSPEED = 100;
  protected static final double STARTTEXTSPEED = 75;
  protected static final double STARTPERIOD = 5.;
  protected static final double DYING_INTERVAL = 2.0;
  protected static final int CANVASX = 700;
  protected static final int CANVASY = 700;
  protected static final double EGORAD = 15;
  protected static final double LEVEL_INIT_TIME = 1.0;

  protected int nextShot = 0;

  protected boolean lost = false;
  protected boolean doneLevel = false;
  protected double starttime = 0;
  protected File smash = null;
  protected File laser = null;

  protected BufferedImage[] alienImage = null;
  protected double[] alienshowTime = null;

  protected BufferedImage[] heartImage = null;
  protected double[] heartshowTime = null;

  protected Animation enemyAnim = null;
  protected Animation heartAnim = null;

  private static Logger logger = LogManager.getLogger(SpaceInvadersLevel.class);

  public SpaceInvadersLevel() {
    super();
    this.canvasX = this.preferredSizeX();
    this.canvasY = this.preferredSizeY();
  }


  /**
   * initially sets up the level. Not called by user interaction, but called every time a layer is
   * restarted from scratch. So make sure that this is possible. Here, resources are loaded only
   * once even if method is called several times.
   * 
   * @param id String identifies level.
   */
  @Override
  public void prepareLevel(String id) {
    logger.info("PREPARE");
    reset();
    this.doneLevel = false;
    this.lost = false;
    resetFlags(FLAGS_LEVEL);

    // set up flags that some objects rely on
    setLevelFlag("delete", new LinkedList<String>());
    setLevelFlag("replace", new LinkedList<String>());
    getOrCreateGlobalFlag("points", Integer.valueOf(0));
    setLevelFlag("enemyShotCounter", Integer.valueOf(0));
    setLevelFlag("gameStatus", "start");
    setLevelFlag("detailedStatus", "std");
    getOrCreateGlobalFlag("egoLives", Integer.valueOf(5));
    setLevelFlag("dying", Double.valueOf(-1));

    // start time measure
    this.starttime = this.getGameTime();

    // music load
    if (this.smash == null) {
      this.smash = new File("./audio/smash.wav");
    }
    if (this.laser == null) {
      this.laser = new File("./audio/laser.wav");
    }

    // ----- Alien
    if (this.enemyAnim == null) {
      String dateiName = "./video/sweetAlien.txt";
      this.enemyAnim = new Animation(dateiName);
    }

    // -----Heart
    if (this.heartAnim == null) {
      String heartName = "./video/heart.txt";
      this.heartAnim = new Animation(heartName);
    }

    // load highscore and update
    HighscoreManager dh = new HighscoreManager();
    int alltimeHighscore = dh.readHSFromFile();
    setGlobalFlag("highscore", alltimeHighscore);
    logger.info("HIGHSCORE" + alltimeHighscore);
  }


  /**
   * (re)draws the level but NOT the objects, they draw themselves. Called by the main game loop.
   * This method mainly draws the background and the scoreboard.
   * 
   * @param g2 Graphics2D object that can, and should be, draw on.
   */
  @Override
  public void redrawLevel(Graphics2D g2) {
    // Set anti-alias!
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    // Set anti-alias for text
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

    // fill background with black
    int[] x = {0, canvasX, canvasX, 0};
    int[] y = {0, 0, canvasY, canvasY};
    Polygon bg = new Polygon(x, y, 4);
    g2.setColor(Color.BLACK);
    g2.fill(bg);

    // draw score in upper left part of playground
    Integer pts = (Integer) getGlobalFlag("points");
    Font drawFont = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as = new AttributedString("Points: " + pts);
    as.addAttribute(TextAttribute.FONT, drawFont);
    as.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as.getIterator(), 10, 20);

    // draw lives counter in upper left part of playground
    Integer lives = (Integer) getGlobalFlag("egoLives");
    Font drawFont2 = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as2 = new AttributedString("Lives: " + lives);
    as2.addAttribute(TextAttribute.FONT, drawFont2);
    as2.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as2.getIterator(), canvasX - 100, 20);

    // draw highscore in left part of playground under score
    Integer highscore = (Integer) getGlobalFlag("highscore");
    Font drawFont3 = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as3 = new AttributedString("Highscore: " + highscore);
    as3.addAttribute(TextAttribute.FONT, drawFont3);
    as3.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as3.getIterator(), 10, 40);

    if (isPaused()) {
      Font drawFont4 = new Font("SansSerif", Font.PLAIN, 50);
      AttributedString as4 = new AttributedString("Das Spiel wurde pausiert.");
      as4.addAttribute(TextAttribute.FONT, drawFont4);
      as4.addAttribute(TextAttribute.FOREGROUND, Color.red);
      g2.drawString(as4.getIterator(), 30, 400);
    }

  }


  /**
   * applies the logic of the level: For now, this is just about deleting shots that are leaving the
   * screen and calling methods 'actionIf..' in case objects collide.
   *
   */
  @Override
  public void applyGameLogic() {
    double gameTime = this.getGameTime();
    String gameStatus = (String) getLevelFlag("gameStatus");
    String subStatus = (String) getLevelFlag("detailedStatus");

    if (gameStatus.equals("start") == true) {
      setupInitialState();

    } else if (gameStatus.equals("starting") == true) {

      if ((gameTime - starttime) > LEVEL_INIT_TIME) {
        setLevelFlag("gameStatus", "init");
      }

    } else if (gameStatus.equals("init") == true) {

      this.createEnemies();
      this.createCollectables();

      setLevelFlag("gameStatus", "playing");

    } else if (gameStatus.equals("playing") == true) {
      GameObject s = this.getObject("ego");

      if (subStatus.equals("std")) {

        // check for collisions of enemy and shots, reuse shots list from before..
        LinkedList<GameObject> enemies = collectObjects("enemy", false);

        // check whether all enemies have been destroyed or escaped
        if (enemies.size() == 0) {
          HighscoreManager hsm = new HighscoreManager();
          HighscoreManager.writeHSToFile((Integer) Playground.getGlobalFlag("points"),
              (Integer) Playground.getGlobalFlag("highscore"));
          this.doneLevel = true;
          logger.info("no enemies left, level done.");
        }

        // loop over enemies to check for collisions or suchlike ...
        LinkedList<GameObject> shots = collectObjects("simpleShot", true);
        for (GameObject e : enemies) {
          // if ego collides with enemy..
          if (s.collisionDetection(e)) {
            actionIfEgoCollidesWithEnemy(e, s);
          }

          // if shot collides with enemy
          for (GameObject shot : shots) {
            if (e.collisionDetection(shot)) {
              actionIfEnemyIsHit(e, shot);
            }
          }
        }

        // collecting hearts
        LinkedList<GameObject> collects = collectObjects("collect", false);
        for (GameObject c : collects) {
          if (s.collisionDetection(c)) {
            actionIfEgoCollidesWithCollect(c, s);
          }
        }

        // loop over enemies and, with a certain probability, launch an enemy shot for each one
        for (GameObject e : enemies) {
          createEnemyShot(e);
        }

        // check for collisions between ego object and enemy shots
        LinkedList<GameObject> eshots = collectObjects("enmyShot", true);
        for (GameObject eshot : eshots) {

          if (s.collisionDetection(eshot)) {
            logger.trace("COLLISION" + eshot.scol.get(0) + "/" + s.scol.get(0));
            actionIfEgoObjectIsHit(eshot, s);
          }
        }
      } // if substatus..
      else if (subStatus.equals("dying")) {
        Double t0 = (Double) getLevelFlag("t0");
        if (gameTime - t0 > DYING_INTERVAL) {
          LinkedList<GameObject> enemies = collectObjects("enemy", false);
          setLevelFlag("detailedStatus", "std");
          s.setActive(true);

          for (GameObject e : enemies) {
            logger.trace("activating" + e.getId());
            e.setActive(true);
          }
        }
      }

    } // if (gameStatus == "playing")


  }


  public boolean gameOver() {
    return lost;
  }


  public boolean levelFinished() {
    return doneLevel;
  }


  /**
   * calculates and returns the preferred size of the level (in pixel) for X-direction
   */
  public int preferredSizeX() {
    return CANVASX;
  }

  /**
   * calculates and returns the preferred size of the level (in pixel) for Y-direction
   */
  public int preferredSizeY() {
    return CANVASY;
  }


  /** Adds ego object and stars and displays startup message. Is called from applyGameLogic */
  protected void setupInitialState() {
    double gameTime = this.getGameTime();
    setLevelFlag("gameStatus", "starting");

    this.createStars();

    // set up ego object
    this.createEgoObject();

    // add text object to playground
    ObjectController ctrl = new LimitedTimeController(gameTime, 3.);
    GameObject readyText = new TextObject("ready?", this, getSizeX() / 2, 0, 0, 100,
        this.getStartupMessage(), 50, Color.RED).addController(ctrl);
    addObject(readyText);

  }


  /**
   * simply returns the text that should be displayed at level start
   * 
   * @return a string that is displayed at start. Should be not longer than 30 characters.
   */
  protected String getStartupMessage() {
    return "Get ready for level X!";
  }


  /**
   * returns a number representing the speed of an enemy object in X direction (pixels/second)
   * 
   * @return a positive value
   */
  protected double calcEnemySpeedX() {
    return SpaceInvadersLevel.ENEMYSPEEDX;
  }


  /**
   * returns a number representing the speed of an enemy object in Y direction (pixels/second)
   * 
   * @return a positive value
   */
  protected double calcEnemySpeedY() {
    return SpaceInvadersLevel.ENEMYSPEEDY;
  }

  /**
   * returns the maximum number of enemy instances (which are created at level start)
   * 
   * @return a positive value
   */
  protected int calcNrEnemies() {
    return SpaceInvadersLevel.NR_ENEMIES;
  }

  /**
   * returns the maximum number of collectables' instances (which are created at level start)
   * 
   * @return a positive value
   */
  protected int calcNrCollect() {
    return SpaceInvadersLevel.NR_COLLECT;
  }


  protected GameObject createEnemyShotObject(GameObject parentObject, String name,
      ObjectController limitedTimeController) {
    GameObject to =
        new TextObject(name, this, parentObject.getX(), parentObject.getY(), 0, ENEMYSHOTSPEED, "I",
            20, Color.YELLOW).generateColliders().addController(limitedTimeController);
    /*
     * // also possible: GameObject to = new RectObject(name, this, parentObject.getX(),
     * parentObject.getY(), 0, ENEMYSHOTSPEED, 4, 20,
     * Color.YELLOW).addController(limitedTimeController) ; to.generateColliders();
     */
    return to;
  }


  protected void createEnemyShot(GameObject e) {
    double gameTime = this.getGameTime();

    double PROB = calcEnemyShotProb();
    double diceThrow = Math.random();
    Integer nrEnemyShots = (Integer) (getLevelFlag("enemyShotCounter"));
    if (diceThrow < PROB) {
      setLevelFlag("enemyShotCounter", Integer.valueOf(++nrEnemyShots));

      LimitedTimeController limitedTimeController = new LimitedTimeController(gameTime, 10.);

      GameObject textObject =
          createEnemyShotObject(e, "enmyShot" + nrEnemyShots, limitedTimeController);

      addObject(textObject);
    }
  }


  /**
   * calculates and returns the probability that an enemy shots. Used by
   * {@link SpaceInvadersLevel#createEnemyShot(GameObject)}.
   * 
   * @return a positive value between 0 (no chance) to 1 (for sure).
   */
  double calcEnemyShotProb() {
    return 0.1 * this.getTimestep();
  }


  ObjectController createEnemyController() {
    return new EnemyController();
  }


  GameObject createSingleEnemy(String name, double x_enemy, double y_enemy, double vx_enemy,
      double vy_enemy, ObjectController enemyController, double gameTime) {

    GameObject tmp =
        new AnimatedGameobject(name, this, x_enemy, y_enemy, vx_enemy, vy_enemy, ENEMYSCALE,
            this.enemyAnim, gameTime, "loop").addController(enemyController).generateColliders();
    return tmp;
  }

  GameObject createSingleCollect(String name) {
    double gameTime = this.getGameTime();
    double cspeedy = 20.;
    double x_collect = Math.random() * this.canvasX;
    double y_collect = Math.random() * this.canvasY / 3;
    double vx_collect = 2 * (Math.random() - 0.5) * 0;
    double vy_collect = Math.random() * cspeedy;

    GameObject tmp = new AnimatedGameobject(name, this, x_collect, y_collect, vx_collect,
        vy_collect, 0.3, this.heartAnim, gameTime, "loop").generateColliders()
            .addController(new EnemyController());
    return tmp;
  }


  void createEnemies() {
    // create enemies
    double gameTime = this.getGameTime();
    double speedx = this.calcEnemySpeedX();
    double speedy = this.calcEnemySpeedY();
    for (int i = 0; i < this.calcNrEnemies(); i++) {
      double x_enemy = Math.random() * this.canvasX;
      double y_enemy = Math.random() * this.canvasY / 3;
      double vx_enemy = 2 * (Math.random() - 0.5) * speedx;
      double vy_enemy = Math.random() * speedy;

      ObjectController enemyController = createEnemyController();
      GameObject enemy = createSingleEnemy("enemy" + i, x_enemy, y_enemy, vx_enemy, vy_enemy,
          enemyController, gameTime);
      addObject(enemy);
    }
  }

  void createCollectables() {
    double gameTime = this.getGameTime();
    // create collectables
    for (int i = 0; i < this.calcNrCollect(); i++) {


      GameObject collect = createSingleCollect("collect" + i);

      addObject(collect);
    }
  }


  void createEgoObject() {
    // add ego to playground at lower bottom
    EgoController egoController =
        new CollisionAwareEgoController(SpaceInvadersLevel.EGORAD, this.laser);

    GameObject ego = new EgoObject("ego", this, canvasX / 2, canvasY - (EGORAD * 2), 0, 0, EGORAD)
        .addController(egoController).generateColliders();

    addObject(ego);
  }


  void createStars() {
    // add stars to playground
    for (int i = 1; i <= LEVEL2STARS; i++) {
      FallingStarController fsc = new FallingStarController();

      GameObject star = new FallingStar("star" + i, this, Math.random() * canvasX,
          Math.random() * 15, 0.0, Math.random() * STARSPEED, Color.WHITE, 1.).addController(fsc);

      addObject(star);
    }
  }


  void createExplosion(double gameTime, GameObject e, String basename, double interval,
      Color color) {
    // spawn a cloud of exploded shards
    for (int i = 0; i < NRSHARDS; i++) {
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVY();
      addObject(
          new FallingStar("shard" + gameTime + "/" + i, this, e.getX(), e.getY(), vx, vy, color, 2)
              .addController(new LimitedTimeController(gameTime, interval)));
    }
  }


  /**
   * implements game behavior if an enemy object is hit by a players' shot. It creates an explosion
   * effect, plays a sound and adds 200 points to the current score (and it removes the enemy object
   * and the shot object).
   * 
   * @param e enemy which was hit
   * @param shot the shot object that hit the enemy
   */
  void actionIfEnemyIsHit(GameObject e, GameObject shot) {

    double gameTime = this.getGameTime();
    createExplosion(gameTime, e, "shard", DYING_INTERVAL, Color.RED);

    Music.music(smash);

    // delete enemy
    deleteObject(e.getId());

    // delete shot
    deleteObject(shot.getId());

    // add to points counter
    Integer pts = (Integer) getGlobalFlag("points");
    setGlobalFlag("points", pts + 200);

  }

  /**
   * implements game behavior if ego object of player touches a collectable
   * {@link gameobjects.GameObject }. Here it adds one extra life and displays a text for a limited
   * time on screen "Extra life!!". The duration is set in constant
   * {@link SpaceInvadersLevel#EXPL_DURATION}.
   * 
   * 
   * @param collect item touched by ego
   * @param ego the players ego object
   */
  void actionIfEgoCollidesWithCollect(GameObject collect, GameObject ego) {
    double gameTime = this.getGameTime();

    if (this.getObject("bonustext") == null) {

      // spawn a bonus points object
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + collect.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + collect.getVY();


      LimitedTimeController bonusTextController =
          new LimitedTimeController(gameTime, SpaceInvadersLevel.EXPL_DURATION);
      GameObject bonusText = new TextObject("bonustext", this, collect.getX(), collect.getY(), vx,
          vy, "Extra life!!", 20, Color.YELLOW).addController(bonusTextController);

      addObject(bonusText);

      // delete collect
      deleteObject(collect.getId());

      // add to points counter
      Integer lives = (Integer) getGlobalFlag("egoLives");
      lives++;
      setGlobalFlag("egoLives", lives);
    }

  }

  /**
   * implements behaviour of game when ego object is touching an enemy. It displays a text "AUAA!!"
   * for a certain time on the game screen. Time span is defined as constant in
   * {@link SpaceInvadersLevel#BONUS_DURATION } .
   * 
   * @param enemy the enemy that was hit
   * @param ego the ego object of the player
   */
  void actionIfEgoCollidesWithEnemy(GameObject enemy, GameObject ego) {

    /**
     * set temporary text over ego. This object lives only for a short time. While it lives, further
     * collisions are ignored. Otherwise a single collision would result in a lot of deducted
     * points...
     */

    double gameTime = this.getGameTime();
    GameObject auaObj = this.getObject("AUA-EGO");

    if (auaObj == null) {
      addObject(new TextObject("AUA-EGO", this, ego.getX(), ego.getY() - 20, ego.getVX(),
          ego.getVY(), "AUAA!!", 10, Color.RED)
              .addController(new LimitedTimeController(gameTime, BONUS_DURATION)));
      // deduct points
      Integer pts = (Integer) getGlobalFlag("points");
      setGlobalFlag("points", pts - 500);
    }

  }

  /**
   * implements what happens if the eog object of player is hit by a shot. It removes the shot from
   * screen, reduces lives by 1, removes the ego and end current playing.
   * 
   * @param eshot the shot object that hits the ego
   * @param ego the ego object of the player
   */
  void actionIfEgoObjectIsHit(GameObject eshot, GameObject ego) {
    logger.debug("collision of " + eshot.getId() + " and " + ego.getId());

    double gameTime = this.getGameTime();
    this.deleteObject(eshot.getId());

    Integer oldLives = (Integer) getGlobalFlag("egoLives");
    int newLives = oldLives - 1; 
    setGlobalFlag("egoLives", newLives);
    logger.debug("set egoLives to " + newLives + " (was " + oldLives + ")");

    if (newLives <= 0) {
      lost = true;
      HighscoreManager.writeHSToFile((Integer) Playground.getGlobalFlag("points"),
          (Integer) Playground.getGlobalFlag("highscore"));

    }

    LinkedList<GameObject> eshots = collectObjects("enmyShot", true);
    for (GameObject _eshot : eshots) {
      deleteObject(_eshot.getId());
    }
    setLevelFlag("detailedStatus", "dying");
    setLevelFlag("t0", gameTime);
    ego.setActive(false);
    createExplosion(gameTime, ego, "egoexp", DYING_INTERVAL, Color.WHITE);
    LinkedList<GameObject> enemies = collectObjects("enemy", false);
    for (GameObject enemy : enemies) {
      enemy.setY(0);
      enemy.setActive(false);
    }

  }



}
