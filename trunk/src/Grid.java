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
            this.x = x;
            this.y = y;

            shaded = false;
            permanentShade = false;
            blinkTimer = null;
            delay = 0;

            needsPaintUpdate = true;
        }

        /**
         * Determines if the cell is currently in a shaded state.
         * 
         * @return true if the cell is shaded.
         */
        public boolean isBlinkingShaded()
        {
            return shaded;
        }

        public boolean isShaded()
        {
            return permanentShade;
        }

        /**
         * Sets the cell's shade mode.
         * 
         * @param shade
         *            true if the cell should be shaded, false otherwise.
         */
        public void setShaded( boolean shade )
        {
            permanentShade = shade;

            needsPaintUpdate = true;
        }

        /**
         * Called every time the blinker timer goes off, indicating a switch in
         * the blinking state.
         */
        public void actionPerformed( ActionEvent evt )
        {
            // Reverse the polarity of turning on/off the shading function.
            shaded = !shaded;

            needsPaintUpdate = true;

            // We need to repaint to add/remove the square.
            repaint();
        }

        /**
         * Turns on blinking for the current cell.
         */
        public void setBlinking()
        {
            shaded = true;
            delay = 300;
            needsPaintUpdate = true;

            blinkTimer = new Timer( delay, this );
            blinkTimer.start();
        }

        /**
         * Stops the blinking timer which prevents the cell from flashing color
         * for attention.
         */
        public void stopBlinking()
        {
            if( blinkTimer != null )
            {
                blinkTimer.stop();
                blinkTimer = null;
            }

            shaded = false;

            needsPaintUpdate = true;
        }

        /**
         * Determines if the cell needs to be painted because of a recent
         * update.
         * 
         * @return true if the cell should be repainted.
         */
        public boolean needsPaintUpdate()
        {
            return needsPaintUpdate;
        }

        /**
         * Indicates that the cell has just been painted and no further painting
         * is required until the next update.
         */
        public void setPaintWasUpdated()
        {
            needsPaintUpdate = false;
        }

        /** Indicates if this cell needs to be shaded in. */
        private boolean shaded;

        /** Indicates if this cell should be permanently shaded. */
        private boolean permanentShade;

        /** Timer used for alternating the blink cycle in this cell. */
        private Timer blinkTimer;

        /** The delay to use for switching between blinking states. */
        private int delay;

        /** The x/y coordinates of the cell in the grid. */
        private int x, y;

        /** Indicates if we need to repaint this cell. */
        private boolean needsPaintUpdate;
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

        mainPanel = new JPanel();
        mainPanel.setLayout( null );
        mainPanel.setLocation( 0, 0 );
        mainPanel.setSize( 600, 680 );
        add( mainPanel );

        // setBounds(100,100,300,100);
        setLocation( 800, 50 );
        setSize( 800, 700 );
        // setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        // Container con = this.getContentPane();
        setLayout( null );
        setVisible( true );

        setResizable( true );

        setGrid( x, y );
    }

    /**
     * Called every time the frame panel needs to be painted. This draws the
     * rows and columns of the grid and any shaded cells that are designated.
     */
    public void paint( Graphics g )
    {
        Graphics2D graphics = (Graphics2D)mainPanel.getGraphics();

        // Draw the vertical lines for the grid.
        for( int i = 0; i < ( columns + 1 ) * cellLength; i += cellLength )
        {
            graphics.drawLine( i, 0, i, rows * cellLength );
        }

        // Draw the horizontal lines for the grid.
        for( int j = 0; j < ( rows + 1 ) * cellLength; j += cellLength )
        {
            graphics.drawLine( 0, j, columns * cellLength, j );
        }

        // Shade any cells that are marked as shaded.
        graphics.setFont( new Font( null, Font.BOLD, 14 ) );
        for( int i = 0; i < getNumRows(); i++ )
        {
            for( int j = 0; j < getNumColumns(); j++ )
            {
                // We only need to repaint cells that were recently updated.
                if( cells[i][j].needsPaintUpdate() )
                {

                    if( cells[i][j].isShaded() )
                    {
                        graphics.setColor( Color.GREEN );
                        // Draw the rectangle(topleftX,topleftY,width,height).
                        graphics.fillRect( j * cellLength + 2, i * cellLength
                                + 2, cellLength - 4, cellLength - 4 );
                    }
                    else if( cells[i][j].isBlinkingShaded() )
                    {
                        graphics.setColor( Color.blue );
                        // Draw the rectangle(topleftX,topleftY,width,height).
                        graphics.fillRect( j * cellLength + 2, i * cellLength
                                + 2, cellLength - 4, cellLength - 4 );
                    }
                    else
                    {
                        graphics.clearRect( j * cellLength + 2, i * cellLength
                                + 2, cellLength - 4, cellLength - 4 );
                    }

                    // Draw the text indicating the cell location.
                    graphics.setColor( Color.black );
                    graphics.drawString( "(" + ( j + 1 ) + ","
                            + ( 0 - ( ( i + 1 ) - ( rows + 1 ) ) ) + ")",
                            cellLength * j + cellLength / 6, cellLength * i
                                    + cellLength / 2 );

                    // Indicate to the cell that we just repainted it.
                    cells[i][j].setPaintWasUpdated();
                }
            }
        }

        // Draw the direction vector (N,E,S,W).
        // graphics.drawString( "", x, y );
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

    /**
     * Gets the cell at the specified location in the grid.
     * 
     * @param x
     *            The x coordinate of the cell in the grid.
     * @param y
     *            The y coordinate of the cell in the grid.
     * @return the cell object that is contained within the grid.
     */
    public Cell getCell( int x, int y )
    {
        System.out.println( "grid.getCell(" + x + ", " + y + ")" );
        int arrayX = convertCoordToArrayX( x );
        int arrayY = convertCoordToArrayY( y );
        if( arrayY + 1 > getNumRows() || arrayX + 1 > getNumColumns() )
        {
            System.out.println( "Cell row/column out of bounds (" + arrayX
                    + ", " + arrayY + ")" );
            return null;
        }

        return cells[arrayY][arrayX];
    }

    /**
     * Disable all blinking cells.
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
        return Math.abs( y - rows );
    }

    /**
     * Initializes the grid to be the size indicated by the parameters.
     * 
     * @param x
     *            The number of columns in the grid.
     * @param y
     *            The number of rows in the grid.
     */
    public void setGrid( int x, int y )
    {
        // Make sure we have a valid grid range.
        if( x <= 0 || y <= 0 )
        {
            System.out.println( "x and y must both be greater than 0" );
            return;
        }

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
        oneBlink = true;

        repaint();
    }

    /**
     * Sets the specified cell to a blinking state mode. It will automatically
     * clear all other blinking cells if only one cell is allowed to blink at a
     * time.
     * 
     * @param x
     *            The x coordinate of the cell in the grid.
     * @param y
     *            The y coordinate of the cell in the grid.
     */
    public void setBlinking( int x, int y )
    {
        if( oneBlink )
        {
            clearBlinkingCells();
        }
        getCell( x, y ).setBlinking();
    }

    /** The main panel where all of the graphics drawing will take place. */
    private JPanel mainPanel;

    /** A double array of all the cells in the grid. */
    private Cell[][] cells;

    /** The number of columns in the grid (x values). */
    private int columns;

    /** The number of rows in the grid (y values). */
    private int rows;

    /** The length of each cell (in pixels) that gets drawn. */
    private int cellLength;

    /** Indicates if only one cell can blink at a time. */
    private boolean oneBlink;

    /**
     * The class version number used during deserialization to verify that the
     * loaded class is compatible with serialization
     */
    private static final long serialVersionUID = 42L;
}
