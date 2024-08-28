#!/bin/sh
cd mcp
diff -ruN clean_src/ src/ > ../changes.patch
