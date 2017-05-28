import sys
import wikipedia
# print "hello"
argument = sys.argv[1]
argument = argument.replace("_"," ")

ans = wikipedia.search(argument, results=1)
print ans