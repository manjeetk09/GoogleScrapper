import sys
import wikipedia

argument = sys.argv[1]
argument = argument.replace("_"," ")
ans = ""
try:
	ans = wikipedia.page(argument, redirect=True, auto_suggest=True)
	# print ans
except wikipedia.exceptions.DisambiguationError as e:
	# print type(e)
	ans =  e.options
	ans = ans[0]
print ans