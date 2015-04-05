TracMe


> Cal Poly Computer Engineering Capstone
TracMe

Kwaku Farkye
James Humphrey
Ken Ugo
March 22, 2013


Professor: Lynne Slivovsky

Table of Contents


1 System Overview								4
1.1 Supported Systems							4
2 System Setup									4
2.1 Check System Requirements						4
2.2 Downloading Eclipse IDE							5
2.3 Downloading TracMe From Repository					5
2.4 Set Class Path To eTrack\_files folder					6
2.5 Downloading Python							7
2.6 Mobile									8
3 System Functionality								8
3.1 Sampling Grid Design							8
3.2 Laptop Sampling								8
3.3 Filtering With Python Script						9
3.4 Localization								10
3.5 Mobile									10
4 Troubleshooting (common problems)						11
5 Resources									11



1 System Overview
This is a cross-platform (mac, windows, linux, android) program that gathers received signal strength index (RSSI) data from wireless access points around the user. This data is then sent to a customized machine learning algorithm (libsvm) which will produce a model that can be used to determine the location of the user in a specified grid using our localization program. The system is broken down into three phases: sampling, training, and localization. In the sampling phase, a GUI is used to help with the sampling process. The GUI is shown in Figure 3. After sampling is done, a file is output with information regarding access point and received signal strengths of each access point. This file will be filtered using a python script and sent for training by Dr. Tran’s machine learning algorithm. Once a model is received from Dr. Tran’s algorithm, localization can be ran by sampling and referencing the model received during offline training.
1.1 Supported Systems
The TracMe system is cross platform and has been implemented on the following operating systems:
> Windows: Vista, 7, 8
> Mac: OSX
> Linux: Ubuntu
> Android: Gingerbread (Version 2.3.6)

Each operating system supported must have a Java Virtual Machine enabled. JRE 1.7 was used for implementing the sampling and localization programs; however, JRE 1.6 will also work. In order to run the python script, python version 2.7.3 must be installed on your device.
2 System Setup
> Completing the following steps will give you a working environment for using the TracMe system. Here is a summary of the steps:

Make sure your system is in the supported systems list.
Download Eclipse IDE.
Use svn to download the code and create a SVN Java project in Eclipse
Set an extra class path to the etrack\_files folder
Download Python for Use with Filtering script
2.1 Check System Requirements
> To check if your system is fit for running the TracMe project, follow the steps of this section to completion. Afterwards, to check if Java has been installed correctly go open a command prompt (terminal in Mac and Linux) and type “java -v”. If Java was installed correctly during the Eclipse download, information will appear telling you what version of Java your system is running.
NOTE: If any of these programs have already been installed on your device, you may skip that section.

2.2 Downloading Eclipse IDE
> To download the Eclipse IDE, go to http://www.eclipse.org/downloads/ and download the Eclipse IDE for Java EE Developers (If you would like a lighter weight version of Eclipse, you may download Eclipse IDE for Java Developers). Make sure to download the 32 bit version if using a 32 bit Operating System and 64 bit version when using a 64 bit Operating System.
> After Eclipse has been installed on your laptop you will need to install a plugin to be able to run subversion.

To install this plugin

Run Eclipse
On the main menu bar, click “help” then select “Eclipse Marketplace”
Once in the marketplace search for “Subclipse”. Proceed to install Subclipse for Eclipse (this will require a restart of Eclipse)

2.3 Downloading TracMe From Repository
> The next step to ensuring proper system setup is to download the TracMe system from the GoogleCode repository. The following steps will properly download the source code for your use:

In Eclipse, go to “File” → “New” → “Project”
In the New Project Wizard window, expand the “SVN” folder and select “Checkout Projects From SVN”. Continue to the next window
Figure 1: SVN New Project


On the next window, select the “create a new repository” radio button and click “Next”. Eclipse will now ask you for the url of the repository. Paste the following repository in: “http://tracme.googlecode.com/svn/trunk/”. Continue on to the next window
Make sure the “Check out as project configured using the New Project Wizard” radio button is selected. Also, select the Check out HEAD Revision checkbox. Click “Finish”
A New Project Wizard window will appear. In this window, select “Java Project” and continue.
Name the new Java Project as “tracme” and select the normal configurations that would be used for any other Java Project. Click “Finish”. Eclipse will now begin downloading the repository code into your new “tracme” project.
2.4 Set Class Path To eTrack\_files folder
> Once training models are received from Dr. Tran, they will need to be used for localization. In order for the localization program to see the model files, the class path must be specified in the Eclipse project.

To set the Class Path To etrack\_files folder

Create a folder inside the Eclipse project named “etrack\_files” and place the folder in the trunk/src/ directory.
In the Eclipse menu bar, select “Projects” and “Properties”. A “Properties for tracme” window will pop up.
On the left hand side, select “Java Build Path” from the list.
Click the “Libraries” tab, then click the “Add Class Folder...” button on the right hand side of the text area.
Figure 2: Setup etrack\_files folder


Locate the “etrack\_files” directory and select it. Click Apply and Ok to finish.

Once this step is done, model files will be unzipped and placed in this folder so that they can be used for localization.

2.5 Downloading Python
> To install python on your device, go to http://python.org/download/ and download the proper version of python for your operating system.
> NOTE: Python version 2.7.3 must be used for the TracMe python script. Other versions of python 2 may be used, as well as python 3, however these versions have not been tested with the script.
> NOTE: You will also need to add a path to the python program to your system’s classpath environment variable in order to run python anywhere on your system. This will be useful for running the TracMe python script. If you are unable to do this, you will need to put the TracMe script, along with the sample and access point table files you are attempting to filter, into the python program’s directory.

http://wiki.python.org/moin/BeginnersGuide provides a nice tutorial to help install python properly on your device.

2.6 Mobile
To install the Android version of this project on your Android device simply install the .apk located in the /com.capstone.TracMe/bin folder of our repository located at http://code.google.com/p/tracme/. There are various ways of achieving an install but the easiest way would be to put the .apk file on your device and execute that file once it is on there.
3 System Functionality
3.1 Sampling Grid Design
> Before running any programs, you will need to setup your localization grid. This grid is where both sampling and positioning will occur. Make sure to pick an area that has access points nearby that are actively broadcasting beacon frames. Partition your grid into coordinates, making each cell in the grid the same size as the next. For example, if you set up a 2x2 grid with cell size of 1 ft x 1 ft, your total grid size will be 2 ft x 2 ft, or 4 ft2. Once you have specified the dimensions of your localization grid, the sampling and localization programs can be launched.
3.2 Laptop Sampling
After TracMe is setup and compiled on your laptop, begin by launching the sampling program:

Enter the following fields into the program before sampling:
The grid size in the x and y direction
Select a previously used access point table file or create a new one using the open dialog box
Select a sample file to be generated using the open dialog box
Set the coordinate you want to begin sampling on
Set the amount of samples you will want to take per coordinate
Indicate the direction you will be standing when doing the sampling
Press the sampling button to begin sampling the first cell.
When the sampling for that cell is complete, move to the next cell and change the coordinate to the new cell. Repeat this step until all cells have been sampled.
When all cells are sampled, press the ‘Save’ button to generate the sample file in the specified location.
NOTE: Figure 3 shows a sample of what the GUI may look like during the course of sampling.


Figure 3: Sampling GUI

3.3 Filtering With Python Script
> To filter the sample output file with TracMe’s FilterAPs.py python script, you will first need to edit a few of the arguments within the script. To do this, follow these steps:

Open the script with a text editor
On line 48 you will see an inFile variable which opens an access point debug file. Change this to open the access point debug file generated during your sampling session.
On the next line you will see the outFile variable. Set this variable to create a new access point table file. This file will contain your filtered access point information.
On the following line, specify the debugFile as a new access point debug file. This file will act as the new access point debug file for your filtered access point table
On line 15, set the sFile variable to open the output file from your sampling session.
On line 16, set the apFile to the same file that you specified on line 49 (see step 4)
On line 17, set the outFile variable to a new output file name. This outFile will be the file with the new filtered sampling information. This file will be sent to Dr. Tran.
On line 66, set the comparison to whatever you would like to filter by (in the file given to you, we are filtering by ‘SecureMustangWireless’.

NOTE: Remember that the files that you are attempting to edit must be in the same directory as your python script.

3.4 Localization
When testing a training model, you must run the localization program while keeping the following notes in mind:

We do not have a full localization GUI which means that the following items will need to be manually entered into the code before testing localization:
Name of training file
Name of raw file
Number of classes along the x and y dimension
Averaging method
Delay time between scans
The grid class has not been integrated with the localization program yet
After the features mentioned above have been set, the localization program (LocalizationProgram.java) can be compiled and ran. To run the program, stand at a location inside your localization grid and run LocalizationProgram. The program will now use the averaging method specified by you to predict a location. The location will be output in the console of your Eclipse window. Also for your convenience, a log file is created, storing information about each prediction. This information includes time taken for scanning and predicting, average values of scan, and prediction results.


3.5 Mobile
After successfully setting up the application on your Android device:

Either swipe from the left to the right or select the icon at the top left of the screen to reveal the “TracMe Settings” page
Fill in all the fields and select “Set Values” when complete
NOTE: The Output file and AP table file do not need the extension entered. (The only type we will be using is .txt and it is appended automatically). These files will be saved in the Downloads folder of your Android device.
For our testing purposes, we take note of the direction faced when sampling. Select “Any Direction” if this information is not important to your sampling set.
After all the values have been entered, swipe from the right to the left or select the icon at the top right of the screen in order to close the “TracMe Settings” page.
Figure 4: Settings pane for Android application


After viewing console log to ensure that the values entered are correct, you can begin sampling by setting the X and Y coordinates and selecting the “Scan” button.
Figure 5: Main pane for Android application


NOTE: Unfortunately, changes cannot be made to the values on the “TracMe Settings” page. If you have any errors in your inputs you will have to close and reopen the TracMe application.
NOTE: The GPS button will display your current GPS location (for testing purposes)
NOTE: The “Save Results” button will become enabled after the first successful scan. This button should only be pressed once after all the cells have been successfully scanned. Multiple presses will result in errors in the output file.
After all the cells have been sampled, press the “Save results” button and close the program.
NOTE: All the output files will be located in the devices “Downloads” folder

4 Troubleshooting (common problems)
The following are some potential problems you should be aware of that you may run into:

When a user redoes a sample in a cell, the redoCellSample() method will not overwrite the previous values, and will instead append it to the end of each sample that gets redone.

When using WinScan.exe, sometimes the expected output causes an InputMismatchException when parsing the string. This problem is believed to be fixed with the current release. In the case that this happens, the program may need to be restarted. Restart the program and create a new sample file, but load the same AP table file. Then at the end of sampling, merge the original sample file with the new sample file made after restarting.

When scanning, make sure to limit the number of features (number of APs) detected, otherwise the localization may not work well. This can be done by using the python script given: FilterAPs.py
If localization testing produces inaccurate results, try increasing the number of cells and decreasing the individual cell size.
Make sure to use the absolute path when executing WinScan.exe from Java. The place to specify the path is in WindowsScanner.java.

5 Resources
This document, as well as source code, and TracMe project information can be found on our google code site: https://code.google.com/p/tracme/