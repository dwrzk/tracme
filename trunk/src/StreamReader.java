////Reference: javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4
////Author: Michael C. Daconta
///import java.util.*;
import java.io.*;

public class StreamReader extends Thread
{

   public StreamReader( InputStream is, String type )
   {
      this( is, type, null );
   }

   public StreamReader( InputStream is, String type, OutputStream redirect )
   {
      this.is = is;
      this.type = type;
      this.os = redirect;

      out = "";
   }

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
            ///System.out.println( type + ">" + line );
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

   public String getOutputStr()
   {
      return out;
   }

   InputStream is;
   String type;
   OutputStream os;

   String out; // A string copy of the output data.
}
