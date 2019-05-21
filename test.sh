#!/bin/bash

rm -rf bin
mkdir bin
javac -d bin src/*/*.java
cd bin
java src.AST.Program ../test-simple.txt
java -jar ../jasmin.jar test-file.j
java $1
printf "\n *** Run finished! ***\n\n"
javap -c $1