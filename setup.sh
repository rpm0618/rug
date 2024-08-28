#!/bin/sh

if [ -d mcp ]; then
    echo "MCP directory already exists, bailing"
    exit 1
fi

mkdir mcp
cd mcp

wget http://www.modcoderpack.com/files/mcp918.zip
unzip mcp918.zip

cd jars
wget https://launcher.mojang.com/v1/objects/5fafba3f58c40dc51b5c3ca72a98f62dfdae1db7/server.jar
mv server.jar minecraft_server.jar
echo "5fafba3f58c40dc51b5c3ca72a98f62dfdae1db7 *minecraft_server.jar" | sha1sum -c || { echo 'invalid hash'; exit 1; }
cd ../

bash ./decompile.sh

cp -r src clean_src

patch -p0 < ../changes.patch
