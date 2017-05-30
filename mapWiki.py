print "entered" 
import sys
try:
	import wikipedia
except ImportError:
	print ImportError
# print "hello"
argument = sys.argv[1]
argument = argument.replace("_"," ")
# print argument
ans = wikipedia.search(argument, results=1)
print ans