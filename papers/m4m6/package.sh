#!/bin/bash

DEST=./package
rm -rf $DEST
./clean.sh
./build.sh
./build.sh
mkdir $DEST
cp *.tex $DEST/
cp ref.bib $DEST/
cp -R images $DEST/
cp entcsmacro.sty $DEST/
cp ../styles/entcs/entcs.cls $DEST/
cp ../styles/entcs/entcs.bst $DEST/
cp lua.def $DEST/
cp paper.pdf $DEST/
