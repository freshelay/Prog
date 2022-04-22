package playground;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Music {
  private static final float volume = 0.02f; // scale 0 silence, 1 no change, 2 double. (linear).

  public static synchronized void music(File track) {

    final File trackname = track;

    new Thread(new Runnable() {

      @Override
      public void run() {
        while (true) {

          try {

            Clip clip = AudioSystem.getClip();
            AudioInputStream inputstream = AudioSystem.getAudioInputStream(trackname);
            clip.open(inputstream);
            FloatControl volumeControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeControl != null) {
              volumeControl.setValue(20f * (float) Math.log10(volume));
            }
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1);

          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      }

    }).start();

  }

}
