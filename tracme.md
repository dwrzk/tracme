# Introduction #

This code is a supplement to eTrack a low cost target monitoring sensor network that can operate using various signal frequencies from devices such as wifi and Xbee Shield. This projects deals with a collection of signal strengths that will be sampled at different locations and orientations across a grid using a portable system (laptop or smartphone); this data will be sent to a base station that runs a machine learning algorithm to create an organizational map of the designated area. Using a grid system, the tracking device can then be located within a specified region with relatively high accuracy compared to a GPS system.


# Goals and Objectives #

The following list contains details about the goals and objectives for tracme:
  * Create a low cost network system that successfully positions a mobile device in a predefined outdoor area
  * Establish a reliable way of sampling WiFi signal strengths of wireless access points
  * Configure the program to organize the data and send it to the base station that performs the machine learning algorithm to generate the model
  * Ease the process of sampling data (RSSI values compared with GPS and actual map coordinates)