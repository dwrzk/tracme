
/**
 * This interface has methods that both the mobile sampling gui and the PC/Mac
 * sampling frame will use. Most methods are used to update fields of the sample program
 * 
 * @author Kwaku Farkye
 *
 */
public interface SamplingGUI {
	
	/**
	 *  This initializes the components that are a part of the GUI 
	 *  
	 */
	public void initComponents();
	
	/**
	 *  This updates the cell location about to be sampled
	 *  
	 */
	public void updateXLoc();
	
	/**
	 *  This updates the cell location about to be sampled
	 *  
	 */
	public void updateYLoc();
	
	/**
	 *  This updates the number of samples for the next run
	 *  
	 */
	public void updateSamples();
	 
	 /**
	  * This updates the amount of cells in x direction.
	  * 
	  */
	public void updateGridX();
	
	/**
	  * This updates the amount of cells in y direction
	  * 
	  */
	public void updateGridY();
	
	/**
	 * Run the cell sample, collecting WiFi signal information in specified cell location
	 * This method should be called when there is a "Sample" or "Run" action
	 * (for example: when the "Sample" button is hit)
	 * 
	 */
	public void runEvent();

	/**
	 * Save the results of sampling to a file.
	 * This method should be called when there is a "save" action.
	 * (for example: when the "Save" button is hit)
	 * 
	 */
	public void saveResults();

}
