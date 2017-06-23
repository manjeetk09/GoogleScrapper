print "entered"
import sys

try:
    import wikipedia
    print "import"
except ImportError:
    print ImportError

print "imported"
argument = sys.argv[1]
argument = argument.replace("_", " ")
print argument
ans = wikipedia.search(argument, results=1)
print ans
# print argument
