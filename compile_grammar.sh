#!/bin/bash

cd src/AST
jjtree Program.jjt
rm TokenMgrError.java ParseException.java Token.java SimpleCharStream.java
javacc Program.jj