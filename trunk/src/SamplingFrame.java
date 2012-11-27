import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This file sets up the gui for SampleProgram.java
 * User will be able to set the grid location, number of samples
 * Sample file, view the sample data, and view GPS information using this GUI
 * @author Kwaku Farkye
 */
 public class SamplingFrame extends JFrame implements ActionListener {
 
    private JPanel mainPanel, fieldsPanel, bottomPanel;
	private JButton run = new JButton("Sample");
	private JButton updateFields = new JButton("Update");
	private JTextField gridXLoc, gridYLoc, outFile;
        private JTextArea printArea = new JTextArea();
	private SampleProgram prog;
 
    /**public static void main(String[] args) {
	   SamplingFrame sample = new SamplingFrame();
	}**/
 
    /* Constructors */
    public SamplingFrame() {
	   super("Sampling Program Frame");
	   //setBounds(100,100,300,100);
	   setSize(800,800);
	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   Container con = this.getContentPane();
	   setLayout(null);
	   prog = new SampleProgram();
	   initComponents();
	   addToFieldPanel();
	   addToBottomPanel();
	   addToMainPanel();
	   setVisible(true);
	}
	
	/* Methods */
	
	/* This initializes the components that are a part of the GUI */
	public void initComponents() {
	
	   //Main panel is where the sampled data will go (in a table)
	   mainPanel = new JPanel();
	   mainPanel.setLayout(null);
	   mainPanel.setLocation(200,10);
	   mainPanel.setSize(600,580);
	   add(mainPanel);
	   
	   //Panel where user can change info of sample (ex: grid locations and sample file)
	   fieldsPanel = new JPanel();
	   fieldsPanel.setLayout(null);
	   fieldsPanel.setLocation(0, 10);
	   fieldsPanel.setSize(180, 790);
	   add(fieldsPanel);
	   
	   //Panel where GPS Location button and Run Button will go
	   bottomPanel = new JPanel();
	   bottomPanel.setLayout(null);
	   bottomPanel.setLocation(200,600);
	   bottomPanel.setSize(600,200);
	   add(bottomPanel);
	   
	   return;
	}
	
	public void addToFieldPanel() {
	   JLabel fileLabel = new JLabel("Output File");
	   fieldsPanel.add(fileLabel);
	   fileLabel.setLocation(50, 50);
	   fileLabel.setSize(100, 50);
	   
	   outFile = new JTextField();
	   fieldsPanel.add(outFile);
	   outFile.setLocation(50, 105);
	   outFile.setSize(100, 50);
	   
	   JLabel gridXLabel = new JLabel("X Grid Location");
	   fieldsPanel.add(gridXLabel);
	   gridXLabel.setLocation(50, 175);
	   gridXLabel.setSize(100,50);
	   
	   gridXLoc = new JTextField();
	   fieldsPanel.add(gridXLoc);
	   gridXLoc.setLocation(50,230);
	   gridXLoc.setSize(100, 75);

	   JLabel gridYLabel = new JLabel("Y Grid Location");
	   fieldsPanel.add(gridYLabel);
	   gridYLabel.setLocation(50, 315);
	   gridYLabel.setSize(100,50);
	   
	   gridYLoc = new JTextField();
	   fieldsPanel.add(gridYLoc);
	   gridYLoc.setLocation(50,360);
	   gridYLoc.setSize(100, 75);

           fieldsPanel.add(updateFields);
           updateFields.setLocation(50, 600);
           updateFields.setSize(100, 50);
           updateFields.addActionListener(this);
	   
           return;	   
	}
	
	public void addToBottomPanel() {
	   bottomPanel.add(run);
	   run.setLocation(100,100);
	   run.setSize(100,50);
	   run.addActionListener(this);
	   
	}
	
	public void addToMainPanel() {
	   System.out.println("Add Table To Main Panel Here");
           mainPanel.add(printArea);
           printArea.setLocation(10,10);
           printArea.setSize(500,500);
	}
	
	public void actionPerformed(ActionEvent evt) {
	   if (evt.getSource() == updateFields) {
	      /*Update Fields button was clicked */
	      if (gridXLoc.getText() == "" && gridYLoc.getText() == "" && outFile.getText() == "") {
		     System.out.println("Nothing to update\n");
		  }
		  /* Check if value input into grid text fields is a number */
		  if (isInteger(gridXLoc.getText())) {
		     //Set grid values in sample program
			 System.out.println("Grid X should be updated\n");
			 prog.setGridX(Integer.parseInt(gridXLoc.getText()));
		  }
		  if (isInteger(gridYLoc.getText())) {
		     System.out.println("Grid Y should be updated\n");
			 prog.setGridY(Integer.parseInt(gridYLoc.getText()));
		  }
		  gridXLoc.setText("");
		  gridYLoc.setText("");
		  outFile.setText("");
	   }
	   else if (evt.getSource() == run) {
	      /*Sample button was hit */
	      //Run the program
		  prog.run(/*printArea*/);
	   }
	   return;
	}
	
	public boolean isInteger(String value) {
	   try {
	      Integer.parseInt(value);
		  return true;
	   }
	   catch (NumberFormatException nfe) {
	      return false;
	   }
	   catch (NullPointerException npe) {
	      return false;
	   }
	}
 }
