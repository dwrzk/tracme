import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class APTable
{
   public APTable()
   {
      // Create and initialize the AP table to 0.
      aps = new ArrayList< AccessPoint >();
      aps.clear();
   }

   public void createTable( String tableName )
   {
      ///File newTableFile = new File( tableName );
   }

   public void addAPToTable( AccessPoint newAp )
   {
      ;
   }

   public void mapAPsToID( ArrayList< AccessPoint > apList, boolean autoIncludeNew )
   {
      for( int i = 0; i < apList.size(); i++ )
      {
         for( int j = 0; j < aps.size(); j++ )
         {
            if( apList.get( i ).getBSSID().equals( aps.get( j ).getBSSID() ) )
            {
               // Set the ID of the mapped AP in the list.
               apList.get( i ).setID( aps.get( j ).getID() );
               break;
            }
         }

         // Check if the AP was not found in the table.
         if( apList.get( i ).getID() < 0 )
         {
            ///System.out.println( "The AP was not found in the table" );

            // Check if the AP should be added to the table.
            if( autoIncludeNew )
            {
               addAPToTable( apList.get( i ) );
            }
            else
            {
               // Prompt the user if he wants the program to save the access point to the table.///
            }
         }
      }
   }

   ///This function assumes the data is in the correct format as it does no error checking on format.
   public int loadTable( String tableName )
   {
      // Stream to read the table file.
      FileInputStream fin;

      try
      {
         // Open the input stream.
         fin = new FileInputStream( tableName );

         // Create a scanner on the input stream so we can parse the file data.
         Scanner tabScan = new Scanner( fin );

         // Clear the previously allocated AP table.
         aps.clear();

         // Loop through each line in the file which contains AP mapping data.
         while( tabScan.hasNext() )
         {
            // Create a new access point that we will be adding to our table.
            AccessPoint newAp = new AccessPoint();

            // Read the id number used for mapping APs.
            newAp.setID( tabScan.nextInt() );

            // Read the unique BSSID of the AP that will be used to map to an id value.
            newAp.setBSSID( tabScan.next() );

            // Add the AP to the table.
            aps.add( newAp );
         }

         // Close the input stream.
         fin.close();
      }
      catch( IOException e )
      {
         System.out.println( "Unable to read from file " + tableName + ". Do you want to create a file by that name?" );
         return -1;
      }
      catch( Exception e )
      {
         System.out.println( "Bad read - generic unkown error - try checking file format" );
         return -1;
      }

      // Return 0 on success.
      return 0;
   }

   public ArrayList< AccessPoint > getAPTable()
   {
      return aps;
   }

   private ArrayList< AccessPoint > aps; // A list of APs stored in the table.

}
