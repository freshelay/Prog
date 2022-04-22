package playground;

import static org.junit.Assert.assertTrue;
import java.awt.Color;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import gameobjects.EgoObject;
import gameobjects.GameObject;
import gameobjects.RectObject;

/**
 * Tests {@link SpaceInvadersLevel} for
 * <ol>
 * <li>calcEnemySpeedX() returns the same value as constant SpaceInvadersLevel.ENEMYSPEEDX
 * <li>calcEnemySpeedY() returns the same value as constant SpaceInvadersLevel.ENEMYSPEEDY
 * <li>calcNrEnemies() returns the same value as constant SpaceInvadersLevel.NR_ENEMIES
 * <li>actionIfEnemyIsHit() adds 200 points to score
 * <li>actionIfEgoObjectIsHit() reduces number of lives (egoLives)
 * </ol>
 * @author jkonert
 *
 */
class SpaceInvadersLevelTest {

  private static SpaceInvadersLevel myLevel;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    myLevel = new SpaceInvadersLevel();
    SpaceInvadersLevel.setGlobalFlag("egoLives",  5);
    SpaceInvadersLevel.setGlobalFlag("points",  500);
    SpaceInvadersLevel.setGlobalFlag("highscore", 5000);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    // nothing 
  }

  @Test
  void testCalcEnemySpeedX() {
    assertTrue("EnemySpeedX is as in SpaceInvadersLevel defined", myLevel.calcEnemySpeedX() == SpaceInvadersLevel.ENEMYSPEEDX);
  }

  @Test
  void testCalcEnemySpeedY() {
    assertTrue("EnemySpeedY is as in SpaceInvadersLevel defined", myLevel.calcEnemySpeedY() == SpaceInvadersLevel.ENEMYSPEEDY);
  }

  @Test
  void testCalcNrEnemies() {
    assertTrue("NrOfEnemies is as in SpaceInvadersLevel defined", myLevel.calcNrEnemies() == SpaceInvadersLevel.NR_ENEMIES);
  }

  
  @Test
  void testActionIfEnemyIsHitPointsUp() {
    Integer numPointsBefore = (Integer)myLevel.getGlobalFlag("points");
    GameObject dummyShot = new RectObject("shot1", myLevel, 0,0,0,0, 12, 12, Color.WHITE);
    GameObject dummyEnemy = new RectObject("ego1", myLevel, 0,0,0,0, 12, 12, Color.BLACK);
    myLevel.addObject(dummyShot);
    myLevel.addObject(dummyEnemy);
    myLevel.actionIfEnemyIsHit(dummyEnemy, dummyShot);;  // this is the call under test
    Integer numPointsAfter = (Integer)myLevel.getGlobalFlag("points");  // changed?
    assertTrue("numPoints is up +200 after EnemyIsHit", numPointsAfter == numPointsBefore + 200); // points are set +200 , check.
  }

  @Test
  void testActionIfEgoObjectIsHitLivesDown() {
    Integer numLivesBefore = (Integer)myLevel.getGlobalFlag("egoLives");
    GameObject dummyShot = new RectObject("shot1", myLevel, 0,0,0,0, 12, 12, Color.RED);
    GameObject dummyEgo = new EgoObject("ego1", myLevel, 0,0,0,0, 5);
    myLevel.addObject(dummyShot);
    myLevel.actionIfEgoObjectIsHit(dummyShot, dummyEgo);  // this is the call under test
    Integer numLivesAfter = (Integer)myLevel.getGlobalFlag("egoLives");  // changed?
    assertTrue("numLives is reduced by one ifEgoIsHit", numLivesAfter == numLivesBefore - 1); // lives is reduced by one
    
  }

}
