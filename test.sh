#!/bin/bash

rm -rf bin
mkdir bin
javac -d bin src/*/*.java
cd bin
java src.AST.Program ../test_files/$1
java -jar ../jasmin.jar test-file.j
java $2
printf "\n *** Run finished! ***\n\n"
javap -c $2