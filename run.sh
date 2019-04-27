#!/bin/bash

rm -rf bin
javac -d bin src/*/*.java
cd bin
java src.AST.Program ../test-file.txt