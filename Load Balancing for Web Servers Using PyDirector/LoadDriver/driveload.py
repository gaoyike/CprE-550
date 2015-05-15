#Load Driver MainFile: driveload.py
#Written by Brendan Campbell in April 2008
import LoadDriver
import sys
from threading import Timer
from xml.dom.minidom import parse

#for arg in sys.argv:
#	print arg

filename = "config.xml"

if len(sys.argv)>=2:
	if sys.argv[1].lower() == "help":
		print "Usage: driveload [configfile] \nIf no configuration file is specified, driveload will look for config.xml in the current directory"
		sys.exit(0)
	filename = sys.argv[1]

try:
	xmlconfig = parse(filename)
	hosts = []
	for host in xmlconfig.getElementsByTagName('host'):
		hosts.append([host.getElementsByTagName('hostname')[0].childNodes[0].data,host.getElementsByTagName('port')[0].childNodes[0].data])
	rate = float(xmlconfig.getElementsByTagName('rate')[0].childNodes[0].data)
	period = float(xmlconfig.getElementsByTagName('period')[0].childNodes[0].data)
	timeout = float(xmlconfig.getElementsByTagName('timeout')[0].childNodes[0].data)
except IndexError:
	print "config.xml is not properly defined.\nThere must be: <rate>,<period>,<timeout> elements.\nAlso, a <host> must have a <hostname> and a <port> element"
	raise
except IOError:
	print "Error while configuring. Is %s defined\n?"%filename,sys.exc_info()[0]
	raise

print "Configured Behavior:"
print "-----------------"
print "rate:",rate
print "period:",period
print "timeout:",timeout
print "-----------------"
print "-----------------"
print "Configured Hosts:"
for host in hosts:
	print "-----------------"
	print "url:",host[0]
	print "port:",host[1]

#create timer
time_thread = Timer(period+timeout,LoadDriver.CollectResults)
terminator_thread = Timer(period,LoadDriver._terminator)
time_thread.start()
terminator_thread.start()

#launch host-testers
for host in hosts:
	tr = LoadDriver.Spawner(host[0],host[1],rate,period,timeout)
	tr.start()

#wait for the results to come in
time_thread.join(period+timeout+1)

#check if the result-gathering is successful
if time_thread.isAlive():
	print "Error in timer thread!"
	sys.exit(1)

#print the response times (in seconds) and the number of attempts
print LoadDriver.CollectionResults
