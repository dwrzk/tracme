import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

///homeandlearn.co.uk/java/write_to_textfile.html
public class WriteFile
{
   public WriteFile( String path )
   {
      this.path = path;
   }

   public WriteFile( String path, boolean append )
   {
      this.path = path;
      this.append = append;
   }

   public void writeToFile( String text )
   {
      try
      {
         FileWriter write = new FileWriter( path, append );
         PrintWriter print_line = new PrintWriter( write );
         print_line.print( text );
         print_line.close();
      }
      catch( IOException e )
      {
         e.printStackTrace();
         System.out.println( "Failed to write to file " + path );
      }
   }

   private String path;
   private boolean append = false;
}
