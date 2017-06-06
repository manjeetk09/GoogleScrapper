import sys
import wikipedia

argument = sys.argv[1]
argument = argument.replace("_"," ")
try:
	ans = wikipedia.page(argument, redirect = True, auto_suggest = False)
except:
	print "";
print ans