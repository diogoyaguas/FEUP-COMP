#!/bin/bash

rm -rf bin
mkdir bin
javac -d bin src/*/*.java
cd bin
java src.AST.Program ../test-file.txt
java -jar ../jasmin.jar test-file.j