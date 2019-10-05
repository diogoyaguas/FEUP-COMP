#!/bin/bash

if [ $# != 1 ] 
	then 
        echo "One and only one argument must be provided!"
		echo "Usage: run.sh [file name]"
		exit 1
	fi 

rm -rf bin
mkdir bin
javac -d bin src/*/*.java
cd bin
java src.AST.Program ../test_files/$1
java -jar ../jasmin.jar test-file.j