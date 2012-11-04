import java.io.*;

/**
 * A stream reader on a separate thread that is capable of reading the output of
 * programs that are run on the command line (stdout). This class is based on
 * code written by Michael C. Daconta at
 * javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4.
 * 
 * @author James Humphrey
 */
public class StreamReader extends Thread
{
   /**
    * Creates and initializes the stream reader with the associated input
    * stream.
    * 
    * @param is
    *           The input stream to read from and record
    */
   public StreamReader( InputStream is )
   {
      this.is = is;
      out = "";
   }

   /**
    * The main thread that records everything on the output stream until the
    * stream is exhausted.
    */
   public void run()
   {
      try
      {
         PrintWriter pw = null;
         if( os != null )
         {
            pw = new PrintWriter( os );
         }

         InputStreamReader isr = new InputStreamReader( is );
         BufferedReader br = new BufferedReader( isr );
         String line = null;
         while( ( line = br.readLine() ) != null )
         {
            if( pw != null )
            {
               pw.println( line );
            }
            out += line + "\n";
         }

         if( pw != null )
         {
            pw.flush();
         }
      }
      catch( IOException ioe )
      {
         ioe.printStackTrace();
      }
   }

   /**
    * Accessor method for the string that contains the output of the stream.
    * 
    * @return A string copy of the output data
    */
   public String getOutputStr()
   {
      return out;
   }

   private InputStream is; ///
   private OutputStream os; ///
   private String out; // A string copy of the output data.
}
