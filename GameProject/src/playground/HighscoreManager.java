package playground;

import java.io.*;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


// Klasse um Highscore umzusetzen
public class HighscoreManager {
  Scanner s;
  private static Logger logger = LogManager.getLogger(HighscoreManager.class);

  public HighscoreManager() {
    try {
      File f = new File("./highscore.txt");
      if (!f.exists()) {
        logger.warn("WARNING: Highscore file was not found and reset");
        writeHSToFile(0, -1);
      }
      s = new Scanner(f);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public int readHSFromFile() {
    if (s.hasNext()) {
      int highscore = s.nextInt();
      return highscore;
    }
    return 0;
  }

  public static void writeHSToFile(Integer pts, Integer highscore) {
    String highscore2 = String.valueOf(pts);
    BufferedWriter bw;
    try {
      if (pts > highscore) {
        FileWriter fw = new FileWriter("./highscore.txt");
        bw = new BufferedWriter(fw);
        bw.write(highscore2);
        bw.close();
        logger.info("Highscore file was opened and saved score: " + highscore2);
      }
    } catch (IOException e) {
      logger.error("File for Highscore not writeable! Score lost.");
    }
  }

  public void closeFile() {
    s.close();
  }
}
