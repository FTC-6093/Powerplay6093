import os

prefix = "Emmy"
suffix = ".java"

for current in os.listdir("."):
    if (
        current[:len(prefix)] != prefix and 
        current[-len(suffix):] == suffix
        ):
        os.rename(current, prefix+current)