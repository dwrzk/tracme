import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * A Grid class that draws a 2 dimensional array of cells and is capable of
 * shading them in for the user to determine which cells are active.
 * 
 * @author James Humphrey
 */
public class Grid extends JFrame
{

    /**
     * Represents a cell that is located within the grid. Can be set to shaded
     * or blinking mode to signify importance.
     * 
     * @author James Humphrey
     */
    public class Cell implements ActionListener
    {
        /**
         * Initialize a default cell object.
         * 
         * @param x
         *            The X location of the cell.
         * @param y
         *            The Y location of the cell.
         */
        public Cell( int x, int y )
        {
            shaded = false;

            this.x = x;
            this.y = y;

            shaded = false;
            delay = 0;
        }

        public boolean isShaded()
        {
            return shaded;
        }

        public void setShaded( boolean shade )
        {
            shaded = shade;
        }

        public void actionPerformed( ActionEvent evt )
        {
            // Reverse the polarity of turning on/off the shading function.
            shaded = !shaded;

            // We need to repaint to add/remove the square.
            repaint();
        }

        public void setBlinking()
        {
            shaded = true;
            delay = 500;

            blinkTimer = new Timer( delay, this );
            blinkTimer.start();
        }

        public void stopBlinking()
        {
            blinkTimer.stop();
            blinkTimer = null;
        }

        private boolean shaded; // Indicates if this cell needs to be shaded in.

        private Timer blinkTimer; //
        private int delay; // The delay to use for switching between blinking
                           // states.

        private int x, y;
    }

    /**
     * Create a new grid with the specified number of cells in the x and y
     * direction.
     * 
     * @param x
     *            The number of columns in the grid.
     * @param y
     *            The number of rows in the grid.
     */
    public Grid( int x, int y )
    {
        super( "Grid" );

        // Make sure we have a valid grid range.
        if( x <= 0 || y <= 0 )
        {
            System.out.println( "x and y must both be greater than 0" );
            this.dispose();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout( null );
        mainPanel.setLocation( 0, 0 );
        mainPanel.setSize( 600, 680 );
        add( mainPanel );

        // setBounds(100,100,300,100);
        setSize( 800, 700 );
        // setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        // Container con = this.getContentPane();
        setLayout( null );
        setVisible( true );

        setResizable( true );

        cells = new Cell[y][x];
        for( int i = 0; i < y; i++ )
        {
            for( int j = 0; j < x; j++ )
            {
                cells[i][j] = new Cell( j, i );
            }
        }

        columns = x;
        rows = y;
        cellLength = 50;
    }

    /**
     * Called every time the frame panel needs to be painted. This draws the
     * rows and columns of the grid and any shaded cells that are designated.
     */
    public void paint( Graphics g )
    {
        // /super.paintComponents( g );

        Graphics2D graphics = (Graphics2D) mainPanel.getGraphics();

        g.clearRect( 0, 0, getWidth(), getHeight() );

        // Draw the vertical lines for the grid.
        for( int i = 0; i < ( columns + 1 ) * cellLength; i += cellLength )
        {
            graphics.drawLine( i, 0, i, columns * cellLength );
        }

        // Draw the horizontal lines for the grid.
        for( int j = 0; j < ( rows + 1 ) * cellLength; j += cellLength )
        {
            graphics.drawLine( 0, j, rows * cellLength, j );
        }

        // Shade any cells that are marked as shaded.

        for( int i = 0; i < getNumRows(); i++ )
        {
            for( int j = 0; j < getNumColumns(); j++ )
            {
                if( cells[i][j].isShaded() )
                {
                    graphics.setColor( Color.blue );
                    // Draw the rectangle( topleftX, topleftY, width, height ).
                    graphics.fillRect( j * cellLength, i * cellLength,
                            cellLength, cellLength );
                }

                // Draw the text indicating the cell location.
                graphics.setColor( Color.black );
                graphics.drawString( "(" + ( j + 1 ) + ","
                        + ( 0 - ( ( i + 1 ) - ( rows + 1 ) ) ) + ")",
                        cellLength * j + cellLength / 4, cellLength * i
                                + cellLength / 2 );
            }
        }

    }

    /**
     * Gets the number of rows in the grid.
     * 
     * @return The number of rows (y direction) in the grid.
     */
    public int getNumRows()
    {
        return cells.length;
    }

    /**
     * Gets the number of columns in the grid.
     * 
     * @return The number of columns (x direction) in the grid.
     */
    public int getNumColumns()
    {
        return cells[0].length;
    }

    public Cell getCell( int x, int y )
    {
        int arrayX = convertCoordToArrayX( x );
        int arrayY = convertCoordToArrayY( y );
        if( arrayY + 1 > getNumRows() || arrayX + 1 > getNumColumns() )
        {
            System.out.println( "Cell row/column out of bounds (" + arrayX
                    + ", " + arrayY + ")" );
            return null;
        }

        System.out.println( "Debug " + arrayX + " " + arrayY );

        return cells[arrayY][arrayX];
    }

    /**
     * Disable any blinking cells.
     */
    public void clearBlinkingCells()
    {
        for( int i = 0; i < rows; i++ )
        {
            for( int j = 0; j < columns; j++ )
            {
                cells[i][j].stopBlinking();
            }
        }
    }

    /**
     * Maps a grid coordinate into the location of the cell array.
     * 
     * @param x
     *            Desired grid coordinate to convert.
     * @return The mapped x grid coordinate value into the cell array.
     */
    private int convertCoordToArrayX( int x )
    {
        return x - 1;
    }

    /**
     * Maps a grid coordinate into the location of the cell array.
     * 
     * @param y
     *            Desired grid coordinate to convert.
     * @return The mapped y grid coordinate value into the cell array.
     */
    private int convertCoordToArrayY( int y )
    {
        return y - 1 + rows - 1;
    }

    private JPanel mainPanel;
    private Cell[][] cells;
    private int columns;
    private int rows;
    private int cellLength; // The length of each cell (in pixels) that gets
                            // drawn.

    private static final long serialVersionUID = 42L;
}
