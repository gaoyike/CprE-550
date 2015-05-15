#Load Driver Module
#Written by Brendan Campbell in April 2008
from __future__ import with_statement
import time
from threading import Thread, Lock, Timer, Event
import socket

#args are: connectionstring rate period timeout

Collection = [[],0]
CollectionLock = Lock()
Termination = Event()

CollectionResults = None

class ConnectionTester(Thread):
	def __init__(self,server,port):
		self.server = server
		self.port = port
		Thread.__init__(self)
	def run(self):
		sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		with CollectionLock:
			Collection[1] = Collection[1]+1
		t0=time.time()
		sock.connect((self.server,int(self.port)))
		sock.sendall("GET / HTTP/1.0\r\nAccpet: text/plain, text/html,text/*\r\n\r\n")
		data = sock.recv(1024)
		alldata = data
		while len(data)>0:
			data = sock.recv(1024)
			alldata = alldata + data
		t0=time.time()-t0
		with CollectionLock:
			Collection[0].append(t0)

def _terminator():
	Termination.set()

class Spawner(Thread):
	def __init__(self, server, port, rate, period, timeout):
		self.server = server
		self.port = port
		self.rate = rate
		self.period = period
		self.timeout = timeout
		Thread.__init__(self)
	def run(self):
		#live = Timer(float(self.period),_terminator) #check this to determine whether or not to continue
		#live.start()
		ttw = 1/float(self.rate) #time to wait
		thread_collection = []
		while not Termination.isSet():
			#spawn a thread that tries to connect to the server
			tr = ConnectionTester(self.server,self.port)
			thread_collection.append(tr)
			tr.start()
			#print "Starting connector thread"
			#wait for some time
			time.sleep(ttw)

def CollectResults():
	with CollectionLock:
		globals()["CollectionResults"] = (Collection[0][:],Collection[1])
