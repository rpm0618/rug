#!/bin/sh

if [ -d dist ]; then
    rm -rf dist
fi

mkdir dist

cd mcp

bash ./recompile.sh
bash ./reobfuscate.sh

cd reobf/minecraft_server
zip -r ../../../dist/rug_server.zip .
