package playground;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class SaveGame {

  private static String datnam = "aktuellerSpielzustand.ser";
  private static Logger logger = LogManager.getLogger(SaveGame.class);


  @SuppressWarnings("unchecked")
  public static void save() {

    // gameobjects.AnimatedGameobject
    ArrayList<String> objArrList = null;
    ObjectInputStream in = null;
    try {
      in = new ObjectInputStream(new FileInputStream(datnam));
      objArrList = (ArrayList<String>) in.readObject();
    } catch (FileNotFoundException ex) {
      logger.warn("Savegame file not (yet) existing!");
    } catch (Exception ex) {
      logger.error(ex);
    } finally {
      try {
        if (in != null)
          in.close();
      } catch (IOException e) {
      }
    }
    if (objArrList == null)
      objArrList = new ArrayList<String>();

    objArrList.add(new String("ArrayListgroesse: " + objArrList.size()));
    logger.debug(objArrList);

    ObjectOutputStream aus = null;
    try {
      aus = new ObjectOutputStream(new FileOutputStream(datnam));
      aus.writeObject(objArrList);
    } catch (IOException ex) {
      logger.error(ex);
    } finally {
      try {
        if (aus != null) {
          aus.flush();
          aus.close();
        }
      } catch (IOException e) {
      }
    }
  }

}
