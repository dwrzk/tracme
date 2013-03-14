import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineEvent.Type;

/**
 * http://stackoverflow.com/questions/577724/trouble-playing-wav-in-java/577926#
 * 577926
 * 
 * @author James Humphrey
 * 
 */
public class AlertSound implements LineListener
{
    @Override
    public synchronized void update( LineEvent event )
    {
        Type eventType = event.getType();
        if( eventType == Type.STOP || eventType == Type.CLOSE )
        {
            donePlaying = true;
            notifyAll();
        }
    }

    public synchronized void waitUntilDone() throws InterruptedException
    {
        while( !donePlaying )
        {
            wait();
        }
    }

    public static void playClip( File clipFile )
    {
        /*AudioInputStream audioInputStream = AudioSystem
                .getAudioInputStream( clipFile );
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener( this );
            clip.open( audioInputStream );
            try
            {
                clip.start();
                this.waitUntilDone();
            }
            finally
            {
                clip.close();
            }
        }
        finally
        {
            audioInputStream.close();
        }*/
    }

    private boolean donePlaying = false;
}
