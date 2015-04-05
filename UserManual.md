# User Manual #
Using only the instructions and information given in the user manual, users should be able to perform all system tasks/functions without calling a helpdesk to get answers to questions on how to proceed.

## System Overview ##
This is a cross-platform (mac, windows, linux, android) program that gathers RSSI data from wireless access points around the user. This data is then sent to a customized machine learning algorithm (libsvm) which will produce a model that can be used to determine the location of the user in a specified grid using our localization program.

## Tasks in setting up the system ##
  * Make sure your system is in the supported systems list.
  * Download Eclipse IDE.
  * Use svn to download the code and create a SVN Java project in Eclipse
  * Set an extra class path to the etrack\_files folder
  * 

## Functionality of the system ##
  * 

## Troubleshooting (common problems) ##

The following are some potential problems you should be aware of that you may run into:
  * When a user redoes a sample in a cell, the redoCellSample() method will not overwrite the previous values, and will instead append it to the end of each sample that gets redone.
  * When using WinScan.exe, sometimes the expected output causes an InputMismatch exception when parsing the string.
  * When scanning, make sure to limit the number of features (number of APs) detected, otherwise the localization may not work well.
  * Make sure to use the absolute path when executing WinScan.exe from Java.
  * The only systems we've tested the sampling program successfully on was a Windows 8, Windows 7, a MacBook Pro laptop running Ubuntu, and an Android phone Gingerbread 2.3.6.