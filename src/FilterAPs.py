import sys
import re
import shlex

class apRelation:
	def __init__(self, oldAp, newAp):
		self.oldAP = oldAp
		self.newAP = newAp
		
	oldAP = -1
	newAP = -1


def filter():
	sFile = open('DexLawnNthUbFix.txt', 'r') #Sample File to read from
	apFile = open('SMWAP_Table.txt_debug.txt', 'r') #AP Table with new id and old id
	outFile = open('DexLawnNthUbSMW.txt', 'w') #Sample File to write to
	
	xgrid = sFile.readline()
	ygrid = sFile.readline()
	numAps = sFile.readline()
	
	outFile.write(xgrid + ygrid + str(totalAPs) + '\n') #Set the 3rd Value to the amount of APs in apFile
	
	while True:
		line = sFile.readline()
		if not line:
			print('Not a line')
			break

		if line[0] == '#':
			outFile.write(line)
			continue
			
		test = re.findall('[-+]?\d+:[-+]?\d+;', line)
		for ap in apList:
			#Get the corresponding value from test, adjust apid to new apid
			data = test[ap.oldAP-1]
			apid = int(ap.newAP)
			rssi = int(data[data.find(":")+1:data.find(";")])
			outFile.write(str(apid) + ':' + str(rssi) + ';')
			
		outFile.write('\n')
		outFile.flush()
	
	print('Done Parsing File\n')

inFile = open('CP_APTable.txt_debug.txt', 'r')
outFile = open('SMWAP_Table.txt', 'w')
debugFile = open('SMWAP_Table.txt_debug.txt', 'w')

apList = [] #List of apRelations

i = 1
totalAPs = 0

while True:
		
	line = inFile.readline()
	if not line:
		print('no line')
		break
		
	words = shlex.split(line)
	print(words, len(words))	
	if words[1] == 'SecureMustangWireless':
		debugFile.write(str(i) + ' ' + words[1] + ' ' + words[2] + ' ' + words[0])
		outFile.write(str(i) + ' ' + words[2])
		i = i + 1;
		totalAPs+=1 #Increment total number of APs found
		debugFile.write('\n')
		outFile.write('\n')
		debugFile.flush()
		outFile.flush()
		relation = apRelation(int(words[0]),int(i)-1)
		apList.append(relation)
			
debugFile.close()
outFile.close()	
filter()
#print('Done parsing file\n')
	