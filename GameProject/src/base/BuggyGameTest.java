package base;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import playground.Level1;
import playground.Level2;
import playground.Level3;
import playground.Level4;


/**
 * Tests {@link BuggyGame} for
 * <ol>
 * <li>loading correctly 4 levels {@link playground.Level1} {@link playground.Level2}
 * {@link playground.Level3} and {@link playground.Level4}
 * </ol>
 *
 *
 */
class BuggyGameTest {

  BuggyGame myGame;

  @BeforeEach
  void setUp() throws Exception {
    myGame = new BuggyGame();

  }

  @AfterEach
  void tearDown() throws Exception {
    // nothing to do afterwards
  }

  @Test
  void testNumberOfLevelsIsFour() {
    assertTrue("levels Array has two entries", myGame.levels != null && myGame.levels.size() == 4);
  }

  @Test
  void testCorrectFourLevelsOrder() {
    assertTrue("levels Array has four entries", myGame.levels != null && myGame.levels.size() == 4);
    assertTrue("first level is Level1",
        myGame.levels.get(0).getClass().equals(new Level1().getClass()));
    assertTrue("second level is Level2",
        myGame.levels.get(1).getClass().equals(new Level2().getClass()));
    assertTrue("third level is Level3",
        myGame.levels.get(2).getClass().equals(new Level3().getClass()));
    assertTrue("fourth (last) level is Level4",
        myGame.levels.get(3).getClass().equals(new Level4().getClass()));
  }


}
