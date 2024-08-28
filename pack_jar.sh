#!/bin/sh

if [ ! -e dist/rug_server.zip ]; then
    echo "Couldn't find rug_server.zip, run build.sh first"
    exit 1
fi

cp mcp/jars/minecraft_server.jar dist/rug_server.jar

cd dist
zipmerge rug_server.jar rug_server.zip
