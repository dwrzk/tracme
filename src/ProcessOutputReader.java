/**
 * Manages the calling of a process, reads the output, and returns a string of
 * the program written to stdout.
 * 
 * @author James Humphrey
 */
public class ProcessOutputReader
{
    public ProcessOutputReader()
    {

    }

    /**
     * Creates a stream reader object and reads all stdout output of a process
     * until it terminates.
     * 
     * @param exec
     *            The command line execution and arguments of the program.
     * @return A string of everythng the process printed to stdout.
     */
    public String runProgram( String exec )
    {
        StreamReader scanOutputReader;
        try
        {
            // Get the current runtime and execute the
            // process.
            Runtime rt = Runtime.getRuntime();
            Process scanProc = rt.exec( exec );

            // Read the output produced by the process.
            scanOutputReader = new StreamReader( scanProc.getInputStream() );

            // Start the output thread.
            scanOutputReader.start();

            // Tell the main thread to wait for the thread to finish before
            // continuing on with the analysis.
            scanOutputReader.join();

            // Check if the process encountered any errors.
            int scanExitVal = scanProc.waitFor();
            System.out.println( "\"" + exec + "\" Exit Value: " + scanExitVal );

            if( scanExitVal == 255 )
            {
                return runProgram( exec );
            }

        }
        catch( Throwable t )
        {
            System.out.println( "Failed execution of " + exec );
            t.printStackTrace();
            return "";
        }

        return scanOutputReader.getOutputStr();
    }
}
