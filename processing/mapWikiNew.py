import sys
import wikipedia

argument = sys.argv[1]
argument = argument.replace("_"," ")
ans = ""
try:
	ans = wikipedia.page(argument, redirect = True, auto_suggest = False)
	print ans
except:
	print ""
# print ans