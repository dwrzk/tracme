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
public class Grid extends JFrame // /implements ActionListener, ChangeListener
{

    /**
     * Represents a cell that is located within the grid. Can be set to shaded
     * or blinking mode to signify importance.
     * 
     * @author James Humphrey
     */
    public class Cell
    {
        public Cell()
        {
            shaded = false;
            blink = false;
        }

        public boolean isShaded()
        {
            return shaded;
        }

        public void setShaded( boolean shade )
        {
            shaded = shade;
        }

        public boolean isBlinking()
        {
            return blink;
        }

        public void setBlink( boolean blink )
        {
            this.blink = blink;
        }

        private boolean shaded; // Indicates if this cell needs to be shaded in.
        private boolean blink; // Indicates if this cell should be blinking
                               // because it is an active cell.
    }

    private class BlinkingCell implements ActionListener
    {
        public BlinkingCell( int x, int y )
        {
            this.x = x;
            this.y = y;

            shadeOn = true;
            delay = 1000;

            blinkTimer = new Timer( delay, this );
            blinkTimer.start();

        }

        public void actionPerformed( ActionEvent evt )
        {
            shadeOn = !shadeOn;
        }

        private Timer blinkTimer;
        private int delay;
        private boolean shadeOn; // Indicates whether the shading is currently
                                 // on or
        // off.
        private int x, y;

        // private static final int BLINK_DELAY = 1000;

    }

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
                cells[i][j] = new Cell();
            }
        }

        columns = x;
        rows = y;
        cellLength = 50;
        blinkCell = null;
    }

    public void paint( Graphics g )
    {
        // /super.paintComponents( g );

        Graphics2D graphics = (Graphics2D) mainPanel.getGraphics();

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
        graphics.setColor( Color.blue );
        for( int i = 0; i < getNumRows(); i++ )
        {
            for( int j = 0; j < getNumColumns(); j++ )
            {
                if( cells[i][j].isShaded() )
                {
                    // Draw the rectangle( topleftX, topleftY, width, height ).
                    graphics.fillRect( j * cellLength, i * cellLength,
                            cellLength, cellLength );
                }
            }
        }

    }

    public int getNumRows()
    {
        return cells.length;
    }

    public int getNumColumns()
    {
        return cells[0].length;
    }

    public Cell getCell( int row, int col )
    {
        if( row + 1 > getNumRows() || col + 1 > getNumColumns() )
        {
            System.out.println( "Cell row/column out of bounds (" + row + ", "
                    + col + ")" );
            return null;
        }

        return cells[row][col];
    }

    public void setBlinkingCell( int x, int y )
    {
        blinkCell = new BlinkingCell( convertCoordToArrayX( x ),
                convertCoordToArrayY( y ) );
    }

    public void clearBlinkingCell()
    {
        blinkCell = null;
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

    private BlinkingCell blinkCell;

    private static final long serialVersionUID = 42L;
}
