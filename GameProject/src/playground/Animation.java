package playground;


import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Animation {

  public LinkedList<String> fileList = null;
  public LinkedList<Double> showtimeList = null;
  public LinkedList<BufferedImage> imageList = null;

  private static Logger logger = LogManager.getLogger(Animation.class);

  public Animation(String datName) {

    Scanner scanner;
    this.fileList = new LinkedList<String>();
    this.showtimeList = new LinkedList<Double>();
    this.imageList = new LinkedList<BufferedImage>();

    try {
      scanner = new Scanner(new File(datName), "UTF-8");

      scanner.useLocale(Locale.GERMANY);

      String zeile;
      double zeit;
      int it = 0;

      while (scanner.hasNext()) {
        if (scanner.hasNextDouble()) {
          zeit = scanner.nextDouble();
          showtimeList.add(zeit);
        } else {
          zeile = scanner.next();
          Path basePath = Paths.get(datName);
          String file = basePath.getParent().toString() + "/" + zeile;
          fileList.add(file);
          try {
            this.imageList.add(ImageIO.read(new File(file)));
            logger.info("img added " + file);

          } catch (IOException e) {
            logger.warn(file + " not found!!");
          }

          it++;
          logger.trace(basePath.getParent().toString() + "/" + zeile);
        }
      }

      scanner.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public LinkedList<String> getFileList() {
    return this.fileList;
  }

  public LinkedList<Double> getShowtimeList() {
    return this.showtimeList;
  }

  public LinkedList<BufferedImage> getImageList() {
    return this.imageList;
  }

}
