#!/bin/bash

rm -r bin
javac -d bin src/*/*.java
cd bin
java src.AST.Program ../test-file.txt