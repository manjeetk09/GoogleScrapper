import sys
import wikipedia

argument = sys.argv[1]
argument = argument.replace("_"," ")
ans = wikipedia.page(argument)
print ans.categories